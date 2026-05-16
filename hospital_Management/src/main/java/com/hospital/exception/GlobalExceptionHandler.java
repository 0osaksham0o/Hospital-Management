package com.hospital.exception;


import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the Hospital Management REST API.
 * Converts exceptions into standardised JSON error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ------------------------------------------------------------------ */
    /*  404 – Resource Not Found                                           */
    /* ------------------------------------------------------------------ */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /* ------------------------------------------------------------------ */
    /*  409 – Conflict / Already Exists                                    */
    /* ------------------------------------------------------------------ */
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyExists(AlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Catches DB-level unique constraint / FK violations as a fallback 409.
     * (e.g. duplicate primary key inserted from a code path that skips the
     * service-layer existsById check)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        String root = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();
        // Distinguish duplicate-key violations from other constraint errors
        if (root != null && (root.toLowerCase().contains("duplicate")
                || root.toLowerCase().contains("unique")
                || root.toLowerCase().contains("primary key"))) {
            return buildResponse(HttpStatus.CONFLICT, "Data already exists in the database.");
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "Incorrect data: database constraint violated. " + root);
    }

    /* ------------------------------------------------------------------ */
    /*  400 – Validation / Bad Request                                     */
    /* ------------------------------------------------------------------ */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder("Validation failed: ");
        ex.getBindingResult().getFieldErrors()
          .forEach(e -> message.append(e.getField())
                               .append(" – ")
                               .append(e.getDefaultMessage())
                               .append("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, message.toString());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /** Handles malformed JSON body (e.g. wrong types, unparseable dates). */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleUnreadable(HttpMessageNotReadableException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST,
                "Incorrect data: malformed or unreadable request body. " + ex.getMessage());
    }

    /** Handles Integer.parseInt / Long.parseLong failures on request parameters. */
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Map<String, Object>> handleNumberFormat(NumberFormatException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST,
                "Incorrect data: invalid number format – " + ex.getMessage());
    }

    /** Handles LocalDateTime.parse failures and similar argument errors. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST,
                "Incorrect data: " + ex.getMessage());
    }

    /* ------------------------------------------------------------------ */
    /*  500 – Internal Server Error (last-resort catch-all)               */
    /* ------------------------------------------------------------------ */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                             "An unexpected error occurred: " + ex.getMessage());
    }

    /* ------------------------------------------------------------------ */
    /*  Helper                                                             */
    /* ------------------------------------------------------------------ */
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timeStamp", LocalDate.now().toString());
        body.put("status", status.value());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}
