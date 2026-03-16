# Process Flow: Sample Collection — RasterOneLab LIS

## Trigger
Order placed and paid (or credit approved). Phlebotomist collects sample(s).

## Flow

```
START
  │
  ▼
Scan order barcode / search by order number
  │
  ▼
Verify patient identity (name, DOB)
  │
  ▼
Review ordered tests ──► Determine required tubes
  │
  ├─ Single tube sufficient ──► Assign 1 tube
  │
  └─ Multiple tubes needed ──► Assign N tubes (by tube type)
  │
  ▼
Print sample barcode labels
  │
  ▼
Collect sample(s)
  │
  ├─ In-lab collection
  │
  └─ Home collection ──► Assign phlebotomist ──► Schedule pickup
  │
  ▼
Affix barcode labels to tubes
  │
  ▼
Confirm collection (scan barcode)
  │
  ▼
Record collection details (time, collector, site)
  │
  ▼
Sample status → COLLECTED
  │
  ▼
Order status → SAMPLE_COLLECTED
  │
  ▼
END
```

## Sample Barcode Format
Format: `SMP-{BranchCode}-{YYYYMMDD}-{sequence}`
Example: `SMP-DEL-20250115-001204`

- One barcode per tube (not per test)
- Barcode type: Code-128
- Label includes: barcode, patient name, UHID, tube color, collection time

## Tube Assignment Logic
Tests are grouped by required tube type:

| Tube Color  | Anticoagulant | Common Tests                         |
|-------------|---------------|--------------------------------------|
| Red (Plain) | None          | LFT, RFT, Lipids, Thyroid, Sugar    |
| Purple (EDTA) | EDTA        | CBC, HbA1c, Blood Group             |
| Blue (Citrate) | Sodium Citrate | PT/INR, APTT, D-Dimer          |
| Grey (Fluoride) | NaF/KOx   | Glucose (Fasting/PP)                |
| Green (Heparin) | Li-Heparin | ABG, Ammonia                       |

Rules:
- Multiple tests sharing the same tube type → 1 tube
- Tests requiring different tubes → multiple tubes
- Each tube gets its own sample barcode
- `sample_tests` table maps which tests belong to which sample tube

## Home Collection Workflow
```
Order placed with homeCollection = true
  │
  ▼
Assign phlebotomist (based on area/availability)
  │
  ▼
Schedule time slot
  │
  ▼
Phlebotomist receives assignment on mobile app
  │
  ▼
Collect at patient location ──► Scan & confirm
  │
  ▼
Transport to lab (cold chain if required)
  │
  ▼
Hand over at reception
```

## API

### Collect Sample
```
POST /api/v1/samples/collect
X-Branch-Id: {branchId}

Body:
{
  "orderId": "uuid",
  "tubes": [
    {
      "tubeType": "EDTA",
      "collectionSite": "LEFT_ARM",
      "collectedBy": "uuid",
      "collectedAt": "2025-01-15T08:30:00Z",
      "quantity": 3.0,
      "unit": "ML"
    },
    {
      "tubeType": "PLAIN",
      "collectionSite": "RIGHT_ARM",
      "collectedBy": "uuid",
      "collectedAt": "2025-01-15T08:31:00Z",
      "quantity": 5.0,
      "unit": "ML"
    }
  ],
  "homeCollection": false,
  "notes": "Patient fasting confirmed"
}

Response:
{
  "success": true,
  "message": "Samples collected",
  "data": {
    "samples": [
      {
        "id": "uuid-1",
        "sampleBarcode": "SMP-DEL-20250115-001204",
        "tubeType": "EDTA",
        "status": "COLLECTED",
        "tests": ["CBC", "HbA1c"]
      },
      {
        "id": "uuid-2",
        "sampleBarcode": "SMP-DEL-20250115-001205",
        "tubeType": "PLAIN",
        "status": "COLLECTED",
        "tests": ["LFT", "RFT", "Lipid Panel"]
      }
    ],
    "orderStatus": "SAMPLE_COLLECTED"
  },
  "timestamp": "2025-01-15T08:32:00Z"
}
```

### Print Labels
```
GET /api/v1/samples/{id}/label
X-Branch-Id: {branchId}
Accept: application/pdf

Response: PDF label (ZPL format for thermal printers)
```

## Database Changes
- `samples` rows created (one per tube, status = COLLECTED)
- `sample_tests` rows link each sample to its tests
- `orders.status` updated to SAMPLE_COLLECTED
- `audit_trail` entry logged

## Validation Rules
1. Order must be in PLACED or PAID status
2. All required tubes must be collected (partial collection not allowed by default)
3. Phlebotomist must be an active user with PHLEBOTOMIST role
4. Collection time cannot be in the future
5. Quantity must meet minimum volume for tube type
6. For home collection, phlebotomist assignment is mandatory

## Error Scenarios
| Scenario                       | Error Code        | Message                               |
|-------------------------------|-------------------|---------------------------------------|
| Order not in valid status      | SMP_ORD_STATUS    | Order is not ready for collection     |
| Missing required tube          | SMP_TUBE_MISSING  | Required tube type EDTA not collected |
| Insufficient volume            | SMP_LOW_VOL       | Minimum volume is 3 ML for EDTA tube |
| Home collection without phlebotomist | SMP_NO_PHLEB | Phlebotomist must be assigned    |

## State Transition
```
Order:  PAID ──► SAMPLE_COLLECTED
Sample: (new) ──► COLLECTED
```

## Related Documentation
- [Test Ordering](02-test-ordering.md)
- [Sample Receiving](04-sample-receiving.md)
- [Sample Types & Tubes](../domain/sample-types-tubes.md)
- [Barcode Strategy](../domain/barcode-strategy.md)
