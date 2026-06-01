package com.ayush.library.repository;

import com.ayush.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Check if a book exists by ISBN
    boolean existsByIsbn(String isbn);

    // Fetch a book by ISBN
    Optional<Book> findByIsbn(String isbn);

    // 📌 Fetch only books that have available copies
    List<Book> findByAvailableCopiesGreaterThan(int copies);
}
