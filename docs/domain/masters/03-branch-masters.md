# L3 Branch Masters — RasterOneLab LIS

Managed by: **BRANCH_ADMIN**
Scope: **Per branch** (specific to a single lab location)

> All tables include `branch_id UUID NOT NULL` and inherit from `BaseEntity`.
> API endpoints follow the pattern `/api/v1/{resource}` and require the `X-Branch-Id` header.
> Branch masters override or extend L2 Organization Masters for a specific location.

---

## 1. Branch Profile

```sql
CREATE TABLE branch (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id              UUID NOT NULL REFERENCES organization(id),
    branch_code         VARCHAR(20) NOT NULL,
    branch_name         VARCHAR(200) NOT NULL,
    branch_type         VARCHAR(30) NOT NULL,      -- MAIN_LAB, SATELLITE, COLLECTION_CENTER, FRANCHISE
    address_line1       VARCHAR(200),
    address_line2       VARCHAR(200),
    city                VARCHAR(100),
    state               VARCHAR(100),
    country_code        VARCHAR(3),
    postal_code         VARCHAR(20),
    phone               VARCHAR(20),
    email               VARCHAR(100),
    latitude            NUMERIC(10,7),
    longitude           NUMERIC(10,7),
    license_number      VARCHAR(100),
    nabl_number         VARCHAR(100),
    manager_user_id     UUID,
    parent_branch_id    UUID REFERENCES branch(id),   -- For satellites
    is_active           BOOLEAN DEFAULT true,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ,
    UNIQUE (org_id, branch_code)
);
```

| branch_code | branch_name | branch_type | city |
|-------------|-------------|-------------|------|
| HQ-MUM | Mumbai Main Lab | MAIN_LAB | Mumbai |
| SAT-PUNE | Pune Satellite | SATELLITE | Pune |
| CC-THANE | Thane Collection Center | COLLECTION_CENTER | Thane |

**API**: `GET /api/v1/branches` · `POST /api/v1/branches` · `PUT /api/v1/branches/{id}`

---

## 2. Branch Working Hours

```sql
CREATE TABLE branch_working_hours (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id       UUID NOT NULL REFERENCES branch(id),
    day_of_week     INT NOT NULL,              -- 1=Monday ... 7=Sunday
    open_time       TIME NOT NULL,
    close_time      TIME NOT NULL,
    is_open         BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (branch_id, day_of_week)
);
```

| Day | Open | Close | Open? |
|-----|------|-------|-------|
| Monday | 07:00 | 21:00 | Yes |
| Tuesday | 07:00 | 21:00 | Yes |
| Sunday | 08:00 | 14:00 | Yes |

**API**: `GET /api/v1/branches/{id}/working-hours` · `PUT /api/v1/branches/{id}/working-hours`

---

## 3. Branch Holidays

Branch-specific holidays that override or supplement the organization holiday calendar.

```sql
CREATE TABLE branch_holiday (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id       UUID NOT NULL REFERENCES branch(id),
    holiday_date    DATE NOT NULL,
    holiday_name    VARCHAR(200),
    is_half_day     BOOLEAN DEFAULT false,
    half_day_close  TIME,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (branch_id, holiday_date)
);
```

**API**: `GET /api/v1/branches/{id}/holidays` · `POST /api/v1/branches/{id}/holidays`

---

## 4. Test Availability

Controls which tests from the org test catalog are offered at this branch.

```sql
CREATE TABLE branch_test_availability (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id       UUID NOT NULL REFERENCES branch(id),
    test_id         UUID NOT NULL REFERENCES test_master(id),
    is_available    BOOLEAN DEFAULT true,
    outsource_to    UUID REFERENCES partner_lab(id),  -- If not done in-house
    tat_override_hours INT,
    effective_from  DATE,
    effective_to    DATE,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (branch_id, test_id)
);
```

