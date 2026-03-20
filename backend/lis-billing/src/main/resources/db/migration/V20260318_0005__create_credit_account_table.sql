CREATE TABLE IF NOT EXISTS credit_account (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    account_name VARCHAR(200) NOT NULL,
    account_type VARCHAR(30) NOT NULL,
    credit_limit NUMERIC(12,2) NOT NULL DEFAULT 0,
    current_balance NUMERIC(12,2) NOT NULL DEFAULT 0,
    contact_person VARCHAR(100),
    contact_phone VARCHAR(20),
    contact_email VARCHAR(150),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP,
    CONSTRAINT uk_credit_account_name_branch UNIQUE (account_name, branch_id),
    CONSTRAINT chk_credit_limit_non_negative CHECK (credit_limit >= 0)
);

CREATE INDEX idx_credit_account_branch ON credit_account(branch_id);
CREATE INDEX idx_credit_account_type ON credit_account(account_type);
CREATE INDEX idx_credit_account_not_deleted ON credit_account(branch_id) WHERE is_deleted = false;
