package com.rasteronelab.lis.report.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for signing a lab report.
 */
public class ReportSignRequest {

    @NotBlank
    private String signedBy;

    private String notes;

    public ReportSignRequest() {
    }

    private ReportSignRequest(Builder builder) {
        this.signedBy = builder.signedBy;
        this.notes = builder.notes;
    }

    public String getSignedBy() { return signedBy; }
    public void setSignedBy(String signedBy) { this.signedBy = signedBy; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String signedBy;
        private String notes;

        public Builder signedBy(String signedBy) { this.signedBy = signedBy; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }

        public ReportSignRequest build() {
            return new ReportSignRequest(this);
        }
    }
}
