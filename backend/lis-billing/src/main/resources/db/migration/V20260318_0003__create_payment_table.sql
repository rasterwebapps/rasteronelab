CREATE TABLE IF NOT EXISTS payment (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    invoice_id UUID NOT NULL REFERENCES invoice(id),
    receipt_number VARCHAR(50) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    transaction_ref VARCHAR(100),
    payment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    received_by VARCHAR(100),
    notes TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uk_receipt_number_branch UNIQUE (receipt_number, branch_id),
    CONSTRAINT chk_payment_amount_positive CHECK (amount > 0)
);

CREATE INDEX idx_payment_branch ON payment(branch_id);
CREATE INDEX idx_payment_invoice ON payment(invoice_id);
CREATE INDEX idx_payment_date ON payment(payment_date);
CREATE INDEX idx_payment_method ON payment(payment_method);
CREATE INDEX idx_payment_not_deleted ON payment(branch_id) WHERE is_deleted = false;
