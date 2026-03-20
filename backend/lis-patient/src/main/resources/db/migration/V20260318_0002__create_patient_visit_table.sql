CREATE TABLE IF NOT EXISTS patient_visit (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    patient_id UUID NOT NULL REFERENCES patient(id),
    visit_number VARCHAR(30) NOT NULL,
    visit_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    visit_type VARCHAR(30) NOT NULL DEFAULT 'WALK_IN',
    referring_doctor_id UUID,
    clinical_notes TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uk_visit_number_branch UNIQUE (visit_number, branch_id)
);

CREATE INDEX idx_patient_visit_branch ON patient_visit(branch_id);
CREATE INDEX idx_patient_visit_patient ON patient_visit(patient_id);
CREATE INDEX idx_patient_visit_date ON patient_visit(visit_date);
CREATE INDEX idx_patient_visit_not_deleted ON patient_visit(branch_id) WHERE is_deleted = false;
