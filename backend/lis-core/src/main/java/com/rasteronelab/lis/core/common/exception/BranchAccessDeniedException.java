package com.rasteronelab.lis.core.common.exception;

import java.util.UUID;

/**
 * Thrown when a user attempts to access data from a branch they don't have access to.
 * Maps to HTTP 403 Forbidden.
 */
public class BranchAccessDeniedException extends LisException {

    public BranchAccessDeniedException(UUID branchId) {
        super("LIS-SEC-002", "Access denied to branch: " + branchId);
    }

    public BranchAccessDeniedException(String message) {
        super("LIS-SEC-002", message);
    }
}