| test_code | available | outsource_to | tat_override |
|-----------|-----------|-------------|-------------|
| CBC | Yes | — | — |
| LFT | Yes | — | — |
| HCV_RNA | No | Partner Lab X | 72h |
| KARYOTYPE | No | Partner Lab Y | 168h |

**API**: `GET /api/v1/branches/{id}/test-availability` · `PUT /api/v1/branches/{id}/test-availability`

---

## 5. Price Overrides

Branch-specific price adjustments that override the organization price catalog.

```sql
CREATE TABLE price_override (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id       UUID NOT NULL REFERENCES branch(id),
    test_id         UUID NOT NULL REFERENCES test_master(id),
    override_price  NUMERIC(10,2) NOT NULL,
    override_type   VARCHAR(20) NOT NULL,      -- FIXED, PERCENTAGE_INCREASE, PERCENTAGE_DECREASE
    effective_from  DATE NOT NULL,
    effective_to    DATE,
    reason          VARCHAR(200),
    approved_by     UUID,
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (branch_id, test_id, effective_from)
);
```

**API**: `GET /api/v1/branches/{id}/price-overrides` · `POST /api/v1/branches/{id}/price-overrides`

---

## 6. Instruments

```sql
CREATE TABLE instrument (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL REFERENCES branch(id),
    instrument_code     VARCHAR(30) NOT NULL,
    instrument_name     VARCHAR(200) NOT NULL,
    manufacturer        VARCHAR(200),
    model               VARCHAR(100),
    serial_number       VARCHAR(100),
    instrument_type_id  UUID,                      -- FK to global lab_equipment_type
    connection_type     VARCHAR(20),               -- SERIAL, TCP, FILE, MANUAL
    connection_config   JSONB,                     -- {"host":"192.168.1.100","port":9100}
    department_id       UUID REFERENCES department(id),
    location            VARCHAR(100),
    installation_date   DATE,
    last_calibration    DATE,
    next_calibration    DATE,
    warranty_expiry     DATE,
    is_active           BOOLEAN DEFAULT true,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ,
    UNIQUE (branch_id, instrument_code)
);
```

| instrument_code | name | model | connection | department |
|----------------|------|-------|-----------|-----------|
| SYSMEX-XN1000 | Sysmex XN-1000 | XN-1000 | TCP | HEMA |
| VITROS-5600 | Ortho Vitros 5600 | 5600 | TCP | BIOCHEM |
| VITEK-2 | bioMérieux VITEK 2 | VITEK 2 | TCP | MICRO |

**API**: `GET /api/v1/instruments` · `POST /api/v1/instruments` · `PUT /api/v1/instruments/{id}`

---

## 7. Instrument-Test Mapping

```sql
CREATE TABLE instrument_test_mapping (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id       UUID NOT NULL REFERENCES branch(id),
    instrument_id   UUID NOT NULL REFERENCES instrument(id),
    test_id         UUID NOT NULL REFERENCES test_master(id),
    is_primary      BOOLEAN DEFAULT true,      -- Primary instrument for this test
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (branch_id, instrument_id, test_id)
);
```

**API**: `GET /api/v1/instruments/{id}/tests` · `POST /api/v1/instruments/{id}/tests`

---

## 8. ASTM Parameter Mapping

Maps instrument-specific result codes to LIS parameter codes for ASTM E1381/E1394 protocol communication.

```sql
CREATE TABLE astm_parameter_mapping (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL REFERENCES branch(id),
    instrument_id       UUID NOT NULL REFERENCES instrument(id),
    instrument_code     VARCHAR(50) NOT NULL,      -- Code sent by instrument
    instrument_param_name VARCHAR(200),
    parameter_id        UUID NOT NULL REFERENCES parameter_master(id),
    unit_conversion     NUMERIC(15,8),             -- Conversion factor if units differ
    is_active           BOOLEAN DEFAULT true,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ,
    UNIQUE (branch_id, instrument_id, instrument_code)
);
```

