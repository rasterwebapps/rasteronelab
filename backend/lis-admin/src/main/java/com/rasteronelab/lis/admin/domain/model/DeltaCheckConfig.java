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
 * Delta Check Config entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Defines delta check thresholds for detecting significant changes between consecutive results.
 */
@Entity
@Table(name = "delta_check_config")
public class DeltaCheckConfig extends BaseEntity {

    @Column(name = "parameter_id", nullable = false, insertable = false, updatable = false)
    private UUID parameterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parameter_id", nullable = false)
    private Parameter parameter;

    @Column(name = "percentage_threshold", nullable = false)
    private BigDecimal percentageThreshold;

    @Column(name = "absolute_threshold")
    private BigDecimal absoluteThreshold;

    @Column(name = "time_window_hours", nullable = false)
    private Integer timeWindowHours = 72;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public UUID getParameterId() {
        return this.parameterId;
    }

    public Parameter getParameter() {
        return this.parameter;
    }

    public BigDecimal getPercentageThreshold() {
        return this.percentageThreshold;
    }

    public BigDecimal getAbsoluteThreshold() {
        return this.absoluteThreshold;
    }

    public Integer getTimeWindowHours() {
        return this.timeWindowHours;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setParameterId(UUID parameterId) {
        this.parameterId = parameterId;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public void setPercentageThreshold(BigDecimal percentageThreshold) {
        this.percentageThreshold = percentageThreshold;
    }

    public void setAbsoluteThreshold(BigDecimal absoluteThreshold) {
        this.absoluteThreshold = absoluteThreshold;
    }

    public void setTimeWindowHours(Integer timeWindowHours) {
        this.timeWindowHours = timeWindowHours;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
