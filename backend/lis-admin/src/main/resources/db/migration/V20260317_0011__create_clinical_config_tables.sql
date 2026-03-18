-- Critical value configuration per parameter
CREATE TABLE critical_value_config (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    parameter_id UUID NOT NULL REFERENCES parameter(id),
    low_threshold DECIMAL(15,4),
    high_threshold DECIMAL(15,4),
    notification_required BOOLEAN NOT NULL DEFAULT true,
    auto_flag BOOLEAN NOT NULL DEFAULT true,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_critical_config_branch_id ON critical_value_config(branch_id);
CREATE INDEX idx_critical_config_parameter_id ON critical_value_config(parameter_id);

-- Delta check configuration per parameter
CREATE TABLE delta_check_config (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    parameter_id UUID NOT NULL REFERENCES parameter(id),
    percentage_threshold DECIMAL(5,2) NOT NULL,
    absolute_threshold DECIMAL(15,4),
    time_window_hours INTEGER NOT NULL DEFAULT 72,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_delta_check_branch_id ON delta_check_config(branch_id);
CREATE INDEX idx_delta_check_parameter_id ON delta_check_config(parameter_id);

-- Auto-validation rules
CREATE TABLE auto_validation_rule (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    parameter_id UUID REFERENCES parameter(id),
    test_id UUID REFERENCES test_master(id),
    rule_name VARCHAR(100) NOT NULL,
    rule_type VARCHAR(30) NOT NULL, -- RANGE_CHECK, DELTA_CHECK, CRITICAL_CHECK, INSTRUMENT_FLAG
    condition_expression VARCHAR(500),
    action_on_pass VARCHAR(30) NOT NULL DEFAULT 'AUTO_VALIDATE', -- AUTO_VALIDATE, FLAG_FOR_REVIEW
    action_on_fail VARCHAR(30) NOT NULL DEFAULT 'FLAG_FOR_REVIEW',
    priority INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_auto_validation_branch_id ON auto_validation_rule(branch_id);
CREATE INDEX idx_auto_validation_parameter_id ON auto_validation_rule(parameter_id);
CREATE INDEX idx_auto_validation_test_id ON auto_validation_rule(test_id);
