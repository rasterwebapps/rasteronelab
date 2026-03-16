# Master Data Overview — RasterOneLab LIS

## Summary: 125 Masters across 5 Levels

```
L1 Global (30)          → Set by SUPER_ADMIN (platform-wide)
  ↓
L2 Organization (53)    → Set by ORG_ADMIN (per organization)
  ↓
L3 Branch (27)          → Set by BRANCH_ADMIN (per branch)
  ↓
L4 Operational (10)     → Set by PATHOLOGIST/ADMIN (daily ops)
  ↓
L5 External (5)         → Set by DOCTOR/PATIENT preferences
```

## User Type Hierarchy

```
SUPER_ADMIN (Developer / BDM)
    │ Creates and manages
    ▼
ORGANIZATION
    │ Has
    ▼
ORG_ADMIN
    │ Creates and manages
    ▼
BRANCH_ADMIN
    │ Manages
    ▼
Staff: PATHOLOGIST, LAB_TECHNICIAN, RECEPTIONIST, PHLEBOTOMIST, BILLING_STAFF
    │ Serves
    ▼
DOCTOR (External)
    │ Refers
    ▼
PATIENT
```

## L1 — Global Masters (30)

| # | Master | Records | Managed By |
|---|--------|---------|-----------|
| 1 | Platform Configuration | Key-value settings | SUPER_ADMIN |
| 2 | Country | 250 | SUPER_ADMIN |
| 3 | State/Province | 5000+ | SUPER_ADMIN |
| 4 | City | 50000+ | SUPER_ADMIN |
| 5 | Currency | 170 | SUPER_ADMIN |
| 6 | Language | 100 | SUPER_ADMIN |
| 7 | Time Zone | 400 | SUPER_ADMIN |
| 8 | Measurement Units | 500+ | SUPER_ADMIN |
| 9 | ICD-10 Codes | 70000+ | SUPER_ADMIN |
| 10 | LOINC Codes | 90000+ | SUPER_ADMIN |
| 11 | SNOMED CT | 350000+ | SUPER_ADMIN |
| 12 | ATC Drug Codes | 6000+ | SUPER_ADMIN |
| 13 | Antibiotics Master | 200+ | SUPER_ADMIN |
| 14 | Microorganism Master | 500+ | SUPER_ADMIN |
| 15 | Sample Type | 50 | SUPER_ADMIN |
| 16 | Tube Type | 30 | SUPER_ADMIN |
| 17 | Specimen Container | 20 | SUPER_ADMIN |
| 18 | Lab Equipment Type | 100 | SUPER_ADMIN |
| 19 | ASTM Instrument Models | 200 | SUPER_ADMIN |
| 20 | Insurance Provider | 500 | SUPER_ADMIN |
| 21 | Accreditation Bodies | 20 | SUPER_ADMIN |
| 22 | Rejection Reasons | 20 | SUPER_ADMIN |
| 23 | Profession/Specialty | 50 | SUPER_ADMIN |
| 24 | Prefix/Salutation | 20 | SUPER_ADMIN |
| 25 | Blood Group | 8 | SUPER_ADMIN |
| 26 | Ethnicity | 50 | SUPER_ADMIN |
| 27 | System Role Types | 11 | SUPER_ADMIN |
| 28 | Notification Event Types | 30+ | SUPER_ADMIN |
| 29 | Report Template Layouts | 10 | SUPER_ADMIN |
| 30 | Email/SMS Gateway Config | 5 | SUPER_ADMIN |

## L2 — Organization Masters (53)

| # | Category | Masters | Count |
|---|----------|---------|-------|
| 1 | Organization Setup | Organization profile, logo, license | 3 |
| 2 | Departments | Lab departments (Biochemistry, Hematology...) | 11 departments |
| 3 | Test Master | Test catalog with parameters | ~500 tests |
| 4 | Parameter Master | Individual measurable parameters | ~2000 params |
| 5 | Test Panels | Bundled tests (Lipid Panel, LFT, KFT...) | ~100 panels |
| 6 | Reference Ranges | Age/gender/trimester-specific ranges | ~5000 ranges |
| 7 | Sample Requirements | Sample type + volume per test | Per test |
| 8 | Calculated Parameters | LDL, eGFR, A/G Ratio formulas | ~50 formulas |
| 9 | Reflex Rules | Auto-trigger follow-up tests | ~100 rules |
| 10 | TAT Configuration | Turn-around time per test | Per test |
| 11 | Price Catalog | Base prices per test | Per test |
| 12 | Users | Staff user accounts | Unlimited |
| 13 | Roles & Permissions | Custom role definitions | Custom |
| 14 | Doctors | Referring doctor directory | Unlimited |
| 15 | Report Templates | PDF layout templates per department | Per dept |
| 16 | Communication Templates | SMS/Email/WhatsApp templates | 30+ |
| 17 | Holiday Calendar | Org-wide holidays | Annual |
| 18 | Discount Schemes | Patient categories and discounts | ~20 |
| 19 | Insurance Tariffs | Insurance-specific pricing | Per insurer |
| 20 | QC Materials | Reference materials for QC | ~50 |
| 21 | Westgard Rules Config | Which rules to apply per test | Per test |
| 22 | Delta Check Rules | Allowed % change per test | Per test |
| 23 | Critical Value Ranges | Critical high/low per parameter | Per param |
| 24 | Auto-Validation Rules | Conditions for auto-approval | ~200 rules |
| 25 | Home Collection Zones | Service area definitions | Per branch |
| 26 | Partner Labs | Sendout labs for referral | ~20 |

