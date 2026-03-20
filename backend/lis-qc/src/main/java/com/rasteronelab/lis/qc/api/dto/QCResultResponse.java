package com.rasteronelab.lis.qc.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class QCResultResponse {

    private UUID id;
    private UUID branchId;
    private UUID qcLotId;
    private UUID testId;
    private String testCode;
    private String testName;
    private BigDecimal measuredValue;
    private BigDecimal targetMean;
    private BigDecimal targetSD;
    private BigDecimal sdIndex;
    private String unit;
    private String westgardFlags;
    private String westgardStatus;
    private boolean accepted;
    private String rejectionReason;
    private String performedBy;
    private LocalDateTime performedAt;
    private String reviewedBy;
    private LocalDateTime reviewedAt;
    private UUID instrumentId;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QCResultResponse() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getBranchId() { return branchId; }
    public void setBranchId(UUID branchId) { this.branchId = branchId; }
    public UUID getQcLotId() { return qcLotId; }
    public void setQcLotId(UUID qcLotId) { this.qcLotId = qcLotId; }
    public UUID getTestId() { return testId; }
    public void setTestId(UUID testId) { this.testId = testId; }
    public String getTestCode() { return testCode; }
    public void setTestCode(String testCode) { this.testCode = testCode; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    public BigDecimal getMeasuredValue() { return measuredValue; }
    public void setMeasuredValue(BigDecimal measuredValue) { this.measuredValue = measuredValue; }
    public BigDecimal getTargetMean() { return targetMean; }
    public void setTargetMean(BigDecimal targetMean) { this.targetMean = targetMean; }
    public BigDecimal getTargetSD() { return targetSD; }
    public void setTargetSD(BigDecimal targetSD) { this.targetSD = targetSD; }
    public BigDecimal getSdIndex() { return sdIndex; }
    public void setSdIndex(BigDecimal sdIndex) { this.sdIndex = sdIndex; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getWestgardFlags() { return westgardFlags; }
    public void setWestgardFlags(String westgardFlags) { this.westgardFlags = westgardFlags; }
    public String getWestgardStatus() { return westgardStatus; }
    public void setWestgardStatus(String westgardStatus) { this.westgardStatus = westgardStatus; }
    public boolean isAccepted() { return accepted; }
    public void setAccepted(boolean accepted) { this.accepted = accepted; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }
    public LocalDateTime getPerformedAt() { return performedAt; }
    public void setPerformedAt(LocalDateTime performedAt) { this.performedAt = performedAt; }
    public String getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    public UUID getInstrumentId() { return instrumentId; }
    public void setInstrumentId(UUID instrumentId) { this.instrumentId = instrumentId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
