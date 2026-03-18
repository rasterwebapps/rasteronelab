-- Number series configuration per branch
CREATE TABLE number_series (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    entity_type VARCHAR(30) NOT NULL, -- UHID, ORDER, SAMPLE, INVOICE, RECEIPT
    prefix VARCHAR(20) NOT NULL,
    suffix VARCHAR(20),
    current_number BIGINT NOT NULL DEFAULT 0,
    padding_length INTEGER NOT NULL DEFAULT 6,
    format_pattern VARCHAR(100), -- e.g., {PREFIX}{YYYY}{SEQ}
    reset_frequency VARCHAR(20) DEFAULT 'YEARLY', -- YEARLY, MONTHLY, NEVER
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uq_number_series_branch_entity UNIQUE(branch_id, entity_type)
);

CREATE INDEX idx_number_series_branch_id ON number_series(branch_id);

-- TAT configuration
CREATE TABLE tat_configuration (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    test_id UUID REFERENCES test_master(id),
    department_id UUID REFERENCES department(id),
    routine_hours INTEGER NOT NULL,
    stat_hours INTEGER,
    critical_hours INTEGER,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_tat_config_branch_id ON tat_configuration(branch_id);
CREATE INDEX idx_tat_config_test_id ON tat_configuration(test_id);
CREATE INDEX idx_tat_config_department_id ON tat_configuration(department_id);

-- Working hours per branch
CREATE TABLE working_hours (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    day_of_week INTEGER NOT NULL, -- 1=Monday, 7=Sunday
    open_time TIME NOT NULL,
    close_time TIME NOT NULL,
    is_working_day BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uq_working_hours_branch_day UNIQUE(branch_id, day_of_week)
);

CREATE INDEX idx_working_hours_branch_id ON working_hours(branch_id);

-- Holiday calendar
CREATE TABLE holiday (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    holiday_date DATE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    is_half_day BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uq_holiday_branch_date UNIQUE(branch_id, holiday_date)
);

CREATE INDEX idx_holiday_branch_id ON holiday(branch_id);
CREATE INDEX idx_holiday_date ON holiday(holiday_date);
