# Process Flow: Sample Receiving — RasterOneLab LIS

## Trigger
Samples arrive at the laboratory (from collection counter or external pickup).

## Flow

```
START
  │
  ▼
Scan sample barcode at lab reception
  │
  ▼
System displays sample details (patient, tests, tube type)
  │
  ▼
Check sample quality
  │
  ├─ Acceptable ──► Accept sample (status → RECEIVED)
  │                       │
  │                       ▼
  │                 TAT clock starts
  │                       │
  │                       ▼
  │                 Aliquot needed?
  │                       │
  │                 ├─ Yes ──► Split into child samples
  │                 │              │
  │                 │              ▼
  │                 │         Generate child barcodes
  │                 │              │
  │                 │              ▼
  │                 │         Status → ALIQUOTED
  │                 │
  │                 └─ No ──► Status → ACCEPTED
  │                                │
  │                                ▼
  │                          Route to department
  │
  └─ Not acceptable ──► Reject sample
                              │
                              ▼
                        Select rejection reason
                              │
                              ▼
                        Notify collection center
                              │
                              ▼
                        Request recollection
                              │
                              ▼
                        END (sample rejected)
```

## Rejection Reasons

| Code   | Reason                  | Description                             |
|--------|-------------------------|-----------------------------------------|
| REJ-01 | Hemolyzed               | Red blood cell lysis, serum appears red |
| REJ-02 | Clotted                 | EDTA/citrate sample has visible clots   |
| REJ-03 | Insufficient quantity   | Volume below minimum for tests ordered  |
| REJ-04 | Wrong tube              | Sample collected in incorrect tube type |
| REJ-05 | Unlabeled               | No barcode or patient identifier        |
| REJ-06 | Lipemic                 | Excessive lipids (milky appearance)     |
| REJ-07 | Expired stability       | Time since collection exceeds stability |
| REJ-08 | Improper transport      | Temperature not maintained during transit|

## Aliquoting Workflow
When tests on the same tube must go to different departments or analyzers:

```
Primary Sample (SMP-DEL-20250115-001204)
  │
  ├──► Aliquot 1 (SMP-DEL-20250115-001204-A) → Biochemistry analyzer
  ├──► Aliquot 2 (SMP-DEL-20250115-001204-B) → Immunoassay analyzer
  └──► Aliquot 3 (SMP-DEL-20250115-001204-C) → Stored as backup
```

- Child samples reference `parent_sample_id`
- Tests are reassigned from parent to appropriate child sample
- Parent sample status changes to ALIQUOTED

## TAT (Turnaround Time)
- **TAT starts**: when sample is received (not when collected)
- **TAT ends**: when report is delivered
- TAT targets configured per test in master data
- Breach alerts triggered at 80% and 100% of TAT target

## API

### Receive Sample
```
PUT /api/v1/samples/{id}/receive
X-Branch-Id: {branchId}

Body:
{
  "receivedBy": "uuid",
  "receivedAt": "2025-01-15T09:00:00Z",
  "condition": "ACCEPTABLE",
  "temperature": 22.5,
  "notes": "Sample in good condition"
}

Response:
{
  "success": true,
  "message": "Sample received",
  "data": {
    "id": "uuid",
    "sampleBarcode": "SMP-DEL-20250115-001204",
    "status": "RECEIVED",
    "receivedAt": "2025-01-15T09:00:00Z",
    "tatStartedAt": "2025-01-15T09:00:00Z"
  },
  "timestamp": "2025-01-15T09:00:01Z"
}
```

### Reject Sample
```
PUT /api/v1/samples/{id}/reject
X-Branch-Id: {branchId}

Body:
{
  "rejectedBy": "uuid",
  "rejectionReason": "REJ-01",
  "comments": "Grossly hemolyzed, affects LFT and lipid results",
  "recollectionRequired": true
}

Response:
{
  "success": true,
  "message": "Sample rejected",
  "data": {
    "id": "uuid",
    "status": "REJECTED",
    "rejectionReason": "Hemolyzed",
    "recollectionRequired": true
  },
  "timestamp": "2025-01-15T09:05:00Z"
}
```

### Aliquot Sample
```
POST /api/v1/samples/{id}/aliquot
X-Branch-Id: {branchId}

Body:
{
  "aliquots": [
    { "departmentId": "uuid-biochem", "testIds": ["uuid-1", "uuid-2"], "volume": 2.0 },
    { "departmentId": "uuid-immuno", "testIds": ["uuid-3"], "volume": 1.5 }
  ]
}

Response:
{
  "success": true,
  "message": "Sample aliquoted",
  "data": {
    "parentSampleId": "uuid",
    "parentStatus": "ALIQUOTED",
    "childSamples": [
      { "id": "uuid-a", "barcode": "SMP-DEL-20250115-001204-A", "department": "BIOCHEMISTRY" },
      { "id": "uuid-b", "barcode": "SMP-DEL-20250115-001204-B", "department": "IMMUNOLOGY" }
    ]
  },
  "timestamp": "2025-01-15T09:10:00Z"
}
```

## Database Changes
- `samples.status` updated to RECEIVED, ACCEPTED, REJECTED, or ALIQUOTED
- `samples.received_at` and `samples.tat_started_at` set on receive
- Child rows created in `samples` for aliquots (with `parent_sample_id`)
- `sample_tests` reassigned to child samples when aliquoted
- `sample_rejections` row created on rejection
- `orders.status` updated to SAMPLE_RECEIVED
- `audit_trail` entry logged

## Validation Rules
1. Sample must be in COLLECTED status to receive
2. Rejection reason is mandatory when rejecting
3. Aliquot volumes must not exceed parent sample volume
4. Each test must be assigned to exactly one aliquot
5. Receiving user must have LAB_TECHNICIAN or LAB_SUPERVISOR role
6. Sample stability window must not be exceeded (collection time → receive time)

## Error Scenarios
| Scenario                      | Error Code          | Message                                |
|------------------------------|---------------------|----------------------------------------|
| Sample not in COLLECTED state | SMP_INVALID_STATUS  | Sample is not in COLLECTED status      |
| Stability window exceeded     | SMP_EXPIRED         | Sample stability period has expired    |
| Aliquot volume exceeds total  | SMP_VOL_EXCEEDED    | Aliquot volumes exceed parent volume   |
| Missing rejection reason      | SMP_REJ_REASON      | Rejection reason is required           |

## State Transition
```
Sample:  COLLECTED ──► RECEIVED ──► ACCEPTED ──► (processing)
                              │           │
                              │           └──► ALIQUOTED ──► (processing)
                              │
                              └──► REJECTED ──► (recollection)

Order:   SAMPLE_COLLECTED ──► SAMPLE_RECEIVED
```

## Related Documentation
- [Sample Collection](03-sample-collection.md)
- [Result Entry & Validation](06-result-entry-validation.md)
- [Sample Types & Tubes](../domain/sample-types-tubes.md)
- [Workflow State Machines](../architecture/workflow-state-machines.md)
