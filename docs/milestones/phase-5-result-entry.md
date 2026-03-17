# Phase 5: Result Entry & Validation (Months 8-12)

> Complete result entry for all 7 departments, auto-validation engine, critical value workflow, and result authorization.

## Milestone Goals

- Department-specific result entry UIs for all 7 departments
- Auto-calculation of derived parameters
- Delta check implementation
- Critical value detection and notification
- Auto-validation engine (rule-based)
- Result authorization workflow
- Angular screens: Result entry (20) + Authorization (8) = 28 screens

## Documentation References

- [Result Entry & Validation Flow](../process-flows/06-result-entry-validation.md)
- [Result Authorization Flow](../process-flows/07-result-authorization.md)
- [Critical Value Workflow](../process-flows/15-critical-value-workflow.md)
- [Biochemistry Department](../domain/departments/01-biochemistry.md)
- [Hematology Department](../domain/departments/02-hematology.md)
- [Microbiology Department](../domain/departments/03-microbiology.md)
- [Histopathology Department](../domain/departments/04-histopathology.md)
- [Clinical Pathology Department](../domain/departments/05-clinical-pathology.md)
- [Serology/Immunology Department](../domain/departments/06-serology-immunology.md)
- [Molecular Biology Department](../domain/departments/07-molecular-biology.md)
- [Result Entry Types](../domain/departments/result-entry-types.md)
- [Critical Values](../domain/critical-values.md)
- [Workflow State Machines — Result](../architecture/workflow-state-machines.md)
- [Screen List — Result Entry & Authorization](../screens/screen-list-complete.md) — Screens 50-77

---

## Issues

### Backend — Result Module (`lis-result`)

#### LIS-069: Implement Result Entry Core API and State Machine
**Labels:** `backend`, `database`
**Description:**
Result entry foundation:
- TestResult entity: order, orderLineItem, parameter, value, unit, abnormalFlag, enteredBy, enteredAt, status
- Result state machine: PENDING → ENTERED → VALIDATED → AUTHORIZED → RELEASED (AMENDED loops to VALIDATED)
- Result entry endpoints: `/api/v1/results` (enter, update, list, get)
- Worklist: pending results by department
- Bulk entry support for multiple parameters

**Acceptance Criteria:**
- [ ] TestResult entity with all fields
- [ ] Result state machine
- [ ] Entry and update endpoints
- [ ] Worklist query by department
- [ ] Flyway migration (partitioned by branch_id)
- [ ] Unit + integration tests

---

#### LIS-070: Implement Biochemistry Numeric Result Entry
**Labels:** `backend`
**Description:**
Biochemistry-specific result processing:
- Numeric value entry with decimal precision
- Auto-calculation of derived parameters:
  - LDL = TC - HDL - (TG/5)
  - VLDL = TG/5
  - eGFR (CKD-EPI formula)
  - A:G Ratio = Albumin / (Total Protein - Albumin)
  - Globulin = Total Protein - Albumin
- Abnormal flagging: compare against reference range (age/gender matched)
- Flag types: CRITICAL_LOW, LOW, NORMAL, HIGH, CRITICAL_HIGH

**Acceptance Criteria:**
- [ ] Numeric result entry for 46+ biochemistry parameters
- [ ] Auto-calculation formulas (5+ derived parameters)
- [ ] Reference range matching (age/gender/pregnancy)
- [ ] Abnormal flag generation
- [ ] Unit tests for calculations

---

#### LIS-071: Implement Hematology Result Entry (CBC + Differential)
**Labels:** `backend`
**Description:**
Hematology-specific result processing:
- CBC numeric entry: WBC, RBC, Hgb, Hct, MCV, MCH, MCHC, PLT
- Auto-calculations: MCV = (Hct/RBC) × 10, MCH = (Hgb/RBC) × 10, MCHC = (Hgb/Hct) × 100
- Differential count entry (neutrophils, lymphocytes, monocytes, eosinophils, basophils)
- Absolute counts calculation from percentage × WBC
- Peripheral smear narrative entry
- Scatter plot image upload support

