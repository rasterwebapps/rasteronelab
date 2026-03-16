# Parameter Configuration — RasterOneLab LIS

## Overview

A **parameter** is a single measurable quantity within a test (e.g., Glucose is a parameter of a Biochemistry test, Hemoglobin is a parameter of CBC).

## parameter_master Table (40+ Columns)

```sql
CREATE TABLE parameter_master (
    id                      UUID PRIMARY KEY,
    branch_id               UUID,           -- NULL = org-level (shared); UUID = branch-override
    test_id                 UUID NOT NULL,
    parameter_code          VARCHAR(50) NOT NULL UNIQUE,
    parameter_name          VARCHAR(200) NOT NULL,
    parameter_short_name    VARCHAR(50),
    loinc_code              VARCHAR(20),
    display_order           INTEGER DEFAULT 0,
    data_type               VARCHAR(30) NOT NULL,  -- INTEGER, DECIMAL, CALCULATED, QUALITATIVE, etc.
    decimal_places          INTEGER DEFAULT 2,
    rounding_mode           VARCHAR(30) DEFAULT 'ROUND_HALF_UP',
    show_trailing_zeros     BOOLEAN DEFAULT TRUE,
    use_thousand_separator  BOOLEAN DEFAULT FALSE,
    unit                    VARCHAR(50),
    unit_display            VARCHAR(50),      -- Display unit (may differ from internal)
    min_instrument_value    NUMERIC(15,6),    -- Below this = re-run
    max_instrument_value    NUMERIC(15,6),    -- Above this = re-run
    qualitative_options     JSONB,            -- ["Reactive","Non-Reactive","Equivocal"]
    formula                 TEXT,             -- For CALCULATED type
    formula_parameters      JSONB,            -- Parameter codes used in formula
    is_calculated           BOOLEAN DEFAULT FALSE,
    is_required             BOOLEAN DEFAULT TRUE,
    is_reportable           BOOLEAN DEFAULT TRUE,
    is_active               BOOLEAN DEFAULT TRUE,
    print_style             VARCHAR(30),      -- NORMAL, BOLD, ITALIC
    normal_text_override    TEXT,             -- Override display text for normals
    department_id           UUID,
    instrument_code         VARCHAR(100),     -- ASTM instrument parameter code
    -- Audit
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by              VARCHAR(100),
    updated_by              VARCHAR(100),
    is_deleted              BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at              TIMESTAMPTZ
);

CREATE INDEX idx_parameter_test ON parameter_master(test_id);
CREATE INDEX idx_parameter_code ON parameter_master(parameter_code);
```

## parameter_reference_range Table

```sql
CREATE TABLE parameter_reference_range (
    id              UUID PRIMARY KEY,
    branch_id       UUID NOT NULL,
    parameter_id    UUID NOT NULL REFERENCES parameter_master(id),
    range_name      VARCHAR(100),
    gender          VARCHAR(10),       -- MALE, FEMALE, ANY
    age_from_days   INTEGER,           -- Age range in days
    age_to_days     INTEGER,
    condition       VARCHAR(50),       -- PREGNANCY_T1, T2, T3
    low_normal      NUMERIC(15,6),
    high_normal     NUMERIC(15,6),
    low_critical    NUMERIC(15,6),
    high_critical   NUMERIC(15,6),
    display_text    TEXT,              -- For qualitative: "Reactive: Positive"
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100)
);
```

## Java Enums

```java
public enum ParameterDataType {
    INTEGER,           // Whole numbers (WBC, RBC counts)
    DECIMAL,           // Decimal numbers (Glucose 110.5, TSH 2.450)
    CALCULATED,        // Formula-based (LDL, eGFR, A/G Ratio)
    QUALITATIVE,       // Discrete options (Reactive/Non-Reactive)
    SEMI_QUANTITATIVE, // Ordinal scale (Nil/Trace/1+/2+/3+)
    NARRATIVE,         // Free text (Histopathology diagnosis)
    NARRATIVE_WITH_IMAGE, // Text + images (Gross examination)
    MATRIX,            // Grid of values (Antibiogram)
    RATIO,             // Titer values (1:20, 1:40, 1:80)
    PERCENTAGE         // Percentage, sum = 100% (Differential count)
}

public enum LisRoundingMode {
    ROUND_HALF_UP,   // Standard (1.235 → 1.24)
    ROUND_HALF_EVEN, // Banker's rounding
    TRUNCATE,        // Just cut (1.239 → 1.23)
    CEILING,         // Always up (1.231 → 1.24)
    NONE             // No rounding
}
```

## ParameterValueFormatter Service

