# L4 Operational Masters — RasterOneLab LIS

Managed by: **PATHOLOGIST / BRANCH_ADMIN**
Scope: **Day-to-day operations** (runtime configuration and logs that drive daily lab workflows)

> Operational masters are living records updated during daily lab operations.
> They support quality assurance, compliance, and workflow management.
> All tables include `branch_id UUID NOT NULL` and inherit from `BaseEntity`.

---

## 1. QC Daily Run Log

Records each quality control run performed on instruments. Drives Levey-Jennings charts and Westgard rule evaluation.

```sql
CREATE TABLE qc_daily_run (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL REFERENCES branch(id),
    instrument_id       UUID NOT NULL REFERENCES instrument(id),
    parameter_id        UUID NOT NULL REFERENCES parameter_master(id),
    qc_material_id      UUID NOT NULL REFERENCES qc_material(id),
    run_date            DATE NOT NULL,
    run_time            TIMESTAMPTZ NOT NULL,
    level               VARCHAR(20) NOT NULL,      -- LEVEL_1, LEVEL_2, LEVEL_3
    observed_value      NUMERIC(15,4) NOT NULL,
    target_value        NUMERIC(15,4) NOT NULL,
    target_sd           NUMERIC(15,4) NOT NULL,
    z_score             NUMERIC(8,4),              -- (observed - target) / sd
    westgard_status     VARCHAR(20) NOT NULL,      -- PASS, WARNING_1_2S, REJECT_1_3S, REJECT_2_2S, REJECT_R_4S
    rules_violated      JSONB,                     -- ["1_3S", "2_2S"]
    action_taken        VARCHAR(30),               -- ACCEPTED, RECALIBRATED, REPEAT_QC, HOLD_RESULTS
    performed_by        UUID NOT NULL,
    reviewed_by         UUID,
    remarks             TEXT,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ
);

CREATE INDEX idx_qc_daily_run_branch_date ON qc_daily_run (branch_id, run_date);
CREATE INDEX idx_qc_daily_run_instrument ON qc_daily_run (instrument_id, parameter_id, run_date);
```

| Field | Example |
|-------|---------|
| instrument | Sysmex XN-1000 |
| parameter | Hemoglobin |
| level | LEVEL_1 |
| observed_value | 12.8 |
| target_value | 12.5 |
| target_sd | 0.3 |
| z_score | +1.0 |
| westgard_status | PASS |

**API**: `GET /api/v1/qc-runs` · `POST /api/v1/qc-runs` · `GET /api/v1/qc-runs/levey-jennings`

---

## 2. Corrective Action Log

Documents corrective actions taken when QC fails, instrument malfunctions, or process deviations occur. Required for NABL/CAP accreditation.

```sql
CREATE TABLE corrective_action_log (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL REFERENCES branch(id),
    incident_date       DATE NOT NULL,
    incident_type       VARCHAR(30) NOT NULL,      -- QC_FAILURE, INSTRUMENT_ERROR, PROCESS_DEVIATION, COMPLAINT
    incident_description TEXT NOT NULL,
    root_cause          TEXT,
    corrective_action   TEXT NOT NULL,
    preventive_action   TEXT,
    affected_instrument_id UUID REFERENCES instrument(id),
    affected_parameter_id UUID REFERENCES parameter_master(id),
    samples_affected    INT DEFAULT 0,
    status              VARCHAR(20) NOT NULL,      -- OPEN, IN_PROGRESS, RESOLVED, CLOSED
    raised_by           UUID NOT NULL,
    assigned_to         UUID,
    resolved_by         UUID,
    resolved_at         TIMESTAMPTZ,
    verified_by         UUID,                      -- Pathologist verification
    verified_at         TIMESTAMPTZ,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ
);
```

| Field | Example |
|-------|---------|
| incident_type | QC_FAILURE |
| description | QC Level 2 failed 1_3S rule for Glucose on Vitros 5600 |
| root_cause | Reagent lot approaching expiry |
| corrective_action | Replaced reagent lot, re-calibrated, re-ran QC |
| preventive_action | Added 15-day expiry alert for reagent lots |
| status | CLOSED |

