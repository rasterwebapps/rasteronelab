package com.rasteronelab.lis.core.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LIS Exception classes")
class ExceptionTest {

    @Test
    @DisplayName("LisException should carry errorCode and message")
    void lisException_shouldHaveErrorCode() {
        LisException ex = new LisException("LIS-ERR-001", "Something failed");

        assertThat(ex.getErrorCode()).isEqualTo("LIS-ERR-001");
        assertThat(ex.getMessage()).isEqualTo("Something failed");
    }

    @Test
    @DisplayName("LisException with cause should preserve cause chain")
    void lisException_shouldPreserveCause() {
        RuntimeException cause = new RuntimeException("root cause");
        LisException ex = new LisException("LIS-ERR-001", "Wrapper", cause);

        assertThat(ex.getCause()).isEqualTo(cause);
        assertThat(ex.getErrorCode()).isEqualTo("LIS-ERR-001");
    }

    @Test
    @DisplayName("NotFoundException should format message with resource and id")
    void notFoundException_shouldHaveCorrectMessage() {
        UUID id = UUID.randomUUID();
        NotFoundException ex = new NotFoundException("Patient", id);

        assertThat(ex.getErrorCode()).isEqualTo("LIS-SYS-001");
        assertThat(ex.getMessage()).isEqualTo("Patient not found with id: " + id);
    }

    @Test
    @DisplayName("NotFoundException with string identifier should format correctly")
    void notFoundException_shouldSupportStringIdentifier() {
        NotFoundException ex = new NotFoundException("Patient", "MRN-12345");

        assertThat(ex.getErrorCode()).isEqualTo("LIS-SYS-001");
        assertThat(ex.getMessage()).isEqualTo("Patient not found: MRN-12345");
    }

    @Test
    @DisplayName("NotFoundException with custom errorCode should use provided code")
    void notFoundException_shouldSupportCustomErrorCode() {
        UUID id = UUID.randomUUID();
        NotFoundException ex = new NotFoundException("LIS-PAT-001", "Patient", id);

        assertThat(ex.getErrorCode()).isEqualTo("LIS-PAT-001");
    }

    @Test
    @DisplayName("ValidationException should support field errors map")
    void validationException_shouldSupportFieldErrors() {
        Map<String, String> fieldErrors = Map.of(
                "email", "Invalid email format",
                "phone", "Phone is required"
        );
        ValidationException ex = new ValidationException(fieldErrors);

        assertThat(ex.getErrorCode()).isEqualTo("LIS-SYS-002");
        assertThat(ex.getFieldErrors()).hasSize(2);
        assertThat(ex.getFieldErrors()).containsEntry("email", "Invalid email format");
        assertThat(ex.getFieldErrors()).containsEntry("phone", "Phone is required");
        assertThat(ex.getErrors()).isEmpty();
    }

    @Test
    @DisplayName("ValidationException with single message should populate errors list")
    void validationException_shouldSupportSingleMessage() {
        ValidationException ex = new ValidationException("Age must be positive");

        assertThat(ex.getErrorCode()).isEqualTo("LIS-SYS-002");
        assertThat(ex.getMessage()).isEqualTo("Age must be positive");
        assertThat(ex.getErrors()).containsExactly("Age must be positive");
        assertThat(ex.getFieldErrors()).isEmpty();
    }

    @Test
    @DisplayName("DuplicateResourceException should format message correctly")
    void duplicateResourceException_shouldHaveCorrectMessage() {
        DuplicateResourceException ex = new DuplicateResourceException("Patient", "email", "test@example.com");

        assertThat(ex.getErrorCode()).isEqualTo("LIS-SYS-003");
        assertThat(ex.getMessage()).isEqualTo("Patient already exists with email: test@example.com");
    }

    @Test
    @DisplayName("DuplicateResourceException with simple message should work")
    void duplicateResourceException_shouldSupportSimpleMessage() {
        DuplicateResourceException ex = new DuplicateResourceException("Duplicate entry");

        assertThat(ex.getErrorCode()).isEqualTo("LIS-SYS-003");
        assertThat(ex.getMessage()).isEqualTo("Duplicate entry");
    }

    @Test
    @DisplayName("BusinessRuleException should carry message and default error code")
    void businessRuleException_shouldHaveCorrectMessage() {
        BusinessRuleException ex = new BusinessRuleException("Cannot cancel completed order");

        assertThat(ex.getErrorCode()).isEqualTo("LIS-BIZ-001");
        assertThat(ex.getMessage()).isEqualTo("Cannot cancel completed order");
    }

    @Test
    @DisplayName("BusinessRuleException with custom errorCode should use provided code")
    void businessRuleException_shouldSupportCustomErrorCode() {
        BusinessRuleException ex = new BusinessRuleException("LIS-BIZ-100", "Custom rule violated");

        assertThat(ex.getErrorCode()).isEqualTo("LIS-BIZ-100");
    }

    @Test
    @DisplayName("BranchAccessDeniedException with UUID should format message")
    void branchAccessDeniedException_shouldHaveCorrectMessage() {
        UUID branchId = UUID.randomUUID();
        BranchAccessDeniedException ex = new BranchAccessDeniedException(branchId);

        assertThat(ex.getErrorCode()).isEqualTo("LIS-SEC-002");
        assertThat(ex.getMessage()).isEqualTo("Access denied to branch: " + branchId);
    }

    @Test
    @DisplayName("BranchAccessDeniedException with string message should work")
    void branchAccessDeniedException_shouldSupportStringMessage() {
        BranchAccessDeniedException ex = new BranchAccessDeniedException("No branch context");

        assertThat(ex.getErrorCode()).isEqualTo("LIS-SEC-002");
        assertThat(ex.getMessage()).isEqualTo("No branch context");
    }

    @Test
    @DisplayName("UnauthorizedException should format action and resource")
    void unauthorizedException_shouldHaveCorrectMessage() {
        UnauthorizedException ex = new UnauthorizedException("delete", "patient records");

        assertThat(ex.getErrorCode()).isEqualTo("LIS-SEC-001");
        assertThat(ex.getMessage()).isEqualTo("Not authorized to delete patient records");
    }

    @Test
    @DisplayName("UnauthorizedException with simple message should work")
    void unauthorizedException_shouldSupportSimpleMessage() {
        UnauthorizedException ex = new UnauthorizedException("Insufficient permissions");

        assertThat(ex.getErrorCode()).isEqualTo("LIS-SEC-001");
        assertThat(ex.getMessage()).isEqualTo("Insufficient permissions");
    }
}
