package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a Parameter.
 */
public class ParameterRequest {

    @NotBlank(message = "Parameter name is required")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;

    @NotBlank(message = "Parameter code is required")
    @Size(max = 30, message = "Code must not exceed 30 characters")
    private String code;

    @Size(max = 200, message = "Print name must not exceed 200 characters")
    private String printName;

    @Size(max = 30, message = "Unit must not exceed 30 characters")
    private String unit;

    @NotBlank(message = "Data type is required")
    private String dataType;

    private Integer decimalPlaces;

    private Integer displayOrder;

    @Size(max = 100, message = "Method must not exceed 100 characters")
    private String method;

    @Size(max = 20, message = "LOINC code must not exceed 20 characters")
    private String loincCode;

    @Size(max = 500, message = "Formula must not exceed 500 characters")
    private String formula;

    private Boolean isCalculated;

    private Boolean isActive;

    public ParameterRequest() {
    }

    public ParameterRequest(String name, String code, String printName, String unit, String dataType, Integer decimalPlaces, Integer displayOrder, String method, String loincCode, String formula, Boolean isCalculated, Boolean isActive) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParameterRequest that = (ParameterRequest) o;
        return java.util.Objects.equals(this.name, that.name) &&
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
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.name, this.code, this.printName, this.unit, this.dataType, this.decimalPlaces, this.displayOrder, this.method, this.loincCode, this.formula, this.isCalculated, this.isActive);
    }

    @Override
    public String toString() {
        return "ParameterRequest{name=" + name +
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
               "}";
    }

    public static ParameterRequestBuilder builder() {
        return new ParameterRequestBuilder();
    }

    public static class ParameterRequestBuilder {
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

        ParameterRequestBuilder() {
        }

        public ParameterRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ParameterRequestBuilder code(String code) {
            this.code = code;
            return this;
        }

        public ParameterRequestBuilder printName(String printName) {
            this.printName = printName;
            return this;
        }

        public ParameterRequestBuilder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public ParameterRequestBuilder dataType(String dataType) {
            this.dataType = dataType;
            return this;
        }

        public ParameterRequestBuilder decimalPlaces(Integer decimalPlaces) {
            this.decimalPlaces = decimalPlaces;
            return this;
        }

        public ParameterRequestBuilder displayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
            return this;
        }

        public ParameterRequestBuilder method(String method) {
            this.method = method;
            return this;
        }

        public ParameterRequestBuilder loincCode(String loincCode) {
            this.loincCode = loincCode;
            return this;
        }

        public ParameterRequestBuilder formula(String formula) {
            this.formula = formula;
            return this;
        }

        public ParameterRequestBuilder isCalculated(Boolean isCalculated) {
            this.isCalculated = isCalculated;
            return this;
        }

        public ParameterRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public ParameterRequest build() {
            return new ParameterRequest(this.name, this.code, this.printName, this.unit, this.dataType, this.decimalPlaces, this.displayOrder, this.method, this.loincCode, this.formula, this.isCalculated, this.isActive);
        }
    }

}
