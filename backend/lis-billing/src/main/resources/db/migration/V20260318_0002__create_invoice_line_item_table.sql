CREATE TABLE IF NOT EXISTS invoice_line_item (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    invoice_id UUID NOT NULL REFERENCES invoice(id),
    order_line_item_id UUID,
    test_id UUID NOT NULL,
    test_code VARCHAR(30) NOT NULL,
    test_name VARCHAR(200) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_price NUMERIC(12,2) NOT NULL DEFAULT 0,
    discount_amount NUMERIC(12,2) NOT NULL DEFAULT 0,
    net_amount NUMERIC(12,2) NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_inv_line_item_branch ON invoice_line_item(branch_id);
CREATE INDEX idx_inv_line_item_invoice ON invoice_line_item(invoice_id);
CREATE INDEX idx_inv_line_item_not_deleted ON invoice_line_item(branch_id) WHERE is_deleted = false;
