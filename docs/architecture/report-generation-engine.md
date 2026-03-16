# Report Generation Engine — RasterOneLab LIS

## Overview

Reports are generated as PDFs using OpenPDF (fork of iText 2.x, LGPL licensed), stored in MinIO, and delivered via SMS/Email/WhatsApp.

## PDF Generation Flow

```
AuthorizationEvent received
    → Load all results for order
    → Load branch report template config
    → Load patient + order details
    → Select department-specific layout
    → Generate PDF (OpenPDF)
    → Add QR code (ZXing)
    → Add barcode (order barcode)
    → Add pathologist signature (image)
    → Add watermark if applicable (PRELIMINARY, AMENDED)
    → Store in MinIO
    → Update report record (pdf_path, status=GENERATED)
    → Publish ReportGeneratedEvent → notification queue
```

## Department-Specific Layouts

| Department | Layout | Special Elements |
|-----------|--------|-----------------|
| Biochemistry | Tabular: Parameter/Result/Unit/Flag/Range | H/L flags in colored cells |
| Hematology | Table + Images + Narrative | CBC scatter plot images |
| Microbiology | Antibiogram table + narrative | S/I/R color coding |
| Histopathology | Pure narrative with images | Embedded gross/microscopic images |
| Clinical Pathology | Tabular +/- scale | Semi-quantitative symbols |
| Serology | Reactive/Non-Reactive table | Titer values |

## PDF Report Structure

```
Page 1+:
┌─────────────────────────────────────────────────────────┐
│  [Branch Logo]  BRANCH NAME                             │
│  Address Line 1, City | Phone | Email                   │
│  Lab License: XXXXX | NABL: MC-XXXXX                    │
├─────────────────────────────────────────────────────────┤
│  Patient: John Doe          │  Order ID: ORD-DEL-...    │
│  UHID: DEL-002345          │  Collected: 15 Jan 2024    │
│  Age/Gender: 45Y/Male      │  Received: 15 Jan 2024     │
│  Ref Doctor: Dr. Smith     │  Reported: 15 Jan 2024     │
├─────────────────────────────────────────────────────────┤
│  DEPARTMENT: BIOCHEMISTRY                               │
│                                                         │
│  Parameter      Result   Unit    Flag  Reference Range  │
│  ─────────────────────────────────────────────────────  │
│  Glucose        110.0    mg/dL         70 - 100         │
│  Creatinine     1.1      mg/dL         0.6 - 1.2       │
│  ...                                                    │
├─────────────────────────────────────────────────────────┤
│  AUTHORIZED BY:                                         │
│  Dr. ABC, MD (Pathology)        [Digital Signature]     │
│  Registration No: XXXXXX                               │
│  Date: 15 Jan 2024 14:30                               │
├─────────────────────────────────────────────────────────┤
│  [QR Code]  Scan to verify | This report is computer   │
│             generated. Page 1 of 1                     │
└─────────────────────────────────────────────────────────┘
```

## MinIO Storage Structure

```
reports/
└── {branchId}/
    └── {year}/
        └── {month}/
            └── {orderId}/
                ├── report-v1.pdf
                ├── report-v2.pdf  (if amended)
                └── metadata.json
```

## Report Versioning

- Every amendment creates new version: v1, v2, v3...
- Old versions stored but not shown to patient/doctor
- Admin can access all versions
- Amendment reason mandatory

## Branch-Specific Customization

```java
@Entity
public class BranchReportTemplate extends BaseEntity {
    private String logoPath;            // MinIO path to logo
    private String primaryColor;        // Brand color (#hex)
    private String pathologistName;
    private String pathologistSignaturePath; // MinIO path
    private Boolean showNablLogo;
    private Boolean showQrCode;
    private String reportDisclaimer;
    private String headerHtml;          // Custom HTML header
    private String footerHtml;          // Custom HTML footer
}
```

## QR Code Content

QR code encodes a URL for report verification:
```
https://verify.rasteronelab.com/reports/{reportId}?hash={sha256hash}
```

Verification page shows:
- Patient name (masked: J*** D***)
- Test names
- Date of report
- Branch name
- "AUTHENTIC" if hash matches
