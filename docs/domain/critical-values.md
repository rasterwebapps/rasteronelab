# Critical Values — RasterOneLab LIS

## Definition
Critical values (panic values) are test results so far outside the reference range that they represent a life-threatening situation requiring **immediate clinical action**.

## Critical Value List

### Hematology

| Test | Critical Low | Critical High |
|------|-------------|--------------|
| Hemoglobin | < 7.0 g/dL | > 20.0 g/dL |
| WBC | < 2,000/μL | > 30,000/μL |
| Platelets | < 50,000/μL | > 1,000,000/μL |
| Prothrombin Time | - | > 30 seconds |
| INR | - | > 3.0 |
| aPTT | - | > 70 seconds |

### Biochemistry

| Test | Critical Low | Critical High |
|------|-------------|--------------|
| Glucose | < 50 mg/dL | > 500 mg/dL |
| Potassium (K+) | < 2.5 mEq/L | > 6.5 mEq/L |
| Sodium (Na+) | < 120 mEq/L | > 160 mEq/L |
| Calcium | < 6.0 mg/dL | > 13.0 mg/dL |
| Creatinine | - | > 10.0 mg/dL |
| BUN/Urea | - | > 100 mg/dL |
| pH (ABG) | < 7.20 | > 7.60 |
| pO2 | < 40 mmHg | - |
| pCO2 | < 20 mmHg | > 70 mmHg |
| Troponin | - | Any elevated |
| Ammonia | - | > 150 μmol/L |

### Microbiology

| Result | Critical Action |
|--------|----------------|
| Positive blood culture | Immediate physician notification |
| Positive CSF culture | Immediate physician notification |
| AFB positive smear | Immediate physician notification |

## Critical Value Workflow (LIS Implementation)

```
Step 1: Detection
  → Auto-detection when result saved with value outside critical range
  → Flag set: is_critical = TRUE
  → Critical value log entry created (status: DETECTED)

Step 2: Pathologist Verification
  → Alert shown on pathologist dashboard (red banner)
  → WebSocket push notification
  → SMS to on-duty pathologist if not acknowledged in 5 minutes

Step 3: Physician Notification (MANDATORY before authorization)
  → Pathologist must record: physician name, time called, method (phone/WhatsApp)
  → Read-back confirmation: physician reads back the value

Step 4: Documentation in LIS
  → critical_value_log updated with:
    - physician_notified_name
    - notification_method
    - notified_at
    - read_back_confirmed = TRUE

Step 5: Result Authorization
  → Only then can result be authorized by pathologist
  → System blocks authorization if critical_notified_at is null

Step 6: Report Release
  → Report marked "CRITICAL" in header
  → Delivered urgently
  → Physician copy auto-sent via email/WhatsApp
```

## Critical Value Log Schema

```sql
CREATE TABLE critical_value_log (
    id                      UUID PRIMARY KEY,
    branch_id               UUID NOT NULL,
    result_id               UUID NOT NULL,
    patient_id              UUID NOT NULL,
    parameter_code          VARCHAR(50),
    critical_value          NUMERIC(15,6),
    flag                    VARCHAR(10),  -- HH or LL
    detected_at             TIMESTAMPTZ NOT NULL,
    physician_notified_name VARCHAR(200),
    notification_method     VARCHAR(50),  -- PHONE, WHATSAPP, IN_PERSON
    notified_at             TIMESTAMPTZ,
    read_back_confirmed     BOOLEAN DEFAULT FALSE,
    acknowledged_at         TIMESTAMPTZ,
    notes                   TEXT
);
```

## Regulatory Requirement

NABL ISO 15189 clause 5.8.4 requires:
- Documented process for critical values
- Immediate notification to authorized person
- Record of notification including: date, time, staff responsible, to whom notified
- Read-back verification documented
