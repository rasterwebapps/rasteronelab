-- Test panel (group of tests)
CREATE TABLE test_panel (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    department_id UUID REFERENCES department(id),
    name VARCHAR(200) NOT NULL,
    code VARCHAR(30) NOT NULL,
    short_name VARCHAR(50),
    description VARCHAR(500),
    panel_price DECIMAL(10,2),
    display_order INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uq_panel_branch_code UNIQUE(branch_id, code)
);

CREATE INDEX idx_panel_branch_id ON test_panel(branch_id);
CREATE INDEX idx_panel_code ON test_panel(code);
CREATE INDEX idx_panel_is_active ON test_panel(is_active);
CREATE INDEX idx_panel_is_deleted ON test_panel(is_deleted);

-- Panel-Test mapping
CREATE TABLE panel_test (
    id UUID PRIMARY KEY,
    panel_id UUID NOT NULL REFERENCES test_panel(id),
    test_id UUID NOT NULL REFERENCES test_master(id),
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uq_panel_test UNIQUE(panel_id, test_id)
);

CREATE INDEX idx_panel_test_panel_id ON panel_test(panel_id);
CREATE INDEX idx_panel_test_test_id ON panel_test(test_id);

-- Nested panel support (panel containing other panels)
CREATE TABLE panel_panel (
    id UUID PRIMARY KEY,
    parent_panel_id UUID NOT NULL REFERENCES test_panel(id),
    child_panel_id UUID NOT NULL REFERENCES test_panel(id),
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uq_panel_panel UNIQUE(parent_panel_id, child_panel_id),
    CONSTRAINT chk_no_self_ref CHECK (parent_panel_id != child_panel_id)
);

CREATE INDEX idx_panel_panel_parent ON panel_panel(parent_panel_id);
CREATE INDEX idx_panel_panel_child ON panel_panel(child_panel_id);
