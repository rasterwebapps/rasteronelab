-- Phase 4: LIS-067 — Sample Management Tables
-- Migration: Create sample_transfer table (inter-branch transfers)

CREATE TABLE sample_transfer (
    id                    UUID         NOT NULL DEFAULT gen_random_uuid(),
    branch_id             UUID         NOT NULL,
    sample_id             UUID         NOT NULL,
    source_branch_id      UUID         NOT NULL,
    destination_branch_id UUID         NOT NULL,
    reason                TEXT,
    transferred_by        UUID         NOT NULL,
    transferred_at        TIMESTAMP    NOT NULL,
    status                VARCHAR(30)  NOT NULL DEFAULT 'INITIATED',
    received_at           TIMESTAMP,
    received_by           UUID,
    notes                 TEXT,

    -- Audit columns (BaseEntity)
    created_at            TIMESTAMP    NOT NULL,
    updated_at            TIMESTAMP    NOT NULL,
    created_by            VARCHAR(255),
    updated_by            VARCHAR(255),
    is_deleted            BOOLEAN      NOT NULL DEFAULT FALSE,
    deleted_at            TIMESTAMP,

    CONSTRAINT pk_sample_transfer PRIMARY KEY (id)
);

CREATE INDEX idx_sample_transfer_sample ON sample_transfer (sample_id);
CREATE INDEX idx_sample_transfer_source ON sample_transfer (source_branch_id, is_deleted);
CREATE INDEX idx_sample_transfer_dest   ON sample_transfer (destination_branch_id);
