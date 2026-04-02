package com.rasteronelab.lis.auth.api.dto;

import java.util.List;

/**
 * Read-only user profile returned by user management endpoints.
 * <p>
 * This DTO intentionally contains <strong>no password fields</strong>.
 * Credentials are managed exclusively inside Keycloak and are never
 * returned or stored by the LIS application.
 * </p>
 */
public class UserResponse {

    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private boolean emailVerified;
    private List<String> roles;

    public UserResponse() {
    }

    public UserResponse(String id, String username, String email,
                        String firstName, String lastName,
                        boolean enabled, boolean emailVerified,
                        List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
        this.emailVerified = emailVerified;
        this.roles = roles;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}
