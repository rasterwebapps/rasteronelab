package com.rasteronelab.lis.core.common.exception;

/**
 * Thrown when attempting to create a resource that already exists.
 * Maps to HTTP 409 Conflict.
 */
public class DuplicateResourceException extends LisException {

    public DuplicateResourceException(String resourceName, String fieldName, String fieldValue) {
        super("LIS-SYS-003", resourceName + " already exists with " + fieldName + ": " + fieldValue);
    }

    public DuplicateResourceException(String message) {
        super("LIS-SYS-003", message);
    }
}
