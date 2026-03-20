package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Parameter entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Represents a laboratory test parameter definition within a branch.
 */
@Entity
@Table(name = "parameter")
public class Parameter extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "print_name")
    private String printName;

    @Column(name = "unit")
    private String unit;

    @Column(name = "data_type", nullable = false)
    private String dataType;

    @Column(name = "decimal_places")
    private Integer decimalPlaces;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "method")
    private String method;

    @Column(name = "loinc_code")
    private String loincCode;

    @Column(name = "formula")
    private String formula;

    @Column(name = "is_calculated", nullable = false)
    private Boolean isCalculated = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getPrintName() {
        return this.printName;
    }

    public String getUnit() {
        return this.unit;
    }

    public String getDataType() {
        return this.dataType;
    }

    public Integer getDecimalPlaces() {
        return this.decimalPlaces;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public String getMethod() {
        return this.method;
    }

    public String getLoincCode() {
        return this.loincCode;
    }

    public String getFormula() {
        return this.formula;
    }

    public Boolean getIsCalculated() {
        return this.isCalculated;
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

    public void setPrintName(String printName) {
        this.printName = printName;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setLoincCode(String loincCode) {
        this.loincCode = loincCode;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public void setIsCalculated(Boolean isCalculated) {
        this.isCalculated = isCalculated;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
