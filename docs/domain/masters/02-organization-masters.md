# L2 Organization Masters — RasterOneLab LIS

Managed by: **ORG_ADMIN** (Lab Director / IT Admin)
Scope: **Per organization** (shared across all branches in org)

> All tables include `org_id UUID NOT NULL` and inherit from `BaseEntity` (`id`, `created_at`, `updated_at`, `created_by`, `updated_by`, `is_deleted`, `deleted_at`).
> Organization-level masters are shared across all branches — `branch_id` is **not** required at this level (only at L3 and below).
> API endpoints follow the pattern `/api/v1/{resource}` and require the `X-Branch-Id` header for branch-scoped queries.

---

## 1. Organization Setup (3 masters)

### 1.1 Organization Profile

```sql
CREATE TABLE organization (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_code        VARCHAR(20) UNIQUE NOT NULL,
    org_name        VARCHAR(200) NOT NULL,
    legal_name      VARCHAR(200),
    org_type        VARCHAR(30) NOT NULL,       -- STANDALONE, CHAIN, FRANCHISE
    logo_url        VARCHAR(500),
    address_line1   VARCHAR(200),
    address_line2   VARCHAR(200),
    city            VARCHAR(100),
    state           VARCHAR(100),
    country_code    VARCHAR(3),
    postal_code     VARCHAR(20),
    phone           VARCHAR(20),
    email           VARCHAR(100),
    website         VARCHAR(200),
    license_number  VARCHAR(100),
    nabl_number     VARCHAR(100),
    cap_number      VARCHAR(100),
    gst_number      VARCHAR(50),
    pan_number      VARCHAR(50),
    default_currency VARCHAR(3) DEFAULT 'INR',
    default_timezone VARCHAR(50) DEFAULT 'Asia/Kolkata',
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ
);
```

**API**: `GET /api/v1/organizations/{orgId}` · `PUT /api/v1/organizations/{orgId}`

### 1.2 Organization Logo & Branding

Stored in MinIO under `orgs/{orgId}/logo.*` — referenced by `organization.logo_url`.

### 1.3 License & Accreditation

| Column | Type | Example |
|--------|------|---------|
| license_number | VARCHAR | MH-LAB-2024-1234 |
| nabl_number | VARCHAR | MC-4567 |
| cap_number | VARCHAR | CAP-8901234 |
| valid_from | DATE | 2024-01-01 |
| valid_until | DATE | 2027-01-01 |

---

## 2. Departments (11 standard departments)

```sql
CREATE TABLE department (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id          UUID NOT NULL REFERENCES organization(id),
    dept_code       VARCHAR(20) NOT NULL,
    dept_name       VARCHAR(100) NOT NULL,
    display_order   INT DEFAULT 0,
    hod_user_id     UUID,
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (org_id, dept_code)
);
```

| # | dept_code | dept_name | Typical Tests |
|---|-----------|-----------|---------------|
| 1 | BIOCHEM | Biochemistry | LFT, KFT, Lipid Profile, Blood Sugar |
| 2 | HEMA | Hematology | CBC, ESR, Coagulation |
| 3 | MICRO | Microbiology | Culture & Sensitivity, AFB |
| 4 | SERO | Serology / Immunology | HIV, HBsAg, Thyroid, Tumor Markers |
| 5 | HISTO | Histopathology | Biopsy, FNAC, Pap Smear |
| 6 | CYTO | Cytology | Body fluid cytology |
| 7 | MOLBIO | Molecular Biology | PCR, COVID RT-PCR, HCV RNA |
| 8 | CLINPATH | Clinical Pathology | Urinalysis, Stool, Semen |
| 9 | GENETICS | Genetics | Karyotyping, FISH, NGS panels |
| 10 | TOXICOL | Toxicology | Drug screening, heavy metals |
| 11 | BLOOD_BANK | Blood Bank / Transfusion | Cross-match, grouping |

**API**: `GET /api/v1/departments` · `POST /api/v1/departments` · `PUT /api/v1/departments/{id}`

---

## 3. Test Master (~500 tests)

