package com.ayush.library.repository;

import com.ayush.library.model.BorrowRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowRequestRepository extends JpaRepository<BorrowRequest, Long> {
    List<BorrowRequest> findByStatus(String status);
    List<BorrowRequest> findByStudentId(Long studentId);
}
