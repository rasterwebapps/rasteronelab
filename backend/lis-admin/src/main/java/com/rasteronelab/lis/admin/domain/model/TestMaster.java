package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Test Master entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Represents a laboratory test definition within a branch.
 */
@Entity
@Table(name = "test_master")
public class TestMaster extends BaseEntity {

    @Column(name = "department_id", nullable = false, insertable = false, updatable = false)
    private UUID departmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "description")
    private String description;

    @Column(name = "sample_type")
    private String sampleType;

    @Column(name = "tube_type")
    private String tubeType;

    @Column(name = "report_section")
    private String reportSection;

    @Column(name = "method")
    private String method;

    @Column(name = "tat_routine_hours")
    private Integer tatRoutineHours;

    @Column(name = "tat_stat_hours")
    private Integer tatStatHours;

    @Column(name = "base_price")
    private BigDecimal basePrice;

    @Column(name = "is_outsourced", nullable = false)
    private Boolean isOutsourced = false;

    @Column(name = "outsource_lab_name")
    private String outsourceLabName;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public UUID getDepartmentId() {
        return this.departmentId;
    }

    public Department getDepartment() {
        return this.department;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getShortName() {
        return this.shortName;
    }

    public String getDescription() {
        return this.description;
    }

    public String getSampleType() {
        return this.sampleType;
    }

    public String getTubeType() {
        return this.tubeType;
    }

    public String getReportSection() {
        return this.reportSection;
    }

    public String getMethod() {
        return this.method;
    }

    public Integer getTatRoutineHours() {
        return this.tatRoutineHours;
    }

    public Integer getTatStatHours() {
        return this.tatStatHours;
    }

    public BigDecimal getBasePrice() {
        return this.basePrice;
    }

    public Boolean getIsOutsourced() {
        return this.isOutsourced;
    }

    public String getOutsourceLabName() {
        return this.outsourceLabName;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public void setTubeType(String tubeType) {
        this.tubeType = tubeType;
    }

    public void setReportSection(String reportSection) {
        this.reportSection = reportSection;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setTatRoutineHours(Integer tatRoutineHours) {
        this.tatRoutineHours = tatRoutineHours;
    }

    public void setTatStatHours(Integer tatStatHours) {
        this.tatStatHours = tatStatHours;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public void setIsOutsourced(Boolean isOutsourced) {
        this.isOutsourced = isOutsourced;
    }

    public void setOutsourceLabName(String outsourceLabName) {
        this.outsourceLabName = outsourceLabName;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
