-- Phase 4: LIS-067 — Sample Management Tables
-- Migration: Create sample table

CREATE TABLE sample (
    id                    UUID         NOT NULL DEFAULT gen_random_uuid(),
    branch_id             UUID         NOT NULL,

    -- Core collection fields
    order_id              UUID         NOT NULL,
    patient_id            UUID         NOT NULL,
    sample_barcode        VARCHAR(50)  NOT NULL,
    tube_type             VARCHAR(20)  NOT NULL,
    sample_type           VARCHAR(100),
    status                VARCHAR(30)  NOT NULL DEFAULT 'COLLECTED',

    -- Collection details
    collected_by          UUID         NOT NULL,
    collected_at          TIMESTAMP    NOT NULL,
    collection_site       VARCHAR(30),
    quantity              NUMERIC(8,2),
    unit                  VARCHAR(20),
    home_collection       BOOLEAN      NOT NULL DEFAULT FALSE,
    notes                 TEXT,

    -- Receiving details
    received_by           UUID,
    received_at           TIMESTAMP,
    tat_started_at        TIMESTAMP,
    temperature           NUMERIC(5,2),

    -- Rejection details
    rejection_reason      VARCHAR(30),
    rejection_comment     TEXT,
    recollection_required BOOLEAN      DEFAULT FALSE,
    rejected_by           UUID,
    rejected_at           TIMESTAMP,

    -- Aliquot fields
    parent_sample_id      UUID,
    aliquot_label         VARCHAR(10),
    aliquot_sequence      INT,

    -- Storage fields
    storage_rack          VARCHAR(50),
    storage_shelf         VARCHAR(50),
    storage_position      VARCHAR(50),
    stored_at             TIMESTAMP,
    disposal_date         TIMESTAMP,

    department_id         UUID,

    -- Audit columns (BaseEntity)
    created_at            TIMESTAMP    NOT NULL,
    updated_at            TIMESTAMP    NOT NULL,
    created_by            VARCHAR(255),
    updated_by            VARCHAR(255),
    is_deleted            BOOLEAN      NOT NULL DEFAULT FALSE,
    deleted_at            TIMESTAMP,

    CONSTRAINT pk_sample PRIMARY KEY (id)
);

-- Indexes for common query patterns
CREATE UNIQUE INDEX uq_sample_barcode ON sample (sample_barcode) WHERE is_deleted = FALSE;
CREATE INDEX idx_sample_branch    ON sample (branch_id, is_deleted);
CREATE INDEX idx_sample_order     ON sample (order_id, branch_id, is_deleted);
CREATE INDEX idx_sample_patient   ON sample (patient_id, branch_id, is_deleted);
CREATE INDEX idx_sample_status    ON sample (status, branch_id, is_deleted);
CREATE INDEX idx_sample_parent    ON sample (parent_sample_id) WHERE parent_sample_id IS NOT NULL;
