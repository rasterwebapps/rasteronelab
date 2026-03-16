# Master Data Setup Flow — Onboarding Process

## Phase 1: System Setup (Day 0 — SUPER_ADMIN)

**Who**: Developer / BDM team  
**Time**: 1-2 days

### Actions
1. Deploy LIS infrastructure (Docker/K8s)
2. Configure Keycloak realm, clients
3. Create SUPER_ADMIN user in Keycloak
4. Seed global masters:
   - Countries, States, Cities
   - LOINC codes import (CSV bulk import)
   - ICD-10 codes import
   - Antibiotic master
   - Microorganism master
   - Sample types, Tube types
   - Measurement units
5. Configure notification gateways (SMS, Email, WhatsApp)
6. Create organization record

**Deliverable**: System is live, no data yet.

---

## Phase 2: Organization Onboarding (Week 1 — ORG_ADMIN)

**Who**: Lab Director / IT Admin  
**Time**: 3-5 days

### Actions
1. Complete organization profile (name, logo, license numbers)
2. Create departments (Biochemistry, Hematology, Microbiology...)
3. **Import test catalog** (most time-consuming step):
   - Import test master CSV (test code, name, department, sample type, TAT)
   - Map parameters to tests
   - Configure reference ranges per parameter (age/gender splits)
   - Mark calculated parameters + formulas
4. Configure test panels (Lipid Panel, LFT, KFT, CBC...)
5. Set base price catalog
6. Configure report templates per department
7. Create SMS/Email/WhatsApp communication templates
8. Set up organizational holiday calendar
9. Create staff user accounts in Keycloak
10. Configure Westgard QC rules per test
11. Configure delta check rules
12. Configure critical value ranges

**Deliverable**: Full test catalog, pricing, and user accounts ready.

---

## Phase 3: Branch Setup (Week 2 — BRANCH_ADMIN)

**Who**: Branch Manager / Lab In-Charge  
**Time**: 2-3 days per branch

### Actions
1. Complete branch profile (address, phone, lab license, NABL number)
2. Set working hours (Mon-Sun, open/close times)
3. Set branch-specific holidays
4. Select which tests are available at this branch
5. Set price overrides (if different from org base price)
6. Register instruments:
   - Add instrument (model, serial, IP/port)
   - Map instrument parameters to LIS parameters
   - Test ASTM connection
7. Configure QC materials available at branch
8. Set up inventory (reagent lots, expiry dates)
9. Configure home collection zones and time slots
10. Set number series (UHID prefix, order prefix)
11. Assign users to branch
12. Upload pathologist digital signature
13. Configure report header/footer/logo

**Deliverable**: Branch is operational — ready for testing.

---

## Phase 4: Operational Configuration (Week 3 — Pathologist/Admin)

**Who**: Chief Pathologist + Branch Admin  
**Time**: 2-3 days

### Actions
1. Set up QC materials — enter target mean and SD from calibration data
2. Configure Westgard rules per test (which rules to enforce)
3. Configure auto-validation rules:
   - Which parameters can be auto-approved
   - Conditions (must pass QC, must be in range, no critical)
4. Set up critical value notification contacts
5. Configure escalation rules (TAT breach alerts)
6. Test complete end-to-end workflow with sample patients
7. Validate instrument ASTM interface with real samples
8. Validate report PDF generation
9. Test SMS/Email delivery

**Deliverable**: Operations validated, auto-validation working.

---

## Phase 5: Staff Training & Testing (Week 4)

**Who**: All staff  
**Time**: 3-5 days

### Training Program
1. Day 1: Receptionist training (registration, ordering, billing)
2. Day 2: Phlebotomist training (collection, barcoding)
3. Day 3: Lab technician training (sample receipt, result entry, instrument interface)
4. Day 4: Pathologist training (validation, authorization, QC)
5. Day 5: Parallel run (paper + LIS simultaneously)

---

## Phase 6: Go-Live

**Who**: All staff + Support team

### Go-Live Checklist
- [x] All staff training completed
- [x] Instrument ASTM interfaces tested
- [x] QC accepted (Westgard rules passing)
- [x] Critical value contacts confirmed
- [x] SMS/Email notifications tested
- [x] Report PDFs validated
- [x] Backup configured
- [x] Support contact shared with staff
- [x] Go-Live announced to referring doctors

---

## New Branch Addition (Repeat Phase 3 onward)

When adding a second/third branch to an existing organization:
1. Phase 3 (Branch Setup) for new branch
2. Inherit org-level test catalog automatically
3. Configure branch-specific overrides
4. TAT: 5-7 days for an experienced team