| Instrument | Instrument Code | LIS Parameter | Conversion |
|-----------|----------------|---------------|-----------|
| Sysmex XN-1000 | WBC | WBC | 1.0 |
| Sysmex XN-1000 | HGB | HGB | 1.0 |
| Vitros 5600 | GLUC | GLUCOSE_FASTING | 1.0 |
| Vitros 5600 | CREA | CREAT | 1.0 |

**API**: `GET /api/v1/instruments/{id}/astm-mappings` · `POST /api/v1/instruments/{id}/astm-mappings`

---

## 9. Instrument Calibration Log

```sql
CREATE TABLE calibration_log (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL REFERENCES branch(id),
    instrument_id       UUID NOT NULL REFERENCES instrument(id),
    calibration_date    TIMESTAMPTZ NOT NULL,
    calibration_type    VARCHAR(30),               -- INITIAL, ROUTINE, POST_MAINTENANCE
    calibrator_lot      VARCHAR(100),
    performed_by        UUID NOT NULL,
    result_status       VARCHAR(20) NOT NULL,      -- PASSED, FAILED, CONDITIONAL
    remarks             TEXT,
    next_calibration    DATE,
    document_url        VARCHAR(500),
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ
);
```

**API**: `GET /api/v1/instruments/{id}/calibrations` · `POST /api/v1/instruments/{id}/calibrations`

---

## 10–12. QC at Branch Level

### 10. QC Materials at Branch

Links org-level QC materials to the specific branch where they are in use — tracks lot assignment and availability.

### 11. QC Frequency

| Parameter Group | QC Frequency | Levels |
|----------------|-------------|--------|
| Biochemistry | Every 8 hours | L1 + L2 |
| Hematology | Every 8 hours | L1 + L2 + L3 |
| Immunoassay | Every 12 hours | L1 + L2 |
| Coagulation | Every 8 hours | L1 + L2 |

### 12. Westgard Rules Override

Branch-specific overrides to the org Westgard configuration — e.g., stricter rules for NABL-accredited branches.

---

## 13. Reagents

```sql
CREATE TABLE reagent (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL REFERENCES branch(id),
    reagent_code        VARCHAR(30) NOT NULL,
    reagent_name        VARCHAR(200) NOT NULL,
    manufacturer        VARCHAR(200),
    catalog_number      VARCHAR(100),
    lot_number          VARCHAR(100),
    expiry_date         DATE,
    instrument_id       UUID REFERENCES instrument(id),
    tests_per_kit       INT,
    storage_temp        VARCHAR(30),               -- ROOM, REFRIGERATED, FROZEN
    is_active           BOOLEAN DEFAULT true,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ,
    UNIQUE (branch_id, reagent_code, lot_number)
);
```

**API**: `GET /api/v1/reagents` · `POST /api/v1/reagents` · `PUT /api/v1/reagents/{id}`

---

## 14. Inventory Stock

```sql
CREATE TABLE inventory_stock (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL REFERENCES branch(id),
    reagent_id          UUID NOT NULL REFERENCES reagent(id),
    storage_location_id UUID REFERENCES storage_location(id),
    quantity_on_hand    INT NOT NULL DEFAULT 0,
    quantity_reserved   INT DEFAULT 0,
    unit_of_measure     VARCHAR(20),
    last_counted_at     TIMESTAMPTZ,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ,
    UNIQUE (branch_id, reagent_id, storage_location_id)
);
```

**API**: `GET /api/v1/inventory` · `PUT /api/v1/inventory/{id}`

---

## 15. Reorder Rules

```sql
CREATE TABLE reorder_rule (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL REFERENCES branch(id),
    reagent_id          UUID NOT NULL REFERENCES reagent(id),
    min_stock_level     INT NOT NULL,
    max_stock_level     INT NOT NULL,
    reorder_quantity    INT NOT NULL,
    lead_time_days      INT DEFAULT 7,
    auto_reorder        BOOLEAN DEFAULT false,
    supplier_id         UUID REFERENCES supplier(id),
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ,
    UNIQUE (branch_id, reagent_id)
);
```

