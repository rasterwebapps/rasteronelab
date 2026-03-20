package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a Role.
 */
public class RoleRequest {

    @NotBlank(message = "Role name is required")
    @Size(max = 100, message = "Role name must not exceed 100 characters")
    private String roleName;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private String permissions;

    private Boolean isActive;

    public RoleRequest() {
    }

    public RoleRequest(String roleName, String description, String permissions, Boolean isActive) {
        this.roleName = roleName;
        this.description = description;
        this.permissions = permissions;
        this.isActive = isActive;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPermissions() {
        return this.permissions;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleRequest that = (RoleRequest) o;
        return java.util.Objects.equals(this.roleName, that.roleName) &&
               java.util.Objects.equals(this.description, that.description) &&
               java.util.Objects.equals(this.permissions, that.permissions) &&
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.roleName, this.description, this.permissions, this.isActive);
    }

    @Override
    public String toString() {
        return "RoleRequest{roleName=" + roleName +
               ", description=" + description +
               ", permissions=" + permissions +
               ", isActive=" + isActive +
               "}";
    }

    public static RoleRequestBuilder builder() {
        return new RoleRequestBuilder();
    }

    public static class RoleRequestBuilder {
        private String roleName;
        private String description;
        private String permissions;
        private Boolean isActive;

        RoleRequestBuilder() {
        }

        public RoleRequestBuilder roleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public RoleRequestBuilder description(String description) {
            this.description = description;
            return this;
        }

        public RoleRequestBuilder permissions(String permissions) {
            this.permissions = permissions;
            return this;
        }

        public RoleRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public RoleRequest build() {
            return new RoleRequest(this.roleName, this.description, this.permissions, this.isActive);
        }
    }

}
