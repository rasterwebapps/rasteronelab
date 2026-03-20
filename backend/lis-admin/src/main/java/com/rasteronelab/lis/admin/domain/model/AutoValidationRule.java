package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

/**
 * Auto Validation Rule entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Defines rules for automatic result validation based on configurable conditions.
 */
@Entity
@Table(name = "auto_validation_rule")
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

    public UUID getParameterId() {
        return this.parameterId;
    }

    public Parameter getParameter() {
        return this.parameter;
    }

    public UUID getTestId() {
        return this.testId;
    }

    public TestMaster getTest() {
        return this.test;
    }

    public String getRuleName() {
        return this.ruleName;
    }

    public String getRuleType() {
        return this.ruleType;
    }

    public String getConditionExpression() {
        return this.conditionExpression;
    }

    public String getActionOnPass() {
        return this.actionOnPass;
    }

    public String getActionOnFail() {
        return this.actionOnFail;
    }

    public Integer getPriority() {
        return this.priority;
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

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public void setTest(TestMaster test) {
        this.test = test;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public void setConditionExpression(String conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    public void setActionOnPass(String actionOnPass) {
        this.actionOnPass = actionOnPass;
    }

    public void setActionOnFail(String actionOnFail) {
        this.actionOnFail = actionOnFail;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
