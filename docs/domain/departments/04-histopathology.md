# Histopathology Department — RasterOneLab LIS

## Overview

- **Code**: HISTO
- **Sample Types**: Biopsy (punch, incisional, excisional), Surgical specimens (resections), FNAC, Fluid cytology (pleural, ascitic, CSF), PAP smear
- **Primary Instruments**: Leica tissue processor, microtome, cryostat (frozen section), microscope with digital imaging
- **TAT**: Routine 3-5 working days, Frozen section 30 minutes, IHC 5-7 days
- **Result Entry**: NARRATIVE type with rich text editor + image uploads + CAP synoptic templates

## Multi-Stage Workflow

### Stage 1: Accessioning & Grossing

Pathologist/grossing technician records:
- Specimen type and source site
- Number of tissue pieces received
- Gross measurements (cm × cm × cm)
- Gross description (narrative: color, consistency, margins, lesion size)
- Number of cassettes submitted
- Photographs of gross specimen (uploaded to MinIO)

### Stage 2: Processing & Embedding

Lab tracks processing status:
- Tissue processor run ID (overnight processing)
- Embedding station confirmation
- Special fixation notes (e.g., decalcification required)

### Stage 3: Sectioning & Staining

- Section thickness: 3-5 μm
- Routine H&E staining for all blocks
- Special stains ordered if needed (see Special Stains table)
- IHC panel requests linked to order

### Stage 4: Microscopy & Reporting

Pathologist enters structured report:
- **Gross Description**: narrative text (carried from Stage 1)
- **Microscopic Description**: detailed narrative with formatting
- **Final Diagnosis**: structured text (one line per diagnosis)
- **Comments/Notes**: ancillary findings, recommendations
- **Images**: microphotographs attached (H&E, IHC stains)

## Special Stains & IHC Panel

| Stain | Code | Category | Common Indication |
|-------|------|----------|-------------------|
| H&E | HE | Routine | All specimens |
| PAS | PAS | Special | Glycogen, fungi, basement membrane |
| Masson Trichrome | MT | Special | Fibrosis grading |
| Reticulin | RET | Special | Liver architecture, lymphoma |
| Ziehl-Neelsen | ZN | Special | Acid-fast bacilli (TB) |
| Congo Red | CR | Special | Amyloid deposits |
| Iron (Perl's) | IRON | Special | Hemosiderin deposits |
| Ki-67 | KI67 | IHC | Proliferation index |
| ER | ER | IHC | Breast cancer — estrogen receptor |
| PR | PR | IHC | Breast cancer — progesterone receptor |
| HER2 | HER2 | IHC | Breast cancer — targeted therapy |
| CK7 | CK7 | IHC | Epithelial origin / metastatic workup |
| CK20 | CK20 | IHC | Epithelial origin / metastatic workup |
| CD20 | CD20 | IHC | B-cell lymphoma |
| CD3 | CD3 | IHC | T-cell lymphoma |
| p53 | P53 | IHC | Tumor suppressor mutation |
| S100 | S100 | IHC | Melanoma, nerve sheath tumors |

## CAP Synoptic Reporting Templates

Structured templates based on College of American Pathologists (CAP) cancer protocols:

### Breast Carcinoma (Example)

| Field | Options |
|-------|---------|
| Specimen Type | Lumpectomy / Mastectomy / Core biopsy |
| Tumor Site | Upper outer / Upper inner / Lower outer / Lower inner / Central |
| Histologic Type | Invasive ductal (NST) / Invasive lobular / DCIS / Other |
| Histologic Grade | Nottingham Grade 1 / 2 / 3 |
| Tumor Size | Greatest dimension (cm) |
| Margins | Negative / Positive (specify distance mm) |
| Lymphovascular Invasion | Present / Absent / Indeterminate |
| Lymph Node Status | pN0 / pN1 / pN2 / pN3 (number positive / total) |
| ER Status | Positive (Allred score) / Negative |
| PR Status | Positive (Allred score) / Negative |
| HER2 Status | Positive (3+) / Equivocal (2+) / Negative (0, 1+) |
| Ki-67 Index | Percentage (%) |
| pTNM Stage | Composite staging |

## Data Model

```java
@Entity
@Table(name = "histopathology_result")
public class HistopathologyResult extends BaseEntity {
    private UUID orderLineItemId;
    private String specimenType;              // BIOPSY, EXCISION, FNAC, FLUID_CYTOLOGY
    private String specimenSite;
    private String clinicalHistory;

    // Grossing
    private String grossDescription;          // Rich text narrative
    private Integer numberOfCassettes;
    private String grossMeasurements;

    // Microscopy
    private String microscopicDescription;    // Rich text narrative
    private String finalDiagnosis;            // Structured text
    private String comments;
    private String icdCode;                   // ICD-O morphology code

    // CAP Synoptic (JSON for flexibility)
    @Column(columnDefinition = "jsonb")
    private String synopticReport;            // CAP template fields as JSON

    // Images (MinIO paths)
    @ElementCollection
    private List<String> grossImageUrls;
    @ElementCollection
    private List<String> microImageUrls;

    // Workflow tracking
    private LocalDateTime grossingCompletedAt;
    private LocalDateTime processingCompletedAt;
    private LocalDateTime sectioningCompletedAt;
    private LocalDateTime reportingCompletedAt;
    private Boolean isFrozenSection;
    private ResultStatus status;              // DRAFT, PRELIMINARY, FINAL, AMENDED
}
```

## Report Print Style

Multi-section PDF with departmental letterhead:
1. **Patient & Specimen Info**: Demographics, specimen type, site, clinical history
2. **Gross Description**: Narrative paragraph
3. **Microscopic Description**: Narrative paragraph with sub-headings
4. **Special Stains / IHC Results**: Table of stain results (if performed)
5. **Final Diagnosis**: Bold, numbered list of diagnoses
6. **Synoptic Report**: CAP-format table (if applicable for malignancies)
7. **Images**: Embedded microphotographs with captions (2 per row)
8. **Pathologist Signature**: Digital signature with qualification and registration number

## QC Notes

- **Tissue fixation**: Minimum 6 hours in 10% neutral buffered formalin (10:1 formalin-to-tissue ratio)
- **IHC controls**: Positive and negative tissue controls run with every IHC batch
- **Frozen section concordance**: Track frozen-to-permanent discrepancy rate (target <2%)
- **Turnaround time monitoring**: Dashboard alerts for specimens exceeding 5-day TAT
- **Amended reports**: Full audit trail with original and amended diagnosis, reason for amendment
- **Peer review**: Random case review (minimum 10% of malignant diagnoses) for quality assurance
