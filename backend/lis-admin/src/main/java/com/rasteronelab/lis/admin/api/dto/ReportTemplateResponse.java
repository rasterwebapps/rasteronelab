package com.rasteronelab.lis.admin.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for ReportTemplate entity.
 * Includes all entity fields plus audit metadata.
 */
public class ReportTemplateResponse {

    private UUID id;
    private UUID branchId;
    private String templateName;
    private UUID departmentId;
    private String description;
    private String headerConfig;
    private String footerConfig;
    private String fontFamily;
    private String layoutType;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public ReportTemplateResponse() {
    }

    public ReportTemplateResponse(UUID id, UUID branchId, String templateName, UUID departmentId, String description, String headerConfig, String footerConfig, String fontFamily, String layoutType, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.templateName = templateName;
        this.departmentId = departmentId;
        this.description = description;
        this.headerConfig = headerConfig;
        this.footerConfig = footerConfig;
        this.fontFamily = fontFamily;
        this.layoutType = layoutType;
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

    public String getTemplateName() {
        return this.templateName;
    }

    public UUID getDepartmentId() {
        return this.departmentId;
    }

    public String getDescription() {
        return this.description;
    }

    public String getHeaderConfig() {
        return this.headerConfig;
    }

    public String getFooterConfig() {
        return this.footerConfig;
    }

    public String getFontFamily() {
        return this.fontFamily;
    }

    public String getLayoutType() {
        return this.layoutType;
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

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHeaderConfig(String headerConfig) {
        this.headerConfig = headerConfig;
    }

    public void setFooterConfig(String footerConfig) {
        this.footerConfig = footerConfig;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
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
        ReportTemplateResponse that = (ReportTemplateResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.templateName, that.templateName) &&
               java.util.Objects.equals(this.departmentId, that.departmentId) &&
               java.util.Objects.equals(this.description, that.description) &&
               java.util.Objects.equals(this.headerConfig, that.headerConfig) &&
               java.util.Objects.equals(this.footerConfig, that.footerConfig) &&
               java.util.Objects.equals(this.fontFamily, that.fontFamily) &&
               java.util.Objects.equals(this.layoutType, that.layoutType) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.templateName, this.departmentId, this.description, this.headerConfig, this.footerConfig, this.fontFamily, this.layoutType, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "ReportTemplateResponse{id=" + id +
               ", branchId=" + branchId +
               ", templateName=" + templateName +
               ", departmentId=" + departmentId +
               ", description=" + description +
               ", headerConfig=" + headerConfig +
               ", footerConfig=" + footerConfig +
               ", fontFamily=" + fontFamily +
               ", layoutType=" + layoutType +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static ReportTemplateResponseBuilder builder() {
        return new ReportTemplateResponseBuilder();
    }

    public static class ReportTemplateResponseBuilder {
        private UUID id;
        private UUID branchId;
        private String templateName;
        private UUID departmentId;
        private String description;
        private String headerConfig;
        private String footerConfig;
        private String fontFamily;
        private String layoutType;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        ReportTemplateResponseBuilder() {
        }

        public ReportTemplateResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ReportTemplateResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public ReportTemplateResponseBuilder templateName(String templateName) {
            this.templateName = templateName;
            return this;
        }

        public ReportTemplateResponseBuilder departmentId(UUID departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public ReportTemplateResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ReportTemplateResponseBuilder headerConfig(String headerConfig) {
            this.headerConfig = headerConfig;
            return this;
        }

        public ReportTemplateResponseBuilder footerConfig(String footerConfig) {
            this.footerConfig = footerConfig;
            return this;
        }

        public ReportTemplateResponseBuilder fontFamily(String fontFamily) {
            this.fontFamily = fontFamily;
            return this;
        }

        public ReportTemplateResponseBuilder layoutType(String layoutType) {
            this.layoutType = layoutType;
            return this;
        }

        public ReportTemplateResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public ReportTemplateResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ReportTemplateResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ReportTemplateResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public ReportTemplateResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public ReportTemplateResponse build() {
            return new ReportTemplateResponse(this.id, this.branchId, this.templateName, this.departmentId, this.description, this.headerConfig, this.footerConfig, this.fontFamily, this.layoutType, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
