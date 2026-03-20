package com.rasteronelab.lis.admin.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Role entity.
 * Includes all entity fields plus audit metadata.
 */
public class RoleResponse {

    private UUID id;
    private UUID branchId;
    private String roleName;
    private String description;
    private String permissions;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public RoleResponse() {
    }

    public RoleResponse(UUID id, UUID branchId, String roleName, String description, String permissions, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.roleName = roleName;
        this.description = description;
        this.permissions = permissions;
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
        RoleResponse that = (RoleResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.roleName, that.roleName) &&
               java.util.Objects.equals(this.description, that.description) &&
               java.util.Objects.equals(this.permissions, that.permissions) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.roleName, this.description, this.permissions, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "RoleResponse{id=" + id +
               ", branchId=" + branchId +
               ", roleName=" + roleName +
               ", description=" + description +
               ", permissions=" + permissions +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static RoleResponseBuilder builder() {
        return new RoleResponseBuilder();
    }

    public static class RoleResponseBuilder {
        private UUID id;
        private UUID branchId;
        private String roleName;
        private String description;
        private String permissions;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        RoleResponseBuilder() {
        }

        public RoleResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public RoleResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public RoleResponseBuilder roleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public RoleResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public RoleResponseBuilder permissions(String permissions) {
            this.permissions = permissions;
            return this;
        }

        public RoleResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public RoleResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public RoleResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public RoleResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public RoleResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public RoleResponse build() {
            return new RoleResponse(this.id, this.branchId, this.roleName, this.description, this.permissions, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
