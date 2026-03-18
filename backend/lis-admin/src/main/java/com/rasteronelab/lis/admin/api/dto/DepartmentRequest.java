package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Request DTO for creating or updating a Department.
 */
public class DepartmentRequest {

    @NotBlank(message = "Department name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Department code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @NotNull(message = "Organization ID is required")
    private UUID orgId;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private Integer displayOrder;

    private Boolean isActive;

    public DepartmentRequest() {
    }

    public DepartmentRequest(String name, String code, UUID orgId, String description, Integer displayOrder, Boolean isActive) {
        this.name = name;
        this.code = code;
        this.orgId = orgId;
        this.description = description;
        this.displayOrder = displayOrder;
        this.isActive = isActive;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public UUID getOrgId() {
        return this.orgId;
    }

    public String getDescription() {
        return this.description;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setOrgId(UUID orgId) {
        this.orgId = orgId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentRequest that = (DepartmentRequest) o;
        return java.util.Objects.equals(this.name, that.name) &&
               java.util.Objects.equals(this.code, that.code) &&
               java.util.Objects.equals(this.orgId, that.orgId) &&
               java.util.Objects.equals(this.description, that.description) &&
               java.util.Objects.equals(this.displayOrder, that.displayOrder) &&
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.name, this.code, this.orgId, this.description, this.displayOrder, this.isActive);
    }

    @Override
    public String toString() {
        return "DepartmentRequest{name=" + name +
               ", code=" + code +
               ", orgId=" + orgId +
               ", description=" + description +
               ", displayOrder=" + displayOrder +
               ", isActive=" + isActive +
               "}";
    }

    public static DepartmentRequestBuilder builder() {
        return new DepartmentRequestBuilder();
    }

    public static class DepartmentRequestBuilder {
        private String name;
        private String code;
        private UUID orgId;
        private String description;
        private Integer displayOrder;
        private Boolean isActive;

        DepartmentRequestBuilder() {
        }

        public DepartmentRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DepartmentRequestBuilder code(String code) {
            this.code = code;
            return this;
        }

        public DepartmentRequestBuilder orgId(UUID orgId) {
            this.orgId = orgId;
            return this;
        }

        public DepartmentRequestBuilder description(String description) {
            this.description = description;
            return this;
        }

        public DepartmentRequestBuilder displayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
            return this;
        }

        public DepartmentRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public DepartmentRequest build() {
            return new DepartmentRequest(this.name, this.code, this.orgId, this.description, this.displayOrder, this.isActive);
        }
    }

}
