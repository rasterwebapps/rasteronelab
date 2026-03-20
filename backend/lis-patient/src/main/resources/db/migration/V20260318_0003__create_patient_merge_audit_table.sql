CREATE TABLE IF NOT EXISTS patient_merge_audit (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    primary_patient_id UUID NOT NULL REFERENCES patient(id),
    merged_patient_id UUID NOT NULL REFERENCES patient(id),
    merged_by VARCHAR(100) NOT NULL,
    merged_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    merge_details JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_merge_audit_branch ON patient_merge_audit(branch_id);
CREATE INDEX idx_merge_audit_primary ON patient_merge_audit(primary_patient_id);
CREATE INDEX idx_merge_audit_merged ON patient_merge_audit(merged_patient_id);
CREATE INDEX idx_merge_audit_not_deleted ON patient_merge_audit(branch_id) WHERE is_deleted = false;
