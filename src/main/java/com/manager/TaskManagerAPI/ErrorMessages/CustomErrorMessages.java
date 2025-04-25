package com.manager.TaskManagerAPI.ErrorMessages;

import java.time.LocalDateTime;

public class CustomErrorMessages {

    private String message;
    private int status;
    private String path;
    private LocalDateTime date;

    public CustomErrorMessages(String message, int status, String path) {
        this.message = message;
        this.status = status;
        this.path = path;
        this.date = LocalDateTime.now(); // give current system time
    }

    public String getMessage() {return message;}
    public String getPath() {return path;}
    public int getStatus() {return status;}
    public LocalDateTime getDate() {return date;}
}
