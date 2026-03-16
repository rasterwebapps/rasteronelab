# Master Data Lifecycle — RasterOneLab LIS

## Overview

Master data flows through 6 phases from initial platform setup to an operational multi-branch lab.

## Phase 1: System Setup (L1 Global Masters)
**When**: Platform deployment day  
**By**: SUPER_ADMIN (Developer / BDM)  
**Duration**: 1-2 days

```
SUPER_ADMIN actions:
├── Deploy infrastructure (Docker/K8s)
├── Configure Keycloak realm + clients
├── Create SUPER_ADMIN user
├── Seed global masters:
│   ├── Geographic: Country, State, City
│   ├── Healthcare standards: LOINC, ICD-10, SNOMED import
│   ├── Lab standards: Antibiotics, Organisms, Sample types, Tubes
│   ├── Measurement units
│   └── System config: Notification gateways, report layouts
└── Create Organization record
```

**Output**: Blank system ready for organization onboarding

---

## Phase 2: Organization Onboarding (L2 Organization Masters)
**When**: After Phase 1  
**By**: ORG_ADMIN (Lab Director / IT Admin)  
**Duration**: 3-7 days (depends on test catalog size)

```
ORG_ADMIN actions:
├── Organization profile (name, logo, license, accreditation)
├── Create departments (11 departments)
├── Test catalog import:
│   ├── Import test master CSV (test code, name, department, TAT)
│   ├── Map parameters to tests
│   ├── Configure reference ranges (age/gender splits)
│   ├── Mark calculated parameters + formulas
│   └── Create test panels (Lipid Panel, LFT, KFT, CBC)
├── Pricing configuration
├── Report templates per department
├── Communication templates (30+ SMS/Email/WhatsApp)
├── Staff user accounts in Keycloak
├── QC configuration (Westgard rules, delta check, critical values)
└── Org-wide holiday calendar
```

**Output**: Test catalog ready, users created, pricing configured

---

## Phase 3: Branch Setup (L3 Branch Masters)
**When**: After Phase 2  
**By**: BRANCH_ADMIN  
**Duration**: 2-3 days per branch

```
BRANCH_ADMIN actions (per branch):
├── Branch profile (address, contacts, lab license, NABL)
├── Working hours (Mon-Sun)
├── Branch holidays
├── Test availability (select from org catalog)
├── Price overrides (if needed)
├── Instrument registration:
│   ├── Add instruments (IP, port, model)
│   ├── Map ASTM codes to parameters
│   └── Test ASTM connection
├── QC materials setup
├── Inventory setup (reagents, lots, expiry)
├── Number series (UHID, Order, Sample prefixes)
├── Assign users to branch
├── Upload pathologist digital signature
└── Configure report header/footer/logo
```

**Output**: Branch is fully configured, ready for testing

---

## Phase 4: Operational Configuration (L4 Masters)
**When**: After Phase 3  
**By**: PATHOLOGIST + BRANCH_ADMIN  
**Duration**: 2-3 days

```
Pathologist + Admin actions:
├── QC material calibration data entry (mean, SD)
├── Auto-validation rule setup
├── Critical value contact list
├── Escalation rules (TAT breach)
├── SOP documents upload
└── Corrective action protocol setup
```

**Output**: All workflows configured, auto-validation operational

---

## Phase 5: Testing & Training
**When**: After Phase 4  
**By**: All staff  
**Duration**: 3-5 days

```
Testing activities:
├── End-to-end test with sample patients
├── Instrument ASTM interface validation
├── Report PDF validation per department
├── Notification delivery testing
├── Critical value workflow testing
└── Parallel run (paper + LIS)

Training:
├── Day 1: Receptionist training
├── Day 2: Phlebotomist training
├── Day 3: Lab technician training
├── Day 4: Pathologist training
└── Day 5: Parallel run
```

---

## Phase 6: Go-Live
**When**: After Phase 5 sign-off  
**Duration**: Ongoing operations

```
Go-live day:
├── DNS switch to production
├── Monitor error rates (first 2 hours critical)
├── Support team on standby
└── First real patient processed

Post go-live:
├── Daily log review (first week)
├── Weekly metrics review
├── Monthly master data review
└── Annual data cleanup + archiving
```

---

## Master Data Change Management

### Change Request Workflow

```
Staff identifies need for change
  → Submits change request to BRANCH_ADMIN
  → BRANCH_ADMIN reviews (minor change: apply directly)
  → For test catalog changes: escalate to ORG_ADMIN
  → For global changes: escalate to SUPER_ADMIN
  → Apply change → Test → Notify affected staff
```

### Change Categories

| Category | Who Can Change | Examples |
|---------|----------------|---------|
| Price update | BRANCH_ADMIN | Price override for branch |
| New test | ORG_ADMIN | Add test to catalog |
| Reference range update | ORG_ADMIN + PATHOLOGIST | New CLSI ranges |
| Critical value threshold | PATHOLOGIST | Update panic value limit |
| New branch | SUPER_ADMIN | Provision new branch |
| New organization | SUPER_ADMIN | Onboard new customer |

### Audit of Master Data Changes

ALL master data changes are logged in `audit_trail` with:
- Old value (JSONB)
- New value (JSONB)
- Changed by (username)
- Changed at (timestamp)
- Reason (free text)
