# Clinical Pathology Department — RasterOneLab LIS

## Overview

- **Code**: CLINPATH
- **Sample Types**: Urine (random, midstream, 24-hour), Stool, CSF, Synovial fluid, Pleural fluid, Ascitic fluid, Semen
- **Primary Instruments**: Sysmex UF-5000 (urine), manual microscopy, Neubauer counting chamber
- **Result Entry**: SEMI_QUANTITATIVE type (+/−, +, ++, +++) with structured fields and free text

## Result Entry UI Behavior

1. Structured form with dropdowns and semi-quantitative selectors
2. Physical parameters entered first (color, appearance, SG, pH)
3. Chemical parameters auto-populated from dipstick reader or manual entry
4. Microscopic findings entered with semi-quantitative grading
5. Free text box for additional observations
6. Auto-flagging for abnormal results (e.g., RBC in urine > 5/HPF)

## Urinalysis Parameters (30+ Parameters)

### Physical Examination

| Parameter | Code | Data Type | Options / Unit | Method |
|-----------|------|-----------|---------------|--------|
| Color | UR_COLOR | DROPDOWN | Pale Yellow / Yellow / Dark Yellow / Amber / Red / Brown / Colorless | Visual |
| Appearance | UR_APP | DROPDOWN | Clear / Slightly Turbid / Turbid / Cloudy | Visual |
| Specific Gravity | UR_SG | DECIMAL | 1.001–1.035 | Refractometer / Dipstick |
| pH | UR_PH | DECIMAL | 4.5–8.5 | Dipstick |
| Volume (24h) | UR_VOL | INTEGER | mL | Measured |

### Chemical Examination (Dipstick)

| Parameter | Code | Data Type | Scale | LOINC |
|-----------|------|-----------|-------|-------|
| Glucose | UR_GLU | SEMI_QUANTITATIVE | Nil / Trace / + / ++ / +++ / ++++ | 2349-9 |
| Protein | UR_PRO | SEMI_QUANTITATIVE | Nil / Trace / + / ++ / +++ / ++++ | 2888-6 |
| Blood | UR_BLD | SEMI_QUANTITATIVE | Nil / Trace / + / ++ / +++ | 5794-3 |
| Ketones | UR_KET | SEMI_QUANTITATIVE | Nil / Trace / + / ++ / +++ | 2514-8 |
| Bilirubin | UR_BIL | SEMI_QUANTITATIVE | Nil / + / ++ / +++ | 1977-8 |
| Urobilinogen | UR_URO | SEMI_QUANTITATIVE | Normal / + / ++ | 20405-7 |
| Nitrite | UR_NIT | QUALITATIVE | Positive / Negative | 2349-9 |
| Leukocyte Esterase | UR_LE | SEMI_QUANTITATIVE | Nil / Trace / + / ++ / +++ | 5799-2 |

### Microscopic Examination

| Parameter | Code | Data Type | Unit | Reference |
|-----------|------|-----------|------|-----------|
| RBC | UR_MRBC | SEMI_QUANTITATIVE | /HPF | 0–2/HPF |
| WBC | UR_MWBC | SEMI_QUANTITATIVE | /HPF | 0–5/HPF |
| Epithelial Cells | UR_EPI | SEMI_QUANTITATIVE | /HPF | Few |
| Casts (Hyaline) | UR_HYCAST | SEMI_QUANTITATIVE | /LPF | Occasional |
| Casts (Granular) | UR_GRCAST | SEMI_QUANTITATIVE | /LPF | Nil |
| Casts (RBC) | UR_RBCCAST | SEMI_QUANTITATIVE | /LPF | Nil |
| Crystals (Calcium Oxalate) | UR_CAOX | SEMI_QUANTITATIVE | /HPF | Few |
| Crystals (Uric Acid) | UR_UACRYS | SEMI_QUANTITATIVE | /HPF | Few |
| Bacteria | UR_BACT | SEMI_QUANTITATIVE | /HPF | Nil |
| Yeast | UR_YEAST | SEMI_QUANTITATIVE | /HPF | Nil |

## Stool Examination Parameters

| Parameter | Code | Data Type | Options / Unit | Method |
|-----------|------|-----------|---------------|--------|
| Color | ST_COLOR | DROPDOWN | Brown / Yellow / Green / Black / Red / Clay / Pale | Visual |
| Consistency | ST_CONS | DROPDOWN | Formed / Semi-formed / Soft / Loose / Watery / Mucoid | Visual |
| Occult Blood | ST_OB | QUALITATIVE | Positive / Negative | Immunochemical |
| Reducing Substances | ST_RS | SEMI_QUANTITATIVE | Nil / Trace / + / ++ | Benedict's |
| Ova | ST_OVA | QUALITATIVE | Seen (specify) / Not seen | Microscopy |
| Cysts | ST_CYST | QUALITATIVE | Seen (specify) / Not seen | Microscopy |
| Parasites | ST_PARA | QUALITATIVE | Seen (specify) / Not seen | Microscopy |
| RBC | ST_RBC | SEMI_QUANTITATIVE | /HPF | Microscopy |
| WBC | ST_WBC | SEMI_QUANTITATIVE | /HPF | Microscopy |
| Fat Globules | ST_FAT | SEMI_QUANTITATIVE | Nil / + / ++ / +++ | Sudan III stain |

