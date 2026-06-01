package com.ayush.library.controller.Student;

import com.ayush.library.model.BorrowRequest;
import com.ayush.library.service.BorrowRequestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/student/borrow-requests")
public class StudentBorrowRequestController {

    @Autowired
    private BorrowRequestService borrowRequestService;

    /** Student sends a borrow request */
    @PostMapping("/{bookId}")
    public ResponseEntity<?> createRequest(@PathVariable Long bookId, HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) return ResponseEntity.status(401).body("Unauthorized: Please login first.");

        BorrowRequest request = borrowRequestService.createBorrowRequest(studentId, bookId);
        return ResponseEntity.ok(request);
    }

    /** Student views all their borrow requests */
    @GetMapping
    public ResponseEntity<List<BorrowRequest>> getMyRequests(HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(borrowRequestService.getRequestsByStudent(studentId));
    }
}