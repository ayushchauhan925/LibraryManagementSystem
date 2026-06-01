package com.ayush.library.controller.admin;

import com.ayush.library.model.BorrowRequest;
import com.ayush.library.model.BorrowedBook;
import com.ayush.library.model.Book;
import com.ayush.library.model.Student;
import com.ayush.library.service.BorrowRequestService;
import com.ayush.library.service.BorrowedBookService;
import com.ayush.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/borrow-requests")
public class AdminBorrowRequestController {

    @Autowired
    private BorrowRequestService borrowRequestService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowedBookService borrowedBookService;

    /** Get all pending borrow requests */
    @GetMapping("/pending")
    public ResponseEntity<List<BorrowRequest>> getPendingRequests() {
        List<BorrowRequest> pendingRequests = borrowRequestService.getRequestsByStatus("PENDING");
        return ResponseEntity.ok(pendingRequests);
    }

    /** Get all rejected borrow requests */
    @GetMapping("/rejected")
    public ResponseEntity<List<BorrowRequest>> getRejectedRequests() {
        List<BorrowRequest> rejectedRequests = borrowRequestService.getRequestsByStatus("REJECTED");
        return ResponseEntity.ok(rejectedRequests);
    }

    /** Approve or Reject borrow request */
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateRequestStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String borrowDate,
            @RequestParam(required = false) String dueDate) {

        Map<String, Object> response = new HashMap<>();
        BorrowRequest request = borrowRequestService.getRequestById(id).orElse(null);

        if (request == null) {
            response.put("success", false);
            response.put("message", "Borrow request not found.");
            return ResponseEntity.ok(response);
        }

        if (!status.equalsIgnoreCase("APPROVED") && !status.equalsIgnoreCase("REJECTED")) {
            response.put("success", false);
            response.put("message", "Invalid status. Use APPROVED or REJECTED.");
            return ResponseEntity.ok(response);
        }

        request.setStatus(status.toUpperCase());

        if (status.equalsIgnoreCase("APPROVED")) {
            Book book = bookService.getBookById(request.getBookId()).orElse(null);

            if (book == null) {
                response.put("success", false);
                response.put("message", "Book not found.");
                return ResponseEntity.ok(response);
            }

            if (book.getAvailableCopies() <= 0) {
                response.put("success", false);
                response.put("message", "Book not available.");
                return ResponseEntity.ok(response);
            }

            // Reduce available copies
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookService.saveBook(book);

            // Create BorrowedBook entry
            BorrowedBook borrowedBook = new BorrowedBook();
            borrowedBook.setBook(book);
            Student student = new Student();
            student.setId(request.getStudentId());
            borrowedBook.setStudent(student);

            if (borrowDate != null) borrowedBook.setBorrowDate(LocalDate.parse(borrowDate));
            else {
                response.put("success", false);
                response.put("message", "Borrow date is required when approving.");
                return ResponseEntity.ok(response);
            }

            LocalDate parsedDueDate = (dueDate != null)
                    ? LocalDate.parse(dueDate)
                    : borrowedBook.getBorrowDate().plusDays(14);
            borrowedBook.setDueDate(parsedDueDate);

            borrowedBookService.saveBorrowedBook(borrowedBook);
        }

        borrowRequestService.saveRequest(request);

        response.put("success", true);
        response.put("message", "Request " + status.toUpperCase() + " successfully.");
        response.put("data", request);

        return ResponseEntity.ok(response);
    }
}