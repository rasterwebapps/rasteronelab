package com.rasteronelab.lis.result.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "test_result")
public class TestResult extends BaseEntity {

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "order_line_item_id", nullable = false)
    private UUID orderLineItemId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "test_id", nullable = false)
    private UUID testId;

    @Column(name = "test_code", nullable = false)
    private String testCode;

    @Column(name = "test_name", nullable = false)
    private String testName;

    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "sample_id")
    private UUID sampleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ResultStatus status = ResultStatus.PENDING;

    @Column(name = "entered_by")
    private String enteredBy;

    @Column(name = "entered_at")
    private LocalDateTime enteredAt;

    @Column(name = "validated_by")
    private String validatedBy;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    @Column(name = "authorized_by")
    private String authorizedBy;

    @Column(name = "authorized_at")
    private LocalDateTime authorizedAt;

    @Column(name = "is_critical")
    private Boolean isCritical = false;

    @Column(name = "critical_acknowledged")
    private Boolean criticalAcknowledged = false;

    @Column(name = "critical_acknowledged_by")
    private String criticalAcknowledgedBy;

    @Column(name = "critical_acknowledged_at")
    private LocalDateTime criticalAcknowledgedAt;

    @Column(name = "has_delta_check_failure")
    private Boolean hasDeltaCheckFailure = false;

    @Column(name = "is_amended")
    private Boolean isAmended = false;

    @Column(name = "amendment_reason")
    private String amendmentReason;

    @Column(name = "amended_by")
    private String amendedBy;

    @Column(name = "amended_at")
    private LocalDateTime amendedAt;

    @Column(name = "comments")
    private String comments;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "testResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResultValue> resultValues = new ArrayList<>();

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

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
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

    public String getCriticalAcknowledgedBy() {
        return criticalAcknowledgedBy;
    }

    public void setCriticalAcknowledgedBy(String criticalAcknowledgedBy) {
        this.criticalAcknowledgedBy = criticalAcknowledgedBy;
    }

    public LocalDateTime getCriticalAcknowledgedAt() {
        return criticalAcknowledgedAt;
    }

    public void setCriticalAcknowledgedAt(LocalDateTime criticalAcknowledgedAt) {
        this.criticalAcknowledgedAt = criticalAcknowledgedAt;
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

    public String getAmendedBy() {
        return amendedBy;
    }

    public void setAmendedBy(String amendedBy) {
        this.amendedBy = amendedBy;
    }

    public LocalDateTime getAmendedAt() {
        return amendedAt;
    }

    public void setAmendedAt(LocalDateTime amendedAt) {
        this.amendedAt = amendedAt;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<ResultValue> getResultValues() {
        return resultValues;
    }

    public void setResultValues(List<ResultValue> resultValues) {
        this.resultValues = resultValues;
    }

    public void addResultValue(ResultValue resultValue) {
        resultValues.add(resultValue);
        resultValue.setTestResult(this);
    }

    public void removeResultValue(ResultValue resultValue) {
        resultValues.remove(resultValue);
        resultValue.setTestResult(null);
    }
}
