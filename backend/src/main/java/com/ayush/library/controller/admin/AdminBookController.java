package com.ayush.library.controller.admin;

import com.ayush.library.model.Book;
import com.ayush.library.service.BookService;
import com.ayush.library.service.BorrowedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/books")
public class AdminBookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowedBookService borrowedBookService;

    /** Add Book */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createBook(@RequestBody Book bookRequest) {
        Map<String, Object> response = new HashMap<>();

        if (bookService.existsByIsbn(bookRequest.getIsbn())) {
            response.put("success", false);
            response.put("message", "Book with this ISBN already exists!");
            return ResponseEntity.ok(response);
        }

        bookRequest.setAvailable(true);
        bookRequest.setAvailableCopies(bookRequest.getTotalCopies());
        Book savedBook = bookService.saveBook(bookRequest);

        response.put("success", true);
        response.put("message", "Book added successfully");
        response.put("book", savedBook);
        return ResponseEntity.ok(response);
    }

    /** Update Book by ID */
    @PutMapping("/id/{id}")
    public ResponseEntity<Map<String, Object>> updateBookById(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        Map<String, Object> response = new HashMap<>();
        return bookService.getBookById(id).map(book -> {
            try {
                applyUpdates(book, updates);
                Book updatedBook = bookService.saveBook(book);
                response.put("success", true);
                response.put("message", "Book updated successfully");
                response.put("book", updatedBook);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                response.put("success", false);
                response.put("message", e.getMessage());
                return ResponseEntity.badRequest().body(response);
            }
        }).orElseGet(() -> {
            response.put("success", false);
            response.put("message", "Book not found with ID: " + id);
            return ResponseEntity.status(404).body(response);
        });
    }

    /** Update Book by ISBN */
    @PutMapping("/isbn/{isbn}")
    public ResponseEntity<Map<String, Object>> updateBookByIsbn(
            @PathVariable String isbn,
            @RequestBody Map<String, Object> updates) {

        Map<String, Object> response = new HashMap<>();
        return bookService.getBookByIsbn(isbn).map(book -> {
            try {
                applyUpdates(book, updates);
                Book updatedBook = bookService.saveBook(book);
                response.put("success", true);
                response.put("message", "Book updated successfully");
                response.put("book", updatedBook);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                response.put("success", false);
                response.put("message", e.getMessage());
                return ResponseEntity.badRequest().body(response);
            }
        }).orElseGet(() -> {
            response.put("success", false);
            response.put("message", "Book not found with ISBN: " + isbn);
            return ResponseEntity.status(404).body(response);
        });
    }

    /** Delete Book by ID */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteBookById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        return bookService.getBookById(id).map(book -> {
            if (borrowedBookService.existsByBookId(book.getId())) {
                response.put("success", false);
                response.put("message", "Cannot delete book; currently borrowed.");
                return ResponseEntity.badRequest().body(response);
            }
            bookService.deleteBook(id);
            response.put("success", true);
            response.put("message", "Book deleted successfully");
            response.put("book", book);
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("success", false);
            response.put("message", "Book not found with ID: " + id);
            return ResponseEntity.status(404).body(response);
        });
    }

    /** Delete Book by ISBN */
    @DeleteMapping("/isbn/{isbn}")
    public ResponseEntity<Map<String, Object>> deleteBookByIsbn(@PathVariable String isbn) {
        Map<String, Object> response = new HashMap<>();
        return bookService.getBookByIsbn(isbn).map(book -> {
            if (borrowedBookService.existsByBookId(book.getId())) {
                response.put("success", false);
                response.put("message", "Cannot delete book; currently borrowed.");
                return ResponseEntity.badRequest().body(response);
            }
            bookService.deleteBook(book.getId());
            response.put("success", true);
            response.put("message", "Book deleted successfully");
            response.put("book", book);
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("success", false);
            response.put("message", "Book not found with ISBN: " + isbn);
            return ResponseEntity.status(404).body(response);
        });
    }

    /** Get Book by ID */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** Get Book by ISBN */
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        return bookService.getBookByIsbn(isbn)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** Get all Books */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    /** Helper method for partial updates */
    private void applyUpdates(Book book, Map<String, Object> updates) {
        if (updates.containsKey("publisher")) book.setPublisher((String) updates.get("publisher"));
        if (updates.containsKey("publishedDate")) book.setPublishedDate(LocalDate.parse(updates.get("publishedDate").toString()));
        if (updates.containsKey("category")) book.setCategory((String) updates.get("category"));
        if (updates.containsKey("rackNumber")) book.setRackNumber((String) updates.get("rackNumber"));
        if (updates.containsKey("totalCopies")) book.setTotalCopies(Integer.parseInt(updates.get("totalCopies").toString()));
        if (updates.containsKey("availableCopies")) book.setAvailableCopies(Integer.parseInt(updates.get("availableCopies").toString()));
        book.setAvailable(book.getAvailableCopies() > 0);
    }
}