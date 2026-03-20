package com.rasteronelab.lis.qc.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class QCResultEntryRequest {

    private UUID qcLotId;
    private BigDecimal measuredValue;
    private String performedBy;
    private UUID instrumentId;
    private String notes;

    public QCResultEntryRequest() {}

    public UUID getQcLotId() { return qcLotId; }
    public void setQcLotId(UUID qcLotId) { this.qcLotId = qcLotId; }
    public BigDecimal getMeasuredValue() { return measuredValue; }
    public void setMeasuredValue(BigDecimal measuredValue) { this.measuredValue = measuredValue; }
    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }
    public UUID getInstrumentId() { return instrumentId; }
    public void setInstrumentId(UUID instrumentId) { this.instrumentId = instrumentId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
