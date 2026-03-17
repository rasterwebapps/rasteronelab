package com.rasteronelab.lis.core.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ApiResponse")
class ApiResponseTest {

    @Test
    @DisplayName("success() should create response with success=true, data set, no errorCode")
    void success_shouldCreateSuccessResponse() {
        String data = "test-data";

        ApiResponse<String> response = ApiResponse.success(data);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEqualTo("test-data");
        assertThat(response.getErrorCode()).isNull();
        assertThat(response.getMessage()).isNull();
    }

    @Test
    @DisplayName("success(message, data) should create response with message and data")
    void successWithMessage_shouldCreateSuccessResponseWithMessage() {
        String message = "Created successfully";
        String data = "patient-123";

        ApiResponse<String> response = ApiResponse.success(message, data);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Created successfully");
        assertThat(response.getData()).isEqualTo("patient-123");
        assertThat(response.getErrorCode()).isNull();
    }

    @Test
    @DisplayName("successMessage() should create response with message only, no data")
    void successMessage_shouldCreateSuccessMessageOnly() {
        ApiResponse<Void> response = ApiResponse.successMessage("Operation completed");

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Operation completed");
        assertThat(response.getData()).isNull();
        assertThat(response.getErrorCode()).isNull();
    }

    @Test
    @DisplayName("error() should create response with success=false, errorCode set, no data")
    void error_shouldCreateErrorResponse() {
        ApiResponse<Void> response = ApiResponse.error("LIS-PAT-001", "Patient not found");

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getErrorCode()).isEqualTo("LIS-PAT-001");
        assertThat(response.getMessage()).isEqualTo("Patient not found");
        assertThat(response.getData()).isNull();
    }

    @Test
    @DisplayName("All factory methods should set a non-null timestamp")
    void allResponses_shouldHaveTimestamp() {
        ApiResponse<String> success = ApiResponse.success("data");
        ApiResponse<String> successMsg = ApiResponse.success("msg", "data");
        ApiResponse<Void> msgOnly = ApiResponse.successMessage("msg");
        ApiResponse<Void> error = ApiResponse.error("ERR", "error");

        assertThat(success.getTimestamp()).isNotNull().isNotBlank();
        assertThat(successMsg.getTimestamp()).isNotNull().isNotBlank();
        assertThat(msgOnly.getTimestamp()).isNotNull().isNotBlank();
        assertThat(error.getTimestamp()).isNotNull().isNotBlank();
    }
}