**API**: `GET /api/v1/reorder-rules` · `POST /api/v1/reorder-rules` · `PUT /api/v1/reorder-rules/{id}`

---

## 16. Suppliers

```sql
CREATE TABLE supplier (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id       UUID NOT NULL REFERENCES branch(id),
    supplier_code   VARCHAR(30) NOT NULL,
    supplier_name   VARCHAR(200) NOT NULL,
    contact_person  VARCHAR(200),
    phone           VARCHAR(20),
    email           VARCHAR(200),
    address         TEXT,
    gst_number      VARCHAR(50),
    payment_terms   VARCHAR(100),
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (branch_id, supplier_code)
);
```

**API**: `GET /api/v1/suppliers` · `POST /api/v1/suppliers` · `PUT /api/v1/suppliers/{id}`

---

## 17. Storage Locations

```sql
CREATE TABLE storage_location (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id       UUID NOT NULL REFERENCES branch(id),
    location_code   VARCHAR(30) NOT NULL,
    location_name   VARCHAR(200) NOT NULL,
    location_type   VARCHAR(30),               -- ROOM_TEMP, REFRIGERATOR, FREEZER, DEEP_FREEZER
    temperature_min NUMERIC(5,2),
    temperature_max NUMERIC(5,2),
    capacity        INT,
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (branch_id, location_code)
);
```

---

## 18. Collection Slots

Time slots available for home sample collection.

```sql
CREATE TABLE collection_slot (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id       UUID NOT NULL REFERENCES branch(id),
    slot_date       DATE,                      -- null = recurring template
    day_of_week     INT,                       -- 1=Mon..7=Sun for templates
    start_time      TIME NOT NULL,
    end_time        TIME NOT NULL,
    max_bookings    INT DEFAULT 5,
    zone_id         UUID,
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ
);
```

**API**: `GET /api/v1/collection-slots` · `POST /api/v1/collection-slots`

---

## 19. Phlebotomist Routes

Defines geographic areas assigned to phlebotomists for home collection.

**API**: `GET /api/v1/phlebotomist-routes` · `POST /api/v1/phlebotomist-routes`

---

## 20. Report Header/Footer

Branch-specific PDF report branding — overrides the organization-level report template header/footer.

| Column | Type | Example |
|--------|------|---------|
| branch_id | UUID | (branch ref) |
| header_html | TEXT | Branch logo + address + NABL number |
| footer_html | TEXT | "Report generated at {branch_name}" |
| watermark_text | VARCHAR | "CONFIDENTIAL" |
| signatory_text | VARCHAR | "Dr. X, MD Pathology" |

---

## 21. Digital Signatures

```sql
CREATE TABLE digital_signature (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id       UUID NOT NULL REFERENCES branch(id),
    user_id         UUID NOT NULL,
    designation     VARCHAR(100),
    signature_image_url VARCHAR(500),          -- Stored in MinIO
    department_id   UUID REFERENCES department(id),
    is_default      BOOLEAN DEFAULT false,
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ
);
```

**API**: `GET /api/v1/digital-signatures` · `POST /api/v1/digital-signatures`

---

## 22. Number Series

Configurable prefix/suffix patterns for auto-generated IDs per branch.

```sql
CREATE TABLE number_series (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id       UUID NOT NULL REFERENCES branch(id),
    entity_type     VARCHAR(30) NOT NULL,      -- UHID, ORDER, SAMPLE, INVOICE, RECEIPT
    prefix          VARCHAR(20),               -- e.g. "MUM-"
    suffix          VARCHAR(20),
    next_number     BIGINT DEFAULT 1,
    pad_length      INT DEFAULT 6,             -- MUM-000001
    reset_frequency VARCHAR(20),               -- NEVER, YEARLY, MONTHLY, DAILY
    last_reset_date DATE,
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (branch_id, entity_type)
);
```

