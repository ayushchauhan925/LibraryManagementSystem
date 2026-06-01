package com.ayush.library.controller.Auth;

import com.ayush.library.dto.AdminLoginRequest;
import com.ayush.library.dto.AdminLoginResponse;
import com.ayush.library.model.Admin;
import com.ayush.library.service.AdminAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/admin")
public class AdminAuthController {

    @Autowired
    private AdminAuthService adminAuthService;

    @PostMapping("/login")
    public AdminLoginResponse login(@RequestBody AdminLoginRequest request, HttpServletRequest httpRequest) {
        var adminOpt = adminAuthService.getAdminByEmail(request.getEmail());

        if (adminOpt.isEmpty() || !request.getPassword().equals(adminOpt.get().getPassword())) {
            return new AdminLoginResponse(false, "Invalid credentials", null, null);
        }

        Admin admin = adminOpt.get();
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("adminId", admin.getId());

        return new AdminLoginResponse(true, "Login successful", session.getId(), admin.getName());
    }

    @PostMapping("/logout")
    public AdminLoginResponse logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return new AdminLoginResponse(true, "Logout successful", null, null);
    }
}