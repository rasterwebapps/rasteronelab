# Process Flow: Billing & Payment — RasterOneLab LIS

## Trigger
Test order created — invoice generation is initiated.

## Flow

```
START
  │
  ▼
Order created with line items (tests / panels)
  │
  ▼
Calculate pricing
  │
  ├─ Base price from test catalog
  ├─ Branch price override (if configured)
  ├─ Insurance tariff (if applicable)
  └─ Rate list by category (Walk-in / Corporate / Doctor-referred)
  │
  ▼
Apply discounts
  │
  ├─ Percentage discount
  ├─ Flat amount discount
  ├─ Coupon code
  └─ Scheme (e.g., "Full Body Checkup" package price)
  │
  ▼
Generate invoice (status → DRAFT)
  │
  ▼
Confirm invoice (status → GENERATED)
  │
  ▼
Accept payment
  │
  ├─ Full payment ──► Status → PAID
  │
  ├─ Partial payment ──► Status → PARTIALLY_PAID
  │                           │
  │                     Record payment, balance due remains
  │
  └─ Credit account ──► Status → SENT (billed to corporate/insurance)
  │
  ▼
Print / email receipt
  │
  ▼
END
```

## Pricing Logic

```
Test Base Price (from test_catalog)
  │
  ▼
Branch Override? ──► branch_test_prices.price (overrides base)
  │
  ▼
Rate List Category?
  │
  ├─ WALK_IN ──► standard price
  ├─ CORPORATE ──► corporate tariff (from corporate_rate_lists)
  ├─ INSURANCE ──► insurance tariff (from insurance_tariffs)
  └─ DOCTOR_REF ──► referring doctor rate list
  │
  ▼
Apply Discounts
  │
  ├─ Scheme discount (package price < sum of tests)
  ├─ Percentage discount (e.g., 10% off)
  ├─ Flat discount (e.g., ₹100 off)
  └─ Coupon code (validates against coupon_master)
  │
  ▼
Net Amount = Σ(line item prices) − discounts + taxes (if applicable)
```

## Payment Methods

| Method      | Details                                     |
|-------------|---------------------------------------------|
| Cash        | Recorded with denomination breakdown        |
| Card        | Transaction reference number stored         |
| UPI         | UPI transaction ID stored                   |
| Insurance   | TPA authorization, pre-auth number          |
| Credit      | Against corporate/doctor credit account     |
| Online      | Payment gateway (Razorpay / PayU)           |

Multiple payment methods can be combined in a single transaction (split payment).

## Partial Payment Handling
- Order can proceed to sample collection after minimum payment threshold (configurable)
- Balance amount tracked in `invoice_payments`
- Subsequent payments update invoice status
- Full payment → PAID; otherwise remains PARTIALLY_PAID

## Credit Management
For corporate and insurance accounts:

```
Corporate account created with credit_limit
  │
  ▼
Order billed to corporate ──► Deduct from available credit
  │
  ▼
Monthly/periodic settlement
  │
  ├─ Generate statement (all invoices for period)
  ├─ Send to corporate / TPA
  └─ Record bulk payment against invoices
```

- Credit limit tracked per corporate account
- Alert when credit utilization > 80%
- Block new orders when credit exhausted

## Refund Workflow

```
Invoice in PAID status
  │
  ▼
Initiate refund (full or partial)
  │
  ▼
Approve refund (requires BILLING_SUPERVISOR role)
  │
  ▼
Process refund via original payment method
  │
  ▼
Invoice status → REFUNDED (full) or remains PAID (partial refund)
  │
  ▼
Credit note generated
```

## API

