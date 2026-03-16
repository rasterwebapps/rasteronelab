package com.rasteronelab.lis.core.common.exception;

import lombok.Getter;

/**
 * Base exception for all LIS application exceptions.
 */
@Getter
public class LisException extends RuntimeException {

    private final String errorCode;

    public LisException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public LisException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
