# Database Schema — RasterOneLab LIS

## Overview

- **Total Tables**: ~143
- **Strategy**: Single schema, row-level branch_id filtering
- **Primary Keys**: UUID v7 (time-ordered for better index performance)
- **Soft Delete**: `is_deleted BOOLEAN` + `deleted_at TIMESTAMPTZ`
- **Auditing**: `created_at`, `updated_at`, `created_by`, `updated_by`

## Standard Column Pattern (ALL tables)

```sql
id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
branch_id    UUID NOT NULL,
-- domain columns here --
created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
created_by   VARCHAR(100),
updated_by   VARCHAR(100),
is_deleted   BOOLEAN NOT NULL DEFAULT FALSE,
deleted_at   TIMESTAMPTZ
```

## Core Tables DDL

### patient

```sql
CREATE TABLE patient (
    id              UUID PRIMARY KEY,
    branch_id       UUID NOT NULL,
    uhid            VARCHAR(50) NOT NULL,
    full_name       VARCHAR(200) NOT NULL,
    gender          VARCHAR(20),
    date_of_birth   DATE,
    phone_number    VARCHAR(20),
    email           VARCHAR(200),
    address         TEXT,
    blood_group     VARCHAR(10),
    allergies       TEXT,
    is_minor        BOOLEAN DEFAULT FALSE,
    guardian_name   VARCHAR(200),
    guardian_phone  VARCHAR(20),
    extra_info      JSONB,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    is_deleted      BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ
);

CREATE UNIQUE INDEX idx_patient_uhid_branch ON patient(uhid, branch_id) WHERE is_deleted = FALSE;
CREATE INDEX idx_patient_branch_id ON patient(branch_id);
CREATE INDEX idx_patient_phone ON patient(phone_number);
CREATE INDEX idx_patient_name ON patient USING gin(to_tsvector('english', full_name));
```

### patient_visit

```sql
CREATE TABLE patient_visit (
    id                  UUID PRIMARY KEY,
    branch_id           UUID NOT NULL,
    patient_id          UUID NOT NULL REFERENCES patient(id),
    visit_date          DATE NOT NULL,
    referring_doctor_id UUID,
    visit_type          VARCHAR(30),  -- WALK_IN, APPOINTMENT, HOME_COLLECTION
    notes               TEXT,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(100),
    updated_by          VARCHAR(100),
    is_deleted          BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at          TIMESTAMPTZ
);

CREATE INDEX idx_patient_visit_patient ON patient_visit(patient_id);
CREATE INDEX idx_patient_visit_branch ON patient_visit(branch_id, is_deleted);
```

### test_order

```sql
CREATE TABLE test_order (
    id                  UUID PRIMARY KEY,
    branch_id           UUID NOT NULL,
    patient_id          UUID NOT NULL REFERENCES patient(id),
    visit_id            UUID REFERENCES patient_visit(id),
    order_barcode       VARCHAR(50) UNIQUE NOT NULL,
    order_status        VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    priority            VARCHAR(20) NOT NULL DEFAULT 'ROUTINE',
    referring_doctor_id UUID,
    clinical_notes      TEXT,
    diagnosis           TEXT,
    total_amount        NUMERIC(12,2) NOT NULL DEFAULT 0,
    discount_amount     NUMERIC(12,2) DEFAULT 0,
    net_amount          NUMERIC(12,2) NOT NULL DEFAULT 0,
    ordered_at          TIMESTAMPTZ,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(100),
    updated_by          VARCHAR(100),
    is_deleted          BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at          TIMESTAMPTZ
);

CREATE INDEX idx_order_branch ON test_order(branch_id, is_deleted);
CREATE INDEX idx_order_patient ON test_order(patient_id);
CREATE INDEX idx_order_barcode ON test_order(order_barcode);
CREATE INDEX idx_order_status ON test_order(order_status, branch_id);
```

### order_line_item

