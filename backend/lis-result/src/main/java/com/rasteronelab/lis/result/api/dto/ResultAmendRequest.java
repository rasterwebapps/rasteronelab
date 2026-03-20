package com.rasteronelab.lis.result.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class ResultAmendRequest {

    @NotNull(message = "Test result ID is required")
    private UUID testResultId;

    @NotBlank(message = "Amendment reason is required")
    private String amendmentReason;

    private List<ResultEntryRequest.ResultValueEntry> values;

    public UUID getTestResultId() {
        return testResultId;
    }

    public void setTestResultId(UUID testResultId) {
        this.testResultId = testResultId;
    }

    public String getAmendmentReason() {
        return amendmentReason;
    }

    public void setAmendmentReason(String amendmentReason) {
        this.amendmentReason = amendmentReason;
    }

    public List<ResultEntryRequest.ResultValueEntry> getValues() {
        return values;
    }

    public void setValues(List<ResultEntryRequest.ResultValueEntry> values) {
        this.values = values;
    }
}
