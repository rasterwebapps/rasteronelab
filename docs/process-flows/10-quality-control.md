# Process Flow: Quality Control — RasterOneLab LIS

## Trigger
QC run required — daily before patient testing, per batch, or per new reagent lot.

## Flow

```
START
  │
  ▼
Select QC material (Level 1 / Level 2 / Level 3)
  │
  ▼
Run QC material on instrument
  │
  ▼
Enter QC values (manual or auto-upload from instrument)
  │
  ▼
Apply Westgard rules
  │
  ├─ All rules pass ──► QC ACCEPTED
  │                          │
  │                          ▼
  │                    Authorize patient testing
  │                          │
  │                          ▼
  │                         END
  │
  ├─ Warning (1-2s) ──► Flag for review
  │                          │
  │                          ▼
  │                    Supervisor reviews
  │                          │
  │                    ├─ Accept with comment ──► QC ACCEPTED
  │                    └─ Reject ──► Corrective action
  │
  └─ Violation (reject rules) ──► QC REJECTED
                                       │
                                       ▼
                                 Block patient testing
                                       │
                                       ▼
                                 Initiate corrective action
                                       │
                                       ├─ Repeat QC with same material
                                       ├─ Recalibrate instrument
                                       ├─ Change reagent lot
                                       └─ Escalate to service engineer
                                       │
                                       ▼
                                 Document corrective action
                                       │
                                       ▼
                                 Re-run QC
                                       │
                                       ▼
                                 (back to START)
```

## Westgard Rules

| Rule  | Type    | Description                                       | Action   |
|-------|---------|---------------------------------------------------|----------|
| 1-2s  | Warning | One value exceeds ±2 SD                           | Warning  |
| 1-3s  | Reject  | One value exceeds ±3 SD                           | Reject   |
| 2-2s  | Reject  | Two consecutive values exceed ±2 SD (same side)   | Reject   |
| R-4s  | Reject  | Range between two values exceeds 4 SD             | Reject   |
| 4-1s  | Reject  | Four consecutive values exceed ±1 SD (same side)  | Reject   |
| 10x   | Reject  | Ten consecutive values on same side of mean        | Reject   |

Rules are evaluated in order. If any reject rule triggers, patient testing is blocked for that test/instrument combination until corrective action is taken and QC passes.

## Levey-Jennings Chart
QC data points plotted on a Levey-Jennings chart:

```
  +3 SD  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  (reject line)
  +2 SD  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  (warning line)
  +1 SD  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─
  Mean   ━━━━━━━●━━━●━━━●━━━●━━━●━━━━━━━━  (target)
  −1 SD  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─
  −2 SD  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  (warning line)
  −3 SD  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  (reject line)
         Day1  Day2  Day3  Day4  Day5
```

- Chart displays last 30 data points by default
- Mean and SD from QC material lot insert values
- Trend detection: shift (7+ same side) or trend (7+ increasing/decreasing)

## QC Material Management
- QC materials have lot numbers with expiry dates
- Each lot has assigned mean and SD values (from manufacturer insert)
- Lab can calculate own mean/SD after 20+ data points
- Material inventory tracked — alerts when stock is low

## Corrective Action Documentation

| Field               | Description                                   |
|--------------------|-----------------------------------------------|
| Problem identified  | What Westgard rule was violated               |
| Root cause          | Reagent, calibration, instrument, operator     |
| Action taken        | Recalibrated, changed lot, repeated, serviced |
| Resolved by         | User who resolved the issue                   |
| Resolution date     | When corrective action completed              |
| Re-run QC result    | QC value after corrective action              |

Corrective actions stored in `qc_corrective_actions` table for audit.

## EQA (External Quality Assessment)
Participation in external proficiency testing programs:

```
Receive EQA sample from program provider
  │
  ▼
Process as regular sample (blinded)
  │
  ▼
Enter results into EQA portal
  │
  ▼
Receive performance report
  │
  ▼
Document in LIS (pass/fail, z-score, bias)
  │
  ▼
Corrective action if performance unsatisfactory
```

EQA results stored in `eqa_submissions` table.

## Impact on Patient Result Authorization
QC status is checked during result authorization:

```
Pathologist authorizes result
  │
  ▼
System checks: Was QC run for this test on this instrument today?
  │
  ├─ QC passed ──► Authorization allowed
  │
  ├─ QC failed ──► Authorization BLOCKED (error: AUTH_QC_FAIL)
  │
  └─ QC not run ──► Warning: "QC not performed today"
                         │
                         ├─ Supervisor override allowed (documented)
                         └─ Block (if configured as mandatory)
```

