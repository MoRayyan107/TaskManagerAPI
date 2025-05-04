package com.manager.TaskManagerAPI.constants;

/**
 * Centralized constants for the Task Manager API.
 * Helps avoid hardcoding values and improves maintainability.
 */
public class AppConstants {

    private AppConstants() {
        // Prevent instantiation
    }
    // JWT Configuration
    public static final String SECRET_KEY = "YOUR_GENERATED_SECURE_KEY_HERE"; // In production, load from environment/config
    public static final long JWT_EXPIRATION = 1000 * 60 * 60 * 10; // 10 hours in milliseconds

    // HTTP Headers
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    //  User Roles (For future use)
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    // Custom Error Messages
    public static final String TOKEN_INVALID = "Token is invalid or expired";
    public static final String UNAUTHORIZED_ACCESS = "You are not authorized to access this resource";
    public static final String TASK_NOT_FOUND = "Task not found with given ID";
    public static final String USER_NOT_FOUND = "User not found";

    // Pagination Defaults (if used globally)
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String DEFAULT_SORT_BY = "title";
    public static final String DEFAULT_SORT_DIRECTION = "asc";
}
