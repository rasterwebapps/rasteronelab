CREATE TABLE insurance_tariff (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    insurance_name VARCHAR(200) NOT NULL,
    plan_name VARCHAR(200) NOT NULL,
    test_id UUID,
    tariff_rate DECIMAL(12,2) NOT NULL,
    effective_from DATE,
    effective_to DATE,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_insurance_tariff_branch_id ON insurance_tariff(branch_id);
CREATE INDEX idx_insurance_tariff_insurance_name ON insurance_tariff(insurance_name);
CREATE INDEX idx_insurance_tariff_plan_name ON insurance_tariff(plan_name);
CREATE INDEX idx_insurance_tariff_test_id ON insurance_tariff(test_id);
CREATE INDEX idx_insurance_tariff_is_active ON insurance_tariff(is_active);
CREATE INDEX idx_insurance_tariff_is_deleted ON insurance_tariff(is_deleted);
