-- =====================================================================
-- V20260319_0001__create_lab_report_table.sql
-- Lab Report table for report generation, signing, and delivery.
-- Part of lis-report module (Phase 7 — Report Generation).
-- =====================================================================

CREATE TABLE IF NOT EXISTS lab_report (
    id                UUID PRIMARY KEY,
    branch_id         UUID         NOT NULL,
    order_id          UUID         NOT NULL,
    patient_id        UUID         NOT NULL,
    patient_name      VARCHAR(200),
    report_number     VARCHAR(50)  NOT NULL,
    report_type       VARCHAR(30)  NOT NULL DEFAULT 'INDIVIDUAL',
    report_status     VARCHAR(30)  NOT NULL DEFAULT 'DRAFT',
    department_id     UUID,
    department_name   VARCHAR(100),
    generated_by      VARCHAR(100),
    generated_at      TIMESTAMP,
    signed_by         VARCHAR(100),
    signed_at         TIMESTAMP,
    delivered_at      TIMESTAMP,
    delivery_channel  VARCHAR(30),
    file_url          VARCHAR(500),
    file_name         VARCHAR(255),
    file_size         BIGINT,
    page_count        INTEGER,
    template_id       UUID,
    version           INTEGER      NOT NULL DEFAULT 1,
    amendment_reason  TEXT,
    notes             TEXT,
    created_at        TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP    NOT NULL DEFAULT NOW(),
    created_by        VARCHAR(100),
    updated_by        VARCHAR(100),
    is_deleted        BOOLEAN      NOT NULL DEFAULT FALSE,
    deleted_at        TIMESTAMP,
    UNIQUE(branch_id, report_number)
);

CREATE INDEX idx_lab_report_branch  ON lab_report(branch_id);
CREATE INDEX idx_lab_report_order   ON lab_report(branch_id, order_id);
CREATE INDEX idx_lab_report_patient ON lab_report(branch_id, patient_id);
CREATE INDEX idx_lab_report_status  ON lab_report(branch_id, report_status);
