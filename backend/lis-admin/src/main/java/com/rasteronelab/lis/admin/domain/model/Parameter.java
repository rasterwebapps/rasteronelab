package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Parameter entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Represents a laboratory test parameter definition within a branch.
 */
@Entity
@Table(name = "parameter")
@Getter
@Setter
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
}
