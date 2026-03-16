# L5 External Masters — RasterOneLab LIS

Managed by: **DOCTOR / PATIENT** (via portal)
Scope: **Portal/external users** (preferences and relationships managed outside the core lab workflow)

> External masters store configuration and relationships for portal users (doctors and patients).
> These tables are updated via the Doctor Portal and Patient Portal frontends.
> All tables include `branch_id UUID NOT NULL` and inherit from `BaseEntity`.

---

## 1. Doctor Portal Preferences

Stores per-doctor settings for the referring doctor portal experience.

```sql
CREATE TABLE doctor_portal_preference (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id               UUID NOT NULL,
    doctor_id               UUID NOT NULL REFERENCES doctor(id),
    default_branch_id       UUID REFERENCES branch(id),
    preferred_language      VARCHAR(10) DEFAULT 'en',
    report_delivery_mode    VARCHAR(30) DEFAULT 'EMAIL',   -- EMAIL, PORTAL, WHATSAPP, SMS
    auto_download_reports   BOOLEAN DEFAULT false,
    notification_new_report BOOLEAN DEFAULT true,
    notification_critical   BOOLEAN DEFAULT true,
    notification_sms        BOOLEAN DEFAULT false,
    notification_email      BOOLEAN DEFAULT true,
    notification_whatsapp   BOOLEAN DEFAULT false,
    dashboard_layout        VARCHAR(20) DEFAULT 'DEFAULT', -- DEFAULT, COMPACT, DETAILED
    default_test_panel_ids  JSONB,                         -- Frequently ordered panels
    letterhead_url          VARCHAR(500),                   -- Doctor's own letterhead for reports
    is_active               BOOLEAN DEFAULT true,
    created_at              TIMESTAMPTZ DEFAULT now(),
    updated_at              TIMESTAMPTZ DEFAULT now(),
    created_by              UUID,
    updated_by              UUID,
    is_deleted              BOOLEAN DEFAULT false,
    deleted_at              TIMESTAMPTZ,
    UNIQUE (doctor_id)
);
```

| Setting | Type | Default | Description |
|---------|------|---------|-------------|
| report_delivery_mode | VARCHAR | EMAIL | How reports are sent to doctor |
| auto_download_reports | BOOLEAN | false | Auto-download PDF when report is ready |
| notification_critical | BOOLEAN | true | Receive alerts for critical values |
| default_test_panel_ids | JSONB | null | Pre-selected panels for quick ordering |
| dashboard_layout | VARCHAR | DEFAULT | Portal dashboard view preference |
| letterhead_url | VARCHAR | null | Custom letterhead for co-branded reports |

**API**: `GET /api/v1/doctor-preferences` · `PUT /api/v1/doctor-preferences`

---

## 2. Patient Portal Preferences

Stores per-patient settings for the patient-facing portal.

```sql
CREATE TABLE patient_portal_preference (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id               UUID NOT NULL,
    patient_id              UUID NOT NULL REFERENCES patient(id),
    preferred_language      VARCHAR(10) DEFAULT 'en',
    report_access_pin       VARCHAR(100),                  -- Hashed PIN for report download
    notification_report_ready BOOLEAN DEFAULT true,
    notification_sms        BOOLEAN DEFAULT true,
    notification_email      BOOLEAN DEFAULT true,
    notification_whatsapp   BOOLEAN DEFAULT false,
    share_with_doctor_ids   JSONB,                         -- Auto-share reports with these doctors
    preferred_branch_id     UUID REFERENCES branch(id),
    preferred_collection_time VARCHAR(20),                  -- MORNING, AFTERNOON, EVENING
    home_address            TEXT,                           -- For home collection
    home_collection_lat     NUMERIC(10,7),
    home_collection_lng     NUMERIC(10,7),
    is_active               BOOLEAN DEFAULT true,
    created_at              TIMESTAMPTZ DEFAULT now(),
    updated_at              TIMESTAMPTZ DEFAULT now(),
    created_by              UUID,
    updated_by              UUID,
    is_deleted              BOOLEAN DEFAULT false,
    deleted_at              TIMESTAMPTZ,
    UNIQUE (patient_id)
);
```

| Setting | Type | Default | Description |
|---------|------|---------|-------------|
| preferred_language | VARCHAR | en | Portal UI language |
| notification_report_ready | BOOLEAN | true | Notify when report is available |
| share_with_doctor_ids | JSONB | null | Auto-share new reports with listed doctors |
| preferred_collection_time | VARCHAR | null | Preferred time slot for home collection |
| home_address | TEXT | null | Default address for home collection booking |

**API**: `GET /api/v1/patient-preferences` · `PUT /api/v1/patient-preferences`

---

## 3. Patient Consent Records

Tracks patient consent for data sharing, communication, and special procedures. Required for GDPR/data protection compliance.

```sql
CREATE TABLE patient_consent (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL,
    patient_id          UUID NOT NULL REFERENCES patient(id),
    consent_type        VARCHAR(50) NOT NULL,      -- DATA_SHARING, SMS_NOTIFICATION, EMAIL_NOTIFICATION,
                                                   -- WHATSAPP, RESEARCH_USE, HIV_TESTING, GENETIC_TESTING
    is_granted          BOOLEAN NOT NULL,
    granted_at          TIMESTAMPTZ,
    revoked_at          TIMESTAMPTZ,
    consent_method      VARCHAR(30),               -- WRITTEN, DIGITAL, VERBAL
    document_url        VARCHAR(500),              -- Scanned consent form in MinIO
    witness_name        VARCHAR(200),
    recorded_by         UUID NOT NULL,             -- Staff who recorded the consent
    ip_address          VARCHAR(45),               -- For digital consent
    valid_until         DATE,
    remarks             TEXT,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ
);

CREATE INDEX idx_patient_consent_patient ON patient_consent (patient_id, consent_type);
```

