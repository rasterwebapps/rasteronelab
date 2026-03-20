package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Request DTO for creating or updating a ReportTemplate.
 */
public class ReportTemplateRequest {

    @NotBlank(message = "Template name is required")
    @Size(max = 200, message = "Template name must not exceed 200 characters")
    private String templateName;

    private UUID departmentId;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private String headerConfig;

    private String footerConfig;

    @Size(max = 100, message = "Font family must not exceed 100 characters")
    private String fontFamily;

    private String layoutType;

    private Boolean isActive;

    public ReportTemplateRequest() {
    }

    public ReportTemplateRequest(String templateName, UUID departmentId, String description, String headerConfig, String footerConfig, String fontFamily, String layoutType, Boolean isActive) {
        this.templateName = templateName;
        this.departmentId = departmentId;
        this.description = description;
        this.headerConfig = headerConfig;
        this.footerConfig = footerConfig;
        this.fontFamily = fontFamily;
        this.layoutType = layoutType;
        this.isActive = isActive;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportTemplateRequest that = (ReportTemplateRequest) o;
        return java.util.Objects.equals(this.templateName, that.templateName) &&
               java.util.Objects.equals(this.departmentId, that.departmentId) &&
               java.util.Objects.equals(this.description, that.description) &&
               java.util.Objects.equals(this.headerConfig, that.headerConfig) &&
               java.util.Objects.equals(this.footerConfig, that.footerConfig) &&
               java.util.Objects.equals(this.fontFamily, that.fontFamily) &&
               java.util.Objects.equals(this.layoutType, that.layoutType) &&
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.templateName, this.departmentId, this.description, this.headerConfig, this.footerConfig, this.fontFamily, this.layoutType, this.isActive);
    }

    @Override
    public String toString() {
        return "ReportTemplateRequest{templateName=" + templateName +
               ", departmentId=" + departmentId +
               ", description=" + description +
               ", headerConfig=" + headerConfig +
               ", footerConfig=" + footerConfig +
               ", fontFamily=" + fontFamily +
               ", layoutType=" + layoutType +
               ", isActive=" + isActive +
               "}";
    }

    public static ReportTemplateRequestBuilder builder() {
        return new ReportTemplateRequestBuilder();
    }

    public static class ReportTemplateRequestBuilder {
        private String templateName;
        private UUID departmentId;
        private String description;
        private String headerConfig;
        private String footerConfig;
        private String fontFamily;
        private String layoutType;
        private Boolean isActive;

        ReportTemplateRequestBuilder() {
        }

        public ReportTemplateRequestBuilder templateName(String templateName) {
            this.templateName = templateName;
            return this;
        }

        public ReportTemplateRequestBuilder departmentId(UUID departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public ReportTemplateRequestBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ReportTemplateRequestBuilder headerConfig(String headerConfig) {
            this.headerConfig = headerConfig;
            return this;
        }

        public ReportTemplateRequestBuilder footerConfig(String footerConfig) {
            this.footerConfig = footerConfig;
            return this;
        }

        public ReportTemplateRequestBuilder fontFamily(String fontFamily) {
            this.fontFamily = fontFamily;
            return this;
        }

        public ReportTemplateRequestBuilder layoutType(String layoutType) {
            this.layoutType = layoutType;
            return this;
        }

        public ReportTemplateRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public ReportTemplateRequest build() {
            return new ReportTemplateRequest(this.templateName, this.departmentId, this.description, this.headerConfig, this.footerConfig, this.fontFamily, this.layoutType, this.isActive);
        }
    }

}