```sql
CREATE TABLE test_master (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id              UUID NOT NULL REFERENCES organization(id),
    test_code           VARCHAR(30) NOT NULL,
    test_name           VARCHAR(200) NOT NULL,
    short_name          VARCHAR(50),
    department_id       UUID NOT NULL REFERENCES department(id),
    sample_type_code    VARCHAR(30),           -- FK to global sample_type
    tube_type_code      VARCHAR(30),           -- FK to global tube_type
    sample_volume_ml    NUMERIC(6,2),
    test_type           VARCHAR(20) NOT NULL,  -- SINGLE, PANEL, PROFILE, CALCULATED
    method              VARCHAR(100),
    tat_hours           INT DEFAULT 24,
    is_outsourced       BOOLEAN DEFAULT false,
    partner_lab_id      UUID,
    loinc_code          VARCHAR(20),
    cpt_code            VARCHAR(20),
    report_template_id  UUID,
    display_order       INT DEFAULT 0,
    instructions        TEXT,                  -- Patient instructions
    is_active           BOOLEAN DEFAULT true,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ,
    UNIQUE (org_id, test_code)
);
```

| test_code | test_name | department | sample_type | tube_type | tat_hours |
|-----------|-----------|------------|-------------|-----------|-----------|
| CBC | Complete Blood Count | HEMA | WHOLE_BLOOD | EDTA_PURPLE | 4 |
| LFT | Liver Function Test | BIOCHEM | SERUM | SST_GOLD | 6 |
| KFT | Kidney Function Test | BIOCHEM | SERUM | SST_GOLD | 6 |
| TSH | Thyroid Stimulating Hormone | SERO | SERUM | SST_GOLD | 8 |
| HBA1C | Glycated Hemoglobin | BIOCHEM | WHOLE_BLOOD | EDTA_PURPLE | 6 |
| URINE_RE | Urine Routine & Microscopy | CLINPATH | URINE_RANDOM | URINE_CONTAINER | 4 |
| HIV_SCREEN | HIV Screening (4th Gen) | SERO | SERUM | SST_GOLD | 8 |
| CS_URINE | Culture & Sensitivity — Urine | MICRO | URINE_RANDOM | STERILE_CONTAINER_50ML | 72 |

**API**: `GET /api/v1/tests` · `POST /api/v1/tests` · `PUT /api/v1/tests/{id}` · `DELETE /api/v1/tests/{id}`

---

## 4. Parameter Master (~2000 parameters)

```sql
CREATE TABLE parameter_master (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id              UUID NOT NULL REFERENCES organization(id),
    param_code          VARCHAR(30) NOT NULL,
    param_name          VARCHAR(200) NOT NULL,
    short_name          VARCHAR(50),
    test_id             UUID NOT NULL REFERENCES test_master(id),
    data_type           VARCHAR(20) NOT NULL,  -- NUMERIC, TEXT, OPTION, FORMULA, SEMI_QUANT
    unit_code           VARCHAR(50),           -- FK to global measurement_unit
    loinc_code          VARCHAR(20),
    decimal_places      INT DEFAULT 2,
    result_options      JSONB,                 -- For OPTION type: ["Reactive","Non-Reactive"]
    formula             TEXT,                  -- For FORMULA type: "TC - HDL - (TG/5)"
    display_order       INT DEFAULT 0,
    is_critical         BOOLEAN DEFAULT false,
    is_reportable       BOOLEAN DEFAULT true,
    is_derived          BOOLEAN DEFAULT false,
    is_active           BOOLEAN DEFAULT true,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ,
    UNIQUE (org_id, param_code)
);
```

| param_code | param_name | test | data_type | unit | example_value |
|------------|-----------|------|-----------|------|---------------|
| HGB | Hemoglobin | CBC | NUMERIC | g/dL | 14.2 |
| WBC | White Blood Cell Count | CBC | NUMERIC | ×10³/μL | 7.8 |
| PLT | Platelet Count | CBC | NUMERIC | ×10³/μL | 250 |
| SGPT | Alanine Aminotransferase | LFT | NUMERIC | U/L | 32 |
| CREAT | Serum Creatinine | KFT | NUMERIC | mg/dL | 0.9 |
| TSH_VAL | TSH | TSH | NUMERIC | μIU/mL | 2.5 |
| HIV_RESULT | HIV Screening Result | HIV_SCREEN | OPTION | — | Non-Reactive |
| LDL_CALC | LDL Cholesterol (calc) | LIPID | FORMULA | mg/dL | TC-HDL-(TG/5) |

**API**: `GET /api/v1/parameters` · `POST /api/v1/parameters` · `PUT /api/v1/parameters/{id}`

---

## 5. Test Panels (~100 panels)

