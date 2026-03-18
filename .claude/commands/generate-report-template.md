# Generate Lab Report Template — /generate-report-template

Generate a complete lab report template with PDF generation service.

## Usage
```
/generate-report-template {department} {templateType}
```

Departments: `biochemistry`, `hematology`, `microbiology`, `histopathology`, `serology`, `molecular`

## What Gets Generated

### 1. PDF Report Service
```java
@Service
public class BiochemistryReportService implements DepartmentReportService {
    private final BranchTemplateService branchTemplateService;
    private final MinioService minioService;
    private final QrCodeService qrCodeService;
    private final BarcodeService barcodeService;

    public BiochemistryReportService(BranchTemplateService branchTemplateService,
                                     MinioService minioService,
                                     QrCodeService qrCodeService,
                                     BarcodeService barcodeService) {
        this.branchTemplateService = branchTemplateService;
        this.minioService = minioService;
        this.qrCodeService = qrCodeService;
        this.barcodeService = barcodeService;
    }
    
    @Override
    public byte[] generateReport(ReportContext context) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        
        document.open();
        addBranchHeader(document, context.getBranchConfig());
        addPatientInfo(document, context.getPatient(), context.getOrder());
        addResultsTable(document, context.getResults());
        addReferenceRanges(document, context.getReferenceRanges());
        addFooter(document, context.getPathologist());
        addQrCode(document, writer, context.getReportId());
        addBarcode(document, context.getOrderBarcode());
        addWatermark(writer, context.getStatus());
        document.close();
        
        String minioPath = minioService.upload(outputStream.toByteArray(), generatePath(context));
        return outputStream.toByteArray();
    }
    
    private void addBranchHeader(Document doc, BranchConfig config) {
        // Logo, branch name, address, license number, NABL accreditation
    }
    
    private void addResultsTable(Document doc, List<BiochemistryResult> results) {
        PdfPTable table = new PdfPTable(5);
        table.setWidths(new float[]{3f, 2f, 1.5f, 1f, 2.5f});
        addHeaderRow(table, "Parameter", "Result", "Unit", "Flag", "Reference Range");
        
        for (var result : results) {
            String flag = calculateFlag(result);
            Color cellColor = getFlagColor(flag);
            addDataRow(table, result.getParameterName(), formatValue(result), result.getUnit(), flag, 
                      formatRange(result.getLowNormal(), result.getHighNormal()), cellColor);
        }
        doc.add(table);
    }
}
```

### 2. Branch-Specific Template Configuration
```java
@Entity
public class BranchReportTemplate extends BaseEntity {
    private String logoPath;          // MinIO path
    private String headerHtml;        // Custom HTML header
    private String footerHtml;        // Custom HTML footer
    private String primaryColor;      // Brand color (hex)
    private Boolean showNablLogo;     // NABL accreditation logo
    private String pathologistName;   // Default authorized signatory
    private String pathologistSignaturePath; // Digital signature
    private Boolean showBarcode;
    private Boolean showQrCode;
    private String reportDisclaimer;
    private String watermarkText;     // e.g., "PRELIMINARY", "AMENDED"
}
```

### 3. Report Template Features
- **Branch Header**: Logo, name, address, phone, email, lab license, NABL number
- **Patient Section**: Name, UHID, age/gender, referring doctor, collection date/time
- **Order Section**: Order ID, barcode, received date, reported date, TAT indicator
- **Results Table**: Parameter | Result | Unit | H/L Flag | Reference Range
- **Graphical Elements**: CBC scatter plots as images, QC charts
- **Footer**: Pathologist name, signature, designation, report validity note
- **QR Code**: Encodes report URL for verification
- **Barcode**: Order/sample barcode (Code 128)
- **Digital Signature**: Pathologist scanned signature image
- **Watermark**: PRELIMINARY/AMENDED/DUPLICATE watermarks
- **Page Numbers**: Page X of Y
- **Report Versioning**: Version number for amended reports

### 4. Report Storage (MinIO)
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
