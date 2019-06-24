package dev.rouchy.roundUp.models;

public class ApiError extends RuntimeException {
    public ApiError(String message, Throwable cause) {
        super(message, cause);
    }
}
