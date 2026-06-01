package com.ayush.library.controller.Student;

import com.ayush.library.model.Student;
import com.ayush.library.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // GET /students/me   (session handled automatically by Spring Session JDBC)
    @GetMapping("/me")
    public Student getMyDetails(HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");

        if (studentId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No active session");
        }

        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }
}
