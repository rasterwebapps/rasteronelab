package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * NotificationTemplate entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Represents a notification template within a branch.
 */
@Entity
@Table(name = "notification_template")
public class NotificationTemplate extends BaseEntity {

    @Column(name = "template_code", nullable = false, length = 50)
    private String templateCode;

    @Column(name = "template_name", nullable = false, length = 200)
    private String templateName;

    @Column(name = "channel", nullable = false, length = 20)
    private String channel;

    @Column(name = "template_body", nullable = false, columnDefinition = "TEXT")
    private String templateBody;

    @Column(name = "event_trigger", length = 100)
    private String eventTrigger;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

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

}
