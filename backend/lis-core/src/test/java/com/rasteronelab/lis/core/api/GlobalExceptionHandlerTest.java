package com.rasteronelab.lis.core.api;

import com.rasteronelab.lis.core.common.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("handleNotFoundException should return 404 with error details")
    void handleNotFoundException_shouldReturn404() {
        UUID id = UUID.randomUUID();
        NotFoundException ex = new NotFoundException("Patient", id);

        ResponseEntity<ApiResponse<Void>> response = handler.handleNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getErrorCode()).isEqualTo("LIS-SYS-001");
        assertThat(response.getBody().getMessage()).contains("Patient not found with id:");
    }

    @Test
    @DisplayName("handleValidationException should return 422 with field errors")
    void handleValidationException_shouldReturn422() {
        Map<String, String> fieldErrors = Map.of("email", "Invalid email format");
        ValidationException ex = new ValidationException(fieldErrors);

        ResponseEntity<ApiResponse<Map<String, String>>> response = handler.handleValidationException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getErrorCode()).isEqualTo("LIS-SYS-002");
        assertThat(response.getBody().getData()).containsEntry("email", "Invalid email format");
    }

    @Test
    @DisplayName("handleDuplicateResourceException should return 409")
    void handleDuplicateResourceException_shouldReturn409() {
        DuplicateResourceException ex = new DuplicateResourceException("Patient", "email", "test@example.com");

        ResponseEntity<ApiResponse<Void>> response = handler.handleDuplicateResourceException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getErrorCode()).isEqualTo("LIS-SYS-003");
        assertThat(response.getBody().getMessage()).contains("Patient already exists");
    }

    @Test
    @DisplayName("handleBusinessRuleException should return 422")
    void handleBusinessRuleException_shouldReturn422() {
        BusinessRuleException ex = new BusinessRuleException("Cannot cancel completed order");

        ResponseEntity<ApiResponse<Void>> response = handler.handleBusinessRuleException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getErrorCode()).isEqualTo("LIS-BIZ-001");
        assertThat(response.getBody().getMessage()).isEqualTo("Cannot cancel completed order");
    }

    @Test
    @DisplayName("handleBranchAccessDeniedException should return 403")
    void handleBranchAccessDeniedException_shouldReturn403() {
        UUID branchId = UUID.randomUUID();
        BranchAccessDeniedException ex = new BranchAccessDeniedException(branchId);

        ResponseEntity<ApiResponse<Void>> response = handler.handleBranchAccessDeniedException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getErrorCode()).isEqualTo("LIS-SEC-002");
        assertThat(response.getBody().getMessage()).contains("Access denied to branch");
    }

    @Test
    @DisplayName("handleUnauthorizedException should return 403")
    void handleUnauthorizedException_shouldReturn403() {
        UnauthorizedException ex = new UnauthorizedException("delete", "patient records");

        ResponseEntity<ApiResponse<Void>> response = handler.handleUnauthorizedException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getErrorCode()).isEqualTo("LIS-SEC-001");
        assertThat(response.getBody().getMessage()).contains("Not authorized to delete patient records");
    }

    @Test
    @DisplayName("handleMethodArgumentNotValid should return 400 with field errors")
    void handleMethodArgumentNotValid_shouldReturn400() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "name", "must not be blank"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ApiResponse<Map<String, String>>> response = handler.handleMethodArgumentNotValid(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getErrorCode()).isEqualTo("LIS-SYS-002");
        assertThat(response.getBody().getData()).containsEntry("name", "must not be blank");
    }

    @Test
    @DisplayName("handleAccessDeniedException should return 403")
    void handleAccessDeniedException_shouldReturn403() {
        AccessDeniedException ex = new AccessDeniedException("Forbidden");

        ResponseEntity<ApiResponse<Void>> response = handler.handleAccessDeniedException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getErrorCode()).isEqualTo("LIS-SEC-001");
        assertThat(response.getBody().getMessage()).isEqualTo("Access denied");
    }

    @Test
    @DisplayName("handleGenericException should return 500")
    void handleGenericException_shouldReturn500() {
        Exception ex = new RuntimeException("Something went wrong");

        ResponseEntity<ApiResponse<Void>> response = handler.handleGenericException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getErrorCode()).isEqualTo("LIS-SYS-999");
        assertThat(response.getBody().getMessage()).isEqualTo("An unexpected error occurred");
    }
}
