# Barcode Strategy — RasterOneLab LIS

## Barcode Formats

| Entity | Format | Example |
|--------|--------|---------|
| Patient (UHID) | {BranchCode}-{NNNNNN} | DEL-001234 |
| Test Order | ORD-{BranchCode}-{YYYYMMDD}-{NNNN} | ORD-DEL-20240115-0001 |
| Sample | SMP-{BranchCode}-{YYYYMMDD}-{NNNNNN} | SMP-DEL-20240115-000123 |
| Invoice | INV-{BranchCode}-{YYYYMMDD}-{NNNN} | INV-DEL-20240115-0001 |
| Report | RPT-{BranchCode}-{YYYYMMDD}-{NNN} | RPT-DEL-20240115-001 |

## Barcode Types

| Format | Use | Example |
|--------|-----|---------|
| Code 128 (1D) | Sample tube labels, order barcodes | Linear barcode |
| QR Code (2D) | Report verification, patient wristband | 2D square code |
| Code 39 | Legacy instrument compatibility | Linear barcode |
| DataMatrix | Small labels on tubes | Compact 2D code |

## Tube Label Design (Thermal Printer 40mm × 25mm)

```
┌────────────────────────────────────────────┐
│  John Doe (45Y/M)    DEL-001234            │
│  SMP-DEL-20240115-000123                   │
│  ████████████████████████████ (barcode)    │
│  Glucose, Creatinine, Urea (serum SST)    │
│  Collected: 15 Jan 2024 09:30              │
│  Delhi Main Lab                            │
└────────────────────────────────────────────┘
```

## Report QR Code

The QR code on every report encodes a verification URL:
```
https://verify.rasteronelab.com/r/{reportId}?sig={hmac-sha256}
```

Scanning takes user to verification page showing:
- Patient name (masked: J*** D***)
- Tests included in report
- Authorized by (pathologist name)
- Date and time of authorization
- "✓ AUTHENTIC REPORT" if signature matches
- Branch contact information

## Printer Configuration

| Printer Type | Model | Connection | Label Size |
|-------------|-------|-----------|-----------|
| Tube Label | Zebra GK420d | USB/LAN | 38mm × 13mm |
| Order Label | Zebra GK420t | USB/LAN | 100mm × 50mm |
| Report | HP LaserJet | Network | A4 |
| Wristband | Zebra HC100 | USB | Patient wristband |

## Number Series Generation

```java
@Service
@Transactional
public class NumberSeriesService {

    // Using database sequence for thread-safe, gap-less numbering
    public String generateSampleBarcode() {
        UUID branchId = BranchContextHolder.requireCurrentBranchId();
        BranchNumberSeries series = repository.findByBranchIdWithLock(branchId);
        series.setSampleSeq(series.getSampleSeq() + 1);

        return String.format("%s-%s-%06d",
            series.getSamplePrefix(),
            LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE),
            series.getSampleSeq());
    }
}
```
