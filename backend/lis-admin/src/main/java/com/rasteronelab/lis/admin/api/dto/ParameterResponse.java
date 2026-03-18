package com.rasteronelab.lis.admin.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Parameter entity.
 */
public class ParameterResponse {

    private UUID id;
    private UUID branchId;
    private String name;
    private String code;
    private String printName;
    private String unit;
    private String dataType;
    private Integer decimalPlaces;
    private Integer displayOrder;
    private String method;
    private String loincCode;
    private String formula;
    private Boolean isCalculated;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public ParameterResponse() {
    }

    public ParameterResponse(UUID id, UUID branchId, String name, String code, String printName, String unit, String dataType, Integer decimalPlaces, Integer displayOrder, String method, String loincCode, String formula, Boolean isCalculated, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.name = name;
        this.code = code;
        this.printName = printName;
        this.unit = unit;
        this.dataType = dataType;
        this.decimalPlaces = decimalPlaces;
        this.displayOrder = displayOrder;
        this.method = method;
        this.loincCode = loincCode;
        this.formula = formula;
        this.isCalculated = isCalculated;
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

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getPrintName() {
        return this.printName;
    }

    public String getUnit() {
        return this.unit;
    }

    public String getDataType() {
        return this.dataType;
    }

    public Integer getDecimalPlaces() {
        return this.decimalPlaces;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public String getMethod() {
        return this.method;
    }

    public String getLoincCode() {
        return this.loincCode;
    }

    public String getFormula() {
        return this.formula;
    }

    public Boolean getIsCalculated() {
        return this.isCalculated;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setLoincCode(String loincCode) {
        this.loincCode = loincCode;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public void setIsCalculated(Boolean isCalculated) {
        this.isCalculated = isCalculated;
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
        ParameterResponse that = (ParameterResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.name, that.name) &&
               java.util.Objects.equals(this.code, that.code) &&
               java.util.Objects.equals(this.printName, that.printName) &&
               java.util.Objects.equals(this.unit, that.unit) &&
               java.util.Objects.equals(this.dataType, that.dataType) &&
               java.util.Objects.equals(this.decimalPlaces, that.decimalPlaces) &&
               java.util.Objects.equals(this.displayOrder, that.displayOrder) &&
               java.util.Objects.equals(this.method, that.method) &&
               java.util.Objects.equals(this.loincCode, that.loincCode) &&
               java.util.Objects.equals(this.formula, that.formula) &&
               java.util.Objects.equals(this.isCalculated, that.isCalculated) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.name, this.code, this.printName, this.unit, this.dataType, this.decimalPlaces, this.displayOrder, this.method, this.loincCode, this.formula, this.isCalculated, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "ParameterResponse{id=" + id +
               ", branchId=" + branchId +
               ", name=" + name +
               ", code=" + code +
               ", printName=" + printName +
               ", unit=" + unit +
               ", dataType=" + dataType +
               ", decimalPlaces=" + decimalPlaces +
               ", displayOrder=" + displayOrder +
               ", method=" + method +
               ", loincCode=" + loincCode +
               ", formula=" + formula +
               ", isCalculated=" + isCalculated +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static ParameterResponseBuilder builder() {
        return new ParameterResponseBuilder();
    }

    public static class ParameterResponseBuilder {
        private UUID id;
        private UUID branchId;
        private String name;
        private String code;
        private String printName;
        private String unit;
        private String dataType;
        private Integer decimalPlaces;
        private Integer displayOrder;
        private String method;
        private String loincCode;
        private String formula;
        private Boolean isCalculated;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        ParameterResponseBuilder() {
        }

        public ParameterResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ParameterResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public ParameterResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ParameterResponseBuilder code(String code) {
            this.code = code;
            return this;
        }

        public ParameterResponseBuilder printName(String printName) {
            this.printName = printName;
            return this;
        }

        public ParameterResponseBuilder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public ParameterResponseBuilder dataType(String dataType) {
            this.dataType = dataType;
            return this;
        }

        public ParameterResponseBuilder decimalPlaces(Integer decimalPlaces) {
            this.decimalPlaces = decimalPlaces;
            return this;
        }

        public ParameterResponseBuilder displayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
            return this;
        }

        public ParameterResponseBuilder method(String method) {
            this.method = method;
            return this;
        }

        public ParameterResponseBuilder loincCode(String loincCode) {
            this.loincCode = loincCode;
            return this;
        }

        public ParameterResponseBuilder formula(String formula) {
            this.formula = formula;
            return this;
        }

        public ParameterResponseBuilder isCalculated(Boolean isCalculated) {
            this.isCalculated = isCalculated;
            return this;
        }

        public ParameterResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public ParameterResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ParameterResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ParameterResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public ParameterResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public ParameterResponse build() {
            return new ParameterResponse(this.id, this.branchId, this.name, this.code, this.printName, this.unit, this.dataType, this.decimalPlaces, this.displayOrder, this.method, this.loincCode, this.formula, this.isCalculated, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
