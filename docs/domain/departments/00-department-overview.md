# Department Overview — RasterOneLab LIS

## All 11 Departments

| # | Department | Code | Result Entry Type | Report Style |
|---|-----------|------|------------------|-------------|
| 1 | Biochemistry | BIO | Numeric tabular grid | Tabular: Parameter/Result/Unit/Flag/Range |
| 2 | Hematology | HEM | Numeric CBC + differential % + narrative | Multi-section: table + scatter images + smear narrative |
| 3 | Microbiology | MIC | Multi-stage culture + antibiogram matrix | Narrative + antibiogram table |
| 4 | Histopathology | HIS | Structured narrative + images | Pure narrative with embedded images |
| 5 | Clinical Pathology | CLI | Semi-quantitative + numeric | Tabular with symbols (+/-) |
| 6 | Serology/Immunology | SER | Qualitative + titer + numeric | Tabular with Reactive/Non-Reactive |
| 7 | Molecular Biology | MOL | CT values + qualitative + curves | Tabular with amplification curve |
| 8 | Cytology | CYT | Structured narrative + Bethesda | Narrative with classification |
| 9 | Toxicology | TOX | Quantitative + qualitative with cutoff | Tabular with interpretation |
| 10 | Genetics | GEN | Karyotype + narrative + images | Narrative with karyogram |
| 11 | Immunohematology | IMH | Blood grouping + crossmatch | Structured form-style |

## Result Entry UI by Department

| Department | Entry UI Pattern |
|-----------|----------------|
| Biochemistry | Spreadsheet-like grid, Tab through cells, auto-calculate |
| Hematology | CBC grid + differential % (must = 100%) + image upload + free text |
| Microbiology | Day-by-day stages: preliminary → organism → sensitivity grid |
| Histopathology | Rich text editor + image upload + structured sections |
| Clinical Pathology | Dropdown for semi-quant + text fields |
| Serology | Dropdown (Reactive/Non-Reactive/Equivocal) + numeric if applicable |
| Molecular Biology | CT value entry + detected/not detected toggle |
| Cytology | Rich text + classification dropdown (Bethesda system) |
| Toxicology | Numeric entry + cutoff comparison + interpretation |
| Genetics | Structured narrative + karyotype upload |
| Immunohematology | Form-based matrix for blood grouping results |

## Data Model: Result Entity Inheritance

```java
// Base result
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "test_result")
public abstract class TestResult extends BaseEntity {
    private UUID orderId;
    private UUID patientId;
    private UUID parameterId;
    private ResultStatus status;
    // ...
}

// Biochemistry (numeric)
@Entity
@Table(name = "numeric_result")
public class NumericResult extends TestResult {
    private BigDecimal numericValue;
    private String unit;
    private BigDecimal lowNormal;
    private BigDecimal highNormal;
    private String flag;  // H, L, HH, LL, *
}

// Microbiology (culture)
@Entity
@Table(name = "culture_result")
public class CultureResult extends TestResult {
    private String specimenDescription;
    private String macroscopicAppearance;
    private List<OrganismResult> organisms;
    private Boolean noGrowth;
    private String comments;
}

// Histopathology (narrative)
@Entity
@Table(name = "narrative_result")
public class NarrativeResult extends TestResult {
    private String grossExamination;
    private String microscopicExamination;
    private String diagnosis;
    private String icd10Code;
    private List<String> imageUrls;  // MinIO paths
}
```

## TAT (Turn-Around Time) by Department

| Department | ROUTINE TAT | STAT TAT | Critical Priority |
|-----------|------------|----------|-----------------|
| Biochemistry | 4 hours | 1 hour | 30 minutes |
| Hematology | 2 hours | 45 minutes | 30 minutes |
| Microbiology | 5-7 days | 24 hours | Interim 24h |
| Histopathology | 5-7 days | 48 hours | 24 hours |
| Serology | 4 hours | 2 hours | 1 hour |
| Molecular Biology | 24-48 hours | 12 hours | 4 hours |
| Clinical Pathology | 2 hours | 1 hour | 30 minutes |
