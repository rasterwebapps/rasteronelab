-- Reference ranges for parameters
CREATE TABLE reference_range (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    parameter_id UUID NOT NULL REFERENCES parameter(id),
    gender VARCHAR(20), -- MALE, FEMALE, OTHER, null for all genders
    age_min DECIMAL(5,2),
    age_max DECIMAL(5,2),
    age_unit VARCHAR(10) DEFAULT 'YEARS', -- YEARS, MONTHS, DAYS
    normal_min DECIMAL(15,4),
    normal_max DECIMAL(15,4),
    critical_low DECIMAL(15,4),
    critical_high DECIMAL(15,4),
    normal_text VARCHAR(500), -- for qualitative results
    unit VARCHAR(50),
    is_pregnancy BOOLEAN NOT NULL DEFAULT false,
    display_text VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_ref_range_branch_id ON reference_range(branch_id);
CREATE INDEX idx_ref_range_parameter_id ON reference_range(parameter_id);
CREATE INDEX idx_ref_range_gender ON reference_range(gender);
CREATE INDEX idx_ref_range_is_deleted ON reference_range(is_deleted);
