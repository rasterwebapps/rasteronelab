package com.rasteronelab.lis.core.common.exception;

import java.util.List;
import java.util.Map;

/**
 * Thrown when business validation fails.
 * Maps to HTTP 422 Unprocessable Entity.
 */
public class ValidationException extends LisException {

    private final List<String> errors;
    private final Map<String, String> fieldErrors;

    public ValidationException(String message) {
        super("LIS-SYS-002", message);
        this.errors = List.of(message);
        this.fieldErrors = Map.of();
    }

    public ValidationException(String errorCode, String message) {
        super(errorCode, message);
        this.errors = List.of(message);
        this.fieldErrors = Map.of();
    }

    public ValidationException(List<String> errors) {
        super("LIS-SYS-002", String.join("; ", errors));
        this.errors = errors;
        this.fieldErrors = Map.of();
    }

    public ValidationException(Map<String, String> fieldErrors) {
        super("LIS-SYS-002", "Validation failed: " + fieldErrors);
        this.errors = List.of();
        this.fieldErrors = fieldErrors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
