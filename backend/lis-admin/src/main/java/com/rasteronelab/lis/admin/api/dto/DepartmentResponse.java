package com.rasteronelab.lis.admin.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Department entity.
 * Includes all entity fields plus organizationName for display.
 */
public class DepartmentResponse {

    private UUID id;
    private UUID orgId;
    private String organizationName;
    private String name;
    private String code;
    private String description;
    private Integer displayOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public DepartmentResponse() {
    }

    public DepartmentResponse(UUID id, UUID orgId, String organizationName, String name, String code, String description, Integer displayOrder, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.orgId = orgId;
        this.organizationName = organizationName;
        this.name = name;
        this.code = code;
        this.description = description;
        this.displayOrder = displayOrder;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getOrgId() {
        return this.orgId;
    }

    public String getOrganizationName() {
        return this.organizationName;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
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

    public void setOrgId(UUID orgId) {
        this.orgId = orgId;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
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
        DepartmentResponse that = (DepartmentResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.orgId, that.orgId) &&
               java.util.Objects.equals(this.organizationName, that.organizationName) &&
               java.util.Objects.equals(this.name, that.name) &&
               java.util.Objects.equals(this.code, that.code) &&
               java.util.Objects.equals(this.description, that.description) &&
               java.util.Objects.equals(this.displayOrder, that.displayOrder) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.orgId, this.organizationName, this.name, this.code, this.description, this.displayOrder, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "DepartmentResponse{id=" + id +
               ", orgId=" + orgId +
               ", organizationName=" + organizationName +
               ", name=" + name +
               ", code=" + code +
               ", description=" + description +
               ", displayOrder=" + displayOrder +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static DepartmentResponseBuilder builder() {
        return new DepartmentResponseBuilder();
    }

    public static class DepartmentResponseBuilder {
        private UUID id;
        private UUID orgId;
        private String organizationName;
        private String name;
        private String code;
        private String description;
        private Integer displayOrder;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        DepartmentResponseBuilder() {
        }

        public DepartmentResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public DepartmentResponseBuilder orgId(UUID orgId) {
            this.orgId = orgId;
            return this;
        }

        public DepartmentResponseBuilder organizationName(String organizationName) {
            this.organizationName = organizationName;
            return this;
        }

        public DepartmentResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DepartmentResponseBuilder code(String code) {
            this.code = code;
            return this;
        }

        public DepartmentResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public DepartmentResponseBuilder displayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
            return this;
        }

        public DepartmentResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public DepartmentResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DepartmentResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DepartmentResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public DepartmentResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public DepartmentResponse build() {
            return new DepartmentResponse(this.id, this.orgId, this.organizationName, this.name, this.code, this.description, this.displayOrder, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
