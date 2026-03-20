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
 * Critical Value Config entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Defines critical value thresholds for a parameter that trigger notifications and flags.
 */
@Entity
@Table(name = "critical_value_config")
public class CriticalValueConfig extends BaseEntity {

    @Column(name = "parameter_id", nullable = false, insertable = false, updatable = false)
    private UUID parameterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parameter_id", nullable = false)
    private Parameter parameter;

    @Column(name = "low_threshold")
    private BigDecimal lowThreshold;

    @Column(name = "high_threshold")
    private BigDecimal highThreshold;

    @Column(name = "notification_required", nullable = false)
    private Boolean notificationRequired = true;

    @Column(name = "auto_flag", nullable = false)
    private Boolean autoFlag = true;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public UUID getParameterId() {
        return this.parameterId;
    }

    public Parameter getParameter() {
        return this.parameter;
    }

    public BigDecimal getLowThreshold() {
        return this.lowThreshold;
    }

    public BigDecimal getHighThreshold() {
        return this.highThreshold;
    }

    public Boolean getNotificationRequired() {
        return this.notificationRequired;
    }

    public Boolean getAutoFlag() {
        return this.autoFlag;
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

    public void setLowThreshold(BigDecimal lowThreshold) {
        this.lowThreshold = lowThreshold;
    }

    public void setHighThreshold(BigDecimal highThreshold) {
        this.highThreshold = highThreshold;
    }

    public void setNotificationRequired(Boolean notificationRequired) {
        this.notificationRequired = notificationRequired;
    }

    public void setAutoFlag(Boolean autoFlag) {
        this.autoFlag = autoFlag;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
