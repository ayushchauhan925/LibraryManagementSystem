package com.ayush.library.service;

import com.ayush.library.model.BorrowRequest;
import com.ayush.library.repository.BorrowRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowRequestService {

    @Autowired
    private BorrowRequestRepository borrowRequestRepository;

    // Existing methods
    public List<BorrowRequest> getAllRequests() {
        return borrowRequestRepository.findAll();
    }

    public Optional<BorrowRequest> getRequestById(Long id) {
        return borrowRequestRepository.findById(id);
    }

    public List<BorrowRequest> getRequestsByStatus(String status) {
        return borrowRequestRepository.findByStatus(status);
    }

    public List<BorrowRequest> getRequestsByStudentId(Long studentId) {
        return borrowRequestRepository.findByStudentId(studentId);
    }

    public BorrowRequest saveRequest(BorrowRequest request) {
        return borrowRequestRepository.save(request);
    }

    public void deleteRequest(Long id) {
        borrowRequestRepository.deleteById(id);
    }

    // ✅ New method: createBorrowRequest for student controller
    public BorrowRequest createBorrowRequest(Long studentId, Long bookId) {
        BorrowRequest request = new BorrowRequest();
        request.setStudentId(studentId);
        request.setBookId(bookId);
        request.setStatus("PENDING");
        request.setRequestDate(LocalDateTime.now());
        return borrowRequestRepository.save(request);
    }

    // ✅ New method: getRequestsByStudent for student controller
    public List<BorrowRequest> getRequestsByStudent(Long studentId) {
        return borrowRequestRepository.findByStudentId(studentId);
    }
}