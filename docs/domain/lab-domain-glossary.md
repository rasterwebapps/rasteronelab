# Lab Domain Glossary — RasterOneLab LIS

## Key Terms

### TAT (Turn-Around Time)
Time elapsed from sample collection to report delivery. Measured and tracked per test.
- **Internal TAT**: Sample receipt → Report authorized
- **External TAT**: Order placed → Report delivered to patient
- Alerts trigger when TAT is breached

### Critical Values (Panic Values)
Test results outside defined critical ranges that require **immediate physician notification**.
- Must be acknowledged by pathologist and documented
- Must have physician notification recorded (who was called, at what time)
- Read-back confirmation required

### Delta Check
Comparison of current result with patient's previous result for the same test.
- Flags when change exceeds configured % threshold
- Catches labeling errors, specimen mix-ups
- Example: Creatinine 1.0 → 8.0 (700% increase) — suspicious

### Westgard Rules
Statistical quality control rules applied to QC materials.
- **1-2s**: Warning (result >2 SD from mean)
- **1-3s**: Reject (result >3 SD)
- **2-2s**: Reject (2 consecutive >2 SD on same side)
- **R-4s**: Reject (range >4 SD between two consecutive)
- **4-1s**: Reject (4 consecutive >1 SD on same side)
- **10-x**: Reject (10 consecutive on same side of mean)

### ASTM Protocol (E1381/E1394)
Standard communication protocol between laboratory instruments and LIS.
- Frame-based transmission (STX, ETX, ENQ, EOT, ACK, NAK)
- Record types: H (header), P (patient), O (order), R (result), L (terminator)
- Bidirectional: LIS sends orders, instrument sends results

### HL7 FHIR
Fast Healthcare Interoperability Resources — modern healthcare data exchange standard.
- Used for HIS/EMR integration
- Patient, Observation, DiagnosticReport FHIR resources
- RESTful API (FHIR server)

### LOINC
Logical Observation Identifiers Names and Codes.
- Universal coding for lab tests and observations
- Example: LOINC 2345-7 = Glucose [Mass/volume] in Serum

### Reflex Testing
Automatic ordering of additional tests based on initial result.
- Example: If TSH is abnormal → automatically add Free T3/T4
- Example: If VDRL Reactive → automatically add TPHA confirmatory
- Configured as rules in master data

### Aliquoting
Dividing one primary sample into multiple child samples.
- Each aliquot tracked separately with its own barcode
- Used when one sample must be processed by multiple instruments
- Child samples linked to parent in database

### CLSI
Clinical and Laboratory Standards Institute.
- Sets standards for breakpoints (S/I/R) in microbiology
- Defines minimum inhibitory concentration (MIC) interpretations

### NABL
National Accreditation Board for Testing and Calibration Laboratories (India).
- Quality accreditation for labs in India
- ISO 15189 based accreditation
- Requires documented QC, calibration, PT

### QC (Quality Control)
Internal process of running known control materials to verify analyzer performance.
- Run at minimum each shift before patient samples
- Results plotted on Levey-Jennings chart
- Westgard rules applied to detect shifts and trends

### EQA/PT (External Quality Assessment / Proficiency Testing)
External body sends unknown samples; lab results compared to peer labs.
- Programs: EQAS India, CMCL, WHO EQAS
- Results submitted periodically (quarterly/monthly)

### Antibiogram
Summary table showing organism sensitivity to various antibiotics.
- Based on MIC or disk diffusion tests
- Results: S (Susceptible), I (Intermediate), R (Resistant)
- CLSI or EUCAST breakpoints applied

### Friedewald Formula
Formula for calculating LDL cholesterol:
`LDL = Total Cholesterol - HDL - (Triglycerides / 5)`
- Valid only when Triglycerides < 400 mg/dL
- Replaced by direct LDL measurement when TG is very high

### CKD-EPI
Chronic Kidney Disease Epidemiology Collaboration formula for eGFR.
- Uses serum creatinine, age, sex
- More accurate than MDRD formula (2021 version removes race adjustment)

### Allred Score
Scoring system for hormone receptor staining in breast cancer histopathology.
- Proportion Score (0-5) + Intensity Score (0-3) = Total (0-8)
- Score ≥3 = Hormone receptor positive

### Bethesda System
Standardized reporting system for cervical cytology (PAP smear).
- Categories: Negative, ASC-US, LSIL, HSIL, ASC-H, AGC, Carcinoma
