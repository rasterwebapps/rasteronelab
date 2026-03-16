# Serology & Immunology Department — RasterOneLab LIS

## Overview

- **Code**: SERO
- **Sample Types**: Serum (Gold SST), Plasma (EDTA for some viral markers), Whole Blood (rapid card tests)
- **Primary Instruments**: Abbott Architect i1000, Roche Cobas e411, Bio-Rad ELISA reader, Rapid test card readers
- **Result Entry**: QUALITATIVE + RATIO type (Reactive/Non-Reactive with titer or signal-to-cutoff ratio)

## Result Entry UI Behavior

1. Qualitative result selected first: Reactive / Non-Reactive / Borderline / Indeterminate
2. Quantitative fields enabled based on test: titer, ratio, or index value
3. Signal-to-cutoff (S/CO) ratio auto-calculated for ELISA/CLIA tests
4. Interpretation auto-populated from cutoff rules (configurable per kit/method)
5. Reflex testing triggered for confirmatory tests (e.g., reactive HIV screen → Western Blot)
6. Batch entry mode for screening panels (e.g., blood bank pre-transfusion)

## Infectious Disease Markers

| Parameter | Code | Data Type | Method | Sample | LOINC |
|-----------|------|-----------|--------|--------|-------|
| HIV 1&2 Ab/Ag (Screen) | HIV_SCR | QUALITATIVE | ECLIA (4th gen) | Serum | 56888-1 |
| HIV Confirmatory | HIV_CONF | QUALITATIVE | Western Blot / NAT | Serum | 7918-6 |
| HBsAg | HBSAG | QUALITATIVE | ECLIA | Serum | 5196-1 |
| HBsAg Quantitative | HBSAG_Q | DECIMAL (IU/mL) | ECLIA | Serum | 75409-2 |
| Anti-HBs | AHBS | DECIMAL (mIU/mL) | ECLIA | Serum | 22322-2 |
| HBeAg | HBEAG | QUALITATIVE | ECLIA | Serum | 13954-3 |
| Anti-HBe | AHBE | QUALITATIVE | ECLIA | Serum | 13955-0 |
| Anti-HBc (Total) | AHBC | QUALITATIVE | ECLIA | Serum | 16935-9 |
| Anti-HBc IgM | AHBC_M | QUALITATIVE | ECLIA | Serum | 31204-1 |
| Anti-HCV | HCV | QUALITATIVE | ECLIA | Serum | 16128-1 |
| Dengue NS1 Ag | DEN_NS1 | QUALITATIVE | ELISA / Rapid | Serum | 75377-1 |
| Dengue IgM | DEN_IGM | QUALITATIVE | ELISA | Serum | 29676-4 |
| Dengue IgG | DEN_IGG | QUALITATIVE | ELISA | Serum | 29661-6 |
| VDRL | VDRL | QUALITATIVE + TITER | Flocculation | Serum | 20507-0 |
| TPHA | TPHA | QUALITATIVE | Hemagglutination | Serum | 8041-6 |
| Widal (O & H) | WIDAL | TITER | Tube agglutination | Serum | 5400-7 |

## Autoimmune Markers

| Parameter | Code | Data Type | Method | Reference | LOINC |
|-----------|------|-----------|--------|-----------|-------|
| ANA (Screen) | ANA_SCR | QUALITATIVE | IIF on HEp-2 | Negative <1:80 | 8061-4 |
| ANA Titer | ANA_TIT | TITER | IIF on HEp-2 | <1:80 | 74976-1 |
| ANA Pattern | ANA_PAT | DROPDOWN | IIF on HEp-2 | - | 74975-3 |
| Anti-dsDNA | DSDNA | DECIMAL (IU/mL) | ELISA / CLIA | <25 IU/mL | 11235-9 |
| Anti-CCP | ACCP | DECIMAL (U/mL) | CLIA | <17 U/mL | 53027-9 |
| Rheumatoid Factor | RF | DECIMAL (IU/mL) | Nephelometry | <14 IU/mL | 11572-5 |
| ASO Titer | ASO | DECIMAL (IU/mL) | Nephelometry | <200 IU/mL | 5370-2 |
| CRP (Quantitative) | CRP_Q | DECIMAL (mg/L) | Nephelometry | <5.0 mg/L | 1988-5 |
| Anti-TPO | ATPO | DECIMAL (IU/mL) | ECLIA | <34 IU/mL | 8099-4 |
| Anti-TG | ATG | DECIMAL (IU/mL) | ECLIA | <115 IU/mL | 8098-6 |
| C3 Complement | C3 | DECIMAL (mg/dL) | Nephelometry | 90–180 | 4485-9 |
| C4 Complement | C4 | DECIMAL (mg/dL) | Nephelometry | 10–40 | 4498-2 |

