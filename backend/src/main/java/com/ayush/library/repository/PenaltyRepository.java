package com.ayush.library.repository;

import com.ayush.library.model.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

    // 🔹 Get all penalties for a given student (through BorrowedBook → Student)
    List<Penalty> findByBorrowedBook_Student_Id(Long studentId);
}
