# Phase 7: Reports, QC & Notifications

> PDF reports, quality control with Westgard rules, and multi-channel notifications.

## Milestone Goals

- PDF report generation with department-specific layouts
- QC system with Westgard rules and Levey-Jennings charts
- Multi-channel notifications (SMS, Email, WhatsApp)
- Inventory management basics
- Angular screens: Reports (10) + QC (10) + Notifications (5) + Inventory (12) = 37 screens

## Documentation References

- [Report Generation Engine](../architecture/report-generation-engine.md)
- [Report Generation & Delivery Flow](../process-flows/08-report-generation-delivery.md)
- [Quality Control Flow](../process-flows/10-quality-control.md)
- [Notification Architecture](../architecture/notification-architecture.md)
- [Async Processing](../architecture/async-processing.md)
- [Screen List — Reports, QC, Notifications, Inventory](../screens/screen-list-complete.md) — Screens 78-87, 100-109, 155-159, 118-129

---

## Issues

### Backend — Report Module (`lis-report`)

#### LIS-101: Implement PDF Report Generation Engine (OpenPDF)
**Labels:** `backend`
**Description:**
Core report generation:
- OpenPDF (LGPL) for PDF creation
- Report triggered on all order results authorized
- PDF structure: branch header (logo, name, address) → patient info → test results → signatures → QR code
- Department-specific result layouts:
  - Biochemistry: tabular with H/L flags
  - Hematology: table + scatter plot image
  - Microbiology: antibiogram matrix with S/I/R colors
  - Histopathology: narrative with embedded images
- Watermark for draft/amended reports
- Endpoints: `/api/v1/reports/generate`, `/api/v1/reports/{id}/download`

**Acceptance Criteria:**
- [ ] OpenPDF report generator service
- [ ] Branch header template (logo, colors)
- [ ] Department-specific result renderers
- [ ] QR code generation on report
- [ ] Watermark support
- [ ] Unit tests for PDF content

---

#### LIS-102: Implement Report Storage in MinIO and Versioning
**Labels:** `backend`, `infrastructure`
**Description:**
Report storage and versioning:
- MinIO storage path: `reports/{branchId}/{year}/{month}/{orderId}/report-vN.pdf`
- Report versioning: v1, v2, v3 on amendments
- Previous versions retained (never overwritten)
- Report state machine: PENDING → GENERATED → RELEASED → DELIVERED
- Amendment workflow: create new version with reason
- Verification URL with SHA-256 hash
- Endpoints: `/api/v1/reports/{id}/release`, `/api/v1/reports/{id}/amend`

**Acceptance Criteria:**
- [ ] MinIO storage integration
- [ ] Version management (v1, v2, v3)
- [ ] Report state machine
- [ ] Amendment with reason tracking
- [ ] SHA-256 verification hash
- [ ] Integration tests

---

#### LIS-103: Implement Report Delivery (SMS, Email, WhatsApp) Integration
**Labels:** `backend`
**Description:**
Multi-channel report delivery:
- Email delivery with PDF attachment
- SMS with report download link
- WhatsApp message with PDF link (via BSP)
- Delivery tracking: PENDING → SENT → DELIVERED → FAILED
- Retry on failure (up to 3 attempts)
- Bulk report delivery for batch processing
- Endpoints: `/api/v1/reports/{id}/deliver`, `/api/v1/reports/bulk-deliver`

**Acceptance Criteria:**
- [ ] Email delivery service
- [ ] SMS delivery service
- [ ] WhatsApp delivery integration
- [ ] Delivery status tracking
- [ ] Retry mechanism
- [ ] Bulk delivery
- [ ] Integration tests

---

#### LIS-104: Implement Branch-Specific Report Branding
**Labels:** `backend`
**Description:**
Customizable report templates per branch:
- Branch logo upload and positioning
- Branch color scheme (header, accents)
- Pathologist signature image
- NABL/accreditation logo display
- Custom disclaimer text
- Custom header/footer text
- Report template configuration API

**Acceptance Criteria:**
- [ ] Branch report template entity
- [ ] Logo/image upload to MinIO
- [ ] Template configuration CRUD
- [ ] PDF rendering with branch-specific styles
- [ ] Preview functionality

---

### Backend — QC Module (`lis-qc`)

#### LIS-105: Implement QC Daily Entry and Westgard Rules Engine
**Labels:** `backend`, `database`
**Description:**
Quality control core:
- QC material management: name, level (1/2/3), lot number, expiry, target mean, SD
- QC result entry: instrument, material, parameter, measured value, date
- Westgard rules evaluation:
  - 1-2s (Warning): 1 control > ±2SD
  - 1-3s (Reject): 1 control > ±3SD
  - 2-2s (Reject): 2 consecutive > ±2SD same side
  - R-4s (Reject): 1 control > +2SD and 1 > -2SD
  - 4-1s (Reject): 4 consecutive > ±1SD same side
  - 10x (Reject): 10 consecutive same side of mean
