package com.ing.andreea.teastore.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(TeaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTeaNotFound(TeaNotFoundException ex) {
        logger.warn("Tea not found: {}", ex.getMessage());

        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    @ExceptionHandler(DuplicateTeaException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateTea(DuplicateTeaException ex) {
        logger.warn("Duplicate tea: {}", ex.getMessage());

        return buildResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String error, String message) {
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .error(error)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, status);
    }
}

