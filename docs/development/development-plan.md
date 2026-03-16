# Development Plan — RasterOneLab LIS (8 Phases)

## Project Timeline: 18-24 Months (3 Full-Stack Developers)

---

## Phase 1: Foundation (Months 1-2)

### Goals
- Project setup (Gradle, Angular, Docker)
- Authentication working end-to-end
- Multi-branch infrastructure proven

### Deliverables
- [x] Monorepo structure (backend + frontend + docs)
- [ ] Keycloak realm configured (roles, custom claims)
- [ ] Spring Security OAuth2 resource server
- [ ] BranchContextHolder + BranchInterceptor
- [ ] Angular auth flow (login, refresh, logout)
- [ ] Docker Compose all services running
- [ ] CI/CD Jenkins pipeline
- [ ] BaseEntity, ApiResponse, exception handling

### Key Technical Decisions
- Confirm branch isolation strategy (row-level filtering chosen)
- Confirm technology versions (Java 21, Spring Boot 3.4, Angular 19)

---

## Phase 2: Administration Module (Months 2-4)

### Goals
- All master data CRUD operational
- Admin screens functional

### Deliverables
- [ ] `lis-admin` module complete
- [ ] Branch management (CRUD)
- [ ] Department management
- [ ] Test master (full CRUD with parameters)
- [ ] Reference range configuration (age/gender/pregnancy)
- [ ] Price catalog
- [ ] Doctor management
- [ ] User management (CRUD in Keycloak)
- [ ] Admin Angular screens (25 screens)
- [ ] Liquibase migrations for all admin tables

---

## Phase 3: Patient & Ordering (Months 4-6)

### Goals
- Complete patient registration to order placement

### Deliverables
- [ ] `lis-patient` module
  - Patient CRUD (search, register, edit, merge)
  - UHID generation
  - Visit management
- [ ] `lis-order` module
  - Test ordering UI
  - Test search + panel expansion
  - Order validation (sample requirements)
  - Barcode generation
- [ ] `lis-billing` module (basic)
  - Invoice auto-generation
  - Payment recording
  - Invoice printing
- [ ] Angular screens: Patient (12) + Order (10) + Billing (12)

---

## Phase 4: Sample Management (Months 6-8)

### Goals
- Complete sample lifecycle from collection to disposal

### Deliverables
- [ ] `lis-sample` module
  - Collection recording
  - Tube label printing (Zebra integration)
  - Sample receiving (accept/reject)
  - Aliquoting
  - Sample tracking
  - Transfer between branches
- [ ] Sample rejection workflow + recollection
- [ ] Angular screens: Sample (14)
- [ ] Barcode scanner integration (WebHID API or external scanner)

---

## Phase 5: Result Entry & Validation (Months 8-12)

### Goals
- Complete result entry for all departments
- Auto-validation working
- Critical value workflow

### Deliverables
- [ ] `lis-result` module
  - Biochemistry numeric grid entry
  - Hematology CBC + differential entry
  - Microbiology culture + antibiogram
  - Histopathology narrative + image upload
  - Clinical pathology semi-quantitative
  - Serology qualitative + titer
  - Molecular biology CT values
- [ ] Delta check implementation
- [ ] Critical value detection + notification
- [ ] Auto-validation engine (rule-based)
- [ ] Result authorization workflow
- [ ] Angular screens: Result entry (20) + Authorization (8)

---

## Phase 6: Instrument Interface (Months 10-13)

### Goals
- ASTM E1381/E1394 bidirectional communication
- Instrument results flowing automatically

### Deliverables
- [ ] `lis-instrument` module
  - TCP/IP ASTM connection manager
  - ASTM frame parser (H, P, O, R, L records)
  - Result mapper (instrument code → parameter code)
  - Host query mode (LIS → instrument orders)
  - Connection status monitoring
  - Reconnect on disconnect
- [ ] Tested with: Roche Cobas c311, Sysmex XN-1000
- [ ] Serial port support (legacy instruments)
- [ ] Instrument screens (8)
- [ ] RabbitMQ integration (results → queue → consumer)

---

## Phase 7: Reports, QC & Notifications (Months 12-16)

### Goals
- PDF reports generated automatically
- QC system operational
- Notifications delivering

### Deliverables
- [ ] `lis-report` module
  - OpenPDF report generation
  - Department-specific layouts (6 layouts)
  - Branch-specific branding
  - QR code + barcode on reports
  - MinIO storage + retrieval
  - Report amendment workflow
- [ ] `lis-qc` module
  - QC daily entry
  - Westgard rules engine
  - Levey-Jennings chart
  - EQA submission
- [ ] `lis-notification` module
  - SMS sending (Twilio/MSG91)
  - Email sending
  - WhatsApp (BSP integration)
  - Delivery tracking
- [ ] Angular screens: Reports (10) + QC (10) + Notifications (5)

---

## Phase 8: Portals, Analytics & Launch Prep (Months 16-22)

### Goals
- Doctor and patient portals live
- Analytics dashboards
- Performance optimization

### Deliverables
- [ ] Doctor portal (Angular, separate app or module)
  - Doctor login (Google or username)
  - Patient reports view
  - Critical value alerts
- [ ] Patient portal
  - OTP login (phone)
  - Report download
  - Invoice view
- [ ] Home collection module
  - Slot booking
  - Phlebotomist schedule
- [ ] Dashboard screens (8) + Analytics (9)
- [ ] Performance testing (JMeter, k6)
- [ ] Security audit
- [ ] Documentation completion
- [ ] Production deployment
- [ ] Staff training materials

---

## Resource Allocation

| Role | Count | Key Modules |
|------|-------|-------------|
| Backend Lead | 1 | Architecture, Core, Instrument Interface |
| Backend Developer | 2 | Patient, Order, Sample, Result, Report |
| Frontend Developer | 2 | Angular screens, portals |
| QA Engineer | 1 | Test automation, Cypress |
| DevOps | 0.5 | CI/CD, K8s, monitoring |
| Domain Expert (Lab) | 0.5 | Master data validation, UAT |

## Risk Register

| Risk | Probability | Impact | Mitigation |
|------|------------|--------|-----------|
| ASTM instrument compatibility issues | Medium | High | Early testing with target instruments |
| Reference range complexity (age/gender) | High | Medium | Domain expert involvement in Phase 2 |
| PDF report layout approval by pathologist | Medium | High | Involve pathologist in Phase 7 early |
| Keycloak custom claims complexity | Low | High | Proof of concept in Phase 1 |
| Data migration from existing LIS | Medium | High | Dedicated migration scripts + validation |
