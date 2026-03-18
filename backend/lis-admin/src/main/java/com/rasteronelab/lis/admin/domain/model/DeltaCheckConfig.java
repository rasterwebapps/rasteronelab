package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Delta Check Config entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Defines delta check thresholds for detecting significant changes between consecutive results.
 */
@Entity
@Table(name = "delta_check_config")
@Getter
@Setter
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
}
