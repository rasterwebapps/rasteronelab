# Molecular Biology Department — RasterOneLab LIS

## Overview

- **Code**: MOLBIO
- **Sample Types**: EDTA Whole Blood, Plasma (EDTA), Nasopharyngeal swab (VTM), Sputum, Tissue (FFPE block), Cervical swab
- **Primary Instruments**: Roche LightCycler 480, Applied Biosystems 7500, Cepheid GeneXpert, Abbott m2000
- **Result Entry**: CT_VALUES + QUALITATIVE type (CT values with Detected/Not Detected interpretation, viral load quantification)

## Result Entry UI Behavior

1. CT values entered per target gene (numeric, 2 decimal places)
2. Internal control CT validated first — reject if IC fails (CT > 35 or absent)
3. Qualitative result auto-calculated from CT cutoff (configurable per assay)
4. Quantitative viral load entered in IU/mL or copies/mL for applicable tests
5. Log₁₀ value auto-calculated from viral load
6. Amplification curve image uploaded from instrument export (stored in MinIO)
7. Batch entry mode: 96-well plate layout with sample mapping

## Common Tests & Parameters

### COVID-19 RT-PCR

| Parameter | Code | Data Type | Cutoff | Target Gene |
|-----------|------|-----------|--------|-------------|
| SARS-CoV-2 E gene | COV_E | CT_VALUE | ≤35 | Envelope |
| SARS-CoV-2 RdRp gene | COV_RDRP | CT_VALUE | ≤35 | RNA-dependent RNA polymerase |
| SARS-CoV-2 N gene | COV_N | CT_VALUE | ≤35 | Nucleocapsid |
| Internal Control (RNase P) | COV_IC | CT_VALUE | ≤35 | Human RNase P |
| Final Result | COV_RES | QUALITATIVE | - | Detected / Not Detected / Indeterminate |

**Interpretation Rules:**
- ≥2 target genes CT ≤35 AND IC ≤35 → **Detected (Positive)**
- All target genes CT >35 AND IC ≤35 → **Not Detected (Negative)**
- 1 target gene CT ≤35 AND IC ≤35 → **Indeterminate (Repeat)**
- IC >35 or absent → **Invalid (Repeat with fresh sample)**

### Hepatitis B DNA (Quantitative)

| Parameter | Code | Data Type | Unit | Detection Range |
|-----------|------|-----------|------|-----------------|
| HBV DNA Viral Load | HBV_VL | DECIMAL | IU/mL | 10–1,000,000,000 |
| HBV DNA Log₁₀ | HBV_LOG | DECIMAL | Log IU/mL | 1.0–9.0 |
| HBV Result | HBV_RES | QUALITATIVE | - | Detected / Not Detected / Below LLOQ |

### Hepatitis C RNA (Quantitative)

| Parameter | Code | Data Type | Unit | Detection Range |
|-----------|------|-----------|------|-----------------|
| HCV RNA Viral Load | HCV_VL | DECIMAL | IU/mL | 12–100,000,000 |
| HCV RNA Log₁₀ | HCV_LOG | DECIMAL | Log IU/mL | 1.08–8.0 |
| HCV Result | HCV_RES | QUALITATIVE | - | Detected / Not Detected / Below LLOQ |

### HIV-1 RNA Viral Load

| Parameter | Code | Data Type | Unit | Detection Range |
|-----------|------|-----------|------|-----------------|
| HIV-1 RNA Viral Load | HIV_VL | DECIMAL | copies/mL | 20–10,000,000 |
| HIV-1 RNA Log₁₀ | HIV_LOG | DECIMAL | Log copies/mL | 1.30–7.0 |
| HIV-1 Result | HIV_RES | QUALITATIVE | - | Detected / Not Detected / Target Not Detected |

### HPV Genotyping

| Parameter | Code | Data Type | Unit | Notes |
|-----------|------|-----------|------|-------|
| HPV Screen | HPV_SCR | QUALITATIVE | - | Detected / Not Detected |
| HPV 16 | HPV_16 | QUALITATIVE | - | High-risk genotype |
| HPV 18 | HPV_18 | QUALITATIVE | - | High-risk genotype |
| Other HR-HPV | HPV_OHR | QUALITATIVE | - | Types 31, 33, 35, 39, 45, 51, 52, 56, 58, 59, 66, 68 |

### TB GeneXpert (MTB/RIF)

