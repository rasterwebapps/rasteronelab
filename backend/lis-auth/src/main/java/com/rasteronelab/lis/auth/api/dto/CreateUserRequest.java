package com.rasteronelab.lis.auth.api.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request payload for creating a new user in Keycloak.
 *
 * <p>The {@code temporaryPassword} is transmitted over TLS directly to the
 * Keycloak Admin API. It is never persisted in the LIS database. Keycloak
 * stores it as a PBKDF2-SHA256 hash with a per-user random salt.</p>
 */
public class CreateUserRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be 3–50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    private String lastName;

    /**
     * Initial temporary password. The user will be prompted to change it on first login.
     * Transmitted to Keycloak over TLS; never written to the LIS database.
     */
    @NotBlank(message = "Temporary password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String temporaryPassword;

    /** Optional list of realm role names to assign immediately. */
    private List<String> roles;

    public CreateUserRequest() {
    }

    public CreateUserRequest(String username, String email, String firstName,
                             String lastName, String temporaryPassword, List<String> roles) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.temporaryPassword = temporaryPassword;
        this.roles = roles;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getTemporaryPassword() { return temporaryPassword; }
    public void setTemporaryPassword(String temporaryPassword) { this.temporaryPassword = temporaryPassword; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}