```sql
CREATE TABLE test_panel (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id          UUID NOT NULL REFERENCES organization(id),
    panel_code      VARCHAR(30) NOT NULL,
    panel_name      VARCHAR(200) NOT NULL,
    department_id   UUID REFERENCES department(id),
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (org_id, panel_code)
);

CREATE TABLE test_panel_item (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    panel_id    UUID NOT NULL REFERENCES test_panel(id),
    test_id     UUID NOT NULL REFERENCES test_master(id),
    display_order INT DEFAULT 0
);
```

| panel_code | panel_name | Tests Included |
|------------|-----------|----------------|
| LIPID_PANEL | Lipid Profile | TC, TG, HDL, LDL, VLDL, TC/HDL Ratio |
| LFT_PANEL | Liver Function Test | Bilirubin (T/D/I), SGOT, SGPT, ALP, GGT, Total Protein, Albumin |
| KFT_PANEL | Kidney Function Test | Urea, Creatinine, Uric Acid, Na, K, Cl, Ca, PO4 |
| THYROID_PANEL | Thyroid Profile | T3, T4, TSH |
| DIABETIC_PANEL | Diabetes Panel | FBS, PPBS, HbA1c |
| MASTER_HEALTH | Master Health Checkup | CBC, LFT, KFT, Lipid, Thyroid, Urine, FBS |

**API**: `GET /api/v1/panels` · `POST /api/v1/panels` · `PUT /api/v1/panels/{id}`

---

## 6. Reference Ranges (~5000 ranges)

```sql
CREATE TABLE reference_range (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id              UUID NOT NULL REFERENCES organization(id),
    parameter_id        UUID NOT NULL REFERENCES parameter_master(id),
    gender              VARCHAR(10),           -- MALE, FEMALE, ALL
    age_min_days        INT DEFAULT 0,
    age_max_days        INT DEFAULT 36500,     -- ~100 years
    trimester           VARCHAR(10),           -- T1, T2, T3, null
    normal_low          NUMERIC(15,4),
    normal_high         NUMERIC(15,4),
    critical_low        NUMERIC(15,4),
    critical_high       NUMERIC(15,4),
    panic_low           NUMERIC(15,4),
    panic_high          NUMERIC(15,4),
    unit_code           VARCHAR(50),
    interpretation_text TEXT,                  -- "Normal", "Borderline High"
    is_active           BOOLEAN DEFAULT true,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ
);
```

| Parameter | Gender | Age Range | Normal Low | Normal High | Critical Low | Critical High |
|-----------|--------|-----------|-----------|-------------|-------------|---------------|
| HGB | MALE | Adult (18-65y) | 13.0 | 17.0 | 7.0 | 20.0 |
| HGB | FEMALE | Adult (18-65y) | 12.0 | 15.5 | 7.0 | 20.0 |
| HGB | ALL | Newborn (0-7d) | 14.0 | 24.0 | 10.0 | 28.0 |
| CREAT | MALE | Adult | 0.7 | 1.3 | 0.4 | 10.0 |
| CREAT | FEMALE | Adult | 0.6 | 1.1 | 0.4 | 10.0 |
| TSH_VAL | ALL | Adult | 0.4 | 4.0 | 0.01 | 50.0 |
| TSH_VAL | FEMALE | Trimester 1 | 0.1 | 2.5 | — | — |

**API**: `GET /api/v1/reference-ranges` · `POST /api/v1/reference-ranges` · `PUT /api/v1/reference-ranges/{id}`

---

## 7. Sample Requirements

Defined per test via `test_master.sample_type_code`, `test_master.tube_type_code`, and `test_master.sample_volume_ml`. Additional constraints stored in:

```sql
CREATE TABLE sample_requirement (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id              UUID NOT NULL,
    test_id             UUID NOT NULL REFERENCES test_master(id),
    sample_type_code    VARCHAR(30) NOT NULL,
    tube_type_code      VARCHAR(30) NOT NULL,
    min_volume_ml       NUMERIC(6,2),
    fasting_required    BOOLEAN DEFAULT false,
    fasting_hours       INT,
    special_instructions TEXT,
    stability_hours     INT,                   -- Room temperature
    stability_hours_fridge INT,                -- 2-8°C
    stability_hours_frozen INT,                -- -20°C
    is_active           BOOLEAN DEFAULT true,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ
);
```

---

## 8. Calculated Parameters (~50 formulas)

Formulas stored in `parameter_master.formula` for parameters with `data_type = 'FORMULA'`.