## L3 — Branch Masters (27)

| # | Master | Purpose |
|---|--------|---------|
| 1 | Branch Profile | Name, address, license, NABL |
| 2 | Branch Working Hours | Mon-Sun open/close times |
| 3 | Branch Holidays | Branch-specific holidays |
| 4 | Test Availability | Which tests active at this branch |
| 5 | Price Overrides | Branch-specific price adjustments |
| 6 | Instruments | Which instruments installed |
| 7 | Instrument-Test Mapping | Which instrument runs which test |
| 8 | ASTM Parameter Mapping | Instrument code → Parameter |
| 9 | Instrument Calibration Log | Calibration records |
| 10 | QC Materials at Branch | Which QC materials in use |
| 11 | QC Frequency | How often to run QC |
| 12 | Westgard Rules Override | Branch-specific QC rules |
| 13 | Reagents | Reagent lot/expiry tracking |
| 14 | Inventory Stock | Current stock levels |
| 15 | Reorder Rules | Min/max stock levels |
| 16 | Suppliers | Reagent/supply vendors |
| 17 | Storage Locations | Freezer/fridge locations |
| 18 | Collection Slots | Home collection time slots |
| 19 | Phlebotomist Routes | Home collection areas |
| 20 | Report Header/Footer | Branch-specific PDF branding |
| 21 | Digital Signatures | Pathologist signatures |
| 22 | Number Series | UHID, Order, Sample prefixes |
| 23 | Users at Branch | Staff assigned to this branch |
| 24 | User Shift Timings | Shift hours per staff |
| 25 | TAT Overrides | Branch-specific TAT |
| 26 | Critical Alert Contacts | Who to call for critical values |
| 27 | Partner Lab Integration | Send-out lab connections |

## L4 — Operational Masters (10)

| # | Master | Updated By |
|---|--------|-----------|
| 1 | QC Daily Run Log | LAB_TECHNICIAN |
| 2 | Corrective Action Log | PATHOLOGIST |
| 3 | EQA Program Enrollment | PATHOLOGIST |
| 4 | EQA Results Submission | PATHOLOGIST |
| 5 | Workflow Status Overrides | BRANCH_ADMIN |
| 6 | Approval Workflows | BRANCH_ADMIN |
| 7 | SOP Documents | PATHOLOGIST |
| 8 | Escalation Rules | BRANCH_ADMIN |
| 9 | Maintenance Logs | LAB_TECHNICIAN |
| 10 | Capacity Planning | BRANCH_ADMIN |

## L5 — External Masters (5)

| # | Master | Managed By |
|---|--------|-----------|
| 1 | Doctor Portal Preferences | DOCTOR |
| 2 | Patient Portal Preferences | PATIENT |
| 3 | Patient Consent Records | RECEPTIONIST |
| 4 | Patient Family Linking | PATIENT |
| 5 | Doctor-Patient Mapping | DOCTOR |

## Master Data Setup Phases

```
Phase 1 (Day 1) — SUPER_ADMIN:
  Global masters, system configuration, organization creation

Phase 2 (Week 1) — ORG_ADMIN:
  Departments, test catalog, parameters, reference ranges,
  pricing, report templates, communication templates, users

Phase 3 (Week 2) — BRANCH_ADMIN:
  Branch profile, working hours, test availability,
  instrument configuration, ASTM mapping, users at branch

Phase 4 (Week 3) — Lab team:
  QC materials, Westgard rules, reagents, inventory setup,
  instrument calibration, critical value contacts

Phase 5 (Week 4) — Testing:
  End-to-end test with sample patients, calibration samples,
  QC run, report generation validation

Phase 6 (Go-Live):
  Production data migration, staff training, go-live
```
