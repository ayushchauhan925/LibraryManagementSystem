package com.ayush.library.controller.Auth;

import com.ayush.library.dto.StudentLoginRequest;
import com.ayush.library.dto.StudentLoginResponse;
import com.ayush.library.model.Student;
import com.ayush.library.service.StudentAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@RestController
@RequestMapping("/auth/student")
public class StudentAuthController {

    @Autowired
    private StudentAuthService studentAuthService;

    @PostMapping("/login")
    public ResponseEntity<StudentLoginResponse> login(
            @RequestBody StudentLoginRequest request,
            HttpServletRequest httpRequest) {

        var studentOpt = studentAuthService.getStudentByEmail(request.getEmail());
        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(401)
                    .body(new StudentLoginResponse(false, "Invalid credentials", null, null));
        }

        Student student = studentOpt.get();
        if (!studentAuthService.checkPassword(request.getPassword(), student.getPassword())) {
            return ResponseEntity.status(401)
                    .body(new StudentLoginResponse(false, "Invalid credentials", null, null));
        }

        // Create Spring Security authentication tied to session
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                student.getEmail(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
        session.setAttribute("studentId", student.getId());

        return ResponseEntity.ok(
                new StudentLoginResponse(true, "Login successful", session.getId(), student.getName())
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<StudentLoginResponse> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return ResponseEntity.ok(new StudentLoginResponse(true, "Logout successful", null, null));
    }
}