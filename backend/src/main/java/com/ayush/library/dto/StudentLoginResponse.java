package com.ayush.library.dto;

public class StudentLoginResponse {
    private boolean success;
    private String message;
    private String sessionId;
    private String studentName;

    public StudentLoginResponse(boolean success, String message, String sessionId, String studentName) {
        this.success = success;
        this.message = message;
        this.sessionId = sessionId;
        this.studentName = studentName;
    }

    // Getters & Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
}
