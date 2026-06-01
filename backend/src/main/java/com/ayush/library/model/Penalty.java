package com.ayush.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "penalties")
public class Penalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Direct link to BorrowedBook (which already has student + book)
    @OneToOne
    @JoinColumn(name = "borrowed_book_id", nullable = false)
    private BorrowedBook borrowedBook;

    private double amount;

    private String reason; // e.g., "Late Return", "Lost Book"

    // Snapshot of important dates from BorrowedBook
    private LocalDate dueDate;
    private LocalDate returnDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Penalty() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // === Getters & Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BorrowedBook getBorrowedBook() {
        return borrowedBook;
    }

    public void setBorrowedBook(BorrowedBook borrowedBook) {
        this.borrowedBook = borrowedBook;

        // auto-copy dueDate & returnDate when borrowedBook is linked
        if (borrowedBook != null) {
            this.dueDate = borrowedBook.getDueDate();
            this.returnDate = borrowedBook.getReturnDate();
        }
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
