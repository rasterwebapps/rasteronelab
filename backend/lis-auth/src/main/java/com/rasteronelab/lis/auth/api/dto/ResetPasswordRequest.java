package com.rasteronelab.lis.auth.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request payload for resetting a user's password.
 *
 * <p>The password value is forwarded to Keycloak over TLS and is never
 * written to the LIS database. Keycloak hashes it using PBKDF2-SHA256
 * with a unique per-user salt before storing it in PostgreSQL.</p>
 */
public class ResetPasswordRequest {

    /**
     * The new password.
     * Transmitted over TLS directly to Keycloak; never persisted in LIS.
     */
    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;

    /**
     * When {@code true} (default) the user must change the password on next login.
     */
    private boolean temporary = true;

    public ResetPasswordRequest() {
    }

    public ResetPasswordRequest(String newPassword, boolean temporary) {
        this.newPassword = newPassword;
        this.temporary = temporary;
    }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public boolean isTemporary() { return temporary; }
    public void setTemporary(boolean temporary) { this.temporary = temporary; }
}
