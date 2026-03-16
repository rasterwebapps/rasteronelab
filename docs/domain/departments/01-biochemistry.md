# Biochemistry Department — RasterOneLab LIS

## Overview

- **Code**: BIO
- **Sample Types**: Serum (Gold SST), Plasma (Lithium Heparin), EDTA, Urine (random/24h), CSF, Body fluids
- **Primary Instruments**: Roche Cobas c311/c501, Beckman AU480/AU680, Mindray BS-800
- **Result Entry**: Numeric tabular grid with Tab-through navigation, auto-flag, auto-calculate, delta check

## Result Entry UI Behavior

1. Grid displays all parameters for the order
2. User tabs through cells (like a spreadsheet)
3. Each cell validates range on exit (flag H/L/HH/LL/* automatically)
4. Calculated parameters auto-fill (LDL, eGFR, A/G Ratio, MCH, MCHC, INR)
5. Delta check: if result >25% change from last, show warning
6. Batch entry mode: enter results for multiple patients simultaneously
7. Import from CSV/Excel for manual analyzer results

## 46+ Parameters

| Parameter | Code | Data Type | Decimal | Unit | Critical Low | Critical High | Formula |
|-----------|------|-----------|---------|------|-------------|--------------|---------|
| Glucose | GLU | DECIMAL | 1 | mg/dL | 50 | 500 | - |
| HbA1c | HBA1C | DECIMAL | 1 | % | - | - | - |
| Urea | UREA | DECIMAL | 1 | mg/dL | - | 200 | - |
| Creatinine | CREA | DECIMAL | 2 | mg/dL | - | 15 | - |
| eGFR | EGFR | INTEGER | 0 | mL/min/1.73m² | - | - | CKD-EPI |
| Uric Acid | UA | DECIMAL | 1 | mg/dL | - | 15 | - |
| Total Protein | TP | DECIMAL | 1 | g/dL | - | - | - |
| Albumin | ALB | DECIMAL | 1 | g/dL | - | - | - |
| Globulin | GLOB | DECIMAL | 1 | g/dL | - | - | TP - ALB |
| A/G Ratio | AGRATIO | DECIMAL | 2 | - | - | - | ALB/GLOB |
| Total Bilirubin | TBIL | DECIMAL | 2 | mg/dL | - | 15 | - |
| Direct Bilirubin | DBIL | DECIMAL | 2 | mg/dL | - | - | - |
| Indirect Bilirubin | IBIL | DECIMAL | 2 | mg/dL | - | - | TBIL - DBIL |
| SGOT (AST) | AST | INTEGER | 0 | U/L | - | - | - |
| SGPT (ALT) | ALT | INTEGER | 0 | U/L | - | - | - |
| Alkaline Phosphatase | ALP | INTEGER | 0 | U/L | - | - | - |
| GGT | GGT | INTEGER | 0 | U/L | - | - | - |
| LDH | LDH | INTEGER | 0 | U/L | - | - | - |
| Total Cholesterol | TC | DECIMAL | 1 | mg/dL | - | - | - |
| HDL Cholesterol | HDL | DECIMAL | 1 | mg/dL | - | - | - |
| LDL Cholesterol | LDL | DECIMAL | 1 | mg/dL | - | - | Friedewald |
| VLDL Cholesterol | VLDL | DECIMAL | 1 | mg/dL | - | - | TG/5 |
| Triglycerides | TG | DECIMAL | 1 | mg/dL | - | 1000 | - |
| TC/HDL Ratio | TCHDL | DECIMAL | 2 | - | - | - | TC/HDL |
| Sodium | NA | INTEGER | 0 | mEq/L | 120 | 160 | - |
| Potassium | K | DECIMAL | 1 | mEq/L | 2.5 | 6.5 | - |
| Chloride | CL | INTEGER | 0 | mEq/L | - | - | - |
| Bicarbonate | HCO3 | INTEGER | 0 | mEq/L | - | - | - |
| Calcium | CA | DECIMAL | 1 | mg/dL | 6.0 | 13.0 | - |
| Phosphorus | PHOS | DECIMAL | 1 | mg/dL | - | - | - |
| Magnesium | MG | DECIMAL | 2 | mg/dL | - | - | - |
| Iron | FE | DECIMAL | 1 | μg/dL | - | - | - |
| TIBC | TIBC | DECIMAL | 1 | μg/dL | - | - | - |
| Transferrin Saturation | TSAT | DECIMAL | 1 | % | - | - | FE/TIBC×100 |
| Ferritin | FERR | DECIMAL | 1 | ng/mL | - | - | - |
| Folate | FOLATE | DECIMAL | 2 | ng/mL | - | - | - |
| Vitamin B12 | B12 | INTEGER | 0 | pg/mL | - | - | - |
| Vitamin D3 | VD3 | DECIMAL | 1 | ng/mL | - | - | - |
| TSH | TSH | DECIMAL | 3 | μIU/mL | - | - | - |
| Free T3 | FT3 | DECIMAL | 2 | pg/mL | - | - | - |
| Free T4 | FT4 | DECIMAL | 2 | ng/dL | - | - | - |
| PSA Total | PSA | DECIMAL | 3 | ng/mL | - | - | - |
| hsCRP | CRP | DECIMAL | 2 | mg/L | - | - | - |
| Procalcitonin | PCT | DECIMAL | 3 | ng/mL | - | - | - |
| NT-proBNP | BNP | INTEGER | 0 | pg/mL | - | - | - |
| Amylase | AMY | INTEGER | 0 | U/L | - | - | - |
| Lipase | LIP | INTEGER | 0 | U/L | - | - | - |

## Calculated Parameter Formulas

```java
// LDL (Friedewald) — Valid when TG < 400
LDL = TC - HDL - (TG / 5)

// eGFR (CKD-EPI 2021)
// Uses patient age and gender from patient record
eGFR = 142 × min(Scr/κ, 1)^α × max(Scr/κ, 1)^(-1.200) × 0.9938^Age
// κ = 0.7 (female), 0.9 (male)
// α = -0.241 (female), -0.302 (male)

// A/G Ratio
A_G = Albumin / (TotalProtein - Albumin)

// Indirect Bilirubin
IndirectBilirubin = TotalBilirubin - DirectBilirubin

// Anion Gap
AnionGap = Sodium - (Chloride + Bicarbonate)
```

## Auto-flagging Rules

| Flag | Meaning | Condition |
|------|---------|----------|
| H | High | value > highNormal |
| L | Low | value < lowNormal |
| HH | Critical High | value > highCritical |
| LL | Critical Low | value < lowCritical |
| A | Abnormal | Qualitative abnormal |
| * | Critical (alert) | HH or LL — requires physician notification |
