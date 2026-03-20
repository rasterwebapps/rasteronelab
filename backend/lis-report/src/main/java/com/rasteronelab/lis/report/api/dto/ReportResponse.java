package com.rasteronelab.lis.report.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for a lab report.
 */
public class ReportResponse {

    private UUID id;
    private UUID orderId;
    private UUID patientId;
    private String patientName;
    private String reportNumber;
    private String reportType;
    private String reportStatus;
    private UUID departmentId;
    private String departmentName;
    private String generatedBy;
    private LocalDateTime generatedAt;
    private String signedBy;
    private LocalDateTime signedAt;
    private LocalDateTime deliveredAt;
    private String deliveryChannel;
    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private Integer pageCount;
    private UUID templateId;
    private Integer version;
    private String amendmentReason;
    private String notes;
    private UUID branchId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public ReportResponse() {
    }

    private ReportResponse(Builder builder) {
        this.id = builder.id;
        this.orderId = builder.orderId;
        this.patientId = builder.patientId;
        this.patientName = builder.patientName;
        this.reportNumber = builder.reportNumber;
        this.reportType = builder.reportType;
        this.reportStatus = builder.reportStatus;
        this.departmentId = builder.departmentId;
        this.departmentName = builder.departmentName;
        this.generatedBy = builder.generatedBy;
        this.generatedAt = builder.generatedAt;
        this.signedBy = builder.signedBy;
        this.signedAt = builder.signedAt;
        this.deliveredAt = builder.deliveredAt;
        this.deliveryChannel = builder.deliveryChannel;
        this.fileUrl = builder.fileUrl;
        this.fileName = builder.fileName;
        this.fileSize = builder.fileSize;
        this.pageCount = builder.pageCount;
        this.templateId = builder.templateId;
        this.version = builder.version;
        this.amendmentReason = builder.amendmentReason;
        this.notes = builder.notes;
        this.branchId = builder.branchId;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.createdBy = builder.createdBy;
        this.updatedBy = builder.updatedBy;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public UUID getPatientId() { return patientId; }
    public void setPatientId(UUID patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getReportNumber() { return reportNumber; }
    public void setReportNumber(String reportNumber) { this.reportNumber = reportNumber; }

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public String getReportStatus() { return reportStatus; }
    public void setReportStatus(String reportStatus) { this.reportStatus = reportStatus; }

    public UUID getDepartmentId() { return departmentId; }
    public void setDepartmentId(UUID departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    public String getSignedBy() { return signedBy; }
    public void setSignedBy(String signedBy) { this.signedBy = signedBy; }

    public LocalDateTime getSignedAt() { return signedAt; }
    public void setSignedAt(LocalDateTime signedAt) { this.signedAt = signedAt; }

    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }

    public String getDeliveryChannel() { return deliveryChannel; }
    public void setDeliveryChannel(String deliveryChannel) { this.deliveryChannel = deliveryChannel; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public Integer getPageCount() { return pageCount; }
    public void setPageCount(Integer pageCount) { this.pageCount = pageCount; }

    public UUID getTemplateId() { return templateId; }
    public void setTemplateId(UUID templateId) { this.templateId = templateId; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public String getAmendmentReason() { return amendmentReason; }
    public void setAmendmentReason(String amendmentReason) { this.amendmentReason = amendmentReason; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public UUID getBranchId() { return branchId; }
    public void setBranchId(UUID branchId) { this.branchId = branchId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID orderId;
        private UUID patientId;
        private String patientName;
        private String reportNumber;
        private String reportType;
        private String reportStatus;
        private UUID departmentId;
        private String departmentName;
        private String generatedBy;
        private LocalDateTime generatedAt;
        private String signedBy;
        private LocalDateTime signedAt;
        private LocalDateTime deliveredAt;
        private String deliveryChannel;
        private String fileUrl;
        private String fileName;
        private Long fileSize;
        private Integer pageCount;
        private UUID templateId;
        private Integer version;
        private String amendmentReason;
        private String notes;
        private UUID branchId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder orderId(UUID orderId) { this.orderId = orderId; return this; }
        public Builder patientId(UUID patientId) { this.patientId = patientId; return this; }
        public Builder patientName(String patientName) { this.patientName = patientName; return this; }
        public Builder reportNumber(String reportNumber) { this.reportNumber = reportNumber; return this; }
        public Builder reportType(String reportType) { this.reportType = reportType; return this; }
        public Builder reportStatus(String reportStatus) { this.reportStatus = reportStatus; return this; }
        public Builder departmentId(UUID departmentId) { this.departmentId = departmentId; return this; }
        public Builder departmentName(String departmentName) { this.departmentName = departmentName; return this; }
        public Builder generatedBy(String generatedBy) { this.generatedBy = generatedBy; return this; }
        public Builder generatedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; return this; }
        public Builder signedBy(String signedBy) { this.signedBy = signedBy; return this; }
        public Builder signedAt(LocalDateTime signedAt) { this.signedAt = signedAt; return this; }
        public Builder deliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; return this; }
        public Builder deliveryChannel(String deliveryChannel) { this.deliveryChannel = deliveryChannel; return this; }
        public Builder fileUrl(String fileUrl) { this.fileUrl = fileUrl; return this; }
        public Builder fileName(String fileName) { this.fileName = fileName; return this; }
        public Builder fileSize(Long fileSize) { this.fileSize = fileSize; return this; }
        public Builder pageCount(Integer pageCount) { this.pageCount = pageCount; return this; }
        public Builder templateId(UUID templateId) { this.templateId = templateId; return this; }
        public Builder version(Integer version) { this.version = version; return this; }
        public Builder amendmentReason(String amendmentReason) { this.amendmentReason = amendmentReason; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }
        public Builder branchId(UUID branchId) { this.branchId = branchId; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public Builder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
        public Builder updatedBy(String updatedBy) { this.updatedBy = updatedBy; return this; }

        public ReportResponse build() {
            return new ReportResponse(this);
        }
    }
}
