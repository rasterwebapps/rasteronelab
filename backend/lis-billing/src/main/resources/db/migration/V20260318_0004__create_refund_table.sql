CREATE TABLE IF NOT EXISTS refund (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    invoice_id UUID NOT NULL REFERENCES invoice(id),
    payment_id UUID REFERENCES payment(id),
    refund_number VARCHAR(50) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    reason VARCHAR(500) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'REQUESTED',
    approved_by VARCHAR(100),
    approved_at TIMESTAMP,
    refund_method VARCHAR(20),
    refund_date TIMESTAMP,
    notes TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uk_refund_number_branch UNIQUE (refund_number, branch_id),
    CONSTRAINT chk_refund_amount_positive CHECK (amount > 0)
);

CREATE INDEX idx_refund_branch ON refund(branch_id);
CREATE INDEX idx_refund_invoice ON refund(invoice_id);
CREATE INDEX idx_refund_status ON refund(status);
CREATE INDEX idx_refund_not_deleted ON refund(branch_id) WHERE is_deleted = false;