- QC status: ACCEPTED, WARNING, REJECTED
- Block patient result authorization if QC rejected
- Endpoints: `/api/v1/qc/runs` (CRUD), `/api/v1/qc/evaluate`

**Acceptance Criteria:**
- [ ] QC material entity and CRUD
- [ ] QC result entry
- [ ] Westgard rules engine (all 6 rules)
- [ ] QC status determination
- [ ] Block authorization on QC failure
- [ ] Flyway migration
- [ ] Unit tests for each Westgard rule

---

#### LIS-106: Implement Levey-Jennings Chart Data API
**Labels:** `backend`
**Description:**
Levey-Jennings statistical charting:
- Data aggregation: daily QC values over 30/60/90 day period
- Mean, SD, coefficient of variation (CV) calculation
- Z-score for each QC value
- Trend detection: 7+ points trending same direction
- Chart data API returning structured data for frontend visualization
- Endpoints: `/api/v1/qc/levey-jennings?parameterId=X&period=30`

**Acceptance Criteria:**
- [ ] QC data aggregation service
- [ ] Statistical calculations (mean, SD, CV, z-score)
- [ ] Trend detection algorithm
- [ ] Chart data endpoint
- [ ] Unit tests

---

#### LIS-107: Implement EQA Submission and Corrective Actions
**Labels:** `backend`
**Description:**
External quality assessment and corrective actions:
- EQA program enrollment: program, cycle, parameters
- EQA result submission: measured values per cycle
- EQA performance tracking (pass/fail per cycle)
- Corrective action documentation: problem, root cause, action taken, resolution, follow-up
- Corrective action linked to QC rejection events
- Endpoints: `/api/v1/qc/eqa` (CRUD), `/api/v1/qc/corrective-actions` (CRUD)

**Acceptance Criteria:**
- [ ] EQA program entity and CRUD
- [ ] EQA result submission
- [ ] Corrective action entity and CRUD
- [ ] Link to QC rejection events
- [ ] Unit tests

---

### Backend — Notification Module (`lis-notification`)

#### LIS-108: Implement Multi-Channel Notification Engine
**Labels:** `backend`, `infrastructure`
**Description:**
Notification system per notification-architecture.md:
- SMS sending (Twilio/AWS SNS integration)
- Email sending (SMTP/SES)
- WhatsApp (Meta BSP integration)
- Template engine with variable substitution: `{{patientName}}`, `{{orderId}}`, etc.
- Notification templates per event type (30+ events)
- DND rules: 8AM-9PM for patients, 24/7 for critical alerts
- Delivery tracking: PENDING → SENT → DELIVERED → FAILED
- Retry: immediate → 5min → 15min → 1hr → escalate

**Acceptance Criteria:**
- [ ] SMS provider integration
- [ ] Email service
- [ ] WhatsApp BSP integration
- [ ] Template engine
- [ ] DND rule enforcement
- [ ] Delivery tracking
- [ ] Retry with exponential backoff
- [ ] Unit + integration tests

---

#### LIS-109: Implement Notification Events and Delivery Tracking
**Labels:** `backend`, `database`
**Description:**
Event-driven notifications:
- Spring Event listeners for notification triggers:
  - Patient registered → welcome SMS
  - Report ready → SMS + Email + WhatsApp with download link
  - Critical value → immediate alert to physician
  - TAT breach → alert to lab manager
  - Payment received → receipt SMS
- Notification log with delivery status
- Notification retry management
- Endpoints: `/api/v1/notifications` (list, get, retry)

**Acceptance Criteria:**
- [ ] Event listeners for all notification triggers
- [ ] Notification log entity
- [ ] Delivery status tracking
- [ ] Retry management
- [ ] Flyway migration
- [ ] Integration tests

---

### Backend — Inventory Module (`lis-inventory`)

#### LIS-110: Implement Basic Inventory Management
**Labels:** `backend`, `database`
**Description:**
Reagent and consumables tracking:
- Reagent entity: name, manufacturer, lotNumber, expiryDate, quantity, minStockLevel
- Stock movement: IN (purchase), OUT (usage), ADJUST
- Low stock alerts (when quantity < minStockLevel)
- Reagent expiry monitoring (30-day advance warning)
- Supplier management: name, contact, address
- Purchase order: supplier, items, status (DRAFT, ORDERED, RECEIVED)
- GRN (Goods Receipt Note) on delivery
- Endpoints: `/api/v1/inventory/reagents` (CRUD), `/api/v1/inventory/stock` (movement)

**Acceptance Criteria:**
- [ ] Reagent/consumable entities
- [ ] Stock movement tracking
- [ ] Low stock alert service
- [ ] Expiry monitoring
- [ ] Purchase order workflow
- [ ] GRN processing
- [ ] Flyway migration
- [ ] Unit tests

---

### Frontend — Report Screens