## Titer Interpretation

Titers reported as reciprocal of highest dilution showing positive reaction:

```
Widal Test Example:
┌──────────────┬─────────────────────────────────────────────┐
│ Antigen      │ 1:20  1:40  1:80  1:160  1:320  Titer     │
├──────────────┼─────────────────────────────────────────────┤
│ S. typhi O   │  +     +     +     +      −     1:160     │
│ S. typhi H   │  +     +     +     +      +     ≥1:320    │
│ S. paratyphi │  −     −     −     −      −     <1:20     │
│ AH           │                                            │
│ S. paratyphi │  +     +     −     −      −     1:40      │
│ BH           │                                            │
└──────────────┴─────────────────────────────────────────────┘
Interpretation: Rising titer ≥1:160 for O and ≥1:320 for H is significant.
```

## Signal-to-Cutoff (S/CO) Ratio

For ELISA and CLIA assays, the S/CO ratio determines qualitative interpretation:

| S/CO Range | Interpretation | Action |
|------------|---------------|--------|
| < 0.9 | Non-Reactive | Report as Negative |
| 0.9 – 1.1 | Borderline / Equivocal | Repeat in duplicate; if still equivocal, retest in 2 weeks |
| > 1.1 | Reactive | Report as Positive; confirmatory test if required |

## Data Model

```java
@Entity
@Table(name = "serology_result")
public class SerologyResult extends BaseEntity {
    private UUID orderLineItemId;
    private String testCode;
    private String qualitativeResult;         // REACTIVE, NON_REACTIVE, BORDERLINE
    private BigDecimal quantitativeValue;     // Titer, ratio, or index
    private String quantitativeUnit;          // IU/mL, mIU/mL, S/CO, titer
    private BigDecimal signalCutoffRatio;     // S/CO for ELISA/CLIA
    private String titerValue;               // "1:160", "1:320"
    private String method;                    // ECLIA, ELISA, RAPID, IIF, NEPHELOMETRY
    private String kitLotNumber;
    private LocalDate kitExpiryDate;
    private String interpretation;            // Auto-generated or manual
    private Boolean isConfirmatoryRequired;
    private UUID confirmatoryOrderId;         // Link to reflex confirmatory order
    private ResultStatus status;

    @OneToOne
    @JoinColumn(name = "order_line_item_id")
    private OrderLineItem orderLineItem;
}
```

## Report Print Style

Tabular PDF with reactive/non-reactive color coding:
1. **Infectious Disease Panel**: Parameter | Method | Result | S/CO or Titer | Interpretation
   - Reactive = **Red bold text**
   - Non-Reactive = Green text
   - Borderline = Orange italic text
2. **Autoimmune Panel**: Parameter | Result | Unit | Reference Range | Interpretation
3. **Titer Details**: Dilution series table (for Widal, VDRL, ASO when titers performed)
4. **Kit Information**: Kit name, lot number, expiry (footer)
5. **Notes**: Borderline results flagged with recommendation for repeat testing

## QC Notes

- **Internal QC**: Run positive, negative, and low-positive controls for every ELISA plate and CLIA reagent lot
- **Kit lot validation**: Parallel testing of old vs. new lot before switching (minimum 10 known samples)
- **EQAS participation**: RIQAS or CAP proficiency testing for all infectious disease markers
- **HIV testing algorithm**: Follow national NACO guidelines — 3-test strategy (rapid + ELISA + confirmatory)
- **Seroconversion window**: Document window period limitations in report comments for early-stage testing
- **Blood bank linkage**: Pre-transfusion screening results (HIV, HBsAg, HCV, VDRL, Malaria) auto-linked to blood bank module
