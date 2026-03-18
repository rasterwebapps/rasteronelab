package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO for assigning a Department to a Branch.
 */
public class BranchDepartmentRequest {

    @NotNull(message = "Branch ID is required")
    private UUID branchId;

    @NotNull(message = "Department ID is required")
    private UUID departmentId;

    private Boolean isActive;

    public BranchDepartmentRequest() {
    }

    public BranchDepartmentRequest(UUID branchId, UUID departmentId, Boolean isActive) {
        this.branchId = branchId;
        this.departmentId = departmentId;
        this.isActive = isActive;
    }

    public UUID getBranchId() {
        return this.branchId;
    }

    public UUID getDepartmentId() {
        return this.departmentId;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BranchDepartmentRequest that = (BranchDepartmentRequest) o;
        return java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.departmentId, that.departmentId) &&
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.branchId, this.departmentId, this.isActive);
    }

    @Override
    public String toString() {
        return "BranchDepartmentRequest{branchId=" + branchId +
               ", departmentId=" + departmentId +
               ", isActive=" + isActive +
               "}";
    }

    public static BranchDepartmentRequestBuilder builder() {
        return new BranchDepartmentRequestBuilder();
    }

    public static class BranchDepartmentRequestBuilder {
        private UUID branchId;
        private UUID departmentId;
        private Boolean isActive;

        BranchDepartmentRequestBuilder() {
        }

        public BranchDepartmentRequestBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public BranchDepartmentRequestBuilder departmentId(UUID departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public BranchDepartmentRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public BranchDepartmentRequest build() {
            return new BranchDepartmentRequest(this.branchId, this.departmentId, this.isActive);
        }
    }

}