## CSF Analysis Parameters

| Parameter | Code | Data Type | Unit | Reference |
|-----------|------|-----------|------|-----------|
| Appearance | CSF_APP | DROPDOWN | Clear / Turbid / Xanthochromic / Bloody | - |
| Total Cell Count | CSF_TCC | INTEGER | cells/μL | 0–5 |
| Differential (Lymphocytes) | CSF_LYM | PERCENTAGE | % | 60–70% |
| Differential (Neutrophils) | CSF_NEU | PERCENTAGE | % | 0–5% |
| Protein | CSF_PRO | DECIMAL | mg/dL | 15–45 |
| Glucose | CSF_GLU | DECIMAL | mg/dL | 40–70 |
| Chloride | CSF_CL | DECIMAL | mEq/L | 118–132 |
| Gram Stain | CSF_GS | NARRATIVE | - | No organisms |
| India Ink | CSF_INK | QUALITATIVE | Positive / Negative | Negative |

## Semen Analysis Parameters (WHO 2021 Criteria)

| Parameter | Code | Data Type | Unit | WHO 2021 Reference |
|-----------|------|-----------|------|-------------------|
| Abstinence Period | SA_ABST | INTEGER | days | 2–7 |
| Volume | SA_VOL | DECIMAL | mL | ≥1.4 |
| pH | SA_PH | DECIMAL | - | ≥7.2 |
| Liquefaction Time | SA_LIQ | INTEGER | minutes | ≤60 |
| Appearance | SA_APP | DROPDOWN | Normal / Abnormal | Opalescent grey |
| Viscosity | SA_VISC | DROPDOWN | Normal / Increased | Normal |
| Concentration | SA_CONC | DECIMAL | ×10⁶/mL | ≥16 |
| Total Sperm Count | SA_TSC | DECIMAL | ×10⁶ | ≥39 |
| Progressive Motility | SA_PMOT | INTEGER | % | ≥30 |
| Total Motility | SA_TMOT | INTEGER | % | ≥42 |
| Non-Progressive Motility | SA_NPMOT | INTEGER | % | - |
| Immotile | SA_IMMOT | INTEGER | % | - |
| Vitality (Live) | SA_VIT | INTEGER | % | ≥54 |
| Normal Morphology | SA_MORPH | INTEGER | % | ≥4 (strict criteria) |
| WBC (Round Cells) | SA_WBC | DECIMAL | ×10⁶/mL | <1.0 |
| Fructose | SA_FRUC | QUALITATIVE | Positive / Negative | Positive |

## Data Model

```java
@Entity
@Table(name = "clinical_pathology_result")
public class ClinicalPathologyResult extends BaseEntity {
    private UUID orderLineItemId;
    private String testCategory;              // URINALYSIS, STOOL, CSF, SEMEN, BODY_FLUID

    // Stored as structured JSON per category
    @Column(columnDefinition = "jsonb")
    private String physicalExamination;       // Color, appearance, SG, pH
    @Column(columnDefinition = "jsonb")
    private String chemicalExamination;       // Dipstick results
    @Column(columnDefinition = "jsonb")
    private String microscopicExamination;    // Cells, casts, crystals

    private String additionalFindings;        // Free text
    private ResultStatus status;

    @OneToOne
    @JoinColumn(name = "order_line_item_id")
    private OrderLineItem orderLineItem;
}
```

## Report Print Style

Tabular PDF with semi-quantitative scale legend:
1. **Physical Examination**: Key-value table (Color, Appearance, SG, pH)
2. **Chemical Examination**: Parameter | Result | Reference — with abnormal highlighting
3. **Microscopic Examination**: Parameter | Result (/HPF or /LPF) | Reference
4. **Scale Legend**: Nil = None detected, Trace = Barely detectable, + = Mild, ++ = Moderate, +++ = Marked, ++++ = Severe
5. **Additional Findings**: Free text (e.g., parasite species identification)

## QC Notes

- **Dipstick QC**: Run positive and negative controls at start of each shift
- **Microscopy standardization**: Centrifuge protocol (400g × 5 min, discard supernatant, resuspend in 0.5 mL)
- **Semen analysis**: WHO 2021 external QA program participation, strict Kruger criteria for morphology
- **CSF urgency**: CSF samples processed immediately upon receipt (STAT priority)
- **24-hour urine**: Volume validation — reject if total volume <300 mL (likely incomplete collection)
- **Specimen rejection criteria**: Unlabeled containers, leaked specimens, excessive delay (>2 hours for urine microscopy)
