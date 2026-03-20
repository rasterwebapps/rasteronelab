-- Role and Permission Tables for RBAC
-- Stores system roles, permissions, and their mappings

CREATE TABLE role (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    is_system_role BOOLEAN NOT NULL DEFAULT false,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);
CREATE INDEX idx_role_branch_id ON role(branch_id);
CREATE INDEX idx_role_code ON role(code);
CREATE INDEX idx_role_is_active ON role(is_active);
CREATE INDEX idx_role_is_deleted ON role(is_deleted);
CREATE UNIQUE INDEX uq_role_branch_code ON role(branch_id, code);

CREATE TABLE permission (
    id UUID PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    module VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);
CREATE INDEX idx_permission_code ON permission(code);
CREATE INDEX idx_permission_module ON permission(module);
CREATE INDEX idx_permission_is_active ON permission(is_active);
CREATE INDEX idx_permission_is_deleted ON permission(is_deleted);

CREATE TABLE role_permission (
    id UUID PRIMARY KEY,
    role_id UUID NOT NULL REFERENCES role(id),
    permission_id UUID NOT NULL REFERENCES permission(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uq_role_permission UNIQUE(role_id, permission_id)
);
CREATE INDEX idx_role_permission_role_id ON role_permission(role_id);
CREATE INDEX idx_role_permission_permission_id ON role_permission(permission_id);