```sql
CREATE TABLE order_line_item (
    id              UUID PRIMARY KEY,
    branch_id       UUID NOT NULL,
    order_id        UUID NOT NULL REFERENCES test_order(id),
    test_id         UUID NOT NULL,
    test_name       VARCHAR(200) NOT NULL,
    test_code       VARCHAR(50) NOT NULL,
    department_id   UUID,
    department_name VARCHAR(100),
    unit_price      NUMERIC(10,2) NOT NULL,
    quantity        INTEGER NOT NULL DEFAULT 1,
    discount_pct    NUMERIC(5,2) DEFAULT 0,
    net_price       NUMERIC(10,2) NOT NULL,
    status          VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    is_deleted      BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ
);

CREATE INDEX idx_line_item_order ON order_line_item(order_id);
CREATE INDEX idx_line_item_branch ON order_line_item(branch_id, is_deleted);
```

### sample_collection

```sql
CREATE TABLE sample_collection (
    id                  UUID PRIMARY KEY,
    branch_id           UUID NOT NULL,
    order_id            UUID NOT NULL REFERENCES test_order(id),
    patient_id          UUID NOT NULL REFERENCES patient(id),
    sample_barcode      VARCHAR(50) UNIQUE NOT NULL,
    tube_type           VARCHAR(50) NOT NULL,
    sample_type         VARCHAR(50) NOT NULL,
    volume_ml           NUMERIC(6,2),
    status              VARCHAR(30) NOT NULL DEFAULT 'COLLECTED',
    collected_at        TIMESTAMPTZ,
    collected_by        VARCHAR(100),
    received_at         TIMESTAMPTZ,
    received_by         VARCHAR(100),
    rejection_reason    VARCHAR(100),
    rejected_at         TIMESTAMPTZ,
    storage_location    VARCHAR(100),
    stored_at           TIMESTAMPTZ,
    disposed_at         TIMESTAMPTZ,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(100),
    updated_by          VARCHAR(100),
    is_deleted          BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at          TIMESTAMPTZ
);

CREATE INDEX idx_sample_branch ON sample_collection(branch_id, is_deleted);
CREATE INDEX idx_sample_barcode ON sample_collection(sample_barcode);
CREATE INDEX idx_sample_order ON sample_collection(order_id);
CREATE INDEX idx_sample_status ON sample_collection(status, branch_id);
```

### test_result (partitioned by branch_id for performance)

```sql
CREATE TABLE test_result (
    id                  UUID NOT NULL,
    branch_id           UUID NOT NULL,
    order_id            UUID NOT NULL,
    order_line_item_id  UUID NOT NULL,
    sample_id           UUID,
    patient_id          UUID NOT NULL,
    parameter_id        UUID,
    parameter_code      VARCHAR(50),
    parameter_name      VARCHAR(200),
    result_type         VARCHAR(30) NOT NULL,
    numeric_value       NUMERIC(15,6),
    text_value          TEXT,
    unit                VARCHAR(30),
    low_normal          NUMERIC(15,6),
    high_normal         NUMERIC(15,6),
    low_critical        NUMERIC(15,6),
    high_critical       NUMERIC(15,6),
    flag                VARCHAR(10),
    status              VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    instrument_id       UUID,
    instrument_result_at TIMESTAMPTZ,
    entered_by          VARCHAR(100),
    entered_at          TIMESTAMPTZ,
    validated_by        VARCHAR(100),
    validated_at        TIMESTAMPTZ,
    authorized_by       VARCHAR(100),
    authorized_at       TIMESTAMPTZ,
    is_critical         BOOLEAN DEFAULT FALSE,
    critical_notified_at TIMESTAMPTZ,
    comments            TEXT,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(100),
    updated_by          VARCHAR(100),
    is_deleted          BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at          TIMESTAMPTZ,
    PRIMARY KEY (id, branch_id)
) PARTITION BY LIST (branch_id);

CREATE INDEX idx_result_order ON test_result(order_id, branch_id);
CREATE INDEX idx_result_status ON test_result(status, branch_id);
CREATE INDEX idx_result_critical ON test_result(is_critical, branch_id) WHERE is_critical = TRUE;
```

### invoice

