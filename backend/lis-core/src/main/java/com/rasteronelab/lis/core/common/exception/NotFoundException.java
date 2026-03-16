package com.rasteronelab.lis.core.common.exception;

import java.util.UUID;

/**
 * Thrown when a requested resource is not found (or not accessible in current branch).
 * Maps to HTTP 404.
 */
public class NotFoundException extends LisException {

    public NotFoundException(String resourceName, UUID id) {
        super("LIS-SYS-001", resourceName + " not found with id: " + id);
    }

    public NotFoundException(String resourceName, String identifier) {
        super("LIS-SYS-001", resourceName + " not found: " + identifier);
    }

    public NotFoundException(String errorCode, String resourceName, UUID id) {
        super(errorCode, resourceName + " not found with id: " + id);
    }
}
