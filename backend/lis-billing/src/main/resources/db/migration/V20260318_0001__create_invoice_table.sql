CREATE TABLE IF NOT EXISTS invoice (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    invoice_number VARCHAR(50) NOT NULL,
    order_id UUID NOT NULL,
    patient_id UUID NOT NULL,
    rate_list_type VARCHAR(30) NOT NULL DEFAULT 'WALK_IN',
    subtotal NUMERIC(12,2) NOT NULL DEFAULT 0,
    discount_amount NUMERIC(12,2) NOT NULL DEFAULT 0,
    discount_type VARCHAR(20),
    discount_reason VARCHAR(200),
    tax_amount NUMERIC(12,2) NOT NULL DEFAULT 0,
    total_amount NUMERIC(12,2) NOT NULL DEFAULT 0,
    paid_amount NUMERIC(12,2) NOT NULL DEFAULT 0,
    balance_amount NUMERIC(12,2) NOT NULL DEFAULT 0,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    invoice_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP,
    notes TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uk_invoice_number_branch UNIQUE (invoice_number, branch_id)
);

CREATE INDEX idx_invoice_branch ON invoice(branch_id);
CREATE INDEX idx_invoice_order ON invoice(order_id);
CREATE INDEX idx_invoice_patient ON invoice(patient_id);
CREATE INDEX idx_invoice_status ON invoice(status);
CREATE INDEX idx_invoice_date ON invoice(invoice_date);
CREATE INDEX idx_invoice_not_deleted ON invoice(branch_id) WHERE is_deleted = false;
