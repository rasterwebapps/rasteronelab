package com.rasteronelab.lis.qc.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "qc_lot")
public class QCLot extends BaseEntity {

    @Column(name = "lot_number", nullable = false)
    private String lotNumber;

    @Column(name = "material_name", nullable = false)
    private String materialName;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private QCLevel level;

    @Column(name = "test_id", nullable = false)
    private UUID testId;

    @Column(name = "test_code")
    private String testCode;

    @Column(name = "test_name")
    private String testName;

    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "target_mean", nullable = false, precision = 18, scale = 6)
    private BigDecimal targetMean;

    @Column(name = "target_sd", nullable = false, precision = 18, scale = 6)
    private BigDecimal targetSD;

    @Column(name = "unit")
    private String unit;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "notes")
    private String notes;

    public QCLot() {
    }

    public QCLot(String lotNumber, String materialName, String manufacturer, QCLevel level,
                 UUID testId, String testCode, String testName, UUID departmentId, String departmentName,
                 BigDecimal targetMean, BigDecimal targetSD, String unit, LocalDate expiryDate,
                 boolean isActive, String notes) {
        this.lotNumber = lotNumber;
        this.materialName = materialName;
        this.manufacturer = manufacturer;
        this.level = level;
        this.testId = testId;
        this.testCode = testCode;
        this.testName = testName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.targetMean = targetMean;
        this.targetSD = targetSD;
        this.unit = unit;
        this.expiryDate = expiryDate;
        this.isActive = isActive;
        this.notes = notes;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public QCLevel getLevel() {
        return level;
    }

    public void setLevel(QCLevel level) {
        this.level = level;
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

    public BigDecimal getTargetMean() {
        return targetMean;
    }

    public void setTargetMean(BigDecimal targetMean) {
        this.targetMean = targetMean;
    }

    public BigDecimal getTargetSD() {
        return targetSD;
    }

    public void setTargetSD(BigDecimal targetSD) {
        this.targetSD = targetSD;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String lotNumber;
        private String materialName;
        private String manufacturer;
        private QCLevel level;
        private UUID testId;
        private String testCode;
        private String testName;
        private UUID departmentId;
        private String departmentName;
        private BigDecimal targetMean;
        private BigDecimal targetSD;
        private String unit;
        private LocalDate expiryDate;
        private boolean isActive = true;
        private String notes;

        public Builder lotNumber(String lotNumber) {
            this.lotNumber = lotNumber;
            return this;
        }

        public Builder materialName(String materialName) {
            this.materialName = materialName;
            return this;
        }

        public Builder manufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public Builder level(QCLevel level) {
            this.level = level;
            return this;
        }

        public Builder testId(UUID testId) {
            this.testId = testId;
            return this;
        }

        public Builder testCode(String testCode) {
            this.testCode = testCode;
            return this;
        }

        public Builder testName(String testName) {
            this.testName = testName;
            return this;
        }

        public Builder departmentId(UUID departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public Builder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public Builder targetMean(BigDecimal targetMean) {
            this.targetMean = targetMean;
            return this;
        }

        public Builder targetSD(BigDecimal targetSD) {
            this.targetSD = targetSD;
            return this;
        }

        public Builder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public Builder expiryDate(LocalDate expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public QCLot build() {
            return new QCLot(lotNumber, materialName, manufacturer, level, testId, testCode,
                    testName, departmentId, departmentName, targetMean, targetSD, unit,
                    expiryDate, isActive, notes);
        }
    }
}
