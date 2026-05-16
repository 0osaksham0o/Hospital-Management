package com.hospital.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a client attempts to create a resource that already exists.
 * Results in a 409 CONFLICT HTTP response.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String message) {
        super(message);
    }

    public AlreadyExistsException(String resourceName, Object id) {
        super(resourceName + " with ID " + id + " already exists in the database.");
    }
}
