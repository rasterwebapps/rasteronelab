-- Notification Template
CREATE TABLE notification_template (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    template_code VARCHAR(50) NOT NULL,
    template_name VARCHAR(200) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    template_body TEXT NOT NULL,
    event_trigger VARCHAR(100),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_notification_template_branch_id ON notification_template(branch_id);
CREATE INDEX idx_notification_template_code ON notification_template(template_code);
CREATE INDEX idx_notification_template_channel ON notification_template(channel);
CREATE INDEX idx_notification_template_is_active ON notification_template(is_active);
CREATE INDEX idx_notification_template_is_deleted ON notification_template(is_deleted);
CREATE INDEX idx_notification_template_branch_code ON notification_template(branch_id, template_code, is_deleted);
