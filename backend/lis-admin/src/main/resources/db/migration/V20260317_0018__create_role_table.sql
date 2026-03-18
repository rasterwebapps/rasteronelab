CREATE TABLE role (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    role_name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    permissions TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_role_branch_id ON role(branch_id);
CREATE INDEX idx_role_role_name ON role(role_name);
CREATE INDEX idx_role_is_active ON role(is_active);
CREATE INDEX idx_role_is_deleted ON role(is_deleted);
