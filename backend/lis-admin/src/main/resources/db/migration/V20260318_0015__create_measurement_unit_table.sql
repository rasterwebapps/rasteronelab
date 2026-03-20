-- Measurement Unit Master Table
-- Stores standard measurement units used in lab reports

CREATE TABLE measurement_unit (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    code VARCHAR(30) NOT NULL,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);
CREATE INDEX idx_measurement_unit_branch_id ON measurement_unit(branch_id);
CREATE INDEX idx_measurement_unit_code ON measurement_unit(code);
CREATE INDEX idx_measurement_unit_category ON measurement_unit(category);
CREATE INDEX idx_measurement_unit_is_active ON measurement_unit(is_active);
CREATE INDEX idx_measurement_unit_is_deleted ON measurement_unit(is_deleted);
