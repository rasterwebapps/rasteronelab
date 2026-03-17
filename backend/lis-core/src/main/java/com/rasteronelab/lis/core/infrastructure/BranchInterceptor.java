package com.rasteronelab.lis.core.infrastructure;

import com.rasteronelab.lis.core.common.exception.BranchAccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Intercepts HTTP requests to establish branch context from the X-Branch-Id header.
 *
 * FLOW:
 * 1. Extract X-Branch-Id header from the request
 * 2. Validate UUID format
 * 3. Validate user has access to the branch (check JWT branchIds claim)
 * 4. Set branch context in BranchContextHolder
 * 5. Always clear BranchContextHolder after request completes (prevent ThreadLocal leaks)
 *
 * Skips interception for actuator, health, and swagger endpoints.
 */
@Slf4j
@Component
public class BranchInterceptor implements HandlerInterceptor {

    private static final String BRANCH_HEADER = "X-Branch-Id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestUri = request.getRequestURI();

        if (shouldSkip(requestUri)) {
            return true;
        }

        String branchIdHeader = request.getHeader(BRANCH_HEADER);
        if (branchIdHeader == null || branchIdHeader.isBlank()) {
            log.debug("No X-Branch-Id header present for URI: {}", requestUri);
            return true;
        }

        UUID branchId = parseBranchId(branchIdHeader);
        validateBranchAccess(branchId);

        BranchContextHolder.setCurrentBranchId(branchId);
        log.debug("Branch context set to: {}", branchId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        BranchContextHolder.clear();
    }

    private boolean shouldSkip(String uri) {
        return uri.startsWith("/actuator")
                || uri.startsWith("/health")
                || uri.startsWith("/api-docs")
                || uri.startsWith("/swagger-ui");
    }

    private UUID parseBranchId(String branchIdHeader) {
        try {
            return UUID.fromString(branchIdHeader);
        } catch (IllegalArgumentException e) {
            throw new BranchAccessDeniedException("Invalid branch ID format: " + branchIdHeader);
        }
    }

    private void validateBranchAccess(UUID branchId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BranchAccessDeniedException(branchId);
        }

        if (!(authentication.getPrincipal() instanceof Jwt jwt)) {
            log.warn("Non-JWT authentication detected, denying branch access for security");
            throw new BranchAccessDeniedException("Branch validation requires JWT authentication");
        }

        List<String> branchIds = jwt.getClaimAsStringList("branchIds");
        if (branchIds == null) {
            branchIds = Collections.emptyList();
        }

        String targetBranchId = branchId.toString();
        boolean hasAccess = branchIds.contains(targetBranchId);

        if (!hasAccess) {
            log.warn("User {} attempted access to unauthorized branch: {}",
                    authentication.getName(), branchId);
            throw new BranchAccessDeniedException(branchId);
        }
    }
}
