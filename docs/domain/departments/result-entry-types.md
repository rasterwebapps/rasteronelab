# Result Entry Types — RasterOneLab LIS

All 10 result data types used across departments.

## 1. INTEGER
- Whole numbers, no decimals
- Examples: WBC count, RBC count, Platelet count
- Precision: 0 decimal places

## 2. DECIMAL
- Numbers with configurable decimal places (1-4)
- Examples: Glucose (1dp), Creatinine (2dp), TSH (3dp)
- Rounding: ROUND_HALF_UP by default

## 3. CALCULATED
- Auto-calculated from other parameters
- Formula stored in parameter master
- Examples: LDL, eGFR, A/G Ratio, MCH, MCHC, INR
- Read-only in result entry screen
- Calculated server-side to prevent manipulation

## 4. QUALITATIVE
- Predefined discrete values
- Examples: Reactive / Non-Reactive / Equivocal, Positive / Negative, Detected / Not Detected
- Options configured per parameter in master

## 5. SEMI_QUANTITATIVE
- Semi-numeric scale
- Values: Nil, Trace, 1+, 2+, 3+, 4+ (stored as 0-5 ordinal)
- Examples: Urine protein, Urine glucose, Stool blood

## 6. NARRATIVE
- Free-text entry
- Examples: Clinical history, Gross examination findings
- Rich text editor (bold, italic, bullets)
- Spell check enabled for medical terminology

## 7. NARRATIVE_WITH_IMAGE
- Narrative text + image attachments
- Examples: Histopathology gross + microscopic + images
- Images stored in MinIO, referenced by URL
- Max 20MB per image, supported formats: JPEG, PNG, TIFF

## 8. MATRIX
- Grid of values (antibiogram)
- Example: Organism × Antibiotic sensitivity (S/I/R)
- Stored as JSONB array of objects

## 9. RATIO
- Titer values (1:20, 1:40, 1:80, 1:160...)
- Examples: VDRL titer, ANA titer, Widal test
- Values stored as denominator integer

## 10. PERCENTAGE
- Values that must sum to 100% (differential count)
- Examples: Neutrophils%, Lymphocytes%, Monocytes%
- Validation: Sum of all differential percentages must equal 100%

## Precision and Rounding Rules

```java
public enum LisRoundingMode {
    ROUND_HALF_UP,    // 1.235 → 1.24 (most common, used by most instruments)
    ROUND_HALF_EVEN,  // Banker's rounding (for statistical precision)
    TRUNCATE,         // 1.239 → 1.23 (cut off, no rounding)
    CEILING,          // Always round up (conservative interpretation)
    NONE              // No rounding applied (display as-is from instrument)
}
```

## Parameter Configuration in Master

```sql
-- Columns in parameter_master:
data_type           VARCHAR(30),  -- One of the 10 types above
decimal_places      INTEGER DEFAULT 2,
rounding_mode       VARCHAR(30) DEFAULT 'ROUND_HALF_UP',
show_trailing_zeros BOOLEAN DEFAULT TRUE,
use_thousand_sep    BOOLEAN DEFAULT FALSE,
qualitative_options JSONB,  -- ["Reactive","Non-Reactive","Equivocal"]
formula             TEXT,   -- For CALCULATED type
min_value           NUMERIC,  -- Instrument range min
max_value           NUMERIC,  -- Instrument range max
unit                VARCHAR(50),
loinc_code          VARCHAR(20)
```
