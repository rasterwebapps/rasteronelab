-- Test Result table: core entity for result entry
CREATE TABLE IF NOT EXISTS test_result (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    order_id UUID NOT NULL,
    order_line_item_id UUID NOT NULL,
    patient_id UUID NOT NULL,
    test_id UUID NOT NULL,
    test_code VARCHAR(50) NOT NULL,
    test_name VARCHAR(255) NOT NULL,
    department_id UUID,
    department_name VARCHAR(100),
    sample_id UUID,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    entered_by VARCHAR(100),
    entered_at TIMESTAMP,
    validated_by VARCHAR(100),
    validated_at TIMESTAMP,
    authorized_by VARCHAR(100),
    authorized_at TIMESTAMP,
    is_critical BOOLEAN NOT NULL DEFAULT FALSE,
    critical_acknowledged BOOLEAN NOT NULL DEFAULT FALSE,
    critical_acknowledged_by VARCHAR(100),
    critical_acknowledged_at TIMESTAMP,
    has_delta_check_failure BOOLEAN NOT NULL DEFAULT FALSE,
    is_amended BOOLEAN NOT NULL DEFAULT FALSE,
    amendment_reason VARCHAR(500),
    amended_by VARCHAR(100),
    amended_at TIMESTAMP,
    comments VARCHAR(1000),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_test_result_branch ON test_result(branch_id);
CREATE INDEX idx_test_result_order ON test_result(order_id, branch_id);
CREATE INDEX idx_test_result_patient ON test_result(patient_id, branch_id);
CREATE INDEX idx_test_result_status ON test_result(status, branch_id);
CREATE INDEX idx_test_result_department ON test_result(department_id, branch_id, status);

-- Result Value table: individual parameter values within a test result
CREATE TABLE IF NOT EXISTS result_value (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    test_result_id UUID NOT NULL REFERENCES test_result(id),
    parameter_id UUID NOT NULL,
    parameter_code VARCHAR(50) NOT NULL,
    parameter_name VARCHAR(255) NOT NULL,
    result_type VARCHAR(30) NOT NULL DEFAULT 'NUMERIC',
    numeric_value DECIMAL(15,4),
    text_value VARCHAR(2000),
    unit VARCHAR(50),
    reference_range_low DECIMAL(15,4),
    reference_range_high DECIMAL(15,4),
    reference_range_text VARCHAR(255),
    abnormal_flag VARCHAR(20) DEFAULT 'NORMAL',
    is_critical BOOLEAN NOT NULL DEFAULT FALSE,
    previous_value DECIMAL(15,4),
    delta_percentage DECIMAL(8,2),
    delta_check_status VARCHAR(20),
    sort_order INTEGER DEFAULT 0,
    is_calculated BOOLEAN NOT NULL DEFAULT FALSE,
    formula VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_result_value_test_result ON result_value(test_result_id, branch_id);
CREATE INDEX idx_result_value_parameter ON result_value(parameter_id, branch_id);

-- Result History table: audit trail for result changes
CREATE TABLE IF NOT EXISTS result_history (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    test_result_id UUID NOT NULL REFERENCES test_result(id),
    action VARCHAR(30) NOT NULL,
    previous_status VARCHAR(30),
    new_status VARCHAR(30),
    performed_by VARCHAR(100) NOT NULL,
    performed_at TIMESTAMP NOT NULL DEFAULT NOW(),
    details VARCHAR(2000),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_result_history_test_result ON result_history(test_result_id, branch_id);