#### LIS-111: Build Report Management screens
**Labels:** `frontend`
**Description:**
Report screens (Screens #78-87):
- Pending reports list
- Report detail view with PDF preview
- Manual report generation trigger
- Report release confirmation
- Report delivery (select channels: SMS, Email, WhatsApp)
- Report amendment with reason dialog
- Report version history viewer
- Report download
- Bulk report delivery

**Acceptance Criteria:**
- [ ] Report list with status filters
- [ ] PDF viewer component (embedded)
- [ ] Delivery channel selector
- [ ] Amendment form
- [ ] Version history timeline
- [ ] Bulk delivery UI

---

### Frontend — QC Screens

#### LIS-112: Build QC Entry, Levey-Jennings, and Westgard screens
**Labels:** `frontend`
**Description:**
Quality control screens (Screens #100-109):
- QC daily entry form: select material, level, enter measured value
- Levey-Jennings chart: interactive chart (Chart.js or similar) with mean, ±1SD, ±2SD, ±3SD lines
- Westgard rules violation list with rule details
- QC accept/reject with corrective action form
- QC summary report
- EQA enrollment and submission screens
- QC calendar/schedule
- Instrument calibration log

**Acceptance Criteria:**
- [ ] QC entry form component
- [ ] Levey-Jennings chart component (interactive)
- [ ] Violation list with rule descriptions
- [ ] Corrective action form
- [ ] EQA management screens
- [ ] QC calendar view

---

### Frontend — Notification and Inventory Screens

#### LIS-113: Build Notification History and Settings screens
**Labels:** `frontend`
**Description:**
Notification screens (Screens #155-159):
- Notification history list with status filters
- Notification detail view
- Manual retry button for failed notifications
- Alert management (configure alert rules)
- Notification settings (channel preferences per event type)

**Acceptance Criteria:**
- [ ] Notification history with filters
- [ ] Detail view
- [ ] Retry functionality
- [ ] Settings configuration UI

---

#### LIS-114: Build Inventory Management screens
**Labels:** `frontend`
**Description:**
Inventory screens (Screens #118-129):
- Inventory dashboard with stock levels
- Reagent/stock list with search/filter
- New stock entry form
- Stock adjustment form
- Reagent usage log
- Low stock alerts list
- Supplier management screens
- Purchase order creation and tracking
- GRN entry
- Reagent expiry monitor
- Stock movement history

**Acceptance Criteria:**
- [ ] Inventory dashboard with KPIs
- [ ] Reagent list and forms
- [ ] Stock movement tracking
- [ ] Supplier management
- [ ] Purchase order workflow UI
- [ ] Expiry alerts

---

### Database

#### LIS-115: Create Flyway migrations for Report, QC, Notification, and Inventory tables
**Labels:** `backend`, `database`
**Description:**
Phase 7 database migrations:
- Report, ReportSection, ReportDelivery tables
- QCMaterial, QCResult, QCRule, CorrectiveAction, EQAProgram tables
- NotificationLog, NotificationTemplate, DeliveryStatus tables (partitioned by created_at)
- Reagent, StockMovement, Supplier, PurchaseOrder, GRN tables

**Acceptance Criteria:**
- [ ] All migrations created
- [ ] Notification log partitioned by date
- [ ] QC result partitioned by measured_at
- [ ] Performance indexes

---

### Testing

#### LIS-116: End-to-end test: Authorization → Report Generation → Delivery
**Labels:** `backend`, `testing`
**Description:**
Complete report lifecycle test:
1. Authorize all results for an order
2. Verify report generation triggered
3. Verify PDF generated with correct content
4. Release report
5. Deliver via SMS and email
6. Verify delivery tracking
7. Test report amendment and version creation

**Acceptance Criteria:**
- [ ] Report generation integration test
- [ ] PDF content verification
- [ ] Delivery tracking verification
- [ ] Amendment and versioning test

---

#### LIS-117: Westgard rules engine comprehensive tests
**Labels:** `backend`, `testing`
**Description:**
Test all Westgard rules with known data:
- 1-2s warning (value at ±2.1 SD)
- 1-3s reject (value at ±3.1 SD)
- 2-2s reject (two consecutive at ±2.1 SD same side)
- R-4s reject (range > 4SD)
- 4-1s reject (four consecutive at ±1.1 SD same side)
- 10x reject (ten consecutive same side of mean)
- Combined rule scenarios
- Edge cases: exact boundary values

**Acceptance Criteria:**
- [ ] Individual rule tests
- [ ] Combined rule scenarios
- [ ] Edge case tests
- [ ] Performance tests (batch QC evaluation)

---

## Completion Criteria

- [ ] PDF reports generated with department-specific layouts
- [ ] Report versioning and amendment workflow functional
- [ ] MinIO storage and retrieval working
- [ ] QC entry with Westgard rules evaluation operational
- [ ] Levey-Jennings charts rendering correctly
- [ ] Multi-channel notifications delivering
- [ ] Notification retry and DND rules enforced
- [ ] Basic inventory management functional
- [ ] All 37 Angular screens implemented
- [ ] 80% test coverage on report, QC, notification, inventory modules
