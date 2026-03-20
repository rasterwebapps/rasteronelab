package com.rasteronelab.lis.result.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "result_value")
public class ResultValue extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_result_id", nullable = false)
    private TestResult testResult;

    @Column(name = "parameter_id", nullable = false)
    private UUID parameterId;

    @Column(name = "parameter_code", nullable = false)
    private String parameterCode;

    @Column(name = "parameter_name", nullable = false)
    private String parameterName;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_type", nullable = false)
    private ResultType resultType = ResultType.NUMERIC;

    @Column(name = "numeric_value", precision = 15, scale = 4)
    private BigDecimal numericValue;

    @Column(name = "text_value", length = 2000)
    private String textValue;

    @Column(name = "unit")
    private String unit;

    @Column(name = "reference_range_low", precision = 15, scale = 4)
    private BigDecimal referenceRangeLow;

    @Column(name = "reference_range_high", precision = 15, scale = 4)
    private BigDecimal referenceRangeHigh;

    @Column(name = "reference_range_text")
    private String referenceRangeText;

    @Enumerated(EnumType.STRING)
    @Column(name = "abnormal_flag")
    private AbnormalFlag abnormalFlag = AbnormalFlag.NORMAL;

    @Column(name = "is_critical")
    private Boolean isCritical = false;

    @Column(name = "previous_value", precision = 15, scale = 4)
    private BigDecimal previousValue;

    @Column(name = "delta_percentage", precision = 8, scale = 2)
    private BigDecimal deltaPercentage;

    @Enumerated(EnumType.STRING)
    @Column(name = "delta_check_status")
    private DeltaCheckStatus deltaCheckStatus;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(name = "is_calculated")
    private Boolean isCalculated = false;

    @Column(name = "formula")
    private String formula;

    @Column(name = "is_active")
    private Boolean isActive = true;

    public TestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
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

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
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

    public AbnormalFlag getAbnormalFlag() {
        return abnormalFlag;
    }

    public void setAbnormalFlag(AbnormalFlag abnormalFlag) {
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

    public DeltaCheckStatus getDeltaCheckStatus() {
        return deltaCheckStatus;
    }

    public void setDeltaCheckStatus(DeltaCheckStatus deltaCheckStatus) {
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

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
