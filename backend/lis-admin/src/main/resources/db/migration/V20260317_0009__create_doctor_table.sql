-- Doctor (referring physician)
CREATE TABLE doctor (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    name VARCHAR(200) NOT NULL,
    code VARCHAR(30),
    specialization VARCHAR(100),
    qualification VARCHAR(200),
    registration_number VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(100),
    address VARCHAR(500),
    referral_commission_percent DECIMAL(5,2) DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_doctor_branch_id ON doctor(branch_id);
CREATE INDEX idx_doctor_name ON doctor(name);
CREATE INDEX idx_doctor_code ON doctor(code);
CREATE INDEX idx_doctor_specialization ON doctor(specialization);
CREATE INDEX idx_doctor_registration_number ON doctor(registration_number);
CREATE INDEX idx_doctor_is_active ON doctor(is_active);
CREATE INDEX idx_doctor_is_deleted ON doctor(is_deleted);
