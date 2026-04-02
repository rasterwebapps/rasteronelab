package com.rasteronelab.lis.auth.api.rest;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rasteronelab.lis.auth.api.dto.CreateUserRequest;
import com.rasteronelab.lis.auth.api.dto.ResetPasswordRequest;
import com.rasteronelab.lis.auth.api.dto.UserResponse;
import com.rasteronelab.lis.auth.application.service.KeycloakAdminService;
import com.rasteronelab.lis.core.api.ApiResponse;

/**
 * REST endpoints for Keycloak user management.
 *
 * <p>All password operations are delegated entirely to Keycloak:
 * no credential data is ever written to the LIS PostgreSQL database.
 * Keycloak stores passwords as PBKDF2-SHA256 hashes with per-user salts.</p>
 *
 * <p>All endpoints require the {@code ROLE_ADMIN} authority.</p>
 */
@RestController
@RequestMapping("/api/v1/auth/users")
public class UserManagementController {

    private final KeycloakAdminService keycloakAdminService;

    public UserManagementController(KeycloakAdminService keycloakAdminService) {
        this.keycloakAdminService = keycloakAdminService;
    }

    /**
     * Creates a new user in Keycloak with a temporary initial password.
     *
     * <p>The password is forwarded over TLS to Keycloak and is hashed before storage.
     * The user must change it on first login.</p>
     *
     * @param request user details including temporary password
     * @return HTTP 201 with the created user's profile (no password field)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        UserResponse user = keycloakAdminService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", user));
    }

    /**
     * Retrieves a user by their Keycloak user ID.
     *
     * @param userId Keycloak UUID
     * @return the user's profile DTO
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable String userId) {
        UserResponse user = keycloakAdminService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    /**
     * Lists users in the realm with optional pagination.
     *
     * @param first zero-based offset (default 0)
     * @param max   maximum results (default 20)
     * @return paginated list of user profiles
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> listUsers(
            @RequestParam(defaultValue = "0") int first,
            @RequestParam(defaultValue = "20") int max) {
        List<UserResponse> users = keycloakAdminService.listUsers(first, max);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    /**
     * Resets a user's password via the Keycloak Admin API.
     *
     * <p>The new password is transmitted over TLS directly to Keycloak.
     * Keycloak stores it as a PBKDF2-SHA256 hash — the LIS service never
     * persists the password value anywhere.</p>
     *
     * @param userId  Keycloak user UUID
     * @param request contains the new password and whether it is temporary
     */
    @PatchMapping("/{userId}/password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @PathVariable String userId,
            @Valid @RequestBody ResetPasswordRequest request) {
        keycloakAdminService.resetPassword(userId, request.getNewPassword(), request.isTemporary());
        return ResponseEntity.ok(ApiResponse.successMessage("Password reset successfully"));
    }

    /**
     * Enables or disables a user account in Keycloak.
     *
     * @param userId  Keycloak user UUID
     * @param enabled {@code true} to enable, {@code false} to disable
     */
    @PatchMapping("/{userId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> setUserEnabled(
            @PathVariable String userId,
            @RequestParam boolean enabled) {
        keycloakAdminService.setUserEnabled(userId, enabled);
        String message = enabled ? "User enabled" : "User disabled";
        return ResponseEntity.ok(ApiResponse.successMessage(message));
    }

    /**
     * Deletes a user from Keycloak.
     *
     * @param userId Keycloak user UUID
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String userId) {
        keycloakAdminService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.successMessage("User deleted successfully"));
    }
}
