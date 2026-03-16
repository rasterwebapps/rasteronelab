# Process Flow: Test Ordering — RasterOneLab LIS

## Trigger
Patient registered (UHID exists), receptionist creates a test order.

## Flow

```
START
  │
  ▼
Search patient (UHID / phone / name)
  │
  ▼
Select patient ──► Verify identity
  │
  ▼
Search tests / panels from catalog
  │
  ├─ Individual test ──► Add to order
  │
  └─ Panel (e.g., Lipid Panel) ──► Expand into sub-tests ──► Add all to order
  │
  ▼
Review order line items
  │
  ├─ Apply discount / coupon / insurance tariff
  │
  ▼
Set referring doctor (optional)
  │
  ▼
Save order (status = DRAFT)
  │
  ▼
Generate order barcode
  │
  ▼
Place order (status = PLACED)
  │
  ▼
Print barcode label
  │
  ▼
Proceed to billing
  │
  ▼
END
```

## Order Barcode Format
Format: `ORD-{BranchCode}-{YYYYMMDD}-{sequence}`
Example: `ORD-DEL-20250115-000342`

- Branch-specific daily sequence
- Stored in `branch_number_series` with pessimistic lock
- Barcode type: Code-128

## Panel Expansion
When a panel is ordered, the system expands it into constituent tests:

| Panel            | Expanded Tests                                            |
|------------------|-----------------------------------------------------------|
| Lipid Panel      | Total Cholesterol, Triglycerides, HDL, LDL, VLDL          |
| CBC              | Hb, WBC, RBC, PCV, MCV, MCH, MCHC, Platelet, Differential |
| Liver Function   | Bilirubin Total/Direct, SGOT, SGPT, ALP, GGT, Protein, Albumin |
| Renal Function   | Urea, Creatinine, Uric Acid, BUN, eGFR                   |

- Panel price may differ from sum of individual test prices
- Individual tests within a panel cannot be removed

## Reflex Test Rules
System auto-adds tests based on configured rules:

| Primary Test | Condition           | Reflex Test Added  |
|-------------|---------------------|--------------------|
| TSH         | TSH > 10 or < 0.1  | Free T3, Free T4   |
| HbA1c       | HbA1c > 6.5%       | Fasting Glucose     |
| Urinalysis  | Protein positive    | Urine Microalbumin  |

Reflex tests are added automatically during result entry, not at ordering.

## API

### Search Tests
```
GET /api/v1/tests/search?query=lipid&type=PANEL
X-Branch-Id: {branchId}

Response:
{
  "success": true,
  "message": "Tests retrieved",
  "data": [
    {
      "id": "uuid",
      "name": "Lipid Panel",
      "code": "LIP-PNL",
      "type": "PANEL",
      "department": "BIOCHEMISTRY",
      "basePrice": 650.00,
      "subTests": ["CHOL", "TG", "HDL", "LDL", "VLDL"]
    }
  ],
  "timestamp": "2025-01-15T10:30:00Z"
}
```

### Create Order
```
POST /api/v1/orders
X-Branch-Id: {branchId}

Body:
{
  "patientId": "uuid",
  "referringDoctorId": "uuid",
  "testIds": ["uuid-1", "uuid-2"],
  "panelIds": ["uuid-3"],
  "discountCode": "CORP10",
  "notes": "Fasting sample"
}

Response:
{
  "success": true,
  "message": "Order created",
  "data": {
    "id": "uuid",
    "orderNumber": "ORD-DEL-20250115-000342",
    "patientId": "uuid",
    "status": "DRAFT",
    "lineItems": [...],
    "totalAmount": 1250.00,
    "discountAmount": 125.00,
    "netAmount": 1125.00,
    "createdAt": "2025-01-15T10:30:00Z"
  },
  "timestamp": "2025-01-15T10:30:00Z"
}
```

### Place Order
```
PUT /api/v1/orders/{id}/place
X-Branch-Id: {branchId}

Response:
{
  "success": true,
  "message": "Order placed",
  "data": { "id": "uuid", "status": "PLACED" },
  "timestamp": "2025-01-15T10:31:00Z"
}
```

## Database Changes
- `orders` row created (status = DRAFT, then PLACED)
- `order_line_items` rows created per test (panel expanded)
- `order_number_series` sequence incremented
- `audit_trail` entry logged

## Validation Rules
1. Patient must exist and belong to the same branch
2. At least one test or panel required
3. Duplicate test in same order not allowed
4. Test must be active and available at the branch
5. Discount code must be valid and not expired
6. Referring doctor must be active (if provided)

## Error Scenarios
| Scenario                     | Error Code       | Message                              |
|-----------------------------|------------------|--------------------------------------|
| Patient not found            | PAT_NOT_FOUND    | Patient does not exist               |
| Duplicate test in order      | ORD_DUP_TEST     | Test already added to this order     |
| Test not available at branch | ORD_TEST_NA      | Test not available at this branch    |
| Invalid discount code        | ORD_DISC_INVALID | Discount code is invalid or expired  |

## State Transition
```
DRAFT ──► PLACED ──► (proceeds to billing / sample collection)
```

## Related Documentation
- [Patient Registration](01-patient-registration.md)
- [Sample Collection](03-sample-collection.md)
- [Billing & Payment](09-billing-payment.md)
- [Barcode Strategy](../domain/barcode-strategy.md)
- [Workflow State Machines](../architecture/workflow-state-machines.md)