| Calculated Param | Formula | Depends On |
|------------------|---------|-----------|
| LDL Cholesterol | TC - HDL - (TG / 5) | TC, HDL, TG |
| eGFR (CKD-EPI) | 141 × min(Cr/κ, 1)^α × max(Cr/κ, 1)^-1.209 × 0.993^Age | Creatinine, Age, Gender |
| A/G Ratio | Albumin / (Total Protein - Albumin) | Albumin, Total Protein |
| Globulin | Total Protein - Albumin | Total Protein, Albumin |
| VLDL Cholesterol | TG / 5 | TG |
| Corrected Calcium | Ca + 0.8 × (4.0 - Albumin) | Calcium, Albumin |
| Anion Gap | Na - (Cl + HCO3) | Na, Cl, HCO3 |
| MCHC | (HGB / HCT) × 100 | HGB, HCT |

---

## 9. Reflex Rules (~100 rules)

```sql
CREATE TABLE reflex_rule (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id              UUID NOT NULL,
    trigger_param_id    UUID NOT NULL REFERENCES parameter_master(id),
    condition_operator  VARCHAR(10) NOT NULL,  -- GT, LT, GTE, LTE, EQ, BETWEEN, IN
    condition_value     VARCHAR(100) NOT NULL,
    reflex_test_id      UUID NOT NULL REFERENCES test_master(id),
    priority            INT DEFAULT 0,
    is_auto_order       BOOLEAN DEFAULT true,
    notification_text   TEXT,
    is_active           BOOLEAN DEFAULT true,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ
);
```

| Trigger Parameter | Condition | Reflex Test | Auto-Order |
|-------------------|-----------|-------------|-----------|
| TSH | > 10.0 | Free T3, Free T4 | Yes |
| TSH | < 0.1 | Free T3, Free T4 | Yes |
| HBsAg | = Reactive | HBV DNA Quantitative | No (suggest) |
| HIV_SCREEN | = Reactive | HIV Confirmatory (Western Blot) | Yes |
| Glucose Fasting | > 126 | HbA1c | Yes |
| PSA Total | > 4.0 | Free PSA + Ratio | Yes |
| Urine Protein | >= 2+ | Urine Protein Creatinine Ratio | Yes |

---

## 10. TAT Configuration

Turn-around time per test — stored in `test_master.tat_hours`.

| Priority | TAT Multiplier | Example (standard 6h) |
|----------|---------------|----------------------|
| ROUTINE | 1.0× | 6 hours |
| URGENT | 0.5× | 3 hours |
| STAT | 0.25× | 1.5 hours |
| CRITICAL | 0.1× | 36 minutes |

---

## 11. Price Catalog

```sql
CREATE TABLE price_catalog (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id          UUID NOT NULL,
    test_id         UUID NOT NULL REFERENCES test_master(id),
    base_price      NUMERIC(10,2) NOT NULL,
    currency_code   VARCHAR(3) DEFAULT 'INR',
    effective_from  DATE NOT NULL,
    effective_to    DATE,
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (org_id, test_id, effective_from)
);
```

| test_code | test_name | base_price (INR) | effective_from |
|-----------|-----------|-----------------|----------------|
| CBC | Complete Blood Count | 350.00 | 2024-01-01 |
| LFT | Liver Function Test | 650.00 | 2024-01-01 |
| TSH | Thyroid Stimulating Hormone | 300.00 | 2024-01-01 |
| LIPID_PANEL | Lipid Profile | 550.00 | 2024-01-01 |

**API**: `GET /api/v1/prices` · `POST /api/v1/prices` · `PUT /api/v1/prices/{id}`

---

## 12. Users (Staff Accounts)

Users are managed in **Keycloak** and synced to the LIS `user_profile` table for application-specific data.

```sql
CREATE TABLE user_profile (
    id              UUID PRIMARY KEY,          -- Same as Keycloak user ID
    org_id          UUID NOT NULL,
    employee_code   VARCHAR(30),
    full_name       VARCHAR(200) NOT NULL,
    email           VARCHAR(200),
    phone           VARCHAR(20),
    designation     VARCHAR(100),
    department_id   UUID REFERENCES department(id),
    qualification   VARCHAR(200),
    license_number  VARCHAR(100),              -- Medical license for pathologists
    signature_url   VARCHAR(500),
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ
);
```

**API**: `GET /api/v1/users` · `POST /api/v1/users` · `PUT /api/v1/users/{id}`

---

## 13. Roles & Permissions

Roles managed in Keycloak realm. Custom permissions stored in:

