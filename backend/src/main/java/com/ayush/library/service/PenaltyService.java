package com.ayush.library.service;

import com.ayush.library.model.Penalty;
import com.ayush.library.repository.PenaltyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PenaltyService {

    private final PenaltyRepository penaltyRepository;

    public PenaltyService(PenaltyRepository penaltyRepository) {
        this.penaltyRepository = penaltyRepository;
    }

    // Get all penalties
    public List<Penalty> getAllPenalties() {
        return penaltyRepository.findAll();
    }

    // Get penalty by ID
    public Optional<Penalty> getPenaltyById(Long id) {
        return penaltyRepository.findById(id);
    }

    // Get penalties for a specific student
    public List<Penalty> getPenaltiesByStudent(Long studentId) {
        return penaltyRepository.findByBorrowedBook_Student_Id(studentId);
    }

    // Get total penalty amount for a student
    public Double getTotalPenalty(Long studentId) {
        return getPenaltiesByStudent(studentId)
                .stream()
                .mapToDouble(Penalty::getAmount)
                .sum();
    }

    // Save a penalty
    public Penalty savePenalty(Penalty penalty) {
        return penaltyRepository.save(penalty);
    }

    // Delete a penalty
    public void deletePenalty(Long id) {
        penaltyRepository.deleteById(id);
    }
}