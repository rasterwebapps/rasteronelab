-- Test master (L3 - Branch level)
CREATE TABLE test_master (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    department_id UUID NOT NULL REFERENCES department(id),
    name VARCHAR(200) NOT NULL,
    code VARCHAR(30) NOT NULL,
    short_name VARCHAR(50),
    description VARCHAR(500),
    sample_type VARCHAR(50),
    tube_type VARCHAR(50),
    report_section VARCHAR(100),
    method VARCHAR(100),
    tat_routine_hours INTEGER,
    tat_stat_hours INTEGER,
    base_price DECIMAL(10,2),
    is_outsourced BOOLEAN NOT NULL DEFAULT false,
    outsource_lab_name VARCHAR(200),
    display_order INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uq_test_branch_code UNIQUE(branch_id, code)
);

CREATE INDEX idx_test_branch_id ON test_master(branch_id);
CREATE INDEX idx_test_department_id ON test_master(department_id);
CREATE INDEX idx_test_code ON test_master(code);
CREATE INDEX idx_test_name ON test_master(name);
CREATE INDEX idx_test_is_active ON test_master(is_active);
CREATE INDEX idx_test_is_deleted ON test_master(is_deleted);

-- Parameter master (L3 - Branch level)
CREATE TABLE parameter (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    name VARCHAR(200) NOT NULL,
    code VARCHAR(30) NOT NULL,
    print_name VARCHAR(200),
    unit VARCHAR(50),
    data_type VARCHAR(30) NOT NULL, -- NUMERIC, TEXT, QUALITATIVE, SEMI_QUANTITATIVE, etc.
    decimal_places INTEGER DEFAULT 2,
    display_order INTEGER NOT NULL DEFAULT 0,
    method VARCHAR(100),
    loinc_code VARCHAR(20),
    formula VARCHAR(500),
    is_calculated BOOLEAN NOT NULL DEFAULT false,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uq_parameter_branch_code UNIQUE(branch_id, code)
);

CREATE INDEX idx_parameter_branch_id ON parameter(branch_id);
CREATE INDEX idx_parameter_code ON parameter(code);
CREATE INDEX idx_parameter_is_active ON parameter(is_active);
CREATE INDEX idx_parameter_is_deleted ON parameter(is_deleted);

-- Test-Parameter mapping
CREATE TABLE test_parameter (
    id UUID PRIMARY KEY,
    test_id UUID NOT NULL REFERENCES test_master(id),
    parameter_id UUID NOT NULL REFERENCES parameter(id),
    display_order INTEGER NOT NULL DEFAULT 0,
    is_mandatory BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uq_test_parameter UNIQUE(test_id, parameter_id)
);

CREATE INDEX idx_test_param_test_id ON test_parameter(test_id);
CREATE INDEX idx_test_param_parameter_id ON test_parameter(parameter_id);