**Acceptance Criteria:**
- [ ] CBC result entry with all parameters
- [ ] Auto-calculations (MCV, MCH, MCHC)
- [ ] Differential count with absolute calculation
- [ ] Peripheral smear narrative
- [ ] Image upload for scatter plots
- [ ] Unit tests

---

#### LIS-072: Implement Microbiology Culture and Antibiogram Entry
**Labels:** `backend`
**Description:**
Microbiology multi-day result workflow:
- Day 1: Preliminary report (gram stain, direct microscopy)
- Day 2-3: Organism identification from culture
- Day 3-5: Antibiogram (antibiotic sensitivity: S/I/R with MIC values)
- Multi-organism support (up to 3 isolates per culture)
- CLSI breakpoint reference for interpretation
- Interim reports at each stage

**Acceptance Criteria:**
- [ ] Culture result entity (multi-day, multi-organism)
- [ ] Antibiogram entry with S/I/R/MIC
- [ ] CLSI breakpoint integration
- [ ] Multi-organism (up to 3 isolates)
- [ ] Interim reporting support
- [ ] Unit tests

---

#### LIS-073: Implement Histopathology Narrative and Image Entry
**Labels:** `backend`
**Description:**
Histopathology result workflow:
- Gross examination entry (narrative text + measurements)
- Microscopic examination entry (narrative)
- Diagnosis entry with ICD-10 coding
- Image upload: gross photos, microscopic images
- Template-based reporting (synoptic reports, CAP protocols)
- MinIO storage for images

**Acceptance Criteria:**
- [ ] Gross/microscopic narrative entry
- [ ] Diagnosis with ICD-10 coding
- [ ] Image upload to MinIO
- [ ] Template-based synoptic reports
- [ ] Unit tests

---

#### LIS-074: Implement Clinical Pathology, Serology, and Molecular Biology Entry
**Labels:** `backend`
**Description:**
Remaining department result types:
- Clinical Pathology: semi-quantitative results (ABSENT, TRACE, 1+, 2+, 3+, 4+) for urine/stool
- Serology/Immunology: qualitative (REACTIVE/NON-REACTIVE) + titer (1:20, 1:40, etc.)
- Molecular Biology: CT values, amplification curves, qualitative DETECTED/NOT_DETECTED

**Acceptance Criteria:**
- [ ] Semi-quantitative result entry
- [ ] Qualitative + titer entry
- [ ] CT value entry with interpretation
- [ ] All result types from result-entry-types.md
- [ ] Unit tests

---

#### LIS-075: Implement Delta Check Engine
**Labels:** `backend`
**Description:**
Delta check comparison with previous results:
- Compare current result with patient's most recent previous result
- Delta percentage calculation: |current - previous| / previous × 100
- Configurable thresholds per parameter (20-50% warning, >50% critical)
- Time window consideration (only compare within configured period)
- Delta check result: PASS, WARNING, CRITICAL
- Block auto-validation if delta check fails

**Acceptance Criteria:**
- [ ] Delta check service
- [ ] Previous result lookup
- [ ] Percentage calculation
- [ ] Configurable thresholds
- [ ] Time window filtering
- [ ] Unit tests

---

#### LIS-076: Implement Critical Value Detection and Notification
**Labels:** `backend`
**Description:**
Critical value alerting per critical-values.md:
- Detect critical values on result entry (compare against critical thresholds)
- Trigger immediate notification (Spring Event → notification module)
- Critical value acknowledgment workflow (physician must acknowledge)
- Cannot auto-authorize critical results
- Critical value log: parameter, value, patient, notifiedDoctor, acknowledgedAt
- Endpoints: `/api/v1/results/critical/pending`, `/api/v1/results/critical/{id}/acknowledge`

**Acceptance Criteria:**
- [ ] Critical value detection on entry
- [ ] Notification event publishing
- [ ] Acknowledgment workflow
- [ ] Critical value log
- [ ] Block auto-authorization for critical results
- [ ] Unit + integration tests

---

#### LIS-077: Implement Auto-Validation Engine
**Labels:** `backend`
**Description:**
Rule-based automatic result verification:
- Auto-validation rules per parameter:
  - Result within normal range
  - Delta check passed
  - QC passed for the day
  - No critical values
