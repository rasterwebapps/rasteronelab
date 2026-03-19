package com.rasteronelab.lis.qc.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class QCLotCreateRequest {

    private String lotNumber;
    private String materialName;
    private String manufacturer;
    private String level;
    private UUID testId;
    private String testCode;
    private String testName;
    private UUID departmentId;
    private String departmentName;
    private BigDecimal targetMean;
    private BigDecimal targetSD;
    private String unit;
    private LocalDate expiryDate;
    private String notes;

    public QCLotCreateRequest() {}

    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public UUID getTestId() { return testId; }
    public void setTestId(UUID testId) { this.testId = testId; }
    public String getTestCode() { return testCode; }
    public void setTestCode(String testCode) { this.testCode = testCode; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    public UUID getDepartmentId() { return departmentId; }
    public void setDepartmentId(UUID departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public BigDecimal getTargetMean() { return targetMean; }
    public void setTargetMean(BigDecimal targetMean) { this.targetMean = targetMean; }
    public BigDecimal getTargetSD() { return targetSD; }
    public void setTargetSD(BigDecimal targetSD) { this.targetSD = targetSD; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
