package com.rasteronelab.lis.admin.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for NotificationTemplate entity.
 * Includes all entity fields plus audit metadata.
 */
public class NotificationTemplateResponse {

    private UUID id;
    private UUID branchId;
    private String templateCode;
    private String templateName;
    private String channel;
    private String templateBody;
    private String eventTrigger;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public NotificationTemplateResponse() {
    }

    public NotificationTemplateResponse(UUID id, UUID branchId, String templateCode, String templateName, String channel, String templateBody, String eventTrigger, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.templateCode = templateCode;
        this.templateName = templateName;
        this.channel = channel;
        this.templateBody = templateBody;
        this.eventTrigger = eventTrigger;
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

    public String getTemplateCode() {
        return this.templateCode;
    }

    public String getTemplateName() {
        return this.templateName;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getTemplateBody() {
        return this.templateBody;
    }

    public String getEventTrigger() {
        return this.eventTrigger;
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

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setTemplateBody(String templateBody) {
        this.templateBody = templateBody;
    }

    public void setEventTrigger(String eventTrigger) {
        this.eventTrigger = eventTrigger;
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
        NotificationTemplateResponse that = (NotificationTemplateResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.templateCode, that.templateCode) &&
               java.util.Objects.equals(this.templateName, that.templateName) &&
               java.util.Objects.equals(this.channel, that.channel) &&
               java.util.Objects.equals(this.templateBody, that.templateBody) &&
               java.util.Objects.equals(this.eventTrigger, that.eventTrigger) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.templateCode, this.templateName, this.channel, this.templateBody, this.eventTrigger, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "NotificationTemplateResponse{id=" + id +
               ", branchId=" + branchId +
               ", templateCode=" + templateCode +
               ", templateName=" + templateName +
               ", channel=" + channel +
               ", templateBody=" + templateBody +
               ", eventTrigger=" + eventTrigger +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static NotificationTemplateResponseBuilder builder() {
        return new NotificationTemplateResponseBuilder();
    }

    public static class NotificationTemplateResponseBuilder {
        private UUID id;
        private UUID branchId;
        private String templateCode;
        private String templateName;
        private String channel;
        private String templateBody;
        private String eventTrigger;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        NotificationTemplateResponseBuilder() {
        }

        public NotificationTemplateResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public NotificationTemplateResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public NotificationTemplateResponseBuilder templateCode(String templateCode) {
            this.templateCode = templateCode;
            return this;
        }

        public NotificationTemplateResponseBuilder templateName(String templateName) {
            this.templateName = templateName;
            return this;
        }

        public NotificationTemplateResponseBuilder channel(String channel) {
            this.channel = channel;
            return this;
        }

        public NotificationTemplateResponseBuilder templateBody(String templateBody) {
            this.templateBody = templateBody;
            return this;
        }

        public NotificationTemplateResponseBuilder eventTrigger(String eventTrigger) {
            this.eventTrigger = eventTrigger;
            return this;
        }

        public NotificationTemplateResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public NotificationTemplateResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public NotificationTemplateResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public NotificationTemplateResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public NotificationTemplateResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public NotificationTemplateResponse build() {
            return new NotificationTemplateResponse(this.id, this.branchId, this.templateCode, this.templateName, this.channel, this.templateBody, this.eventTrigger, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