```sql
CREATE TABLE invoice (
    id              UUID PRIMARY KEY,
    branch_id       UUID NOT NULL,
    order_id        UUID NOT NULL REFERENCES test_order(id),
    patient_id      UUID NOT NULL REFERENCES patient(id),
    invoice_number  VARCHAR(50) UNIQUE NOT NULL,
    status          VARCHAR(30) NOT NULL DEFAULT 'GENERATED',
    subtotal        NUMERIC(12,2) NOT NULL,
    discount_amount NUMERIC(12,2) DEFAULT 0,
    tax_amount      NUMERIC(12,2) DEFAULT 0,
    total_amount    NUMERIC(12,2) NOT NULL,
    paid_amount     NUMERIC(12,2) DEFAULT 0,
    balance_amount  NUMERIC(12,2) NOT NULL,
    payment_method  VARCHAR(30),
    paid_at         TIMESTAMPTZ,
    due_date        DATE,
    notes           TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    is_deleted      BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ
);

CREATE INDEX idx_invoice_branch ON invoice(branch_id, is_deleted);
CREATE INDEX idx_invoice_order ON invoice(order_id);
CREATE INDEX idx_invoice_status ON invoice(status, branch_id);
CREATE INDEX idx_invoice_patient ON invoice(patient_id);
```

### report

```sql
CREATE TABLE report (
    id                  UUID PRIMARY KEY,
    branch_id           UUID NOT NULL,
    order_id            UUID NOT NULL REFERENCES test_order(id),
    patient_id          UUID NOT NULL REFERENCES patient(id),
    report_number       VARCHAR(50),
    status              VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    version             INTEGER NOT NULL DEFAULT 1,
    pdf_path            VARCHAR(500),  -- MinIO path
    qr_code_url         VARCHAR(500),
    authorized_by       UUID,
    authorized_at       TIMESTAMPTZ,
    released_at         TIMESTAMPTZ,
    delivered_at        TIMESTAMPTZ,
    amendment_reason    TEXT,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by          VARCHAR(100),
    updated_by          VARCHAR(100),
    is_deleted          BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at          TIMESTAMPTZ
);

CREATE INDEX idx_report_branch ON report(branch_id, is_deleted);
CREATE INDEX idx_report_order ON report(order_id);
CREATE INDEX idx_report_status ON report(status, branch_id);
```

### audit_trail (partitioned by created_at for retention management)

```sql
CREATE TABLE audit_trail (
    id              UUID NOT NULL,
    branch_id       UUID NOT NULL,
    entity_type     VARCHAR(50) NOT NULL,
    entity_id       UUID NOT NULL,
    action          VARCHAR(50) NOT NULL,
    old_value       JSONB,
    new_value       JSONB,
    changed_by      VARCHAR(100),
    changed_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    ip_address      INET,
    user_agent      TEXT,
    PRIMARY KEY (id, changed_at)
) PARTITION BY RANGE (changed_at);

-- Create monthly partitions
CREATE TABLE audit_trail_2024_01 PARTITION OF audit_trail
    FOR VALUES FROM ('2024-01-01') TO ('2024-02-01');

CREATE INDEX idx_audit_entity ON audit_trail(entity_type, entity_id);
CREATE INDEX idx_audit_branch ON audit_trail(branch_id, changed_at);
```

### critical_value_log

```sql
CREATE TABLE critical_value_log (
    id                      UUID PRIMARY KEY,
    branch_id               UUID NOT NULL,
    result_id               UUID NOT NULL,
    patient_id              UUID NOT NULL,
    order_id                UUID NOT NULL,
    parameter_code          VARCHAR(50),
    parameter_name          VARCHAR(200),
    critical_value          NUMERIC(15,6),
    flag                    VARCHAR(10),
    pathologist_id          UUID,
    detected_at             TIMESTAMPTZ NOT NULL,
    physician_notified_name VARCHAR(200),
    notification_method     VARCHAR(50),
    notified_at             TIMESTAMPTZ,
    read_back_confirmed     BOOLEAN DEFAULT FALSE,
    acknowledged_at         TIMESTAMPTZ,
    notes                   TEXT,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    branch_id_idx           UUID GENERATED ALWAYS AS (branch_id) STORED
);

CREATE INDEX idx_critical_branch ON critical_value_log(branch_id, detected_at);
CREATE INDEX idx_critical_acknowledged ON critical_value_log(branch_id) WHERE acknowledged_at IS NULL;
```

## Partitioning Strategy

| Table | Partition By | Retention |
|-------|-------------|-----------|
| test_result | LIST(branch_id) | Permanent |
| audit_trail | RANGE(changed_at) | 7 years |
| notification_log | RANGE(created_at) | 1 year |
| qc_result | RANGE(measured_at) | 2 years |
