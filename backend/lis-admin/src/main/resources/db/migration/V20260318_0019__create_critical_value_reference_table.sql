-- Critical Value Reference Table
-- Stores reference critical value ranges per parameter code (global reference, not branch-specific)

CREATE TABLE critical_value_reference (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    parameter_code VARCHAR(30) NOT NULL,
    gender VARCHAR(10) NOT NULL DEFAULT 'ALL',
    age_min DECIMAL(5,2),
    age_max DECIMAL(5,2),
    critical_low DECIMAL(15,4),
    critical_high DECIMAL(15,4),
    unit VARCHAR(50),
    notification_priority VARCHAR(20) NOT NULL DEFAULT 'URGENT',
    requires_verbal_confirmation BOOLEAN NOT NULL DEFAULT true,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);
CREATE INDEX idx_critical_value_ref_branch_id ON critical_value_reference(branch_id);
CREATE INDEX idx_critical_value_ref_parameter_code ON critical_value_reference(parameter_code);
CREATE INDEX idx_critical_value_ref_gender ON critical_value_reference(gender);
CREATE INDEX idx_critical_value_ref_is_active ON critical_value_reference(is_active);
CREATE INDEX idx_critical_value_ref_is_deleted ON critical_value_reference(is_deleted);
