package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.*;

/**
 * Request DTO for creating or updating an application user.
 */
public class AppUserRequest {

    @NotBlank(message = "Keycloak user ID is required")
    @Size(max = 100, message = "Keycloak user ID must not exceed 100 characters")
    private String keycloakUserId;

    @NotBlank(message = "Username is required")
    @Size(max = 100, message = "Username must not exceed 100 characters")
    private String username;

    @Size(max = 200, message = "Email must not exceed 200 characters")
    @Email(message = "Email must be a valid email address")
    private String email;

    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @Size(max = 50, message = "Employee ID must not exceed 50 characters")
    private String employeeId;

    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;

    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designation;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    private Boolean isActive;

    public AppUserRequest() {
    }

    public AppUserRequest(String keycloakUserId, String username, String email, String firstName, String lastName, String employeeId, String department, String designation, String phone, Boolean isActive) {
        this.keycloakUserId = keycloakUserId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = employeeId;
        this.department = department;
        this.designation = designation;
        this.phone = phone;
        this.isActive = isActive;
    }

    public String getKeycloakUserId() {
        return this.keycloakUserId;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmployeeId() {
        return this.employeeId;
    }

    public String getDepartment() {
        return this.department;
    }

    public String getDesignation() {
        return this.designation;
    }

    public String getPhone() {
        return this.phone;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setKeycloakUserId(String keycloakUserId) {
        this.keycloakUserId = keycloakUserId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUserRequest that = (AppUserRequest) o;
        return java.util.Objects.equals(this.keycloakUserId, that.keycloakUserId) &&
               java.util.Objects.equals(this.username, that.username) &&
               java.util.Objects.equals(this.email, that.email) &&
               java.util.Objects.equals(this.firstName, that.firstName) &&
               java.util.Objects.equals(this.lastName, that.lastName) &&
               java.util.Objects.equals(this.employeeId, that.employeeId) &&
               java.util.Objects.equals(this.department, that.department) &&
               java.util.Objects.equals(this.designation, that.designation) &&
               java.util.Objects.equals(this.phone, that.phone) &&
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.keycloakUserId, this.username, this.email, this.firstName, this.lastName, this.employeeId, this.department, this.designation, this.phone, this.isActive);
    }

    @Override
    public String toString() {
        return "AppUserRequest{keycloakUserId=" + keycloakUserId +
               ", username=" + username +
               ", email=" + email +
               ", firstName=" + firstName +
               ", lastName=" + lastName +
               ", employeeId=" + employeeId +
               ", department=" + department +
               ", designation=" + designation +
               ", phone=" + phone +
               ", isActive=" + isActive +
               "}";
    }

    public static AppUserRequestBuilder builder() {
        return new AppUserRequestBuilder();
    }

    public static class AppUserRequestBuilder {
        private String keycloakUserId;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private String employeeId;
        private String department;
        private String designation;
        private String phone;
        private Boolean isActive;

        AppUserRequestBuilder() {
        }

        public AppUserRequestBuilder keycloakUserId(String keycloakUserId) {
            this.keycloakUserId = keycloakUserId;
            return this;
        }

        public AppUserRequestBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AppUserRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AppUserRequestBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AppUserRequestBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AppUserRequestBuilder employeeId(String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public AppUserRequestBuilder department(String department) {
            this.department = department;
            return this;
        }

        public AppUserRequestBuilder designation(String designation) {
            this.designation = designation;
            return this;
        }

        public AppUserRequestBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public AppUserRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public AppUserRequest build() {
            return new AppUserRequest(this.keycloakUserId, this.username, this.email, this.firstName, this.lastName, this.employeeId, this.department, this.designation, this.phone, this.isActive);
        }
    }

}
