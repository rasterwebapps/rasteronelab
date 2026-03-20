package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a NotificationTemplate.
 */
public class NotificationTemplateRequest {

    @NotBlank(message = "Template code is required")
    @Size(max = 50, message = "Template code must not exceed 50 characters")
    private String templateCode;

    @NotBlank(message = "Template name is required")
    @Size(max = 200, message = "Template name must not exceed 200 characters")
    private String templateName;

    @NotBlank(message = "Channel is required")
    @Size(max = 20, message = "Channel must not exceed 20 characters")
    private String channel;

    @NotBlank(message = "Template body is required")
    private String templateBody;

    @Size(max = 100, message = "Event trigger must not exceed 100 characters")
    private String eventTrigger;

    private Boolean isActive;

    public NotificationTemplateRequest() {
    }

    public NotificationTemplateRequest(String templateCode, String templateName, String channel, String templateBody, String eventTrigger, Boolean isActive) {
        this.templateCode = templateCode;
        this.templateName = templateName;
        this.channel = channel;
        this.templateBody = templateBody;
        this.eventTrigger = eventTrigger;
        this.isActive = isActive;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTemplateRequest that = (NotificationTemplateRequest) o;
        return java.util.Objects.equals(this.templateCode, that.templateCode) &&
               java.util.Objects.equals(this.templateName, that.templateName) &&
               java.util.Objects.equals(this.channel, that.channel) &&
               java.util.Objects.equals(this.templateBody, that.templateBody) &&
               java.util.Objects.equals(this.eventTrigger, that.eventTrigger) &&
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.templateCode, this.templateName, this.channel, this.templateBody, this.eventTrigger, this.isActive);
    }

    @Override
    public String toString() {
        return "NotificationTemplateRequest{templateCode=" + templateCode +
               ", templateName=" + templateName +
               ", channel=" + channel +
               ", templateBody=" + templateBody +
               ", eventTrigger=" + eventTrigger +
               ", isActive=" + isActive +
               "}";
    }

    public static NotificationTemplateRequestBuilder builder() {
        return new NotificationTemplateRequestBuilder();
    }

    public static class NotificationTemplateRequestBuilder {
        private String templateCode;
        private String templateName;
        private String channel;
        private String templateBody;
        private String eventTrigger;
        private Boolean isActive;

        NotificationTemplateRequestBuilder() {
        }

        public NotificationTemplateRequestBuilder templateCode(String templateCode) {
            this.templateCode = templateCode;
            return this;
        }

        public NotificationTemplateRequestBuilder templateName(String templateName) {
            this.templateName = templateName;
            return this;
        }

        public NotificationTemplateRequestBuilder channel(String channel) {
            this.channel = channel;
            return this;
        }

        public NotificationTemplateRequestBuilder templateBody(String templateBody) {
            this.templateBody = templateBody;
            return this;
        }

        public NotificationTemplateRequestBuilder eventTrigger(String eventTrigger) {
            this.eventTrigger = eventTrigger;
            return this;
        }

        public NotificationTemplateRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public NotificationTemplateRequest build() {
            return new NotificationTemplateRequest(this.templateCode, this.templateName, this.channel, this.templateBody, this.eventTrigger, this.isActive);
        }
    }

}
