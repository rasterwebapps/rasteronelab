CREATE TABLE IF NOT EXISTS test_order (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    order_number VARCHAR(50) NOT NULL,
    patient_id UUID NOT NULL,
    visit_id UUID,
    referring_doctor_id UUID,
    priority VARCHAR(20) NOT NULL DEFAULT 'ROUTINE',
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    clinical_history TEXT,
    special_instructions TEXT,
    barcode VARCHAR(50),
    estimated_completion_time TIMESTAMP,
    completed_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    cancel_reason TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uk_order_number_branch UNIQUE (order_number, branch_id)
);

CREATE INDEX idx_test_order_branch ON test_order(branch_id);
CREATE INDEX idx_test_order_patient ON test_order(patient_id);
CREATE INDEX idx_test_order_visit ON test_order(visit_id);
CREATE INDEX idx_test_order_status ON test_order(status);
CREATE INDEX idx_test_order_date ON test_order(order_date);
CREATE INDEX idx_test_order_barcode ON test_order(barcode);
CREATE INDEX idx_test_order_not_deleted ON test_order(branch_id) WHERE is_deleted = false;
