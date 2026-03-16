# Process Flow: Result Authorization — RasterOneLab LIS

## Trigger
Results entered and verified by technician, awaiting pathologist review.

## Flow

```
START
  │
  ▼
Pathologist opens authorization worklist
  │
  ▼
Select result for review
  │
  ▼
Review entered values, flags, and delta checks
  │
  ▼
Check QC status for instrument / batch
  │
  ├─ QC failed ──► Block authorization ──► Notify lab supervisor
  │
  └─ QC passed
        │
        ▼
  Check for critical values
        │
        ├─ Critical value present ──► Trigger critical value notification
        │                                  │
        │                                  ▼
        │                           Notify referring doctor & patient
        │
        └─ No critical values
        │
        ▼
  Decision
        │
        ├─ Approve ──► Apply digital signature ──► Status → AUTHORIZED
        │                                                │
        │                                                ▼
        │                                          Trigger report generation
        │
        └─ Reject ──► Add rejection comments ──► Status → back to ENTERED
                            │
                            ▼
                      Notify technician for correction
                            │
                            ▼
                           END
```

## Critical Value Detection
When a result is flagged as CRITICAL_HIGH or CRITICAL_LOW:

1. System blocks authorization until critical value acknowledgment
2. Pathologist confirms the critical value
3. Notification sent via SMS / WhatsApp / call to:
   - Referring doctor (mandatory)
   - Patient (if configured)
4. Acknowledgment with timestamp recorded in `critical_value_log`

```
Critical Value Alert Example:
  Patient: John Doe (DEL-001234)
  Test: Potassium
  Result: 6.8 mEq/L (Critical High > 6.0)
  Action Required: Immediate clinical notification
```

## Pathologist Digital Signature
- Pathologist's signature stored as image in `user_signatures` table
- On authorization, signature ID linked to result
- Signature appears on printed/PDF report
- Only users with PATHOLOGIST role can authorize
- A pathologist cannot authorize their own entered results (maker-checker)

## Amendment Workflow (Post-Authorization)
Once results are authorized, any change requires a formal amendment:

```
AUTHORIZED result
  │
  ▼
Pathologist initiates amendment
  │
  ▼
Enter corrected values + amendment reason (mandatory)
  │
  ▼
Status → AMENDED
  │
  ▼
Original values preserved in audit trail
  │
  ▼
New report generated with amendment note
  │
  ▼
Previous report recalled (if already delivered)
  │
  ▼
Amended report delivered
```

Amendment reasons are tracked and reported for quality metrics.

## API

### Authorization Worklist
```
GET /api/v1/results/authorization-worklist?departmentId={deptId}
X-Branch-Id: {branchId}

Response:
{
  "success": true,
  "message": "Authorization worklist retrieved",
  "data": [
    {
      "resultId": "uuid",
      "sampleBarcode": "SMP-DEL-20250115-001204",
      "patientName": "John Doe",
      "uhid": "DEL-001234",
      "testName": "Lipid Panel",
      "status": "ENTERED",
      "hasCriticalValues": false,
      "enteredBy": "Tech Name",
      "enteredAt": "2025-01-15T10:30:00Z"
    }
  ],
  "timestamp": "2025-01-15T11:00:00Z"
}
```

### Authorize Result
```
PUT /api/v1/results/{id}/authorize
X-Branch-Id: {branchId}

Body:
{
  "authorizedBy": "uuid-pathologist",
  "signatureId": "uuid-sig",
  "comments": "Results consistent with clinical history",
  "criticalValueAcknowledged": false
}

Response:
{
  "success": true,
  "message": "Result authorized",
  "data": {
    "resultId": "uuid",
    "status": "AUTHORIZED",
    "authorizedBy": "Dr. Smith",
    "authorizedAt": "2025-01-15T11:15:00Z",
    "reportGenerationTriggered": true
  },
  "timestamp": "2025-01-15T11:15:00Z"
}
```

### Reject Result (Send Back)
```
PUT /api/v1/results/{id}/reject
X-Branch-Id: {branchId}

Body:
{
  "rejectedBy": "uuid-pathologist",
  "reason": "Delta check exceeds threshold — please re-run sample",
  "sendBackTo": "uuid-technician"
}

Response:
{
  "success": true,
  "message": "Result rejected and sent back for correction",
  "data": { "resultId": "uuid", "status": "ENTERED" },
  "timestamp": "2025-01-15T11:20:00Z"
}
```

### Amend Result
```
PUT /api/v1/results/{id}/amend
X-Branch-Id: {branchId}

Body:
{
  "amendedBy": "uuid-pathologist",
  "reason": "Transcription error in Potassium value",
  "correctedValues": [
    { "parameterId": "uuid-k", "oldValue": "6.8", "newValue": "4.8", "unit": "mEq/L" }
  ]
}

Response:
{
  "success": true,
  "message": "Result amended",
  "data": {
    "resultId": "uuid",
    "status": "AMENDED",
    "amendmentNumber": 1,
    "previousReportRecalled": true
  },
  "timestamp": "2025-01-15T14:00:00Z"
}
```

## Database Changes
- `results.status` updated to AUTHORIZED or AMENDED
- `results.authorized_by`, `results.authorized_at` set
- `result_amendments` row created for amendments (with before/after values)
- `critical_value_log` row created if critical value acknowledged
- `report_generation_queue` event published
- `audit_trail` entry logged

## Validation Rules
1. Result must be in ENTERED or VERIFIED status to authorize
2. QC must have passed for the instrument on the test date
3. Critical values must be acknowledged before authorization
4. Pathologist cannot authorize results they personally entered
5. Amendment reason is mandatory (min 10 characters)
6. Only PATHOLOGIST or SENIOR_PATHOLOGIST role can authorize

## Error Scenarios
| Scenario                         | Error Code          | Message                                    |
|---------------------------------|---------------------|--------------------------------------------|
| QC not passed                    | AUTH_QC_FAIL        | QC has not passed for this instrument      |
| Critical value not acknowledged  | AUTH_CRIT_NOACK     | Critical value must be acknowledged        |
| Self-authorization attempt       | AUTH_SELF_ENTRY     | Cannot authorize self-entered results      |
| Result not in valid status       | AUTH_INVALID_STATUS | Result is not in ENTERED/VERIFIED status   |
| Amendment without reason         | AUTH_AMEND_REASON   | Amendment reason is required               |

## State Transition
```
Result:  ENTERED ──► VERIFIED ──► AUTHORIZED ──► AMENDED
                                       │              │
                                       │         (new report)
                                       │
                                  (report generated)
```

## Related Documentation
- [Result Entry & Validation](06-result-entry-validation.md)
- [Report Generation & Delivery](08-report-generation-delivery.md)
- [Critical Value Workflow](15-critical-value-workflow.md)
- [Quality Control](10-quality-control.md)
- [Workflow State Machines](../architecture/workflow-state-machines.md)
