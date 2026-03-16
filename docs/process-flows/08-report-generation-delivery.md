# Process Flow: Report Generation & Delivery — RasterOneLab LIS

## Trigger
All test results for an order are authorized (or individual department report triggered).

## Flow

```
START
  │
  ▼
All results for order AUTHORIZED?
  │
  ├─ No ──► Wait (partial results not reported by default)
  │
  └─ Yes
        │
        ▼
  Select report template (based on department / order type)
        │
        ▼
  Populate template with:
    - Branch header, logo, address
    - Patient demographics
    - Test results with flags and reference ranges
    - Pathologist name and digital signature
    - QR code for verification
        │
        ▼
  Generate PDF (status → GENERATING)
        │
        ▼
  Store PDF in MinIO (S3-compatible storage)
        │
        ▼
  Status → GENERATED
        │
        ▼
  Pathologist releases report (status → RELEASED)
        │
        ▼
  Deliver report via configured channels
        │
        ├─ SMS (short link to portal)
        ├─ Email (PDF attachment)
        ├─ WhatsApp (PDF + link)
        ├─ Doctor portal (available for download)
        └─ Patient portal (available for download)
        │
        ▼
  Status → DELIVERED
        │
        ▼
  END
```

## Template Selection

| Department       | Template Type      | Key Features                          |
|-----------------|-------------------|---------------------------------------|
| Biochemistry    | Tabular            | Parameter table, reference ranges     |
| Hematology      | Tabular + Chart    | CBC table, differential pie chart     |
| Microbiology    | Narrative + Table  | Culture result, antibiogram table     |
| Histopathology  | Narrative          | Gross/micro descriptions, diagnosis   |
| Clinical Path   | Semi-tabular       | Urine/stool with semi-quant symbols   |
| Serology        | Tabular            | Qualitative + titer values            |
| Molecular Bio   | Tabular + Interp   | CT values, interpretation text        |

## Branch-Specific Customization
- **Header**: Branch name, address, phone, NABL accreditation number
- **Logo**: Branch-specific logo (stored in MinIO under `branch:{branchId}/logo`)
- **Footer**: Disclaimer, signatory information
- **Watermark**: "DRAFT" for unreleased reports
- Configured in `branch_report_settings` table

## QR Code Verification
Each report includes a QR code that encodes:
```
https://reports.rasteronelab.com/verify/{reportId}
```
Scanning the QR code displays:
- Patient name, UHID
- Report date
- Test names and results
- Verification status (authentic / tampered)

## Report Storage (MinIO)
```
Bucket: lis-reports
Path:   branch/{branchId}/reports/{YYYY}/{MM}/{reportId}.pdf

Metadata:
  - Content-Type: application/pdf
  - x-amz-meta-patient-id: {patientId}
  - x-amz-meta-order-id: {orderId}
  - x-amz-meta-branch-id: {branchId}
```

Retention: Reports retained for 5 years (configurable per branch).

## Delivery Channels

| Channel        | Mechanism                          | Content                     |
|---------------|------------------------------------|-----------------------------|
| SMS           | SMS gateway with short URL         | "Your report is ready: {link}" |
| Email         | SMTP with PDF attachment           | Subject: "Lab Report Ready"   |
| WhatsApp      | WhatsApp Business API              | PDF attachment + text message |
| Doctor Portal | Available in doctor's dashboard    | Download link                 |
| Patient Portal| Available in patient's portal      | View + download               |

## API

### Generate Report
```
POST /api/v1/reports/generate
X-Branch-Id: {branchId}

Body:
{
  "orderId": "uuid",
  "templateId": "uuid-template",
  "format": "PDF"
}

Response:
{
  "success": true,
  "message": "Report generation initiated",
  "data": {
    "reportId": "uuid",
    "orderId": "uuid",
    "status": "GENERATING",
    "estimatedCompletionSeconds": 5
  },
  "timestamp": "2025-01-15T12:00:00Z"
}
```

### Download Report
```
GET /api/v1/reports/{id}/download
X-Branch-Id: {branchId}
Accept: application/pdf

Response: Binary PDF stream
Headers:
  Content-Type: application/pdf
  Content-Disposition: attachment; filename="RPT-DEL-20250115-000342.pdf"
```

### Release Report
```
PUT /api/v1/reports/{id}/release
X-Branch-Id: {branchId}

Body:
{
  "releasedBy": "uuid-pathologist",
  "deliveryChannels": ["SMS", "EMAIL", "WHATSAPP"]
}

Response:
{
  "success": true,
  "message": "Report released and delivery initiated",
  "data": {
    "reportId": "uuid",
    "status": "RELEASED",
    "deliveryStatus": {
      "SMS": "QUEUED",
      "EMAIL": "QUEUED",
      "WHATSAPP": "QUEUED"
    }
  },
  "timestamp": "2025-01-15T12:05:00Z"
}
```

## Database Changes
- `reports` row created (status progresses through GENERATING → GENERATED → RELEASED → DELIVERED)
- `reports.storage_path` set to MinIO object key
- `report_deliveries` rows created per channel with delivery status
- `orders.status` updated to REPORT_GENERATED then DELIVERED
- `notification_queue` events published for each delivery channel
- `audit_trail` entry logged

## Validation Rules
1. All tests in the order must be in AUTHORIZED status
2. Report template must exist for the department
3. Branch report settings (header, logo) must be configured
4. Pathologist must release before delivery
5. Patient must have at least one contact method for delivery
6. Amended reports must include amendment note and supersede previous version

## Error Scenarios
| Scenario                          | Error Code          | Message                                  |
|----------------------------------|---------------------|------------------------------------------|
| Not all results authorized        | RPT_INCOMPLETE      | Not all test results are authorized      |
| Template not found                | RPT_NO_TEMPLATE     | Report template not found for department |
| MinIO storage failure             | RPT_STORAGE_FAIL    | Failed to store report in object storage |
| No delivery contact               | RPT_NO_CONTACT      | Patient has no email or phone for delivery |
| Report already delivered          | RPT_ALREADY_DLVRD   | Report has already been delivered        |

## State Transition
```
Report:  PENDING ──► GENERATING ──► GENERATED ──► RELEASED ──► DELIVERED
                                                                    │
                                                               (if amended)
                                                                    │
                                                                    ▼
                                                               AMENDED ──► RECALLED
                                                                    │
                                                                    ▼
                                                              (new report generated)
```

## Related Documentation
- [Result Authorization](07-result-authorization.md)
- [Billing & Payment](09-billing-payment.md)
- [Report Generation Engine](../architecture/report-generation-engine.md)
- [Notification Architecture](../architecture/notification-architecture.md)
- [Workflow State Machines](../architecture/workflow-state-machines.md)