- If ALL conditions met → auto-set status to VALIDATED
- Configurable per parameter (enable/disable auto-validation)
- Audit trail: who/what validated (SYSTEM for auto, userId for manual)

**Acceptance Criteria:**
- [ ] Auto-validation rule engine
- [ ] Configurable rules per parameter
- [ ] Integration with QC, delta check, reference range
- [ ] Audit trail for auto vs manual validation
- [ ] Unit + integration tests

---

#### LIS-078: Implement Result Authorization Workflow
**Labels:** `backend`
**Description:**
Pathologist authorization:
- Authorization endpoint: only PATHOLOGIST role can authorize
- Batch authorization: authorize multiple results at once
- Cannot authorize if critical values unacknowledged
- Cannot authorize if QC failed
- Authorization sets result status to AUTHORIZED
- Trigger report generation event on all results for an order authorized
- Result amendment: authorized results can be amended with reason
- Endpoints: `/api/v1/results/{id}/authorize`, `/api/v1/results/batch-authorize`, `/api/v1/results/{id}/amend`

**Acceptance Criteria:**
- [ ] Authorization service with role check
- [ ] Batch authorization
- [ ] Pre-authorization validation (QC, critical values)
- [ ] Report generation trigger
- [ ] Amendment workflow with reason
- [ ] Unit + integration tests

---

### Frontend — Result Entry Screens

