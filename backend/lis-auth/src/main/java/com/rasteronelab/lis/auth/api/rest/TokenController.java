package com.rasteronelab.lis.auth.api.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rasteronelab.lis.auth.application.service.TokenBlacklistService;
import com.rasteronelab.lis.core.api.ApiResponse;

/**
 * Provides token lifecycle endpoints: logout (revoke current token).
 *
 * <p>Since this service uses stateless JWT bearer tokens, traditional session
 * invalidation is not possible. Instead, logout adds the token's JTI to a
 * Redis blacklist with a TTL equal to the token's remaining lifetime. The
 * {@link com.rasteronelab.lis.auth.config.TokenBlacklistFilter} rejects any
 * subsequent request that presents a blacklisted token.</p>
 */
@RestController
@RequestMapping("/api/v1/auth")
public class TokenController {

    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenBlacklistService tokenBlacklistService;

    public TokenController(TokenBlacklistService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    /**
     * Logs out the currently authenticated user by revoking their JWT.
     *
     * <p>The token's JTI is stored in Redis with a TTL matching the token's remaining
     * validity. Any further request presenting the same token will receive HTTP 401.</p>
     *
     * @param authorizationHeader the raw Authorization header (Bearer &lt;token&gt;)
     * @param jwt                 the decoded JWT of the current user (injected by Spring Security)
     */
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader("Authorization") String authorizationHeader,
            @AuthenticationPrincipal Jwt jwt) {

        String rawToken = authorizationHeader.startsWith(BEARER_PREFIX)
                ? authorizationHeader.substring(BEARER_PREFIX.length())
                : authorizationHeader;

        tokenBlacklistService.blacklist(jwt, rawToken);

        return ResponseEntity.ok(ApiResponse.successMessage("Logged out successfully"));
    }
}
