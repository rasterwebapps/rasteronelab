package com.rasteronelab.lis.result.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class ResultEntryRequest {

    @NotNull(message = "Test result ID is required")
    private UUID testResultId;

    private List<ResultValueEntry> values;

    private String comments;

    public UUID getTestResultId() {
        return testResultId;
    }

    public void setTestResultId(UUID testResultId) {
        this.testResultId = testResultId;
    }

    public List<ResultValueEntry> getValues() {
        return values;
    }

    public void setValues(List<ResultValueEntry> values) {
        this.values = values;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public static class ResultValueEntry {

        @NotNull(message = "Parameter ID is required")
        private UUID parameterId;

        private String parameterCode;

        private String parameterName;

        private String resultType;

        private String numericValue;

        private String textValue;

        private String unit;

        private String referenceRangeLow;

        private String referenceRangeHigh;

        private String referenceRangeText;

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

        public String getNumericValue() {
            return numericValue;
        }

        public void setNumericValue(String numericValue) {
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

        public String getReferenceRangeLow() {
            return referenceRangeLow;
        }

        public void setReferenceRangeLow(String referenceRangeLow) {
            this.referenceRangeLow = referenceRangeLow;
        }

        public String getReferenceRangeHigh() {
            return referenceRangeHigh;
        }

        public void setReferenceRangeHigh(String referenceRangeHigh) {
            this.referenceRangeHigh = referenceRangeHigh;
        }

        public String getReferenceRangeText() {
            return referenceRangeText;
        }

        public void setReferenceRangeText(String referenceRangeText) {
            this.referenceRangeText = referenceRangeText;
        }
    }
}
