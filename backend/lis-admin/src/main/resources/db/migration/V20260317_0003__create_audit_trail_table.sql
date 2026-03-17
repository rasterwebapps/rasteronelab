-- Audit trail table for tracking all data changes
-- branch_id has no FK constraint: audit records must persist even if branch is deleted,
-- and SUPER_ADMIN operations may span branches.
CREATE TABLE audit_trail (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    entity_name VARCHAR(100) NOT NULL,
    entity_id UUID NOT NULL,
    action VARCHAR(20) NOT NULL, -- CREATE, UPDATE, DELETE, RESTORE
    changed_by VARCHAR(255) NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    old_value JSONB,
    new_value JSONB,
    ip_address VARCHAR(50),
    user_agent VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_audit_trail_branch_id ON audit_trail(branch_id);
CREATE INDEX idx_audit_trail_entity ON audit_trail(entity_name, entity_id);
CREATE INDEX idx_audit_trail_changed_at ON audit_trail(changed_at);
CREATE INDEX idx_audit_trail_changed_by ON audit_trail(changed_by);
