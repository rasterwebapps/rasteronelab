# Hematology Department — RasterOneLab LIS

## Overview

- **Code**: HEM
- **Sample Types**: EDTA Whole Blood (Purple tube), Citrate Plasma (Blue tube for coagulation)
- **Primary Instruments**: Sysmex XN-1000/XN-3000, Mindray BC-6000, Horiba Yumizen
- **Result Entry**: Multi-section: CBC numeric grid + Differential % + Scatter plot images + Peripheral smear narrative

## 4 Result Entry Sections

### Section 1: CBC Automated (Numeric)

| Parameter | Code | Type | Decimal | Unit | Critical Low | Critical High |
|-----------|------|------|---------|------|-------------|--------------|
| WBC Count | WBC | DECIMAL | 2 | ×10³/μL | 2.0 | 30.0 |
| RBC Count | RBC | DECIMAL | 2 | ×10⁶/μL | 2.0 | 7.0 |
| Hemoglobin | HGB | DECIMAL | 1 | g/dL | 7.0 | 20.0 |
| Hematocrit | HCT | DECIMAL | 1 | % | 20 | 60 |
| MCV | MCV | DECIMAL | 1 | fL | - | - |
| MCH | MCH | DECIMAL | 1 | pg | - | - |
| MCHC | MCHC | DECIMAL | 1 | g/dL | - | - |
| RDW-CV | RDW | DECIMAL | 1 | % | - | - |
| Platelet Count | PLT | INTEGER | 0 | ×10³/μL | 50 | 1000 |
| MPV | MPV | DECIMAL | 1 | fL | - | - |
| Reticulocytes | RETIC | DECIMAL | 2 | % | - | - |

### Section 2: Differential Count (PERCENTAGE type)

**CRITICAL: All 5 values must sum to 100%**

| Parameter | Code | Reference (Adult) |
|-----------|------|-----------------|
| Neutrophils% | NEU_PCT | 50-70% |
| Lymphocytes% | LYM_PCT | 20-40% |
| Monocytes% | MON_PCT | 2-10% |
| Eosinophils% | EOS_PCT | 1-6% |
| Basophils% | BAS_PCT | 0-1% |

Absolute counts (CALCULATED):
- Neutrophils Abs = WBC × NEU_PCT / 100
- Lymphocytes Abs = WBC × LYM_PCT / 100

### Section 3: Scatter Plots / Histograms (NARRATIVE_WITH_IMAGE)

- WBC scatter plot (X: side scatter, Y: fluorescence)
- RBC histogram
- PLT histogram

Images auto-captured from Sysmex XN via HL7/ASTM or manual upload.
Stored in MinIO, referenced as URLs in result.

### Section 4: Peripheral Blood Smear (NARRATIVE)

Structured narrative with:
- **RBC Morphology Checkboxes**: Normocytic, Microcytic, Macrocytic, Hypochromic, Polychromasia, Anisocytosis, Poikilocytosis
  - Sub-options: Target cells, Schistocytes, Spherocytes, Elliptocytes, Sickle cells, Tear drop cells, Burr cells
- **WBC Morphology**: Hypersegmented neutrophils, Band forms, Toxic granulation, Döhle bodies, Blasts, Atypical lymphocytes
- **Platelet Appearance**: Adequate, Decreased, Increased, Large platelets, Clumps

Free text box for additional findings.

## Reference Ranges (Gender/Age-specific)

### Hemoglobin

| Population | Low | High |
|-----------|-----|------|
| Adult Male | 13.5 | 17.5 g/dL |
| Adult Female | 12.0 | 15.5 g/dL |
| Newborn | 14.0 | 24.0 g/dL |
| Child 6-12 yr | 11.5 | 15.5 g/dL |
| Pregnant | 11.0 | 14.0 g/dL |

## Data Model

```java
@Entity
@Table(name = "hematology_result")
public class HematologyResult extends BaseEntity {
    
    // CBC fields
    private BigDecimal wbc;
    private BigDecimal rbc;
    private BigDecimal hemoglobin;
    private BigDecimal hematocrit;
    private BigDecimal mcv;
    private BigDecimal mch;
    private BigDecimal mchc;
    private BigDecimal rdwCv;
    private BigDecimal platelets;
    
    // Differential (must sum to 100)
    private BigDecimal neutrophilsPct;
    private BigDecimal lymphocytesPct;
    private BigDecimal monocytesPct;
    private BigDecimal eosinophilsPct;
    private BigDecimal basophilsPct;
    
    // Images (MinIO paths)
    @ElementCollection
    private List<String> scatterPlotUrls;
    
    // Peripheral smear
    private String peripheralSmearFindings;  // Free text narrative
    private Boolean hasAtypicalCells;
    private Boolean hasBlasts;
    
    @OneToOne
    @JoinColumn(name = "order_line_item_id")
    private OrderLineItem orderLineItem;
}
```

## Report Print Style

Multi-section PDF:
1. **CBC Table**: Parameter | Result | Unit | Flag | Reference Range
2. **Differential**: Separate section with bar chart visual
3. **Scatter Plots**: Embedded images (2 per row)
4. **Peripheral Smear**: Narrative text section (only if done)
