package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * Antibiotic entity for microbiology sensitivity testing.
 * Extends BaseEntity for multi-branch support via branchId.
 */
@Entity
@Table(name = "antibiotic")
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

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getAntibioticGroup() {
        return this.antibioticGroup;
    }

    public BigDecimal getClsiBreakpointS() {
        return this.clsiBreakpointS;
    }

    public BigDecimal getClsiBreakpointR() {
        return this.clsiBreakpointR;
    }

    public String getClsiMethod() {
        return this.clsiMethod;
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

    public void setAntibioticGroup(String antibioticGroup) {
        this.antibioticGroup = antibioticGroup;
    }

    public void setClsiBreakpointS(BigDecimal clsiBreakpointS) {
        this.clsiBreakpointS = clsiBreakpointS;
    }

    public void setClsiBreakpointR(BigDecimal clsiBreakpointR) {
        this.clsiBreakpointR = clsiBreakpointR;
    }

    public void setClsiMethod(String clsiMethod) {
        this.clsiMethod = clsiMethod;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