| consent_type | Description | Default | Revocable |
|-------------|-------------|---------|-----------|
| DATA_SHARING | Share reports with referring doctor | Granted | Yes |
| SMS_NOTIFICATION | Send SMS updates | Granted | Yes |
| EMAIL_NOTIFICATION | Send email updates | Granted | Yes |
| WHATSAPP | Send WhatsApp messages | Not granted | Yes |
| RESEARCH_USE | Use anonymized data for research | Not granted | Yes |
| HIV_TESTING | Consent for HIV testing | Must be explicit | Yes |
| GENETIC_TESTING | Consent for genetic/genomic tests | Must be explicit | Yes |

**API**: `GET /api/v1/patient-consents` · `POST /api/v1/patient-consents` · `PUT /api/v1/patient-consents/{id}`

---

## 4. Patient Family Linking

Links patients into family groups for family history tracking and bundled billing.

```sql
CREATE TABLE patient_family_link (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL,
    family_group_id     UUID NOT NULL,             -- Shared UUID for the family group
    patient_id          UUID NOT NULL REFERENCES patient(id),
    relationship        VARCHAR(30) NOT NULL,      -- SELF, SPOUSE, CHILD, PARENT, SIBLING, OTHER
    is_primary_member   BOOLEAN DEFAULT false,     -- Head of family for billing
    linked_by           UUID,                      -- Staff or patient who created the link
    linked_at           TIMESTAMPTZ DEFAULT now(),
    is_active           BOOLEAN DEFAULT true,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ,
    UNIQUE (family_group_id, patient_id)
);

CREATE INDEX idx_family_link_patient ON patient_family_link (patient_id);
CREATE INDEX idx_family_link_group ON patient_family_link (family_group_id);
```

| family_group_id | patient | relationship | primary |
|----------------|---------|-------------|---------|
| FAM-001 | Rajesh Kumar | SELF | Yes |
| FAM-001 | Priya Kumar | SPOUSE | No |
| FAM-001 | Arjun Kumar | CHILD | No |

**Use cases**:
- View family history across linked patients
- Consolidated family billing and discount schemes
- Auto-suggest tests based on family medical history
- Insurance family coverage management

**API**: `GET /api/v1/patient-family-links` · `POST /api/v1/patient-family-links` · `DELETE /api/v1/patient-family-links/{id}`

---

## 5. Doctor-Patient Mapping

Tracks the referring relationship between doctors and patients for report routing, analytics, and commission tracking.

```sql
CREATE TABLE doctor_patient_mapping (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id           UUID NOT NULL,
    doctor_id           UUID NOT NULL REFERENCES doctor(id),
    patient_id          UUID NOT NULL REFERENCES patient(id),
    relationship_type   VARCHAR(30) DEFAULT 'REFERRING',  -- REFERRING, PRIMARY_CARE, SPECIALIST
    first_referral_date DATE,
    last_visit_date     DATE,
    total_referrals     INT DEFAULT 1,
    is_active           BOOLEAN DEFAULT true,
    created_at          TIMESTAMPTZ DEFAULT now(),
    updated_at          TIMESTAMPTZ DEFAULT now(),
    created_by          UUID,
    updated_by          UUID,
    is_deleted          BOOLEAN DEFAULT false,
    deleted_at          TIMESTAMPTZ,
    UNIQUE (doctor_id, patient_id)
);

CREATE INDEX idx_doctor_patient_doctor ON doctor_patient_mapping (doctor_id);
CREATE INDEX idx_doctor_patient_patient ON doctor_patient_mapping (patient_id);
```

| doctor | patient | type | referrals | last_visit |
|--------|---------|------|-----------|-----------|
| Dr. Sharma | Rajesh Kumar | REFERRING | 5 | 2024-03-15 |
| Dr. Sharma | Priya Kumar | REFERRING | 2 | 2024-02-20 |
| Dr. Patel | Rajesh Kumar | SPECIALIST | 1 | 2024-01-10 |

**Use cases**:
- Auto-populate referring doctor on repeat visits
- Doctor portal: view all referred patients and their reports
- Commission calculation for referring doctors
- Analytics: referral patterns, doctor-wise revenue

**API**: `GET /api/v1/doctor-patient-mappings` · `POST /api/v1/doctor-patient-mappings`

---

## Portal Integration Architecture

```
Doctor Portal (Angular)          Patient Portal (Angular)
       │                                │
       ▼                                ▼
  /api/v1/doctor-*              /api/v1/patient-*
       │                                │
       ▼                                ▼
  Keycloak Auth                   Keycloak Auth
  (DOCTOR role)                   (PATIENT role)
       │                                │
       └────────────┬───────────────────┘
                    ▼
            Spring Gateway
                    │
                    ▼
         lis-integration module
         (portal REST controllers)
                    │
                    ▼
         PostgreSQL (branch_id scoped)
```

---

## Notes

- External masters are managed by portal users, but **created/seeded by internal staff** (e.g., receptionist records initial patient consent during registration).
- All tables are filtered by `branch_id` via `BranchContextHolder`.
- Patient consent records are **never hard-deleted** — revocation sets `revoked_at` timestamp and `is_granted = false`.
- Doctor preferences are tied to the `doctor` record from L2 Organization Masters.
- Patient family links use a shared `family_group_id` (UUID) to group members — no separate family entity table.
- Doctor-patient mappings are auto-created on first order referral and updated on subsequent visits.
- Portal APIs use Keycloak tokens with `DOCTOR` or `PATIENT` realm roles — users can only access their own data.
- Audit trail (`created_by`, `updated_by`) tracks who made changes, even when the change originates from a portal user.