**API**: `GET /api/v1/corrective-actions` · `POST /api/v1/corrective-actions` · `PUT /api/v1/corrective-actions/{id}`

---

## 3. EQA Program Enrollment

External Quality Assessment (proficiency testing) program registrations.

```sql
CREATE TABLE eqa_enrollment (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL REFERENCES branch(id),
    program_name        VARCHAR(200) NOT NULL,     -- e.g. "RIQAS Biochemistry", "CMC-QAS Hematology"
    provider            VARCHAR(200) NOT NULL,     -- e.g. "Randox", "Bio-Rad EQAS"
    department_id       UUID REFERENCES department(id),
    enrollment_year     INT NOT NULL,
    enrollment_number   VARCHAR(100),
    cycle_frequency     VARCHAR(20),               -- MONTHLY, QUARTERLY, BI_ANNUAL
    start_date          DATE,
    end_date            DATE,
    status              VARCHAR(20) DEFAULT 'ACTIVE',  -- ACTIVE, EXPIRED, CANCELLED
    contact_person      VARCHAR(200),
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ
);
```

**API**: `GET /api/v1/eqa-enrollments` · `POST /api/v1/eqa-enrollments` · `PUT /api/v1/eqa-enrollments/{id}`

---

## 4. EQA Results Submission

Records submitted to EQA providers and their evaluation results.

```sql
CREATE TABLE eqa_result (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL REFERENCES branch(id),
    enrollment_id       UUID NOT NULL REFERENCES eqa_enrollment(id),
    cycle_number        VARCHAR(30) NOT NULL,      -- e.g. "2024-C03"
    parameter_id        UUID NOT NULL REFERENCES parameter_master(id),
    submitted_value     NUMERIC(15,4),
    peer_mean           NUMERIC(15,4),
    peer_sd             NUMERIC(15,4),
    sdi                 NUMERIC(8,4),              -- Standard Deviation Index
    evaluation          VARCHAR(20),               -- ACCEPTABLE, WARNING, UNACCEPTABLE
    submission_date     DATE,
    result_date         DATE,
    submitted_by        UUID,
    remarks             TEXT,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ
);
```

| cycle | parameter | submitted | peer_mean | sdi | evaluation |
|-------|-----------|-----------|-----------|-----|-----------|
| 2024-C03 | Glucose | 98.5 | 99.2 | -0.3 | ACCEPTABLE |
| 2024-C03 | Creatinine | 1.45 | 1.38 | +1.2 | ACCEPTABLE |
| 2024-C03 | Potassium | 5.8 | 4.9 | +2.5 | WARNING |

**API**: `GET /api/v1/eqa-results` · `POST /api/v1/eqa-results`

---

## 5. Workflow Status Overrides

Allows branch admins to customize the sample/order workflow state machine for specific test types or departments.

```sql
CREATE TABLE workflow_status_override (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL REFERENCES branch(id),
    entity_type         VARCHAR(30) NOT NULL,      -- SAMPLE, ORDER, RESULT
    department_id       UUID REFERENCES department(id),
    from_status         VARCHAR(30) NOT NULL,
    to_status           VARCHAR(30) NOT NULL,
    is_allowed          BOOLEAN DEFAULT true,
    required_role       VARCHAR(50),
    auto_transition     BOOLEAN DEFAULT false,
    notify_on_transition BOOLEAN DEFAULT false,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ,
    UNIQUE (branch_id, entity_type, department_id, from_status, to_status)
);
```

**API**: `GET /api/v1/workflow-overrides` · `POST /api/v1/workflow-overrides`

---

## 6. Approval Workflows

Multi-level approval chains for result authorization.

