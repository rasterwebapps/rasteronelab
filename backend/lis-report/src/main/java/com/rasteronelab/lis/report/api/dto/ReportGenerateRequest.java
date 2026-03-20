package com.rasteronelab.lis.report.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO for generating a new lab report.
 */
public class ReportGenerateRequest {

    @NotNull
    private UUID orderId;

    @NotNull
    private UUID patientId;

    @NotNull
    private String patientName;

    private UUID departmentId;

    private String departmentName;

    private String reportType = "INDIVIDUAL";

    private UUID templateId;

    private String notes;

    public ReportGenerateRequest() {
    }

    private ReportGenerateRequest(Builder builder) {
        this.orderId = builder.orderId;
        this.patientId = builder.patientId;
        this.patientName = builder.patientName;
        this.departmentId = builder.departmentId;
        this.departmentName = builder.departmentName;
        this.reportType = builder.reportType;
        this.templateId = builder.templateId;
        this.notes = builder.notes;
    }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public UUID getPatientId() { return patientId; }
    public void setPatientId(UUID patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public UUID getDepartmentId() { return departmentId; }
    public void setDepartmentId(UUID departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public UUID getTemplateId() { return templateId; }
    public void setTemplateId(UUID templateId) { this.templateId = templateId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID orderId;
        private UUID patientId;
        private String patientName;
        private UUID departmentId;
        private String departmentName;
        private String reportType = "INDIVIDUAL";
        private UUID templateId;
        private String notes;

        public Builder orderId(UUID orderId) { this.orderId = orderId; return this; }
        public Builder patientId(UUID patientId) { this.patientId = patientId; return this; }
        public Builder patientName(String patientName) { this.patientName = patientName; return this; }
        public Builder departmentId(UUID departmentId) { this.departmentId = departmentId; return this; }
        public Builder departmentName(String departmentName) { this.departmentName = departmentName; return this; }
        public Builder reportType(String reportType) { this.reportType = reportType; return this; }
        public Builder templateId(UUID templateId) { this.templateId = templateId; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }

        public ReportGenerateRequest build() {
            return new ReportGenerateRequest(this);
        }
    }
}
