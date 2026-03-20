package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

/**
 * ReportTemplate entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Represents a report template configuration within a branch.
 */
@Entity
@Table(name = "report_template")
public class ReportTemplate extends BaseEntity {

    @Column(name = "template_name", nullable = false)
    private String templateName;

    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "description")
    private String description;

    @Column(name = "header_config", columnDefinition = "TEXT")
    private String headerConfig;

    @Column(name = "footer_config", columnDefinition = "TEXT")
    private String footerConfig;

    @Column(name = "font_family")
    private String fontFamily;

    @Column(name = "layout_type", nullable = false)
    private String layoutType = "PORTRAIT";

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

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

}