```sql
CREATE TABLE approval_workflow (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL REFERENCES branch(id),
    workflow_name       VARCHAR(200) NOT NULL,
    department_id       UUID REFERENCES department(id),
    test_id             UUID REFERENCES test_master(id),
    is_active           BOOLEAN DEFAULT true,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ
);

CREATE TABLE approval_workflow_step (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    workflow_id         UUID NOT NULL REFERENCES approval_workflow(id),
    step_order          INT NOT NULL,
    approver_role       VARCHAR(50) NOT NULL,       -- LAB_TECHNICIAN, SENIOR_TECH, PATHOLOGIST
    approver_user_id    UUID,                       -- Specific user or null for any with role
    is_mandatory        BOOLEAN DEFAULT true,
    auto_approve_hours  INT,                        -- Auto-approve if no action in N hours
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now()
);
```

| Workflow | Step 1 | Step 2 | Step 3 |
|----------|--------|--------|--------|
| Biochemistry Standard | Tech Entry | Senior Review | Pathologist Authorize |
| Hematology CBC | Tech Entry | Auto-validate | Pathologist (if flagged) |
| Histopathology | Pathologist Dictation | Senior Pathologist Review | — |
| Critical Results | Tech Entry | Pathologist STAT | Branch Admin notify |

**API**: `GET /api/v1/approval-workflows` · `POST /api/v1/approval-workflows` · `PUT /api/v1/approval-workflows/{id}`

---

## 7. SOP Documents

Standard Operating Procedure document management for compliance.

```sql
CREATE TABLE sop_document (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL REFERENCES branch(id),
    sop_code            VARCHAR(30) NOT NULL,
    sop_title           VARCHAR(200) NOT NULL,
    department_id       UUID REFERENCES department(id),
    category            VARCHAR(50),               -- PRE_ANALYTICAL, ANALYTICAL, POST_ANALYTICAL, SAFETY
    version             VARCHAR(10) NOT NULL,
    effective_date      DATE NOT NULL,
    review_date         DATE,
    document_url        VARCHAR(500) NOT NULL,     -- Stored in MinIO
    file_type           VARCHAR(10),               -- PDF, DOCX
    approved_by         UUID,
    approved_at         TIMESTAMPTZ,
    status              VARCHAR(20) DEFAULT 'DRAFT', -- DRAFT, ACTIVE, SUPERSEDED, ARCHIVED
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ,
    UNIQUE (branch_id, sop_code, version)
);
```

| sop_code | title | category | version | status |
|----------|-------|----------|---------|--------|
| SOP-PRE-001 | Sample Collection & Labeling | PRE_ANALYTICAL | 3.0 | ACTIVE |
| SOP-ANA-010 | CBC Analysis on Sysmex XN-1000 | ANALYTICAL | 2.1 | ACTIVE |
| SOP-POST-005 | Critical Value Reporting | POST_ANALYTICAL | 1.2 | ACTIVE |
| SOP-SAF-001 | Biohazard Spill Cleanup | SAFETY | 1.0 | ACTIVE |

**API**: `GET /api/v1/sop-documents` · `POST /api/v1/sop-documents` · `PUT /api/v1/sop-documents/{id}`

---

## 8. Escalation Rules

Defines automatic escalation when TAT is breached, critical values are not acknowledged, or other SLA violations occur.

```sql
CREATE TABLE escalation_rule (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL REFERENCES branch(id),
    rule_name           VARCHAR(200) NOT NULL,
    trigger_event       VARCHAR(50) NOT NULL,      -- TAT_BREACH, CRITICAL_NOT_ACK, QC_FAILURE, SAMPLE_REJECTION
    trigger_threshold   INT,                       -- Minutes/count threshold
    escalation_level    INT NOT NULL DEFAULT 1,    -- 1, 2, 3
    notify_role         VARCHAR(50),               -- BRANCH_ADMIN, PATHOLOGIST, ORG_ADMIN
    notify_user_id      UUID,
    notify_channels     VARCHAR(100),              -- SMS,EMAIL,WHATSAPP,IN_APP
    is_active           BOOLEAN DEFAULT true,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ
);
```

