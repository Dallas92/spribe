package com.example.spribe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

//@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    public Map<String, Object> handleCustomException(ConflictException ex) {
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.CONFLICT.value(),
                "error", "Conflict",
                "message", ex.getMessage()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public Map<String, Object> handleCustomException(NotFoundException ex) {
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "Not found",
                "message", ex.getMessage()
        );
    }

    @ExceptionHandler(OperationForbiddenException.class)
    public Map<String, Object> handleCustomException(OperationForbiddenException ex) {
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.FORBIDDEN.value(),
                "error", "Forbidden",
                "message", ex.getMessage()
        );
    }
}