| Parameter | Code | Data Type | Unit | Notes |
|-----------|------|-----------|------|-------|
| MTB Result | MTB_RES | QUALITATIVE | - | Detected / Not Detected / Invalid |
| MTB Semiquantitative | MTB_SQ | DROPDOWN | - | High / Medium / Low / Very Low / Trace |
| Rifampicin Resistance | MTB_RIF | QUALITATIVE | - | Detected / Not Detected / Indeterminate |
| Probe CT Values | MTB_CT | CT_VALUE | - | Individual probe CT values (A–E) |

## CT Value Interpretation Logic

```java
@Service
public class CtValueInterpretationService {

    private static final BigDecimal IC_CUTOFF = new BigDecimal("35");

    public String interpretCtValue(BigDecimal ctValue, BigDecimal cutoff, BigDecimal icCtValue) {
        // Step 1: Validate internal control
        if (icCtValue == null || icCtValue.compareTo(IC_CUTOFF) > 0) {
            return "INVALID";
        }

        // Step 2: Interpret target
        if (ctValue == null || ctValue.compareTo(BigDecimal.ZERO) == 0) {
            return "NOT_DETECTED";
        }

        if (ctValue.compareTo(cutoff) <= 0) {
            return "DETECTED";
        }

        return "NOT_DETECTED";
    }

    public BigDecimal calculateLogValue(BigDecimal viralLoad) {
        if (viralLoad == null || viralLoad.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        return BigDecimal.valueOf(Math.log10(viralLoad.doubleValue()))
            .setScale(2, RoundingMode.HALF_UP);
    }
}
```

## Data Model

```java
@Entity
@Table(name = "molecular_result")
public class MolecularResult extends BaseEntity {
    private UUID orderLineItemId;
    private String testCode;
    private String assayName;                 // Kit/assay used
    private String instrumentName;

    // CT Values (per target gene)
    @Column(columnDefinition = "jsonb")
    private String ctValues;                  // {"E": 22.5, "RdRp": 24.1, "N": 23.8, "IC": 28.3}

    // Qualitative
    private String qualitativeResult;         // DETECTED, NOT_DETECTED, INDETERMINATE, INVALID

    // Quantitative (viral load)
    private BigDecimal viralLoad;             // IU/mL or copies/mL
    private String viralLoadUnit;             // IU_PER_ML, COPIES_PER_ML
    private BigDecimal logValue;              // Auto-calculated Log₁₀

    // Internal control
    private BigDecimal internalControlCt;
    private Boolean internalControlValid;

    // Amplification curve
    @ElementCollection
    private List<String> amplificationCurveUrls;  // MinIO paths

    // Plate/run info
    private String plateId;
    private String wellPosition;              // A1–H12
    private String runId;
    private LocalDateTime runDateTime;

    private ResultStatus status;

    @OneToOne
    @JoinColumn(name = "order_line_item_id")
    private OrderLineItem orderLineItem;
}
```

## Report Print Style

Structured PDF with clear Detected/Not Detected callout:
1. **Test Information**: Assay name, kit lot, instrument, specimen type
2. **CT Values Table**: Target Gene | CT Value | Cutoff | Interpretation — per gene
3. **Final Result**: Large bold text — **DETECTED** (red) or **NOT DETECTED** (green)
4. **Viral Load** (if quantitative): Value in IU/mL + Log₁₀ value + trend graph (if prior results exist)
5. **Amplification Curve**: Embedded image of fluorescence vs. cycle plot
6. **Internal Control**: IC CT value and validity status
7. **Clinical Note**: Assay sensitivity, detection limit, window period disclaimer
8. **Run Details**: Plate ID, well position, run date/time (footer)

## QC Notes

- **Internal controls**: Every run must include a valid internal control (IC). Results rejected if IC fails
- **External controls**: Positive and negative extraction controls included in every extraction batch
- **EQAS participation**: QCMD or CAP molecular proficiency testing panels (quarterly)
- **Contamination prevention**: Unidirectional workflow (extraction → amplification → detection rooms), dedicated pipettes per zone, UV decontamination
- **LLOQ tracking**: Results below lower limit of quantification (LLOQ) reported as "Detected, below LLOQ" (not as a number)
- **Reagent management**: Kit lot number, expiry date, and storage temperature logged for every run
- **Turnaround time**: COVID-19 results within 24 hours, viral load results within 3-5 working days
- **Repeat testing**: Indeterminate results automatically trigger repeat order in LIS workflow
