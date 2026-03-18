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
 * Test Panel entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Represents a panel/profile grouping multiple tests within a branch.
 */
@Entity
@Table(name = "test_panel")
public class TestPanel extends BaseEntity {

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

    @Column(name = "panel_price")
    private BigDecimal panelPrice;

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

    public BigDecimal getPanelPrice() {
        return this.panelPrice;
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

    public void setPanelPrice(BigDecimal panelPrice) {
        this.panelPrice = panelPrice;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
