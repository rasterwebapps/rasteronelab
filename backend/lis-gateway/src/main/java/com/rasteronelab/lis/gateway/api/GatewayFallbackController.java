package com.rasteronelab.lis.gateway.api;

import com.rasteronelab.lis.core.api.ApiResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Fallback controller for the gateway circuit breaker.
 * Returns a standard error response when a downstream service is unavailable.
 */
@RestController
@Slf4j
public class GatewayFallbackController {

    @GetMapping("/fallback")
    public ResponseEntity<ApiResponse<Void>> fallbackGet() {
        return fallbackResponse();
    }

    @PostMapping("/fallback")
    public ResponseEntity<ApiResponse<Void>> fallbackPost() {
        return fallbackResponse();
    }

    private ResponseEntity<ApiResponse<Void>> fallbackResponse() {
        log.warn("Circuit breaker triggered — returning fallback response");
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.error("LIS-GW-001", "Service temporarily unavailable"));
    }
}
