package com.rasteronelab.lis.auth.config;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rasteronelab.lis.auth.application.service.TokenBlacklistService;

/**
 * Servlet filter that rejects requests carrying a blacklisted (logged-out) JWT
 * before Spring Security's JWT decoder has a chance to accept it.
 *
 * <p>Runs once per request. If the bearer token is found in the Redis blacklist
 * the request is terminated with {@code 401 Unauthorized}.</p>
 */
@Component
public class TokenBlacklistFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenBlacklistService tokenBlacklistService;

    public TokenBlacklistFilter(TokenBlacklistService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length());
            if (tokenBlacklistService.isBlacklisted(token)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write(
                        "{\"success\":false,\"errorCode\":\"LIS-AUTH-004\","
                        + "\"message\":\"Token has been revoked\","
                        + "\"timestamp\":\"" + java.time.Instant.now().toString() + "\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