```sql
CREATE TABLE custom_permission (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id          UUID NOT NULL,
    role_name       VARCHAR(50) NOT NULL,
    resource        VARCHAR(100) NOT NULL,     -- e.g. "test_master", "patient", "result"
    action          VARCHAR(20) NOT NULL,      -- CREATE, READ, UPDATE, DELETE, APPROVE
    is_allowed      BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now()
);
```

| Role | Typical Permissions |
|------|-------------------|
| ORG_ADMIN | Full access to all org resources |
| BRANCH_ADMIN | Branch-scoped admin access |
| PATHOLOGIST | Result approval, report authorization |
| LAB_TECHNICIAN | Sample processing, result entry |
| RECEPTIONIST | Patient registration, order entry, billing |
| PHLEBOTOMIST | Sample collection, barcode scanning |
| BILLING_STAFF | Invoice creation, payment collection |

---

## 14. Doctors (Referring Doctor Directory)

```sql
CREATE TABLE doctor (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id          UUID NOT NULL,
    doctor_code     VARCHAR(30),
    full_name       VARCHAR(200) NOT NULL,
    qualification   VARCHAR(200),
    specialization  VARCHAR(100),
    hospital_name   VARCHAR(200),
    phone           VARCHAR(20),
    email           VARCHAR(200),
    commission_pct  NUMERIC(5,2) DEFAULT 0,
    is_portal_user  BOOLEAN DEFAULT false,
    keycloak_user_id UUID,
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ
);
```

**API**: `GET /api/v1/doctors` · `POST /api/v1/doctors` · `PUT /api/v1/doctors/{id}`

---

## 15. Report Templates

```sql
CREATE TABLE report_template (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id              UUID NOT NULL,
    template_code       VARCHAR(30) NOT NULL,
    template_name       VARCHAR(200) NOT NULL,
    department_id       UUID REFERENCES department(id),
    layout_type         VARCHAR(50) NOT NULL,      -- From global report_template_layouts
    header_html         TEXT,
    footer_html         TEXT,
    body_template       TEXT,                       -- Thymeleaf / Jasper template
    css_styles          TEXT,
    page_size           VARCHAR(10) DEFAULT 'A4',
    orientation         VARCHAR(10) DEFAULT 'PORTRAIT',
    is_default          BOOLEAN DEFAULT false,
    is_active           BOOLEAN DEFAULT true,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ,
    UNIQUE (org_id, template_code)
);
```

**API**: `GET /api/v1/report-templates` · `POST /api/v1/report-templates` · `PUT /api/v1/report-templates/{id}`

---

## 16. Communication Templates (30+)

| Template Code | Channel | Event | Example |
|--------------|---------|-------|---------|
| REG_CONFIRM | SMS | Patient registered | "Dear {name}, your UHID is {uhid}..." |
| SAMPLE_COLLECTED | SMS | Sample collected | "Sample collected for {tests}. Report expected by {tat}." |
| REPORT_READY | SMS+Email | Report ready | "Your report for {tests} is ready. Download: {link}" |
| CRITICAL_ALERT | SMS+WhatsApp | Critical value | "CRITICAL: {param} = {value} for patient {name}." |
| PAYMENT_RECEIPT | Email | Payment received | "Payment of ₹{amount} received. Invoice: {invoice}." |
| DOCTOR_REPORT | Email | Report for doctor | "Report for your patient {name} is attached." |

---

## 17. Holiday Calendar

Organization-wide holidays that affect TAT calculations and sample scheduling.

**API**: `GET /api/v1/holidays` · `POST /api/v1/holidays` · `DELETE /api/v1/holidays/{id}`

---

## 18. Discount Schemes (~20)

```sql
CREATE TABLE discount_scheme (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id          UUID NOT NULL,
    scheme_code     VARCHAR(30) NOT NULL,
    scheme_name     VARCHAR(200) NOT NULL,
    discount_type   VARCHAR(20) NOT NULL,      -- PERCENTAGE, FLAT_AMOUNT
    discount_value  NUMERIC(10,2) NOT NULL,
    applicable_to   VARCHAR(30),               -- ALL, WALK_IN, CORPORATE, SENIOR_CITIZEN
    min_bill_amount NUMERIC(10,2) DEFAULT 0,
    valid_from      DATE,
    valid_to        DATE,
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (org_id, scheme_code)
);
```

---

## 19. Insurance Tariffs

Insurance-specific pricing per test, per insurer.

