-- Sample Rejection Reason Master Table
-- Stores standard reasons for sample rejection

CREATE TABLE rejection_reason (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    code VARCHAR(50) NOT NULL,
    reason VARCHAR(200) NOT NULL,
    category VARCHAR(30) NOT NULL DEFAULT 'PRE_ANALYTICAL',
    severity VARCHAR(20) NOT NULL DEFAULT 'MAJOR',
    requires_recollection BOOLEAN NOT NULL DEFAULT true,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);
CREATE INDEX idx_rejection_reason_branch_id ON rejection_reason(branch_id);
CREATE INDEX idx_rejection_reason_code ON rejection_reason(code);
CREATE INDEX idx_rejection_reason_category ON rejection_reason(category);
CREATE INDEX idx_rejection_reason_is_active ON rejection_reason(is_active);
CREATE INDEX idx_rejection_reason_is_deleted ON rejection_reason(is_deleted);
