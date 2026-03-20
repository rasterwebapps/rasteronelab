package com.rasteronelab.lis.admin.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for AppUser entity.
 * Includes all entity fields plus audit metadata.
 */
public class AppUserResponse {

    private UUID id;
    private UUID branchId;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public AppUserResponse() {
    }

    public AppUserResponse(UUID id, UUID branchId, String keycloakUserId, String username, String email, String firstName, String lastName, String employeeId, String department, String designation, String phone, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
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
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getBranchId() {
        return this.branchId;
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

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUserResponse that = (AppUserResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.keycloakUserId, that.keycloakUserId) &&
               java.util.Objects.equals(this.username, that.username) &&
               java.util.Objects.equals(this.email, that.email) &&
               java.util.Objects.equals(this.firstName, that.firstName) &&
               java.util.Objects.equals(this.lastName, that.lastName) &&
               java.util.Objects.equals(this.employeeId, that.employeeId) &&
               java.util.Objects.equals(this.department, that.department) &&
               java.util.Objects.equals(this.designation, that.designation) &&
               java.util.Objects.equals(this.phone, that.phone) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.keycloakUserId, this.username, this.email, this.firstName, this.lastName, this.employeeId, this.department, this.designation, this.phone, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "AppUserResponse{id=" + id +
               ", branchId=" + branchId +
               ", keycloakUserId=" + keycloakUserId +
               ", username=" + username +
               ", email=" + email +
               ", firstName=" + firstName +
               ", lastName=" + lastName +
               ", employeeId=" + employeeId +
               ", department=" + department +
               ", designation=" + designation +
               ", phone=" + phone +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static AppUserResponseBuilder builder() {
        return new AppUserResponseBuilder();
    }

    public static class AppUserResponseBuilder {
        private UUID id;
        private UUID branchId;
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
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        AppUserResponseBuilder() {
        }

        public AppUserResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public AppUserResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public AppUserResponseBuilder keycloakUserId(String keycloakUserId) {
            this.keycloakUserId = keycloakUserId;
            return this;
        }

        public AppUserResponseBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AppUserResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AppUserResponseBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AppUserResponseBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AppUserResponseBuilder employeeId(String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public AppUserResponseBuilder department(String department) {
            this.department = department;
            return this;
        }

        public AppUserResponseBuilder designation(String designation) {
            this.designation = designation;
            return this;
        }

        public AppUserResponseBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public AppUserResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public AppUserResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AppUserResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public AppUserResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public AppUserResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public AppUserResponse build() {
            return new AppUserResponse(this.id, this.branchId, this.keycloakUserId, this.username, this.email, this.firstName, this.lastName, this.employeeId, this.department, this.designation, this.phone, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
