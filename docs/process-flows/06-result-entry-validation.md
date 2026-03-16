# Process Flow: Result Entry & Validation — RasterOneLab LIS

## Trigger
Sample accepted at lab and processing complete (manual or instrument upload).

## Flow

```
START
  │
  ▼
Open department worklist
  │
  ▼
Select sample / order from worklist
  │
  ▼
Enter results (varies by department)
  │
  ├─ Numeric entry (Biochemistry, Hematology)
  ├─ Narrative/text (Histopathology)
  ├─ Culture + Antibiogram (Microbiology)
  ├─ Semi-quantitative (Urinalysis)
  └─ Qualitative (Serology: Reactive/Non-reactive)
  │
  ▼
Auto-calculate derived parameters
  │
  ▼
Auto-flag abnormal results (Normal / High / Low / Critical)
  │
  ▼
Delta check (compare with patient's previous result)
  │
  ▼
Auto-validation rules check
  │
  ├─ Passes auto-validation ──► Status → ENTERED (auto-verified)
  │
  └─ Fails auto-validation ──► Requires manual review
  │
  ▼
Save results (status → ENTERED)
  │
  ▼
Check for reflex test triggers
  │
  ├─ Triggered ──► Auto-add reflex tests to order
  │
  └─ No trigger
  │
  ▼
END
```

## Department-Specific Entry Types

| Department      | Entry Type        | Example                                  |
|----------------|-------------------|------------------------------------------|
| Biochemistry   | Numeric tabular   | Glucose: 126 mg/dL                       |
| Hematology     | Numeric + scatter | Hb: 12.5 g/dL, WBC differential         |
| Microbiology   | Culture + AST     | Organism: E.coli, Sensitive: Amikacin    |
| Histopathology | Narrative + images| Microscopic description, diagnosis        |
| Clinical Path  | Semi-quantitative | Urine Protein: 2+ (++)                   |
| Serology       | Qualitative/titer | HBsAg: Reactive, Titer: 1:256           |
| Molecular Bio  | CT values + interp| CT value: 28.5, Detected                 |

## Auto-Calculation of Derived Parameters

| Parameter | Formula                                         |
|-----------|--------------------------------------------------|
| LDL       | Total Cholesterol − HDL − (Triglycerides / 5)    |
| VLDL      | Triglycerides / 5                                |
| A/G Ratio | Albumin / (Total Protein − Albumin)              |
| Globulin  | Total Protein − Albumin                          |
| eGFR      | CKD-EPI formula (age, sex, creatinine)           |
| BUN       | Urea × 0.467                                    |
| MCH       | (Hb / RBC) × 10                                 |
| MCHC      | (Hb / PCV) × 100                                |
| MCV       | (PCV / RBC) × 10                                |

Derived parameters are auto-calculated on save. Technician cannot manually override.

## Abnormal Flag Logic

```
Result value vs Reference Range:
  │
  ├─ Below Critical Low   ──► Flag: CRITICAL_LOW   (🔴 immediate alert)
  ├─ Below Normal Low      ──► Flag: LOW            (↓)
  ├─ Within Normal Range   ──► Flag: NORMAL          (—)
  ├─ Above Normal High     ──► Flag: HIGH            (↑)
  └─ Above Critical High  ──► Flag: CRITICAL_HIGH  (🔴 immediate alert)
```

Reference ranges are age- and gender-specific (configured in master data).

## Delta Check
Compares current result with the patient's most recent previous result for the same test:

| Change         | Action                                  |
|---------------|------------------------------------------|
| ≤ 20% change  | No flag                                  |
| 20–50% change | Warning flag (⚠️ review recommended)     |
| > 50% change  | Critical flag (🔴 must verify before save)|

Delta threshold is configurable per test parameter.

## Auto-Validation Rules
Results may skip manual verification if ALL conditions are met:
1. Result is within normal range
2. Delta check passes (≤ 20% change)
3. QC for the batch/instrument has passed
4. No instrument error flags
5. Test is configured for auto-validation

Auto-validated results move directly to ENTERED → VERIFIED.

## API

### Get Worklist
```
GET /api/v1/results/worklist?departmentId={deptId}&status=PENDING
X-Branch-Id: {branchId}

Response:
{
  "success": true,
  "message": "Worklist retrieved",
  "data": [
    {
      "resultId": "uuid",
      "sampleBarcode": "SMP-DEL-20250115-001204",
      "patientName": "John Doe",
      "uhid": "DEL-001234",
      "testCode": "GLU-F",
      "testName": "Glucose Fasting",
      "status": "PENDING",
      "sampleReceivedAt": "2025-01-15T09:00:00Z",
      "tatDeadline": "2025-01-15T13:00:00Z"
    }
  ],
  "timestamp": "2025-01-15T10:00:00Z"
}
```

### Enter Results
```
PUT /api/v1/results/{id}/enter
X-Branch-Id: {branchId}

Body:
{
  "values": [
    {
      "parameterId": "uuid-glucose",
      "value": "126",
      "unit": "mg/dL"
    },
    {
      "parameterId": "uuid-hba1c",
      "value": "7.2",
      "unit": "%"
    }
  ],
  "instrumentId": "uuid",
  "remarks": "Fasting confirmed by patient"
}

Response:
{
  "success": true,
  "message": "Results entered",
  "data": {
    "resultId": "uuid",
    "status": "ENTERED",
    "parameters": [
      {
        "name": "Glucose Fasting",
        "value": "126",
        "unit": "mg/dL",
        "referenceRange": "70-110",
        "flag": "HIGH",
        "deltaPercent": 12.5,
        "deltaFlag": "NORMAL"
      }
    ],
    "autoValidated": false
  },
  "timestamp": "2025-01-15T10:30:00Z"
}
```

## Database Changes
- `results.status` updated to IN_PROGRESS then ENTERED
- `result_parameters` rows populated with values, flags, delta info
- `derived_parameters` auto-calculated and stored
- Reflex tests added to `order_line_items` if triggered
- `audit_trail` entry logged with before/after values

## Validation Rules
1. Sample must be in ACCEPTED status
2. All mandatory parameters must have values
3. Numeric values must be within instrument measurable range
4. Unit must match configured unit for the parameter
5. Only users with TECHNICIAN or SENIOR_TECHNICIAN role can enter results
6. Result cannot be re-entered once AUTHORIZED (use amendment flow)

## Error Scenarios
| Scenario                        | Error Code         | Message                                 |
|--------------------------------|--------------------|-----------------------------------------|
| Sample not accepted             | RES_SMP_STATUS     | Sample is not in ACCEPTED status        |
| Value out of measurable range   | RES_OUT_OF_RANGE   | Value exceeds instrument range          |
| Mandatory parameter missing     | RES_PARAM_MISSING  | Required parameter glucose not provided |
| QC not passed for instrument    | RES_QC_FAIL        | QC has not passed for this instrument   |

## State Transition
```
Result:  PENDING ──► IN_PROGRESS ──► ENTERED
                                        │
                              (if auto-validated)
                                        │
                                        ▼
                                     VERIFIED
```

## Related Documentation
- [Sample Receiving](04-sample-receiving.md)
- [Result Authorization](07-result-authorization.md)
- [Result Entry Types](../domain/departments/result-entry-types.md)
- [Parameter Configuration](../domain/parameters/parameter-configuration.md)
- [Critical Values](../domain/critical-values.md)
