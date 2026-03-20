-- Sample Type Master Table
-- Stores tube/container types used for specimen collection

CREATE TABLE sample_type (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    code VARCHAR(30) NOT NULL,
    name VARCHAR(100) NOT NULL,
    tube_color VARCHAR(50),
    container VARCHAR(100),
    additive VARCHAR(200),
    min_volume_ml DECIMAL(6,2) DEFAULT 0,
    stability_room_temp_hours INTEGER,
    stability_refrigerated_hours INTEGER,
    stability_frozen_days INTEGER,
    special_instructions TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    deleted_at TIMESTAMP
);
CREATE INDEX idx_sample_type_branch_id ON sample_type(branch_id);
CREATE INDEX idx_sample_type_code ON sample_type(code);
CREATE INDEX idx_sample_type_is_active ON sample_type(is_active);
CREATE INDEX idx_sample_type_is_deleted ON sample_type(is_deleted);
