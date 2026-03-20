-- Discount Scheme (discount configuration per branch)
CREATE TABLE discount_scheme (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    scheme_code VARCHAR(50) NOT NULL,
    scheme_name VARCHAR(200) NOT NULL,
    applicable_to VARCHAR(30) NOT NULL DEFAULT 'WALK_IN' CHECK (applicable_to IN ('WALK_IN', 'CORPORATE', 'INSURANCE')),
    discount_type VARCHAR(30) NOT NULL DEFAULT 'PERCENTAGE' CHECK (discount_type IN ('PERCENTAGE', 'FIXED_AMOUNT')),
    discount_value DECIMAL(12,2) NOT NULL,
    min_transaction_amount DECIMAL(12,2),
    start_date DATE,
    end_date DATE,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_discount_scheme_branch_id ON discount_scheme(branch_id);
CREATE INDEX idx_discount_scheme_code ON discount_scheme(scheme_code);
CREATE INDEX idx_discount_scheme_applicable_to ON discount_scheme(applicable_to);
CREATE INDEX idx_discount_scheme_is_active ON discount_scheme(is_active);
CREATE INDEX idx_discount_scheme_is_deleted ON discount_scheme(is_deleted);
