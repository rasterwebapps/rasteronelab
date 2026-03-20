package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Request DTO for creating or updating an Auto Validation Rule.
 */
public class AutoValidationRuleRequest {

    private UUID parameterId;

    private UUID testId;

    @NotBlank(message = "Rule name is required")
    @Size(max = 100, message = "Rule name must not exceed 100 characters")
    private String ruleName;

    @NotBlank(message = "Rule type is required")
    @Size(max = 30, message = "Rule type must not exceed 30 characters")
    private String ruleType;

    @Size(max = 500, message = "Condition expression must not exceed 500 characters")
    private String conditionExpression;

    @Size(max = 30, message = "Action on pass must not exceed 30 characters")
    private String actionOnPass;

    @Size(max = 30, message = "Action on fail must not exceed 30 characters")
    private String actionOnFail;

    private Integer priority;

    private Boolean isActive;

    public AutoValidationRuleRequest() {
    }

    public AutoValidationRuleRequest(UUID parameterId, UUID testId, String ruleName, String ruleType, String conditionExpression, String actionOnPass, String actionOnFail, Integer priority, Boolean isActive) {
        this.parameterId = parameterId;
        this.testId = testId;
        this.ruleName = ruleName;
        this.ruleType = ruleType;
        this.conditionExpression = conditionExpression;
        this.actionOnPass = actionOnPass;
        this.actionOnFail = actionOnFail;
        this.priority = priority;
        this.isActive = isActive;
    }

    public UUID getParameterId() {
        return this.parameterId;
    }

    public UUID getTestId() {
        return this.testId;
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

    public void setTestId(UUID testId) {
        this.testId = testId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutoValidationRuleRequest that = (AutoValidationRuleRequest) o;
        return java.util.Objects.equals(this.parameterId, that.parameterId) &&
               java.util.Objects.equals(this.testId, that.testId) &&
               java.util.Objects.equals(this.ruleName, that.ruleName) &&
               java.util.Objects.equals(this.ruleType, that.ruleType) &&
               java.util.Objects.equals(this.conditionExpression, that.conditionExpression) &&
               java.util.Objects.equals(this.actionOnPass, that.actionOnPass) &&
               java.util.Objects.equals(this.actionOnFail, that.actionOnFail) &&
               java.util.Objects.equals(this.priority, that.priority) &&
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.parameterId, this.testId, this.ruleName, this.ruleType, this.conditionExpression, this.actionOnPass, this.actionOnFail, this.priority, this.isActive);
    }

    @Override
    public String toString() {
        return "AutoValidationRuleRequest{parameterId=" + parameterId +
               ", testId=" + testId +
               ", ruleName=" + ruleName +
               ", ruleType=" + ruleType +
               ", conditionExpression=" + conditionExpression +
               ", actionOnPass=" + actionOnPass +
               ", actionOnFail=" + actionOnFail +
               ", priority=" + priority +
               ", isActive=" + isActive +
               "}";
    }

    public static AutoValidationRuleRequestBuilder builder() {
        return new AutoValidationRuleRequestBuilder();
    }

    public static class AutoValidationRuleRequestBuilder {
        private UUID parameterId;
        private UUID testId;
        private String ruleName;
        private String ruleType;
        private String conditionExpression;
        private String actionOnPass;
        private String actionOnFail;
        private Integer priority;
        private Boolean isActive;

        AutoValidationRuleRequestBuilder() {
        }

        public AutoValidationRuleRequestBuilder parameterId(UUID parameterId) {
            this.parameterId = parameterId;
            return this;
        }

        public AutoValidationRuleRequestBuilder testId(UUID testId) {
            this.testId = testId;
            return this;
        }

        public AutoValidationRuleRequestBuilder ruleName(String ruleName) {
            this.ruleName = ruleName;
            return this;
        }

        public AutoValidationRuleRequestBuilder ruleType(String ruleType) {
            this.ruleType = ruleType;
            return this;
        }

        public AutoValidationRuleRequestBuilder conditionExpression(String conditionExpression) {
            this.conditionExpression = conditionExpression;
            return this;
        }

        public AutoValidationRuleRequestBuilder actionOnPass(String actionOnPass) {
            this.actionOnPass = actionOnPass;
            return this;
        }

        public AutoValidationRuleRequestBuilder actionOnFail(String actionOnFail) {
            this.actionOnFail = actionOnFail;
            return this;
        }

        public AutoValidationRuleRequestBuilder priority(Integer priority) {
            this.priority = priority;
            return this;
        }

        public AutoValidationRuleRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public AutoValidationRuleRequest build() {
            return new AutoValidationRuleRequest(this.parameterId, this.testId, this.ruleName, this.ruleType, this.conditionExpression, this.actionOnPass, this.actionOnFail, this.priority, this.isActive);
        }
    }

}