## API

### Create QC Run
```
POST /api/v1/qc/runs
X-Branch-Id: {branchId}

Body:
{
  "instrumentId": "uuid",
  "testId": "uuid",
  "qcMaterialId": "uuid",
  "level": "LEVEL_1",
  "value": 145.3,
  "unit": "mg/dL",
  "runBy": "uuid",
  "runAt": "2025-01-15T07:30:00Z"
}

Response:
{
  "success": true,
  "message": "QC run recorded",
  "data": {
    "id": "uuid",
    "status": "ACCEPTED",
    "value": 145.3,
    "mean": 148.0,
    "sd": 3.5,
    "zScore": -0.77,
    "westgardViolations": [],
    "patientTestingAllowed": true
  },
  "timestamp": "2025-01-15T07:30:01Z"
}
```

### Get Levey-Jennings Chart Data
```
GET /api/v1/qc/charts/{testId}?instrumentId={id}&days=30
X-Branch-Id: {branchId}

Response:
{
  "success": true,
  "message": "Chart data retrieved",
  "data": {
    "testName": "Glucose",
    "instrumentName": "Beckman AU5800",
    "mean": 148.0,
    "sd": 3.5,
    "dataPoints": [
      { "date": "2025-01-01", "level1": 147.2, "level2": 289.5 },
      { "date": "2025-01-02", "level1": 149.1, "level2": 291.0 },
      { "date": "2025-01-03", "level1": 146.8, "level2": 288.3 }
    ],
    "violations": []
  },
  "timestamp": "2025-01-15T08:00:00Z"
}
```

### Record Corrective Action
```
POST /api/v1/qc/runs/{id}/corrective-action
X-Branch-Id: {branchId}

Body:
{
  "problemIdentified": "1-3s violation on Glucose Level 1",
  "rootCause": "REAGENT",
  "actionTaken": "Changed reagent lot from LOT-2024-A to LOT-2025-B and recalibrated",
  "resolvedBy": "uuid",
  "rerunQcId": "uuid-new-qc-run"
}

Response:
{
  "success": true,
  "message": "Corrective action documented",
  "data": {
    "id": "uuid",
    "qcRunId": "uuid",
    "status": "RESOLVED",
    "rerunPassed": true
  },
  "timestamp": "2025-01-15T08:30:00Z"
}
```

## Database Changes
- `qc_runs` row created with value, mean, SD, z-score, status
- `qc_westgard_evaluations` rows for each rule evaluation
- `qc_corrective_actions` row created when corrective action documented
- `eqa_submissions` row for external QA results
- `audit_trail` entry logged

## Validation Rules
1. QC material must be active and not expired
2. Instrument must be active and assigned to the branch
3. QC value must be numeric and within instrument measurable range
4. At least one QC run per level per day per test (if configured as mandatory)
5. Corrective action is mandatory before re-running QC after rejection
6. Only LAB_SUPERVISOR or QC_MANAGER role can override QC warnings

## Error Scenarios
| Scenario                         | Error Code        | Message                                    |
|---------------------------------|-------------------|--------------------------------------------|
| QC material expired              | QC_MAT_EXPIRED    | QC material lot has expired                |
| Instrument not configured        | QC_INSTR_NA       | Instrument not configured for this test    |
| Westgard violation               | QC_WESTGARD_FAIL  | Westgard rule 1-3s violated                |
| No corrective action documented  | QC_NO_CA          | Corrective action required before re-run   |
| QC not run today                 | QC_NOT_RUN        | QC has not been performed today            |

## State Transition
```
QC Run:  (new) ──► ACCEPTED ──► (patient testing allowed)
                │
                ├──► WARNING ──► REVIEWED ──► ACCEPTED (with comment)
                │                        └──► REJECTED
                │
                └──► REJECTED ──► CORRECTIVE_ACTION ──► RE-RUN ──► (back to evaluation)
```

## Related Documentation
- [Result Entry & Validation](06-result-entry-validation.md)
- [Result Authorization](07-result-authorization.md)
- [Critical Value Workflow](15-critical-value-workflow.md)
- [Workflow State Machines](../architecture/workflow-state-machines.md)
- [Audit Trail Design](../architecture/audit-trail-design.md)
