package com.ayush.library.repository;

import com.ayush.library.model.BorrowedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {

    // Find all borrowed books by student ID
    List<BorrowedBook> findByStudent_Id(Long studentId);

    // Find all borrowed books by book ID
    List<BorrowedBook> findByBook_Id(Long bookId);

    // Count how many times a book is currently borrowed
    long countByBook_Id(Long bookId);

    // Quick check if a book is currently borrowed
    boolean existsByBook_Id(Long bookId);
}