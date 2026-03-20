CREATE TABLE IF NOT EXISTS order_line_item (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    order_id UUID NOT NULL REFERENCES test_order(id),
    test_id UUID NOT NULL,
    test_code VARCHAR(30) NOT NULL,
    test_name VARCHAR(200) NOT NULL,
    department_id UUID,
    sample_type VARCHAR(50),
    tube_type VARCHAR(50),
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    unit_price NUMERIC(12,2) NOT NULL DEFAULT 0,
    discount_amount NUMERIC(12,2) NOT NULL DEFAULT 0,
    net_price NUMERIC(12,2) NOT NULL DEFAULT 0,
    is_urgent BOOLEAN NOT NULL DEFAULT false,
    is_outsourced BOOLEAN NOT NULL DEFAULT false,
    outsource_lab VARCHAR(200),
    remarks TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_order_line_item_branch ON order_line_item(branch_id);
CREATE INDEX idx_order_line_item_order ON order_line_item(order_id);
CREATE INDEX idx_order_line_item_test ON order_line_item(test_id);
CREATE INDEX idx_order_line_item_status ON order_line_item(status);
CREATE INDEX idx_order_line_item_not_deleted ON order_line_item(branch_id) WHERE is_deleted = false;