### Generate Invoice
```
POST /api/v1/invoices
X-Branch-Id: {branchId}

Body:
{
  "orderId": "uuid",
  "rateListCategory": "WALK_IN",
  "discountCode": "NEWYEAR10",
  "discountPercent": null,
  "discountAmount": null,
  "corporateAccountId": null
}

Response:
{
  "success": true,
  "message": "Invoice generated",
  "data": {
    "id": "uuid",
    "invoiceNumber": "INV-DEL-20250115-000198",
    "orderId": "uuid",
    "status": "GENERATED",
    "lineItems": [
      { "testName": "Lipid Panel", "basePrice": 650.00, "discountedPrice": 585.00 },
      { "testName": "CBC", "basePrice": 350.00, "discountedPrice": 315.00 }
    ],
    "grossAmount": 1000.00,
    "discountAmount": 100.00,
    "taxAmount": 0.00,
    "netAmount": 900.00
  },
  "timestamp": "2025-01-15T08:15:00Z"
}
```

### Record Payment
```
PUT /api/v1/invoices/{id}/pay
X-Branch-Id: {branchId}

Body:
{
  "payments": [
    {
      "method": "CASH",
      "amount": 500.00,
      "reference": null
    },
    {
      "method": "UPI",
      "amount": 400.00,
      "reference": "UPI-TXN-123456"
    }
  ]
}

Response:
{
  "success": true,
  "message": "Payment recorded",
  "data": {
    "invoiceId": "uuid",
    "status": "PAID",
    "totalPaid": 900.00,
    "balanceDue": 0.00,
    "receiptNumber": "RCT-DEL-20250115-000198",
    "payments": [
      { "method": "CASH", "amount": 500.00 },
      { "method": "UPI", "amount": 400.00 }
    ]
  },
  "timestamp": "2025-01-15T08:20:00Z"
}
```

### Process Refund
```
POST /api/v1/invoices/{id}/refund
X-Branch-Id: {branchId}

Body:
{
  "amount": 900.00,
  "reason": "Order cancelled before sample collection",
  "refundMethod": "CASH",
  "approvedBy": "uuid-supervisor"
}

Response:
{
  "success": true,
  "message": "Refund processed",
  "data": {
    "invoiceId": "uuid",
    "status": "REFUNDED",
    "refundAmount": 900.00,
    "creditNoteNumber": "CN-DEL-20250115-000012"
  },
  "timestamp": "2025-01-15T09:00:00Z"
}
```

## Database Changes
- `invoices` row created (status progression: DRAFT → GENERATED → PAID)
- `invoice_line_items` rows per test with pricing breakdown
- `invoice_payments` rows per payment transaction
- `invoice_refunds` row created on refund
- `credit_notes` row created for refunds
- `corporate_credit_ledger` updated for credit transactions
- `orders.status` updated to PAID after full payment
- `audit_trail` entry logged

## Validation Rules
1. Order must exist and belong to the same branch
2. Discount code must be valid, active, and not expired
3. Payment amount must equal or exceed minimum threshold
4. Credit account must have sufficient available credit
5. Refund amount cannot exceed total paid amount
6. Refund requires BILLING_SUPERVISOR approval
7. Insurance pre-authorization number is mandatory for insurance payments

## Error Scenarios
| Scenario                        | Error Code          | Message                                  |
|--------------------------------|---------------------|------------------------------------------|
| Invalid discount code           | BIL_DISC_INVALID    | Discount code is invalid or expired      |
| Insufficient credit             | BIL_CREDIT_LIMIT    | Corporate credit limit exceeded          |
| Refund exceeds paid amount      | BIL_REFUND_EXCESS   | Refund amount exceeds total paid         |
| Payment amount mismatch         | BIL_PAY_MISMATCH    | Payment total does not match invoice     |
| Invoice already fully paid      | BIL_ALREADY_PAID    | Invoice is already fully paid            |

## State Transition
```
Invoice: DRAFT ──► GENERATED ──► SENT ──► PARTIALLY_PAID ──► PAID
                                   │                           │
                                   └──► PAID (full payment)    └──► REFUNDED
                                   │
                                   └──► CANCELLED
                                   │
                                   └──► OVERDUE (auto, past due date)
```

## Related Documentation
- [Test Ordering](02-test-ordering.md)
- [Sample Collection](03-sample-collection.md)
- [Report Generation & Delivery](08-report-generation-delivery.md)
- [Workflow State Machines](../architecture/workflow-state-machines.md)
- [Error Codes](../architecture/error-codes.md)