| Trigger | Threshold | Level 1 | Level 2 | Level 3 |
|---------|-----------|---------|---------|---------|
| TAT_BREACH | +30 min | Lab Supervisor (SMS) | Branch Admin (SMS+Email) | Org Admin (All) |
| CRITICAL_NOT_ACK | +15 min | Pathologist (SMS) | Branch Admin (Call) | Org Admin (All) |
| QC_FAILURE | Immediate | Lab Supervisor (In-App) | Pathologist (SMS) | — |
| SAMPLE_REJECTION | > 5/day | Branch Admin (Email) | Org Admin (Email) | — |

**API**: `GET /api/v1/escalation-rules` · `POST /api/v1/escalation-rules` · `PUT /api/v1/escalation-rules/{id}`

---

## 9. Maintenance Logs

Tracks scheduled and unscheduled instrument maintenance.

```sql
CREATE TABLE maintenance_log (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL REFERENCES branch(id),
    instrument_id       UUID NOT NULL REFERENCES instrument(id),
    maintenance_type    VARCHAR(30) NOT NULL,      -- DAILY, WEEKLY, MONTHLY, ANNUAL, BREAKDOWN
    maintenance_date    TIMESTAMPTZ NOT NULL,
    description         TEXT NOT NULL,
    performed_by        UUID NOT NULL,
    engineer_name       VARCHAR(200),              -- External service engineer
    engineer_company    VARCHAR(200),
    parts_replaced      TEXT,
    downtime_hours      NUMERIC(6,2),
    next_maintenance    DATE,
    status              VARCHAR(20) DEFAULT 'COMPLETED', -- SCHEDULED, IN_PROGRESS, COMPLETED, PENDING_PARTS
    document_url        VARCHAR(500),
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ
);
```

| Type | Frequency | Typical Tasks |
|------|-----------|---------------|
| DAILY | Every day | Cleaning, background check, flush lines |
| WEEKLY | Every week | Reagent check, waste disposal, decontamination |
| MONTHLY | Every month | Deep clean, replace tubing, verify calibration |
| ANNUAL | Yearly | Preventive maintenance contract (AMC) visit |
| BREAKDOWN | Ad-hoc | Unscheduled repair after fault |

**API**: `GET /api/v1/maintenance-logs` · `POST /api/v1/maintenance-logs` · `PUT /api/v1/maintenance-logs/{id}`

---

## 10. Capacity Planning

Defines daily test capacity per instrument/department to manage workload and scheduling.

```sql
CREATE TABLE capacity_plan (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL REFERENCES branch(id),
    department_id       UUID REFERENCES department(id),
    instrument_id       UUID REFERENCES instrument(id),
    max_tests_per_day   INT NOT NULL,
    max_tests_per_shift INT,
    current_load_pct    NUMERIC(5,2) DEFAULT 0,
    overflow_action     VARCHAR(30),               -- QUEUE, OUTSOURCE, REJECT
    overflow_partner_lab_id UUID REFERENCES partner_lab(id),
    effective_from      DATE,
    effective_to        DATE,
    is_active           BOOLEAN DEFAULT true,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ,
    UNIQUE (branch_id, department_id, instrument_id, effective_from)
);
```

| Department | Instrument | Max/Day | Overflow |
|-----------|-----------|---------|----------|
| BIOCHEM | Vitros 5600 | 500 | QUEUE |
| HEMA | Sysmex XN-1000 | 300 | OUTSOURCE |
| MICRO | VITEK 2 | 50 | QUEUE |
| SERO | Architect i2000 | 200 | OUTSOURCE |

**API**: `GET /api/v1/capacity-plans` · `POST /api/v1/capacity-plans` · `PUT /api/v1/capacity-plans/{id}`

---

## Notes

- Operational masters are **write-heavy** — they accumulate records during daily lab operations.
- QC run logs and corrective action logs are **never deleted** (soft or hard) for compliance purposes.
- All tables are filtered by `branch_id` via `BranchContextHolder`.
- EQA records support NABL/CAP accreditation requirements for external proficiency testing.
- SOP documents are versioned — superseded versions are archived, not deleted.
- Escalation rules integrate with the `lis-notification` module for multi-channel alerts.
- Maintenance logs tie into instrument status — an instrument under maintenance is automatically marked unavailable.
- Capacity planning data feeds into the order routing engine to balance workload across instruments.
