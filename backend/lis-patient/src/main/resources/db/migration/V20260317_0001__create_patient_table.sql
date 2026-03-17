-- Patient demographics table
CREATE TABLE patient (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    patient_id_number VARCHAR(50) NOT NULL,
    title VARCHAR(10),
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    age_years INTEGER,
    age_months INTEGER,
    age_days INTEGER,
    gender VARCHAR(20) NOT NULL,
    phone VARCHAR(20),
    alternate_phone VARCHAR(20),
    email VARCHAR(255),
    address_line_1 VARCHAR(255),
    address_line_2 VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100) DEFAULT 'India',
    pincode VARCHAR(20),
    nationality VARCHAR(100) DEFAULT 'Indian',
    blood_group VARCHAR(10),
    referred_by_doctor_id UUID, -- FK to doctor table (created in Phase 3)
    org_id UUID,               -- FK to organization table (cross-module, no FK constraint)
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE UNIQUE INDEX idx_patient_branch_id_number ON patient(branch_id, patient_id_number);
CREATE INDEX idx_patient_branch_id ON patient(branch_id);
CREATE INDEX idx_patient_name ON patient(branch_id, first_name, last_name);
CREATE INDEX idx_patient_phone ON patient(branch_id, phone);
CREATE INDEX idx_patient_branch_is_deleted ON patient(branch_id, is_deleted);