#### LIS-079: Build Pending Results Dashboard
**Labels:** `frontend`
**Description:**
Pending results overview (Screen #50):
- Dashboard showing pending results by department
- Count badges per department
- Quick navigation to department worklist
- TAT countdown indicators
- STAT priority highlighting

**Acceptance Criteria:**
- [ ] Department-wise pending count cards
- [ ] TAT countdown display
- [ ] STAT priority highlighting
- [ ] Navigation to worklists

---

#### LIS-080: Build Biochemistry Result Entry Grid
**Labels:** `frontend`
**Description:**
Biochemistry entry screen (Screen #51):
- Spreadsheet-like grid for numeric entry
- Columns: Parameter, Result, Unit, Range, Flag
- Auto-populated reference ranges based on patient demographics
- Color-coded abnormal flags (red for critical, yellow for abnormal)
- Auto-calculated derived parameters
- Previous result comparison column

**Acceptance Criteria:**
- [ ] Data grid component for numeric entry
- [ ] Reference range display
- [ ] Color-coded flags
- [ ] Auto-calculation display
- [ ] Previous result column
- [ ] Keyboard navigation (Tab between cells)

---

#### LIS-081: Build Hematology Result Entry screens
**Labels:** `frontend`
**Description:**
Hematology entry screens (Screens #52-55):
- CBC result entry grid
- Differential count entry form
- Scatter plot image upload
- Peripheral smear narrative editor (rich text)
- Auto-calculated indices display

**Acceptance Criteria:**
- [ ] CBC entry grid
- [ ] Differential form with percentage/absolute toggle
- [ ] Image upload component
- [ ] Rich text editor for narrative
- [ ] Auto-calculation display

---

#### LIS-082: Build Microbiology Result Entry screens
**Labels:** `frontend`
**Description:**
Microbiology entry screens (Screens #56-58):
- Day 1 preliminary entry (gram stain, microscopy)
- Organism identification (searchable dropdown)
- Antibiogram matrix: organism × antibiotic grid with S/I/R selectors
- MIC value entry
- Multi-organism tabbed interface

**Acceptance Criteria:**
- [ ] Multi-day entry workflow
- [ ] Organism search/select
- [ ] Antibiogram matrix component
- [ ] S/I/R selector (color-coded: green/yellow/red)
- [ ] Multi-organism tabs

---

#### LIS-083: Build Histopathology and remaining department entry screens
**Labels:** `frontend`
**Description:**
Remaining result entry screens (Screens #59-66):
- Histopathology: gross, microscopic, diagnosis (rich text editors + image upload)
- Clinical Pathology: semi-quantitative selectors
- Serology: qualitative + titer entry
- Molecular Biology: CT value entry
- Result edit/correction screen
- Bulk result entry for manual analyzers

**Acceptance Criteria:**
- [ ] Histopathology multi-section editor
- [ ] Image upload with preview
- [ ] Semi-quantitative dropdown selectors
- [ ] Qualitative radio buttons + titer input
- [ ] CT value numeric entry
- [ ] Bulk entry table

---

#### LIS-084: Build Result Validation and Authorization screens
**Labels:** `frontend`
**Description:**
Validation and authorization screens (Screens #70-77):
- Pending validation list with filters
- Validation screen: review results, approve/reject
- Pending authorization list (pathologist view)
- Authorization screen: review, authorize, batch authorize
- Critical value pending acknowledgment list
- Critical value notification form
- Result amendment screen with reason
- Amendment history viewer

**Acceptance Criteria:**
- [ ] Validation list and screen
- [ ] Authorization list and screen
- [ ] Batch authorize functionality
- [ ] Critical value acknowledgment UI
- [ ] Amendment form with reason
- [ ] Amendment history display

---

#### LIS-085: Build Delta Check Review and Critical Value Documentation screens
**Labels:** `frontend`
**Description:**
Clinical review screens (Screens #67-68):
- Delta check review: show current vs previous with percentage change
- Color-coded delta indicators (green/yellow/red)
- Override option with reason documentation
- Critical value documentation: log notification details, physician response

**Acceptance Criteria:**
- [ ] Delta check comparison display
- [ ] Override with reason dialog
- [ ] Critical value documentation form
- [ ] Notification log display

---

### Database

#### LIS-086: Create Flyway migrations for Result module tables
**Labels:** `backend`, `database`
**Description:**
Phase 5 database migrations:
- TestResult table (LIST partitioned by branch_id)
- ResultValidation, ResultAuthorization tables
- CriticalValueLog table
- DeltaCheckResult table
- ResultAmendment table
- CultureResult, AntibioticSensitivity tables (microbiology)
- HistopathologyResult, HistopathologyImage tables
- ResultAttachment table (for images)
- Performance indexes on status, department, patient, order

**Acceptance Criteria:**
- [ ] All migrations with partitioning strategy
- [ ] Foreign keys to Order, Patient, Parameter tables
- [ ] Performance indexes
- [ ] Partition setup for test_result

---

### Testing

#### LIS-087: End-to-end test: Sample → Result Entry → Validation → Authorization
**Labels:** `backend`, `testing`
**Description:**
Complete result lifecycle test:
1. Accept samples from Phase 4
2. Enter biochemistry results (with auto-calculations)
3. Verify delta check execution
4. Verify critical value detection
5. Test auto-validation (normal results pass)
6. Test manual validation + authorization
7. Verify report generation trigger
8. Test amendment workflow

**Acceptance Criteria:**
- [ ] Happy path: entry → auto-validate → authorize
- [ ] Critical value detection and blocking
- [ ] Delta check warning and override
- [ ] Amendment with reason
- [ ] All department types tested

---

#### LIS-088: Department-specific result calculation tests
**Labels:** `backend`, `testing`
**Description:**
Unit tests for all auto-calculations:
- Lipid panel: LDL, VLDL calculations
- Renal: eGFR (CKD-EPI), BUN
- Liver: A:G ratio, Globulin
- Hematology: MCV, MCH, MCHC, absolute counts
- Verify precision/rounding (BigDecimal)
- Edge cases: division by zero, null inputs

**Acceptance Criteria:**
- [ ] Tests for each formula
- [ ] Edge case handling
- [ ] BigDecimal precision verification
- [ ] Reference range boundary tests

---

## Completion Criteria

- [ ] All 7 department result entry types functional
- [ ] Auto-calculations working for derived parameters
- [ ] Delta check comparing with previous results
- [ ] Critical value detection triggering notifications
- [ ] Auto-validation engine operational for eligible results
- [ ] Pathologist authorization workflow complete
- [ ] Result amendment with versioning
- [ ] All 28 Angular screens implemented
- [ ] 80% test coverage on lis-result module
- [ ] Performance: result entry p95 < 500ms
