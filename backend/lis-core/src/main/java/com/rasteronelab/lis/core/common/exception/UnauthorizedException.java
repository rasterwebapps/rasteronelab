package com.rasteronelab.lis.core.common.exception;

/**
 * Thrown when a user attempts an action they are not authorized to perform.
 * Maps to HTTP 403 Forbidden.
 */
public class UnauthorizedException extends LisException {

    public UnauthorizedException(String message) {
        super("LIS-SEC-001", message);
    }

    public UnauthorizedException(String action, String resource) {
        super("LIS-SEC-001", "Not authorized to " + action + " " + resource);
    }
}
