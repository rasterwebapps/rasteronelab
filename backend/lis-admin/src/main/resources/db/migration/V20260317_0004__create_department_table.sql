-- Department master (L2 - Organization level)
CREATE TABLE department (
    id UUID PRIMARY KEY,
    org_id UUID NOT NULL REFERENCES organization(id),
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) NOT NULL,
    description VARCHAR(500),
    display_order INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uq_department_org_code UNIQUE(org_id, code)
);

CREATE INDEX idx_department_org_id ON department(org_id);
CREATE INDEX idx_department_code ON department(code);
CREATE INDEX idx_department_is_active ON department(is_active);
CREATE INDEX idx_department_is_deleted ON department(is_deleted);

-- Branch-Department mapping
CREATE TABLE branch_department (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL REFERENCES branch(id),
    department_id UUID NOT NULL REFERENCES department(id),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uq_branch_department UNIQUE(branch_id, department_id)
);

CREATE INDEX idx_branch_dept_branch_id ON branch_department(branch_id);
CREATE INDEX idx_branch_dept_department_id ON branch_department(department_id);
