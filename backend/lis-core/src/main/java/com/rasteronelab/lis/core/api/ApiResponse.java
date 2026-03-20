package com.rasteronelab.lis.core.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Standard API response wrapper for all LIS REST endpoints.
 *
 * Usage:
 *   return ResponseEntity.ok(ApiResponse.success(data));
 *   return ResponseEntity.ok(ApiResponse.success("Patient created", data));
 *   return ResponseEntity.badRequest().body(ApiResponse.error("LIS-PAT-001", "Patient not found"));
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private String errorCode;
    private String timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(now())
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(now())
                .build();
    }

    public static <T> ApiResponse<T> successMessage(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(now())
                .build();
    }

    public static <T> ApiResponse<T> error(String errorCode, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .errorCode(errorCode)
                .message(message)
                .timestamp(now())
                .build();
    }

    private static String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public ApiResponse() {
    }

    public ApiResponse(boolean success, String message, T data, String errorCode, String timestamp) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiResponse that = (ApiResponse) o;
        return java.util.Objects.equals(this.success, that.success) &&
               java.util.Objects.equals(this.message, that.message) &&
               java.util.Objects.equals(this.data, that.data) &&
               java.util.Objects.equals(this.errorCode, that.errorCode) &&
               java.util.Objects.equals(this.timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.success, this.message, this.data, this.errorCode, this.timestamp);
    }

    @Override
    public String toString() {
        return "ApiResponse{success=" + success +
               ", message=" + message +
               ", data=" + data +
               ", errorCode=" + errorCode +
               ", timestamp=" + timestamp +
               "}";
    }

    public static <T> ApiResponseBuilder<T> builder() {
        return new ApiResponseBuilder<>();
    }

    public static class ApiResponseBuilder<T> {
        private boolean success;
        private String message;
        private T data;
        private String errorCode;
        private String timestamp;

        ApiResponseBuilder() {
        }

        public ApiResponseBuilder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        public ApiResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ApiResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ApiResponseBuilder<T> errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public ApiResponseBuilder<T> timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ApiResponse<T> build() {
            return new ApiResponse<>(this.success, this.message, this.data, this.errorCode, this.timestamp);
        }
    }

}
