package com.ayush.library.controller.Student;

import com.ayush.library.model.Book;
import com.ayush.library.service.BookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/student/books")
public class StudentBookController {

    @Autowired
    private BookService bookService;

    /** Get all available books */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAvailableBooks(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Long studentId = (Long) session.getAttribute("studentId");

        if (studentId == null) {
            response.put("success", false);
            response.put("message", "Please login first.");
            return ResponseEntity.status(401).body(response);
        }

        List<Book> books = bookService.getAvailableBooks(0);
        response.put("success", true);
        response.put("count", books.size());
        response.put("books", books);
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    /** Get book by ID */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBookById(@PathVariable Long id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Long studentId = (Long) session.getAttribute("studentId");

        if (studentId == null) {
            response.put("success", false);
            response.put("message", "Please login first.");
            return ResponseEntity.status(401).body(response);
        }

        Optional<Book> bookOpt = bookService.getBookById(id);
        if (bookOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Book not found with ID: " + id);
            return ResponseEntity.status(404).body(response);
        }

        response.put("success", true);
        response.put("book", bookOpt.get());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }
}