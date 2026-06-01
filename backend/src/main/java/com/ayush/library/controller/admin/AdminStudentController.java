package com.ayush.library.controller.admin;

import com.ayush.library.model.Student;
import com.ayush.library.service.StudentService;
import com.ayush.library.service.BorrowedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/students")
public class AdminStudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private BorrowedBookService borrowedBookService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /** Add Student */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createStudent(@RequestBody Student studentRequest) {
        Map<String, Object> response = new HashMap<>();

        if (studentService.existsByEmail(studentRequest.getEmail())) {
            response.put("success", false);
            response.put("message", "Student with this email already exists!");
            return ResponseEntity.ok(response);
        }

        studentRequest.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        Student savedStudent = studentService.saveStudent(studentRequest);

        response.put("success", true);
        response.put("message", "Student created successfully");
        response.put("student", savedStudent);
        return ResponseEntity.ok(response);
    }

    /** Update Student */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        Map<String, Object> response = new HashMap<>();

        return studentService.getStudentById(id).map(student -> {
            // Update allowed fields only
            if (updatedStudent.getDepartment() != null) student.setDepartment(updatedStudent.getDepartment());
            if (updatedStudent.getCurrentSemester() != null) student.setCurrentSemester(updatedStudent.getCurrentSemester());
            if (updatedStudent.getAddress() != null) student.setAddress(updatedStudent.getAddress());
            if (updatedStudent.getPhoneNumber() != null) student.setPhoneNumber(updatedStudent.getPhoneNumber());
            if (updatedStudent.getEmail() != null) student.setEmail(updatedStudent.getEmail());
            if (updatedStudent.getPassword() != null) student.setPassword(passwordEncoder.encode(updatedStudent.getPassword()));
            student.setActive(updatedStudent.isActive());
            student.setUpdatedAt(LocalDateTime.now());

            Student saved = studentService.saveStudent(student);
            response.put("success", true);
            response.put("student", saved);
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("success", false);
            response.put("message", "Student not found with ID: " + id);
            return ResponseEntity.status(404).body(response);
        });
    }

    /** Delete Student */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteStudent(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        return studentService.getStudentById(id).map(student -> {
            if (!borrowedBookService.getBorrowedBooksByStudent(id).isEmpty()) {
                response.put("success", false);
                response.put("message", "Cannot delete student; has borrowed books.");
                return ResponseEntity.badRequest().body(response);
            }
            studentService.deleteStudent(id);
            response.put("success", true);
            response.put("message", "Student deleted successfully");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("success", false);
            response.put("message", "Student not found with ID: " + id);
            return ResponseEntity.status(404).body(response);
        });
    }

    /** Get Student by ID */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** Get Student by Email */
    @GetMapping("/email/{email}")
    public ResponseEntity<Student> getStudentByEmail(@PathVariable String email) {
        return studentService.getStudentByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** Get all Students */
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }
}