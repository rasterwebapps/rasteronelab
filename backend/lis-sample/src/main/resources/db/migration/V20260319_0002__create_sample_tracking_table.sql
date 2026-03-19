-- Phase 4: LIS-067 — Sample Management Tables
-- Migration: Create sample_tracking table (sample timeline audit trail)

CREATE TABLE sample_tracking (
    id           UUID         NOT NULL DEFAULT gen_random_uuid(),
    branch_id    UUID         NOT NULL,
    sample_id    UUID         NOT NULL,
    status       VARCHAR(30)  NOT NULL,
    event_time   TIMESTAMP    NOT NULL,
    performed_by UUID,
    comments     TEXT,

    -- Audit columns (BaseEntity)
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP    NOT NULL,
    created_by   VARCHAR(255),
    updated_by   VARCHAR(255),
    is_deleted   BOOLEAN      NOT NULL DEFAULT FALSE,
    deleted_at   TIMESTAMP,

    CONSTRAINT pk_sample_tracking PRIMARY KEY (id)
);

CREATE INDEX idx_sample_tracking_sample ON sample_tracking (sample_id, event_time ASC);
CREATE INDEX idx_sample_tracking_branch ON sample_tracking (branch_id, is_deleted);
