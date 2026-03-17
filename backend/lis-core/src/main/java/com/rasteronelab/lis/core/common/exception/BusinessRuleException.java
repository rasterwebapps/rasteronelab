package com.rasteronelab.lis.core.common.exception;

/**
 * Thrown when a business rule is violated.
 * Maps to HTTP 422 Unprocessable Entity.
 */
public class BusinessRuleException extends LisException {

    public BusinessRuleException(String message) {
        super("LIS-BIZ-001", message);
    }

    public BusinessRuleException(String errorCode, String message) {
        super(errorCode, message);
    }
}
