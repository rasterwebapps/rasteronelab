package com.rasteronelab.lis.core.api;

import com.rasteronelab.lis.core.common.exception.BranchAccessDeniedException;
import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.DuplicateResourceException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.common.exception.UnauthorizedException;
import com.rasteronelab.lis.core.common.exception.ValidationException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Global exception handler for all LIS REST endpoints.
 * Converts exceptions into standardized {@link ApiResponse} error responses.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(NotFoundException ex) {
        String correlationId = generateCorrelationId();
        log.warn("[{}] Resource not found: {}", correlationId, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(ValidationException ex) {
        String correlationId = generateCorrelationId();
        log.warn("[{}] Validation failed: {}", correlationId, ex.getMessage());

        Map<String, String> fieldErrors = ex.getFieldErrors();
        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .success(false)
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .data(fieldErrors.isEmpty() ? null : fieldErrors)
                .timestamp(now())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateResourceException(DuplicateResourceException ex) {
        String correlationId = generateCorrelationId();
        log.warn("[{}] Duplicate resource: {}", correlationId, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessRuleException(BusinessRuleException ex) {
        String correlationId = generateCorrelationId();
        log.warn("[{}] Business rule violation: {}", correlationId, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(BranchAccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleBranchAccessDeniedException(BranchAccessDeniedException ex) {
        String correlationId = generateCorrelationId();
        log.warn("[{}] Branch access denied: {}", correlationId, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedException(UnauthorizedException ex) {
        String correlationId = generateCorrelationId();
        log.warn("[{}] Unauthorized: {}", correlationId, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        String correlationId = generateCorrelationId();

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage()));

        log.warn("[{}] Bean validation failed: {}", correlationId, fieldErrors);

        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .success(false)
                .errorCode("LIS-SYS-002")
                .message("Validation failed")
                .data(fieldErrors)
                .timestamp(now())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolation(
            ConstraintViolationException ex) {
        String correlationId = generateCorrelationId();

        Map<String, String> fieldErrors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage(),
                        (existing, replacement) -> existing
                ));

        log.warn("[{}] Constraint violation: {}", correlationId, fieldErrors);

        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .success(false)
                .errorCode("LIS-SYS-002")
                .message("Constraint violation")
                .data(fieldErrors)
                .timestamp(now())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        String correlationId = generateCorrelationId();
        log.warn("[{}] Access denied: {}", correlationId, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("LIS-SEC-001", "Access denied"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        String correlationId = generateCorrelationId();
        log.error("[{}] Unexpected error: {}", correlationId, ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("LIS-SYS-999", "An unexpected error occurred"));
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

    private static String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
