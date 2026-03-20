CREATE TABLE report_template (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    template_name VARCHAR(200) NOT NULL,
    department_id UUID,
    description VARCHAR(500),
    header_config TEXT,
    footer_config TEXT,
    font_family VARCHAR(100),
    layout_type VARCHAR(20) NOT NULL DEFAULT 'PORTRAIT',
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_report_template_branch_id ON report_template(branch_id);
CREATE INDEX idx_report_template_template_name ON report_template(template_name);
CREATE INDEX idx_report_template_department_id ON report_template(department_id);
CREATE INDEX idx_report_template_layout_type ON report_template(layout_type);
CREATE INDEX idx_report_template_is_active ON report_template(is_active);
CREATE INDEX idx_report_template_is_deleted ON report_template(is_deleted);
