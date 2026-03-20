CREATE TABLE IF NOT EXISTS patient (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    uhid VARCHAR(30) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100),
    date_of_birth DATE,
    age_years INTEGER,
    age_months INTEGER,
    age_days INTEGER,
    gender VARCHAR(10) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(150),
    blood_group VARCHAR(10),
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100) DEFAULT 'India',
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    nationality VARCHAR(50) DEFAULT 'Indian',
    id_type VARCHAR(30),
    id_number VARCHAR(50),
    notes TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uk_patient_uhid_branch UNIQUE (uhid, branch_id)
);

CREATE INDEX idx_patient_branch_id ON patient(branch_id);
CREATE INDEX idx_patient_uhid ON patient(uhid);
CREATE INDEX idx_patient_phone ON patient(phone);
CREATE INDEX idx_patient_name ON patient(first_name, last_name);
CREATE INDEX idx_patient_not_deleted ON patient(branch_id) WHERE is_deleted = false;
