-- Rate list type: WALK_IN, CORPORATE, INSURANCE, DOCTOR_REF
-- Price catalog
CREATE TABLE price_catalog (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    test_id UUID REFERENCES test_master(id),
    panel_id UUID REFERENCES test_panel(id),
    rate_list_type VARCHAR(30) NOT NULL, -- WALK_IN, CORPORATE, INSURANCE, DOCTOR_REF
    price DECIMAL(10,2) NOT NULL,
    effective_from DATE,
    effective_to DATE,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT chk_test_or_panel CHECK (
        (test_id IS NOT NULL AND panel_id IS NULL) OR
        (test_id IS NULL AND panel_id IS NOT NULL)
    )
);

CREATE INDEX idx_price_catalog_branch_id ON price_catalog(branch_id);
CREATE INDEX idx_price_catalog_test_id ON price_catalog(test_id);
CREATE INDEX idx_price_catalog_panel_id ON price_catalog(panel_id);
CREATE INDEX idx_price_catalog_rate_list_type ON price_catalog(rate_list_type);
CREATE INDEX idx_price_catalog_is_active ON price_catalog(is_active);
CREATE INDEX idx_price_catalog_is_deleted ON price_catalog(is_deleted);
