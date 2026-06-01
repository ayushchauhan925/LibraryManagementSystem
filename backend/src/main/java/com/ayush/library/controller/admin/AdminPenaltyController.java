package com.ayush.library.controller.admin;

import com.ayush.library.model.Penalty;
import com.ayush.library.service.PenaltyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/penalties")
public class AdminPenaltyController {

    @Autowired
    private PenaltyService penaltyService;

    /** Get all penalties */
    @GetMapping
    public ResponseEntity<List<Penalty>> getAllPenalties() {
        return ResponseEntity.ok(penaltyService.getAllPenalties());
    }

    /** Get penalties for a specific student */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Penalty>> getPenaltiesByStudent(@PathVariable Long studentId) {
        List<Penalty> penalties = penaltyService.getPenaltiesByStudent(studentId);
        if (penalties.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(penalties);
    }

    /** Get penalty by ID */
    @GetMapping("/{id}")
    public ResponseEntity<Penalty> getPenaltyById(@PathVariable Long id) {
        return penaltyService.getPenaltyById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}