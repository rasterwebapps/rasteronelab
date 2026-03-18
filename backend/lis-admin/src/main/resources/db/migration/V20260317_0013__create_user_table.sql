-- Local user entity for extended attributes (synced with Keycloak)
CREATE TABLE app_user (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    keycloak_user_id VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(200),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    employee_id VARCHAR(50),
    department VARCHAR(100),
    designation VARCHAR(100),
    phone VARCHAR(20),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uq_user_keycloak_id UNIQUE(keycloak_user_id)
);

CREATE INDEX idx_user_branch_id ON app_user(branch_id);
CREATE INDEX idx_user_keycloak_id ON app_user(keycloak_user_id);
CREATE INDEX idx_user_username ON app_user(username);
CREATE INDEX idx_user_employee_id ON app_user(employee_id);
CREATE INDEX idx_user_is_active ON app_user(is_active);
CREATE INDEX idx_user_is_deleted ON app_user(is_deleted);
