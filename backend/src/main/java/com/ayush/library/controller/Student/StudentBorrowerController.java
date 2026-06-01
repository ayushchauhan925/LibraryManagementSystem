package com.ayush.library.controller.Student;

import com.ayush.library.model.BorrowedBook;
import com.ayush.library.model.Penalty;
import com.ayush.library.service.BorrowedBookService;
import com.ayush.library.service.PenaltyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
public class StudentBorrowerController {

    private final BorrowedBookService borrowedBookService;
    private final PenaltyService penaltyService;

    public StudentBorrowerController(BorrowedBookService borrowedBookService, PenaltyService penaltyService) {
        this.borrowedBookService = borrowedBookService;
        this.penaltyService = penaltyService;
    }

    @GetMapping("/borrowed")
    public List<BorrowedBook> getBorrowedBooks(HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        return borrowedBookService.getBorrowedBooksByStudent(studentId);
    }

    @PutMapping("/borrowed/{id}/return")
    public BorrowedBook returnBorrowedBook(@PathVariable Long id, @RequestBody Map<String, String> body, HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        return borrowedBookService.returnBook(studentId, id, LocalDate.parse(body.get("returnDate")));
    }
}