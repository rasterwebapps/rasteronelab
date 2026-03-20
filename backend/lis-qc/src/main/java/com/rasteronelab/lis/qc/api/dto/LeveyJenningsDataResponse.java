package com.rasteronelab.lis.qc.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class LeveyJenningsDataResponse {

    private String lotNumber;
    private String testName;
    private BigDecimal targetMean;
    private BigDecimal targetSD;
    private String unit;
    private List<QCDataPoint> dataPoints;

    public LeveyJenningsDataResponse() {}

    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    public BigDecimal getTargetMean() { return targetMean; }
    public void setTargetMean(BigDecimal targetMean) { this.targetMean = targetMean; }
    public BigDecimal getTargetSD() { return targetSD; }
    public void setTargetSD(BigDecimal targetSD) { this.targetSD = targetSD; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public List<QCDataPoint> getDataPoints() { return dataPoints; }
    public void setDataPoints(List<QCDataPoint> dataPoints) { this.dataPoints = dataPoints; }

    public static class QCDataPoint {
        private LocalDateTime date;
        private BigDecimal value;
        private BigDecimal sdIndex;
        private String westgardFlags;
        private boolean accepted;

        public QCDataPoint() {}

        public QCDataPoint(LocalDateTime date, BigDecimal value, BigDecimal sdIndex,
                           String westgardFlags, boolean accepted) {
            this.date = date;
            this.value = value;
            this.sdIndex = sdIndex;
            this.westgardFlags = westgardFlags;
            this.accepted = accepted;
        }

        public LocalDateTime getDate() { return date; }
        public void setDate(LocalDateTime date) { this.date = date; }
        public BigDecimal getValue() { return value; }
        public void setValue(BigDecimal value) { this.value = value; }
        public BigDecimal getSdIndex() { return sdIndex; }
        public void setSdIndex(BigDecimal sdIndex) { this.sdIndex = sdIndex; }
        public String getWestgardFlags() { return westgardFlags; }
        public void setWestgardFlags(String westgardFlags) { this.westgardFlags = westgardFlags; }
        public boolean isAccepted() { return accepted; }
        public void setAccepted(boolean accepted) { this.accepted = accepted; }
    }
}