```java
@Service
public class ParameterValueFormatter {

    public String format(BigDecimal value, ParameterMaster parameter) {
        if (value == null) return "";

        int decimalPlaces = parameter.getDecimalPlaces();
        LisRoundingMode mode = parameter.getRoundingMode();

        BigDecimal rounded = switch (mode) {
            case ROUND_HALF_UP -> value.setScale(decimalPlaces, RoundingMode.HALF_UP);
            case ROUND_HALF_EVEN -> value.setScale(decimalPlaces, RoundingMode.HALF_EVEN);
            case TRUNCATE -> value.setScale(decimalPlaces, RoundingMode.DOWN);
            case CEILING -> value.setScale(decimalPlaces, RoundingMode.CEILING);
            case NONE -> value;
        };

        // Show trailing zeros based on parameter config
        String formatted = parameter.isShowTrailingZeros()
            ? String.format("%." + decimalPlaces + "f", rounded)
            : rounded.stripTrailingZeros().toPlainString();

        // Thousand separator
        if (parameter.isUseThousandSeparator()) {
            formatted = addThousandSeparator(formatted);
        }

        return formatted;
    }
}
```

## CalculatedParameterEngine Service

```java
@Service
public class CalculatedParameterEngine {

    /**
     * LDL Cholesterol (Friedewald Formula)
     * LDL = TC - HDL - (TG / 5)
     * Only valid when TG < 400 mg/dL
     */
    public BigDecimal calculateLDL(Map<String, BigDecimal> params) {
        BigDecimal tc = params.get("TC");
        BigDecimal hdl = params.get("HDL");
        BigDecimal tg = params.get("TG");

        if (tc == null || hdl == null || tg == null) return null;
        if (tg.compareTo(new BigDecimal("400")) >= 0) return null; // Invalid range

        return tc.subtract(hdl).subtract(tg.divide(BigDecimal.valueOf(5), 2, HALF_UP));
    }

    /**
     * A/G Ratio
     */
    public BigDecimal calculateAGRatio(Map<String, BigDecimal> params) {
        BigDecimal albumin = params.get("ALB");
        BigDecimal totalProtein = params.get("TP");
        if (albumin == null || totalProtein == null) return null;
        BigDecimal globulin = totalProtein.subtract(albumin);
        if (globulin.compareTo(BigDecimal.ZERO) <= 0) return null;
        return albumin.divide(globulin, 2, HALF_UP);
    }

    /**
     * eGFR (CKD-EPI 2021)
     */
    public BigDecimal calculateEGFR(BigDecimal creatinine, int ageYears, Gender gender) {
        double scr = creatinine.doubleValue();
        double kappa = gender == Gender.FEMALE ? 0.7 : 0.9;
        double alpha = gender == Gender.FEMALE ? -0.241 : -0.302;
        double scrOverKappa = scr / kappa;

        double egfr = 142
            * Math.pow(Math.min(scrOverKappa, 1), alpha)
            * Math.pow(Math.max(scrOverKappa, 1), -1.200)
            * Math.pow(0.9938, ageYears);

        return BigDecimal.valueOf(Math.round(egfr)); // INTEGER result
    }

    /**
     * Indirect Bilirubin
     */
    public BigDecimal calculateIndirectBilirubin(Map<String, BigDecimal> params) {
        BigDecimal tbil = params.get("TBIL");
        BigDecimal dbil = params.get("DBIL");
        if (tbil == null || dbil == null) return null;
        return tbil.subtract(dbil).setScale(2, HALF_UP);
    }

    /**
     * VLDL
     */
    public BigDecimal calculateVLDL(Map<String, BigDecimal> params) {
        BigDecimal tg = params.get("TG");
        if (tg == null) return null;
        return tg.divide(BigDecimal.valueOf(5), 1, HALF_UP);
    }
}
```

## Key Parameters by Department (137+ total)

| Dept | Count | Examples |
|------|-------|---------|
| Biochemistry | 46 | Glucose, Creatinine, LFT, Lipids, Thyroid, Electrolytes |
| Hematology | 28 | WBC, RBC, Hgb, HCT, MCV, MCH, MCHC, PLT, Differentials, Reticulocytes |
| Coagulation | 8 | PT, INR, APTT, TT, Fibrinogen, D-dimer, Factor VIII, Factor IX |
| Clinical Pathology | 15 | Urine: pH, SG, Protein, Glucose, Ketones, Bilirubin, Urobilinogen, RBC, WBC, Casts |
| Serology | 16 | VDRL, TPHA, HBsAg, HCV Ab, HIV 1&2, TORCH panel |
| Molecular Biology | 12 | COVID-19 PCR, HBV DNA, HCV RNA, TB PCR, HPV, HIV VL |
| Tumor Markers | 12 | PSA, CEA, AFP, CA-125, CA-19.9, CA-15.3, LDH |

## Reference Range Examples

```sql
-- Glucose: different for fasting vs random, no gender/age split
-- Normal: 70-100 mg/dL fasting; Critical low: <50; Critical high: >500

-- Hemoglobin: gender-specific
-- Male: 13.5-17.5 g/dL | Female: 12.0-15.5 g/dL
-- Critical low: <7 g/dL (both)

-- TSH: age-specific
-- Newborn: 0.7-15.2 μIU/mL
-- Adult: 0.35-4.94 μIU/mL
-- Elderly (>60): 0.5-8.9 μIU/mL
-- Pregnancy T1: 0.1-2.5 μIU/mL
```