| entity_type | prefix | pad_length | example |
|-------------|--------|-----------|---------|
| UHID | MUM- | 6 | MUM-000001 |
| ORDER | ORD-MUM- | 8 | ORD-MUM-00000001 |
| SAMPLE | SMP-MUM- | 8 | SMP-MUM-00000001 |
| INVOICE | INV-MUM- | 8 | INV-MUM-00000001 |

**API**: `GET /api/v1/number-series` · `PUT /api/v1/number-series/{id}`

---

## 23. Users at Branch

Maps organization-level users to specific branches.

```sql
CREATE TABLE branch_user (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id       UUID NOT NULL REFERENCES branch(id),
    user_id         UUID NOT NULL REFERENCES user_profile(id),
    role_at_branch  VARCHAR(50) NOT NULL,
    is_primary_branch BOOLEAN DEFAULT false,
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (branch_id, user_id)
);
```

**API**: `GET /api/v1/branches/{id}/users` · `POST /api/v1/branches/{id}/users`

---

## 24. User Shift Timings

```sql
CREATE TABLE user_shift (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id       UUID NOT NULL REFERENCES branch(id),
    user_id         UUID NOT NULL,
    shift_name      VARCHAR(50),               -- MORNING, AFTERNOON, NIGHT
    start_time      TIME NOT NULL,
    end_time        TIME NOT NULL,
    effective_from  DATE,
    effective_to    DATE,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ
);
```

---

## 25. TAT Overrides

Branch-specific turn-around time adjustments stored in `branch_test_availability.tat_override_hours`.

---

## 26. Critical Alert Contacts

```sql
CREATE TABLE critical_alert_contact (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id       UUID NOT NULL REFERENCES branch(id),
    contact_name    VARCHAR(200) NOT NULL,
    role            VARCHAR(50),               -- PATHOLOGIST, BRANCH_ADMIN, HOD
    phone           VARCHAR(20),
    email           VARCHAR(200),
    whatsapp        VARCHAR(20),
    notify_channels VARCHAR(100),              -- SMS,EMAIL,WHATSAPP
    priority        INT DEFAULT 0,
    is_active       BOOLEAN DEFAULT true,
    created_at      TIMESTAMPTZ DEFAULT now(),
    updated_at      TIMESTAMPTZ DEFAULT now(),
    created_by      UUID,
    updated_by      UUID,
    is_deleted      BOOLEAN DEFAULT false,
    deleted_at      TIMESTAMPTZ
);
```

**API**: `GET /api/v1/critical-alert-contacts` · `POST /api/v1/critical-alert-contacts`

---

## 27. Partner Lab Integration

Branch-level configuration for send-out lab connections.

| Column | Type | Example |
|--------|------|---------|
| branch_id | UUID | (branch ref) |
| partner_lab_id | UUID | FK to partner_lab |
| integration_type | VARCHAR | API, HL7, EMAIL, MANUAL |
| api_endpoint | VARCHAR | https://partner.api/v1/orders |
| credentials_vault_key | VARCHAR | partner-lab-x-api-key |
| auto_send | BOOLEAN | true |
| result_import_mode | VARCHAR | AUTO, MANUAL_REVIEW |

**API**: `GET /api/v1/partner-lab-integrations` · `POST /api/v1/partner-lab-integrations`

---

## Notes

- Branch masters are **always** filtered by `branch_id` via `BranchContextHolder`.
- Collection centers (`branch_type = COLLECTION_CENTER`) inherit test availability and pricing from their parent `MAIN_LAB` unless overridden.
- Instruments and reagents are branch-specific — they physically exist at a single location.
- Number series ensures unique IDs per branch with configurable prefixes.
- All branch data is scoped: a `BRANCH_ADMIN` can only manage their assigned branch(es).
- Cross-branch visibility requires `ORG_ADMIN` or `SUPER_ADMIN` role.
