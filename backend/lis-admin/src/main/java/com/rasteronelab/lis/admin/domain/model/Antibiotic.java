package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Antibiotic entity for microbiology sensitivity testing.
 * Extends BaseEntity for multi-branch support via branchId.
 */
@Entity
@Table(name = "antibiotic")
@Getter
@Setter
public class Antibiotic extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "antibiotic_group")
    private String antibioticGroup;

    @Column(name = "clsi_breakpoint_s")
    private BigDecimal clsiBreakpointS;

    @Column(name = "clsi_breakpoint_r")
    private BigDecimal clsiBreakpointR;

    @Column(name = "clsi_method")
    private String clsiMethod;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
