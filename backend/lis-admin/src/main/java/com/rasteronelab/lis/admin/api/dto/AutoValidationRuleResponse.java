package com.rasteronelab.lis.admin.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for AutoValidationRule entity.
 * Includes parameterName and testName for display purposes.
 */
public class AutoValidationRuleResponse {

    private UUID id;
    private UUID branchId;
    private UUID parameterId;
    private String parameterName;
    private UUID testId;
    private String testName;
    private String ruleName;
    private String ruleType;
    private String conditionExpression;
    private String actionOnPass;
    private String actionOnFail;
    private Integer priority;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public AutoValidationRuleResponse() {
    }

    public AutoValidationRuleResponse(UUID id, UUID branchId, UUID parameterId, String parameterName, UUID testId, String testName, String ruleName, String ruleType, String conditionExpression, String actionOnPass, String actionOnFail, Integer priority, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.parameterId = parameterId;
        this.parameterName = parameterName;
        this.testId = testId;
        this.testName = testName;
        this.ruleName = ruleName;
        this.ruleType = ruleType;
        this.conditionExpression = conditionExpression;
        this.actionOnPass = actionOnPass;
        this.actionOnFail = actionOnFail;
        this.priority = priority;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getBranchId() {
        return this.branchId;
    }

    public UUID getParameterId() {
        return this.parameterId;
    }

    public String getParameterName() {
        return this.parameterName;
    }

    public UUID getTestId() {
        return this.testId;
    }

    public String getTestName() {
        return this.testName;
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

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

    public void setParameterId(UUID parameterId) {
        this.parameterId = parameterId;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public void setTestName(String testName) {
        this.testName = testName;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutoValidationRuleResponse that = (AutoValidationRuleResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.parameterId, that.parameterId) &&
               java.util.Objects.equals(this.parameterName, that.parameterName) &&
               java.util.Objects.equals(this.testId, that.testId) &&
               java.util.Objects.equals(this.testName, that.testName) &&
               java.util.Objects.equals(this.ruleName, that.ruleName) &&
               java.util.Objects.equals(this.ruleType, that.ruleType) &&
               java.util.Objects.equals(this.conditionExpression, that.conditionExpression) &&
               java.util.Objects.equals(this.actionOnPass, that.actionOnPass) &&
               java.util.Objects.equals(this.actionOnFail, that.actionOnFail) &&
               java.util.Objects.equals(this.priority, that.priority) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.parameterId, this.parameterName, this.testId, this.testName, this.ruleName, this.ruleType, this.conditionExpression, this.actionOnPass, this.actionOnFail, this.priority, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "AutoValidationRuleResponse{id=" + id +
               ", branchId=" + branchId +
               ", parameterId=" + parameterId +
               ", parameterName=" + parameterName +
               ", testId=" + testId +
               ", testName=" + testName +
               ", ruleName=" + ruleName +
               ", ruleType=" + ruleType +
               ", conditionExpression=" + conditionExpression +
               ", actionOnPass=" + actionOnPass +
               ", actionOnFail=" + actionOnFail +
               ", priority=" + priority +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static AutoValidationRuleResponseBuilder builder() {
        return new AutoValidationRuleResponseBuilder();
    }

    public static class AutoValidationRuleResponseBuilder {
        private UUID id;
        private UUID branchId;
        private UUID parameterId;
        private String parameterName;
        private UUID testId;
        private String testName;
        private String ruleName;
        private String ruleType;
        private String conditionExpression;
        private String actionOnPass;
        private String actionOnFail;
        private Integer priority;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        AutoValidationRuleResponseBuilder() {
        }

        public AutoValidationRuleResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public AutoValidationRuleResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public AutoValidationRuleResponseBuilder parameterId(UUID parameterId) {
            this.parameterId = parameterId;
            return this;
        }

        public AutoValidationRuleResponseBuilder parameterName(String parameterName) {
            this.parameterName = parameterName;
            return this;
        }

        public AutoValidationRuleResponseBuilder testId(UUID testId) {
            this.testId = testId;
            return this;
        }

        public AutoValidationRuleResponseBuilder testName(String testName) {
            this.testName = testName;
            return this;
        }

        public AutoValidationRuleResponseBuilder ruleName(String ruleName) {
            this.ruleName = ruleName;
            return this;
        }

        public AutoValidationRuleResponseBuilder ruleType(String ruleType) {
            this.ruleType = ruleType;
            return this;
        }

        public AutoValidationRuleResponseBuilder conditionExpression(String conditionExpression) {
            this.conditionExpression = conditionExpression;
            return this;
        }

        public AutoValidationRuleResponseBuilder actionOnPass(String actionOnPass) {
            this.actionOnPass = actionOnPass;
            return this;
        }

        public AutoValidationRuleResponseBuilder actionOnFail(String actionOnFail) {
            this.actionOnFail = actionOnFail;
            return this;
        }

        public AutoValidationRuleResponseBuilder priority(Integer priority) {
            this.priority = priority;
            return this;
        }

        public AutoValidationRuleResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public AutoValidationRuleResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AutoValidationRuleResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public AutoValidationRuleResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public AutoValidationRuleResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public AutoValidationRuleResponse build() {
            return new AutoValidationRuleResponse(this.id, this.branchId, this.parameterId, this.parameterName, this.testId, this.testName, this.ruleName, this.ruleType, this.conditionExpression, this.actionOnPass, this.actionOnFail, this.priority, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
