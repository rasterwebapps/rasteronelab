-- Report Template Table
-- Stores configurable report templates per department

CREATE TABLE report_template (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    department_code VARCHAR(20),
    template_name VARCHAR(200) NOT NULL,
    template_type VARCHAR(50) NOT NULL DEFAULT 'STANDARD',
    header_config JSONB,
    footer_config JSONB,
    body_config JSONB,
    paper_size VARCHAR(10) NOT NULL DEFAULT 'A4',
    orientation VARCHAR(20) NOT NULL DEFAULT 'PORTRAIT',
    is_default BOOLEAN NOT NULL DEFAULT false,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);
CREATE INDEX idx_report_template_branch_id ON report_template(branch_id);
CREATE INDEX idx_report_template_department_code ON report_template(department_code);
CREATE INDEX idx_report_template_is_default ON report_template(is_default);
CREATE INDEX idx_report_template_is_active ON report_template(is_active);
CREATE INDEX idx_report_template_is_deleted ON report_template(is_deleted);
