package com.rasteronelab.lis.qc.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "qc_result")
public class QCResult extends BaseEntity {

    @Column(name = "qc_lot_id", nullable = false)
    private UUID qcLotId;

    @Column(name = "test_id", nullable = false)
    private UUID testId;

    @Column(name = "test_code")
    private String testCode;

    @Column(name = "test_name")
    private String testName;

    @Column(name = "measured_value", nullable = false, precision = 12, scale = 4)
    private BigDecimal measuredValue;

    @Column(name = "target_mean", nullable = false, precision = 12, scale = 4)
    private BigDecimal targetMean;

    @Column(name = "target_sd", nullable = false, precision = 12, scale = 4)
    private BigDecimal targetSD;

    @Column(name = "sd_index", precision = 12, scale = 4)
    private BigDecimal sdIndex;

    @Column(name = "unit")
    private String unit;

    @Column(name = "westgard_flags")
    private String westgardFlags;

    @Enumerated(EnumType.STRING)
    @Column(name = "westgard_status", nullable = false)
    private WestgardStatus westgardStatus = WestgardStatus.NOT_EVALUATED;

    @Column(name = "accepted")
    private boolean accepted = true;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "performed_by")
    private String performedBy;

    @Column(name = "performed_at")
    private LocalDateTime performedAt;

    @Column(name = "reviewed_by")
    private String reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "instrument_id")
    private UUID instrumentId;

    @Column(name = "notes")
    private String notes;

    public QCResult() {
    }

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

    public WestgardStatus getWestgardStatus() { return westgardStatus; }
    public void setWestgardStatus(WestgardStatus westgardStatus) { this.westgardStatus = westgardStatus; }

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
}
