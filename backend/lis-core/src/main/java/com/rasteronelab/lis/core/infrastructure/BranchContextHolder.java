package com.rasteronelab.lis.core.infrastructure;

import com.rasteronelab.lis.core.common.exception.BranchAccessDeniedException;

import java.util.UUID;

/**
 * ThreadLocal holder for the current branch context.
 *
 * USAGE:
 * - Set in BranchInterceptor before request processing
 * - Get in service layer to scope all queries to current branch
 * - Clear in BranchInterceptor after request (or in finally block)
 *
 * RULES:
 * - ALWAYS call clear() in finally block to prevent thread leaks
 * - NEVER use in async/reactive code without explicitly passing branchId
 */
public final class BranchContextHolder {

    private static final ThreadLocal<UUID> BRANCH_CONTEXT = new ThreadLocal<>();

    private BranchContextHolder() {
        // Utility class - no instantiation
    }

    public static void setCurrentBranchId(UUID branchId) {
        if (branchId == null) {
            throw new IllegalArgumentException("Branch ID cannot be null");
        }
        BRANCH_CONTEXT.set(branchId);
    }

    public static UUID getCurrentBranchId() {
        UUID branchId = BRANCH_CONTEXT.get();
        if (branchId == null) {
            throw new BranchAccessDeniedException("No branch context set. Ensure X-Branch-Id header is present.");
        }
        return branchId;
    }

    public static UUID getCurrentBranchIdOrNull() {
        return BRANCH_CONTEXT.get();
    }

    public static boolean hasBranchContext() {
        return BRANCH_CONTEXT.get() != null;
    }

    public static void clear() {
        BRANCH_CONTEXT.remove();
    }
}
