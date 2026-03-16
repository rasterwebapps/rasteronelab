# Microbiology Department — RasterOneLab LIS

## Overview

- **Code**: MIC
- **Sample Types**: Blood, Urine, Sputum, BAL, CSF, Wound swabs, Throat swabs, Stool
- **TAT**: 5-7 days (culture) / 24 hours (interim)
- **Special**: Multi-stage entry (Day 1 → Day 2-3 → Day 5)

## Multi-Stage Culture Result Entry

### Stage 1: Day 1 — Macroscopic Examination (Preliminary)

Lab technician records:
- Sample appearance (e.g., "Turbid, yellowish urine")
- Gram stain result: "Gram-negative rods in clusters, 3+ WBCs"
- Preliminary note: "Growth pending"
- Release: **PRELIMINARY REPORT** (interim)

### Stage 2: Day 2-3 — Organism Identification

Colonies identified:
- Select organism from dropdown (microorganism master)
- Colony count: ">100,000 CFU/mL" or "Scanty", "Moderate", "Heavy"
- Resistance markers: ESBL?, MRSA?, CRE?, VRE?
- Add additional organisms if polymicrobial

### Stage 3: Day 3-5 — Antibiotic Sensitivity (Antibiogram)

For each organism, complete antibiogram matrix:
- Antibiotics from branch antibiotic panel
- MIC values or disk diffusion (zone diameter)
- S/I/R auto-calculated from CLSI breakpoints
- Manual override allowed with reason

### Sensitivity Result Entry Grid

```
Organism: Klebsiella pneumoniae (ESBL)
┌────────────────────┬─────────┬──────┬────────┐
│ Antibiotic         │ MIC/Zone│ CLSI │ Result │
├────────────────────┼─────────┼──────┼────────┤
│ Amoxicillin        │ R       │ —    │ R      │
│ Amox-Clavulanate   │ >16     │ CLSI │ R      │
│ Piperacillin-Tazo  │ <=16    │ CLSI │ S      │
│ Ceftriaxone        │ >2      │ CLSI │ R      │
│ Imipenem           │ <=1     │ CLSI │ S      │
│ Meropenem          │ <=1     │ CLSI │ S      │
│ Amikacin           │ <=16    │ CLSI │ S      │
│ Ciprofloxacin      │ >1      │ CLSI │ R      │
│ Fosfomycin         │ <=64    │ CLSI │ S      │
│ Colistin           │ <=2     │ CLSI │ S      │
└────────────────────┴─────────┴──────┴────────┘
ESBL: POSITIVE | CRE: NEGATIVE | KPC: NEGATIVE
```

## Data Model

```java
@Entity
@Table(name = "culture_result")
public class CultureResult extends BaseEntity {
    private UUID orderLineItemId;
    private String specimenDescription;        // Day 1
    private String gramStainResult;
    private String macroscopicAppearance;
    private Boolean noGrowth;
    private Boolean contaminant;
    private String day1Note;

    @OneToMany(mappedBy = "cultureResult", cascade = CascadeType.ALL)
    private List<OrganismResult> organisms;

    private String finalComments;
    private ResultStatus status;               // PRELIMINARY, FINAL
}

@Entity
@Table(name = "organism_result")
public class OrganismResult extends BaseEntity {
    private UUID cultureResultId;
    private UUID organismsId;         // From microorganism master
    private String organismName;
    private String colonyCount;       // ">100,000 CFU/mL"
    private Boolean isEsbl;
    private Boolean isMrsa;
    private Boolean isCre;
    private Boolean isVre;
    private String resistanceNotes;

    @OneToMany(mappedBy = "organismResult", cascade = CascadeType.ALL)
    private List<AntibioticSensitivity> sensitivities;
}

@Entity
@Table(name = "antibiotic_sensitivity")
public class AntibioticSensitivity extends BaseEntity {
    private UUID organismResultId;
    private UUID antibioticId;
    private String antibioticName;
    private String micValue;          // e.g., "<=2" or ">16"
    private Integer zoneSize;         // mm (for disk diffusion)
    private String interpretation;   // S, I, R
    private String clsiStandard;     // e.g., "CLSI M100-2023"
    private Boolean isOverride;
    private String overrideReason;
}
```

## Report Print Style

1. **Specimen Information**: Type, collection, received date
2. **Gram Stain**: Preliminary findings
3. **Culture Results**: For each organism:
   - Organism name + identification confidence
   - Resistance markers (ESBL POSITIVE highlighted in red)
4. **Antibiogram**: Tabular with S/I/R color coding:
   - S = Green
   - I = Orange
   - R = Red
5. **Comments/Interpretation**: Clinical notes

## CLSI Breakpoint Integration

```java
@Service
public class ClsiBreakpointService {

    public String interpretMic(UUID antibioticId, UUID organismsId, BigDecimal mic) {
        ClsiBreakpoint breakpoint = clsiRepository
            .findByAntibioticAndOrganism(antibioticId, organismsId);

        if (breakpoint == null) return "UNKNOWN";

        if (mic.compareTo(breakpoint.getSensibleMic()) <= 0) return "S";
        if (mic.compareTo(breakpoint.getResistantMic()) >= 0) return "R";
        return "I";
    }
}
```

## Interim Report Workflow

- Day 1: Preliminary report released (Gram stain result)
- Day 3: Updated with organism identification
- Day 5: Final report with complete antibiogram
- Patient/doctor notified at each stage via SMS/WhatsApp
