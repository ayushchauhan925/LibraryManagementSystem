package com.ayush.library.controller.Student;

import com.ayush.library.model.Penalty;
import com.ayush.library.service.PenaltyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentPenaltyController {

    private final PenaltyService penaltyService;

    public StudentPenaltyController(PenaltyService penaltyService) {
        this.penaltyService = penaltyService;
    }

    @GetMapping("/penalties")
    public List<Penalty> getPenalties(HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        return penaltyService.getPenaltiesByStudent(studentId);
    }

    @GetMapping("/penalties/total")
    public Double getTotalPenalty(HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        return penaltyService.getTotalPenalty(studentId);
    }
}