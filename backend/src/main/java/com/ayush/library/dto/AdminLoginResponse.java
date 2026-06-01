package com.ayush.library.dto;

public class AdminLoginResponse {
    private boolean success;
    private String message;
    private String sessionId;
    private String adminName;

    public AdminLoginResponse(boolean success, String message, String sessionId, String adminName) {
        this.success = success;
        this.message = message;
        this.sessionId = sessionId;
        this.adminName = adminName;
    }

    public boolean isSuccess() {
        return success;
    }
    public String getMessage() {
        return message;
    }
    public String getSessionId() {
        return sessionId;
    }
    public String getAdminName() {
        return adminName;
    }
}
