package com.rasteronelab.lis.result.api.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TestResultResponse {

    private UUID id;
    private UUID orderId;
    private UUID orderLineItemId;
    private UUID patientId;
    private UUID testId;
    private String testCode;
    private String testName;
    private UUID departmentId;
    private String departmentName;
    private UUID sampleId;
    private String status;
    private String enteredBy;
    private LocalDateTime enteredAt;
    private String validatedBy;
    private LocalDateTime validatedAt;
    private String authorizedBy;
    private LocalDateTime authorizedAt;
    private Boolean isCritical;
    private Boolean criticalAcknowledged;
    private Boolean hasDeltaCheckFailure;
    private Boolean isAmended;
    private String amendmentReason;
    private String comments;
    private List<ResultValueResponse> resultValues;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getOrderLineItemId() {
        return orderLineItemId;
    }

    public void setOrderLineItemId(UUID orderLineItemId) {
        this.orderLineItemId = orderLineItemId;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public UUID getTestId() {
        return testId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public UUID getSampleId() {
        return sampleId;
    }

    public void setSampleId(UUID sampleId) {
        this.sampleId = sampleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        this.enteredBy = enteredBy;
    }

    public LocalDateTime getEnteredAt() {
        return enteredAt;
    }

    public void setEnteredAt(LocalDateTime enteredAt) {
        this.enteredAt = enteredAt;
    }

    public String getValidatedBy() {
        return validatedBy;
    }

    public void setValidatedBy(String validatedBy) {
        this.validatedBy = validatedBy;
    }

    public LocalDateTime getValidatedAt() {
        return validatedAt;
    }

    public void setValidatedAt(LocalDateTime validatedAt) {
        this.validatedAt = validatedAt;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public LocalDateTime getAuthorizedAt() {
        return authorizedAt;
    }

    public void setAuthorizedAt(LocalDateTime authorizedAt) {
        this.authorizedAt = authorizedAt;
    }

    public Boolean getIsCritical() {
        return isCritical;
    }

    public void setIsCritical(Boolean isCritical) {
        this.isCritical = isCritical;
    }

    public Boolean getCriticalAcknowledged() {
        return criticalAcknowledged;
    }

    public void setCriticalAcknowledged(Boolean criticalAcknowledged) {
        this.criticalAcknowledged = criticalAcknowledged;
    }

    public Boolean getHasDeltaCheckFailure() {
        return hasDeltaCheckFailure;
    }

    public void setHasDeltaCheckFailure(Boolean hasDeltaCheckFailure) {
        this.hasDeltaCheckFailure = hasDeltaCheckFailure;
    }

    public Boolean getIsAmended() {
        return isAmended;
    }

    public void setIsAmended(Boolean isAmended) {
        this.isAmended = isAmended;
    }

    public String getAmendmentReason() {
        return amendmentReason;
    }

    public void setAmendmentReason(String amendmentReason) {
        this.amendmentReason = amendmentReason;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<ResultValueResponse> getResultValues() {
        return resultValues;
    }

    public void setResultValues(List<ResultValueResponse> resultValues) {
        this.resultValues = resultValues;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
