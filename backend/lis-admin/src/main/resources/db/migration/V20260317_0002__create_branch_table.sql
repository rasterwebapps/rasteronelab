-- Branch table (L2 - multi-branch support, the key tenant isolation entity)
CREATE TABLE branch (
    id UUID PRIMARY KEY,
    org_id UUID NOT NULL REFERENCES organization(id),
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) NOT NULL,
    display_name VARCHAR(255),
    branch_type VARCHAR(50) NOT NULL DEFAULT 'LAB',
    address_line_1 VARCHAR(255),
    address_line_2 VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100) DEFAULT 'India',
    pincode VARCHAR(20),
    phone VARCHAR(20),
    email VARCHAR(255),
    nabl_number VARCHAR(100),
    license_number VARCHAR(100),
    header_text TEXT,
    footer_text TEXT,
    logo_url VARCHAR(500),
    report_header_url VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uq_branch_org_code UNIQUE(org_id, code)
);

CREATE INDEX idx_branch_org_id ON branch(org_id);
CREATE INDEX idx_branch_code ON branch(code);
CREATE INDEX idx_branch_is_active ON branch(is_active);
CREATE INDEX idx_branch_is_deleted ON branch(is_deleted);
