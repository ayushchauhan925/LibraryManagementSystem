package com.ayush.library.service;

import com.ayush.library.model.BorrowedBook;
import com.ayush.library.model.Penalty;
import com.ayush.library.repository.BorrowedBookRepository;
import com.ayush.library.repository.PenaltyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowedBookService {

    private final BorrowedBookRepository borrowedBookRepository;
    private final PenaltyRepository penaltyRepository;

    public BorrowedBookService(BorrowedBookRepository borrowedBookRepository,
                               PenaltyRepository penaltyRepository) {
        this.borrowedBookRepository = borrowedBookRepository;
        this.penaltyRepository = penaltyRepository;
    }

    // Get all borrowed books
    public List<BorrowedBook> getAllBorrowedBooks() {
        return borrowedBookRepository.findAll();
    }

    // Get borrowed book by ID
    public BorrowedBook getBorrowedBookById(Long id) {
        return borrowedBookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrow record not found"));
    }

    // Get borrowed books for a student
    public List<BorrowedBook> getBorrowedBooksByStudent(Long studentId) {
        return borrowedBookRepository.findByStudent_Id(studentId);
    }

    // Get borrowed books for a book ID
    public List<BorrowedBook> getBorrowedBooksByBook(Long bookId) {
        return borrowedBookRepository.findByBook_Id(bookId);
    }

    // Count borrowed books for a specific book
    public long countBorrowedBooksByBookId(Long bookId) {
        return borrowedBookRepository.countByBook_Id(bookId);
    }

    // Check if a book is borrowed
    public boolean existsByBookId(Long bookId) {
        return borrowedBookRepository.existsByBook_Id(bookId);
    }

    // Save borrowed book
    public BorrowedBook saveBorrowedBook(BorrowedBook borrowedBook) {
        return borrowedBookRepository.save(borrowedBook);
    }

    // Delete borrowed book
    public void deleteBorrowedBook(Long id) {
        borrowedBookRepository.deleteById(id);
    }

    // Return a borrowed book
    public BorrowedBook returnBook(Long studentId, Long borrowedBookId, LocalDate returnDate) {
        BorrowedBook borrowedBook = borrowedBookRepository.findById(borrowedBookId)
                .orElseThrow(() -> new RuntimeException("Borrow record not found"));

        if (!borrowedBook.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("Not your borrow record");
        }

        if (borrowedBook.isReturned()) {
            throw new RuntimeException("Book already returned");
        }

        borrowedBook.setReturned(true);
        borrowedBook.setReturnDate(returnDate);

        // Penalty check
        if (borrowedBook.getDueDate() != null && returnDate.isAfter(borrowedBook.getDueDate())) {
            long daysLate = java.time.temporal.ChronoUnit.DAYS.between(borrowedBook.getDueDate(), returnDate);
            double fineAmount = daysLate * 100.0;
            borrowedBook.setFineAmount(fineAmount);

            Penalty penalty = new Penalty();
            penalty.setBorrowedBook(borrowedBook);
            penalty.setAmount(fineAmount);
            penalty.setReason("Late Return");
            penaltyRepository.save(penalty);
        } else {
            borrowedBook.setFineAmount(0.0);
        }

        return borrowedBookRepository.save(borrowedBook);
    }
}