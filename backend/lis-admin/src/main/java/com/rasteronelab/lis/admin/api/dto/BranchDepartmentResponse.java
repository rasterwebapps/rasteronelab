package com.rasteronelab.lis.admin.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for BranchDepartment mapping entity.
 * Includes branch and department names for display.
 */
public class BranchDepartmentResponse {

    private UUID id;
    private UUID branchId;
    private String branchName;
    private UUID departmentId;
    private String departmentName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public BranchDepartmentResponse() {
    }

    public BranchDepartmentResponse(UUID id, UUID branchId, String branchName, UUID departmentId, String departmentName, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.branchName = branchName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
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

    public String getBranchName() {
        return this.branchName;
    }

    public UUID getDepartmentId() {
        return this.departmentId;
    }

    public String getDepartmentName() {
        return this.departmentName;
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

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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
        BranchDepartmentResponse that = (BranchDepartmentResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.branchName, that.branchName) &&
               java.util.Objects.equals(this.departmentId, that.departmentId) &&
               java.util.Objects.equals(this.departmentName, that.departmentName) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.branchName, this.departmentId, this.departmentName, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "BranchDepartmentResponse{id=" + id +
               ", branchId=" + branchId +
               ", branchName=" + branchName +
               ", departmentId=" + departmentId +
               ", departmentName=" + departmentName +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static BranchDepartmentResponseBuilder builder() {
        return new BranchDepartmentResponseBuilder();
    }

    public static class BranchDepartmentResponseBuilder {
        private UUID id;
        private UUID branchId;
        private String branchName;
        private UUID departmentId;
        private String departmentName;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        BranchDepartmentResponseBuilder() {
        }

        public BranchDepartmentResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public BranchDepartmentResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public BranchDepartmentResponseBuilder branchName(String branchName) {
            this.branchName = branchName;
            return this;
        }

        public BranchDepartmentResponseBuilder departmentId(UUID departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public BranchDepartmentResponseBuilder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public BranchDepartmentResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public BranchDepartmentResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public BranchDepartmentResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public BranchDepartmentResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public BranchDepartmentResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public BranchDepartmentResponse build() {
            return new BranchDepartmentResponse(this.id, this.branchId, this.branchName, this.departmentId, this.departmentName, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
