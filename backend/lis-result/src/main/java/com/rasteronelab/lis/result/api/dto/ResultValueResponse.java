package com.rasteronelab.lis.result.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ResultValueResponse {

    private UUID id;
    private UUID parameterId;
    private String parameterCode;
    private String parameterName;
    private String resultType;
    private BigDecimal numericValue;
    private String textValue;
    private String unit;
    private BigDecimal referenceRangeLow;
    private BigDecimal referenceRangeHigh;
    private String referenceRangeText;
    private String abnormalFlag;
    private Boolean isCritical;
    private BigDecimal previousValue;
    private BigDecimal deltaPercentage;
    private String deltaCheckStatus;
    private Integer sortOrder;
    private Boolean isCalculated;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getParameterId() {
        return parameterId;
    }

    public void setParameterId(UUID parameterId) {
        this.parameterId = parameterId;
    }

    public String getParameterCode() {
        return parameterCode;
    }

    public void setParameterCode(String parameterCode) {
        this.parameterCode = parameterCode;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public BigDecimal getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(BigDecimal numericValue) {
        this.numericValue = numericValue;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getReferenceRangeLow() {
        return referenceRangeLow;
    }

    public void setReferenceRangeLow(BigDecimal referenceRangeLow) {
        this.referenceRangeLow = referenceRangeLow;
    }

    public BigDecimal getReferenceRangeHigh() {
        return referenceRangeHigh;
    }

    public void setReferenceRangeHigh(BigDecimal referenceRangeHigh) {
        this.referenceRangeHigh = referenceRangeHigh;
    }

    public String getReferenceRangeText() {
        return referenceRangeText;
    }

    public void setReferenceRangeText(String referenceRangeText) {
        this.referenceRangeText = referenceRangeText;
    }

    public String getAbnormalFlag() {
        return abnormalFlag;
    }

    public void setAbnormalFlag(String abnormalFlag) {
        this.abnormalFlag = abnormalFlag;
    }

    public Boolean getIsCritical() {
        return isCritical;
    }

    public void setIsCritical(Boolean isCritical) {
        this.isCritical = isCritical;
    }

    public BigDecimal getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(BigDecimal previousValue) {
        this.previousValue = previousValue;
    }

    public BigDecimal getDeltaPercentage() {
        return deltaPercentage;
    }

    public void setDeltaPercentage(BigDecimal deltaPercentage) {
        this.deltaPercentage = deltaPercentage;
    }

    public String getDeltaCheckStatus() {
        return deltaCheckStatus;
    }

    public void setDeltaCheckStatus(String deltaCheckStatus) {
        this.deltaCheckStatus = deltaCheckStatus;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsCalculated() {
        return isCalculated;
    }

    public void setIsCalculated(Boolean isCalculated) {
        this.isCalculated = isCalculated;
    }
}
