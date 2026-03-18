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

import java.util.UUID;

/**
 * Auto Validation Rule entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Defines rules for automatic result validation based on configurable conditions.
 */
@Entity
@Table(name = "auto_validation_rule")
@Getter
@Setter
public class AutoValidationRule extends BaseEntity {

    @Column(name = "parameter_id", insertable = false, updatable = false)
    private UUID parameterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parameter_id")
    private Parameter parameter;

    @Column(name = "test_id", insertable = false, updatable = false)
    private UUID testId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    private TestMaster test;

    @Column(name = "rule_name", nullable = false, length = 100)
    private String ruleName;

    @Column(name = "rule_type", nullable = false, length = 30)
    private String ruleType;

    @Column(name = "condition_expression", length = 500)
    private String conditionExpression;

    @Column(name = "action_on_pass", nullable = false, length = 30)
    private String actionOnPass = "AUTO_VALIDATE";

    @Column(name = "action_on_fail", nullable = false, length = 30)
    private String actionOnFail = "FLAG_FOR_REVIEW";

    @Column(name = "priority", nullable = false)
    private Integer priority = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
