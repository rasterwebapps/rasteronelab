# L1 Global Masters — RasterOneLab LIS

Managed by: **SUPER_ADMIN** (Developer or BDM team)
Scope: **Platform-wide** (shared across all organizations and branches)

## 1. Platform Configuration

Key-value settings for the entire platform:
- `platform.name` = "RasterOneLab LIS"
- `platform.version` = "1.0.0"
- `platform.default_language` = "en"
- `platform.support_email` = "support@rasteronelab.com"
- `platform.max_file_size_mb` = "20"
- `platform.session_timeout_minutes` = "60"
- `platform.pdf_watermark_text` = "CONFIDENTIAL"
- `platform.qr_verify_base_url` = "https://verify.rasteronelab.com"

## 2. Geographic Data

| Master | Records | Import Method |
|--------|---------|--------------|
| Country | 250 | CSV bulk import |
| State/Province | 5000+ | CSV bulk import |
| City | 50000+ | CSV bulk import |

## 3. Healthcare Standards

### LOINC (Logical Observation Identifiers Names and Codes)
- 90,000+ codes
- Import from: https://loinc.org (free registration)
- Key fields: LOINC_NUM, COMPONENT, SYSTEM, SCALE_TYP, CLASS
- Used for: Each parameter in test catalog has a LOINC code
- Enables: HL7 FHIR interoperability, lab accreditation

### ICD-10 (International Classification of Diseases)
- 70,000+ codes (ICD-10-CM for US, ICD-10 for others)
- Import from: WHO or local health authority
- Used for: Histopathology diagnosis, clinical notes

### SNOMED CT
- 350,000+ concepts
- Used for: Histopathology morphology codes, findings
- License: Free in many countries (check local rules)

## 4. Laboratory Standards

### Antibiotics Master (200+ entries)
| Column | Type | Example |
|--------|------|---------|
| antibiotic_code | VARCHAR | AMC |
| antibiotic_name | VARCHAR | Amoxicillin-Clavulanate |
| drug_class | VARCHAR | Beta-lactam/Beta-lactamase inhibitor |
| route | VARCHAR | Oral, IV |
| clsi_category | VARCHAR | Penicillins |
| atc_code | VARCHAR | J01CR02 |

### Microorganism Master (500+ entries)
| Column | Type | Example |
|--------|------|---------|
| organism_code | VARCHAR | KLPN |
| organism_name | VARCHAR | Klebsiella pneumoniae |
| gram_stain | VARCHAR | NEGATIVE |
| organism_type | VARCHAR | BACTERIA |
| snomed_code | VARCHAR | 56415008 |
| is_reportable | BOOLEAN | true |
| resistance_flags | JSONB | ["ESBL_capable"] |

### Sample Types (50 entries)
SERUM, PLASMA_HEPARIN, PLASMA_EDTA, WHOLE_BLOOD, URINE_RANDOM, URINE_24H, URINE_FIRST_MORNING, CSF, SPUTUM, BAL, STOOL, TISSUE, SWAB_THROAT, SWAB_VAGINAL, SWAB_WOUND, BLOOD_CULTURE, SYNOVIAL_FLUID, PLEURAL_FLUID, PERITONEAL_FLUID, BONE_MARROW, AMNIOTIC_FLUID, SEMINAL_FLUID, PERICARDIAL_FLUID, SALIVA, NAIL, HAIR, SKIN_SCRAPING

### Tube Types (30 entries)
SST_GOLD, EDTA_PURPLE, EDTA_PINK, CITRATE_BLUE, HEPARIN_GREEN, FLUORIDE_GREY, PLAIN_RED, TRACE_ELEMENT_DARKBLUE, SPS_YELLOW, URINE_CONTAINER, STOOL_CONTAINER, SWAB_AMIES, SWAB_CHARCOAL, BLOOD_CULTURE_AEROBIC, BLOOD_CULTURE_ANAEROBIC, PAEDIATRIC_BLOOD_CULTURE, STERILE_CONTAINER_10ML, STERILE_CONTAINER_50ML

## 5. Measurement Units (500+ entries)

```sql
CREATE TABLE measurement_unit (
    id          UUID PRIMARY KEY,
    unit_code   VARCHAR(50) UNIQUE NOT NULL,
    unit_name   VARCHAR(100),
    unit_symbol VARCHAR(30),
    category    VARCHAR(50),  -- CONCENTRATION, VOLUME, TIME, RATIO, etc.
    si_unit     BOOLEAN,
    conversion_factor NUMERIC(15,8)  -- To SI unit
);

-- Examples:
-- mg/dL, g/dL, μg/dL, ng/mL, pg/mL (concentration)
-- mmol/L, μmol/L, nmol/L (SI concentration)
-- mEq/L, μEq/L (electrolytes)
-- U/L, IU/L, μIU/mL (enzyme/hormone activity)
-- mm/hr, fl, pg (special)
-- seconds, minutes (time)
```

## 6. System Configuration

### Notification Gateways
- SMS: Twilio, AWS SNS, MSG91 (India), Textlocal
- Email: SMTP, AWS SES, SendGrid
- WhatsApp: Gupshup, Kaleyra, Infobip

### Report Template Layouts (10 base layouts)
1. BIOCHEMISTRY_TABULAR — Standard tabular with parameters in rows
2. HEMATOLOGY_MULTI_SECTION — Table + images + narrative
3. MICROBIOLOGY_CULTURE — Culture narrative + antibiogram
4. HISTOPATHOLOGY_NARRATIVE — Free text with images
5. SEROLOGY_QUALITATIVE — Reactive/Non-Reactive table
6. MOLECULAR_PCR — CT values + qualitative
7. CLINICAL_PATHOLOGY — Urinalysis semi-quantitative
8. CARDIOLOGY_ECHO — Structured measurements + images
9. COMBINED_REPORT — Multiple departments on one report
10. EMERGENCY_REPORT — Simplified rapid results

### Rejection Reasons (20)
HEMOLYZED, CLOTTED, INSUFFICIENT_QUANTITY, WRONG_TUBE, UNLABELED, LIPEMIC, ICTERIC, EXPIRED_TUBE, DETERIORATED, OLD_SAMPLE, IMPROPER_STORAGE, WRONG_PATIENT, DUPLICATE_SAMPLE, LEAKED, CONTAMINATED, WRONG_TEST, BARCODE_UNREADABLE, CONTAINER_BROKEN, MISSING_PAPERWORK, OTHER