```sql
CREATE TABLE insurance_tariff (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id              UUID NOT NULL,
    insurance_provider_id UUID NOT NULL,       -- FK to global insurance_provider
    test_id             UUID NOT NULL REFERENCES test_master(id),
    tariff_price        NUMERIC(10,2) NOT NULL,
    effective_from      DATE NOT NULL,
    effective_to        DATE,
    is_active           BOOLEAN DEFAULT true,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ
);
```

---

## 20–21. QC Materials & Westgard Rules

### QC Materials (~50)

```sql
CREATE TABLE qc_material (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id          UUID NOT NULL,
    material_code   VARCHAR(30) NOT NULL,
    material_name   VARCHAR(200) NOT NULL,
    manufacturer    VARCHAR(200),
    lot_number      VARCHAR(100),
    expiry_date     DATE,
    level           VARCHAR(20),               -- LEVEL_1, LEVEL_2, LEVEL_3
    parameter_id    UUID REFERENCES parameter_master(id),
    target_value    NUMERIC(15,4),
    target_sd       NUMERIC(15,4),
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ
);
```

### Westgard Rules Config

| Rule Code | Description | Applies To |
|-----------|-------------|-----------|
| 1_2S | One control > 2 SD — Warning | All parameters |
| 1_3S | One control > 3 SD — Rejection | All parameters |
| 2_2S | Two consecutive controls > 2 SD same side | Systematic error |
| R_4S | Range of two controls > 4 SD | Random error |
| 4_1S | Four consecutive controls > 1 SD same side | Trend |
| 10_X | Ten consecutive controls same side of mean | Shift |

---

## 22. Delta Check Rules

Maximum allowed percentage change between current and previous results.

| Parameter | Max Delta (%) | Time Window | Action |
|-----------|--------------|-------------|--------|
| Hemoglobin | 20% | 7 days | Flag for review |
| Creatinine | 50% | 30 days | Flag for review |
| Potassium | 25% | 3 days | Flag + alert |
| Sodium | 10% | 3 days | Flag + alert |

---

## 23. Critical Value Ranges

Stored in `reference_range.critical_low` and `reference_range.critical_high`. When a result falls in this range, the system triggers immediate notification via `CRITICAL_ALERT` communication template.

---

## 24. Auto-Validation Rules (~200)

```sql
CREATE TABLE auto_validation_rule (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id              UUID NOT NULL,
    parameter_id        UUID NOT NULL REFERENCES parameter_master(id),
    rule_type           VARCHAR(30) NOT NULL,  -- RANGE, DELTA, QC_STATUS, REPEAT
    condition_json      JSONB NOT NULL,
    action_on_fail      VARCHAR(20) DEFAULT 'HOLD_FOR_REVIEW',
    priority            INT DEFAULT 0,
    is_active           BOOLEAN DEFAULT true,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ
);
```

**Example conditions (JSONB)**:
- `{"type": "RANGE", "low": 0, "high": 300}` — result must be in plausible range
- `{"type": "DELTA", "max_pct": 20, "window_days": 7}` — delta check
- `{"type": "QC_STATUS", "must_be": "PASSED"}` — QC must be passed today
- `{"type": "LINEARITY", "min": 0, "max": 500}` — within instrument linearity

---

## 25. Home Collection Zones

Service area definitions for home sample collection.

**API**: `GET /api/v1/collection-zones` · `POST /api/v1/collection-zones`

---

## 26. Partner Labs (~20)

```sql
CREATE TABLE partner_lab (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id          UUID NOT NULL,
    lab_code        VARCHAR(30) NOT NULL,
    lab_name        VARCHAR(200) NOT NULL,
    contact_person  VARCHAR(200),
    phone           VARCHAR(20),
    email           VARCHAR(200),
    address         TEXT,
    integration_type VARCHAR(20),              -- MANUAL, API, HL7, EMAIL
    api_endpoint    VARCHAR(500),
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (org_id, lab_code)
);
```

**API**: `GET /api/v1/partner-labs` · `POST /api/v1/partner-labs` · `PUT /api/v1/partner-labs/{id}`

---

## Notes

- All queries MUST filter by `org_id` (and `branch_id` where applicable) — enforced by `BranchContextHolder`.
- Soft delete: `is_deleted = true`, `deleted_at = now()` — records are never physically deleted.
- Audit trail: `created_by`, `updated_by` track the Keycloak user UUID.
- All entities extend `BaseEntity` in the Java domain model.
- Price and tariff tables use `effective_from` / `effective_to` for temporal pricing.
- Cross-references to L1 Global Masters use code-based lookups (e.g., `sample_type_code`, `unit_code`).
