# Phase 8: Portals, Analytics & Launch Prep

> Doctor and patient portals, analytics dashboards, performance optimization, and production launch.

## Milestone Goals

- Doctor portal (separate Angular module/app)
- Patient portal with OTP login
- Home collection module
- Analytics dashboards and operational reports
- Performance testing and optimization
- Security audit
- Production deployment preparation

## Documentation References

- [System Architecture](../architecture/system-architecture.md)
- [Security Architecture](../architecture/security-architecture.md)
- [Deployment Guide](../development/deployment-guide.md)
- [Testing Strategy](../development/testing-strategy.md)
- [Screen List — Portals, Dashboards, Analytics](../screens/screen-list-complete.md) — Screens 6-13, 160-194
- [Audit Trail Design](../architecture/audit-trail-design.md)

---

## Issues

### Backend — Doctor Portal

#### LIS-118: Implement Doctor Portal Backend API
**Labels:** `backend`, `security`
**Description:**
Doctor-specific APIs:
- Doctor authentication (Google SSO or username/password via Keycloak)
- Doctor dashboard: my patients' reports (last 30 days)
- Patient search within doctor's referrals
- Report viewing and download (only referred patients)
- Critical value alerts for doctor's patients
- Order new test via e-referral
- Endpoints: `/api/v1/doctor-portal/dashboard`, `/api/v1/doctor-portal/patients`, `/api/v1/doctor-portal/reports`

**Acceptance Criteria:**
- [ ] Doctor authentication (Keycloak DOCTOR role)
- [ ] Dashboard API (recent reports)
- [ ] Patient search (referral-scoped)
- [ ] Report viewing (authorization: only referred patients)
- [ ] Critical value alert API
- [ ] Unit + integration tests

---

#### LIS-119: Implement E-Referral Order API
**Labels:** `backend`
**Description:**
Doctor-initiated test ordering:
- Doctor can order tests for their patients remotely
- Simplified order flow (no payment at portal)
- Order appears at branch reception for processing
- Notification to branch on new e-referral
- Track e-referral orders separately
- Endpoints: `/api/v1/doctor-portal/orders` (create, list)

**Acceptance Criteria:**
- [ ] E-referral order creation
- [ ] Branch notification on new referral
- [ ] Referral order tracking
- [ ] Unit tests

---

### Backend — Patient Portal

#### LIS-120: Implement Patient Portal Backend API
**Labels:** `backend`, `security`
**Description:**
Patient self-service APIs:
- Patient authentication: OTP login (phone number verification)
- Resource Owner Password flow with OTP in Keycloak
- Patient dashboard: my reports
- Report download (own reports only)
- Invoice history and outstanding balance
- Profile update (phone, email, address)
- Endpoints: `/api/v1/patient-portal/dashboard`, `/api/v1/patient-portal/reports`, `/api/v1/patient-portal/invoices`

**Acceptance Criteria:**
- [ ] OTP-based authentication
- [ ] Patient dashboard API
- [ ] Report download (authorization: own reports only)
- [ ] Invoice history API
- [ ] Profile update API
- [ ] Unit + integration tests

---

### Backend — Home Collection

#### LIS-121: Implement Home Collection Module
**Labels:** `backend`, `database`
**Description:**
Home collection workflow:
- Collection request: patient, tests, preferred date/time, address, location (lat/long)
- Slot booking: available time slots per area
- Phlebotomist assignment and schedule management
- Route planning optimization (basic — group by area)
- Collection confirmation (mark collected at patient's home)
- Failed collection management (reschedule, cancel)
- Endpoints: `/api/v1/home-collection/requests` (CRUD), `/api/v1/home-collection/schedule`

**Acceptance Criteria:**
- [ ] HomeCollectionRequest entity
- [ ] Slot booking service
- [ ] Phlebotomist schedule management
- [ ] Collection confirmation
- [ ] Failed collection handling
- [ ] Flyway migration
- [ ] Unit tests

---

### Backend — Dashboard & Analytics

#### LIS-122: Implement Dashboard APIs
**Labels:** `backend`
**Description:**
Main dashboard data endpoints:
- Summary cards: today's orders, pending results, reports generated, revenue
- TAT compliance: percentage of orders within TAT by department
- Pending actions: count by category (collections, receipts, results, authorizations)
- Critical values pending acknowledgment count
- Instrument status summary
- Endpoints: `/api/v1/dashboard/summary`, `/api/v1/dashboard/tat`, `/api/v1/dashboard/pending`

**Acceptance Criteria:**
- [ ] Summary statistics API
- [ ] TAT compliance calculation
- [ ] Pending actions aggregation
- [ ] Critical value pending count
- [ ] Instrument status summary
- [ ] Redis caching for dashboard data
- [ ] Unit tests

---

#### LIS-123: Implement Analytics and Operational Report APIs
**Labels:** `backend`
**Description:**
Operational reporting:
- Test volume report: tests by department, date range, comparison
- Revenue report: daily/weekly/monthly revenue by department, payment method
- TAT analysis: average TAT by test, department, trend over time
- QC performance: pass/fail rates, corrective action summary
- Doctor-wise referral report: orders per referring doctor
- Patient demographics report: age/gender distribution
- Instrument utilization: tests per instrument, uptime
- Audit trail report: user activity log (SUPER_ADMIN only)
- Custom report builder: flexible query builder (stretch goal)
- Endpoints: `/api/v1/reports/analytics/{reportType}`

**Acceptance Criteria:**
- [ ] Test volume aggregation
- [ ] Revenue calculation
- [ ] TAT analysis with trends
- [ ] QC performance summary
- [ ] Referral report
- [ ] Demographics report
- [ ] Instrument utilization
- [ ] Audit trail query
- [ ] Unit tests

---

### Frontend — Doctor Portal

#### LIS-124: Build Doctor Portal Angular screens
**Labels:** `frontend`
**Description:**
Doctor portal screens (Screens #160-169):
- Doctor login page (Google SSO + username/password)
- Doctor dashboard (my patients' reports, critical alerts)
- Patient search within referrals
- Patient report list and viewer
- Report download
- Order new test (e-referral form)
- Critical value alerts list
- Profile settings
- Help page

**Acceptance Criteria:**
- [ ] Doctor login with Google SSO
- [ ] Dashboard with recent reports
- [ ] Patient search
- [ ] Report viewer with download
- [ ] E-referral order form
- [ ] Critical value alerts
- [ ] All 10 screens implemented

---

### Frontend — Patient Portal

#### LIS-125: Build Patient Portal Angular screens
**Labels:** `frontend`
**Description:**
Patient portal screens (Screens #170-177):
- Patient login (OTP via phone number)
- Patient dashboard (my reports summary)
- Report list with date filter
- Report viewer with download
- Invoice history
- Payment (if online payment enabled — stretch goal)
- Profile update form
- Help/contact page

**Acceptance Criteria:**
- [ ] OTP login flow
- [ ] Dashboard with report summary
- [ ] Report list and viewer
- [ ] Invoice history
- [ ] Profile update
- [ ] All 8 screens implemented

---

### Frontend — Home Collection

#### LIS-126: Build Home Collection Angular screens
**Labels:** `frontend`
**Description:**
Home collection screens (Screens #178-185):
- Home collection request form (patient, tests, date, time, address)
- Collection slot booking calendar
- Phlebotomist schedule view (calendar/list)
- Route planning display (map view — stretch goal)
- Patient location on map
- Collection confirmation screen
- Home collection report
- Failed collection management

**Acceptance Criteria:**
- [ ] Request form component
- [ ] Slot booking calendar
- [ ] Schedule management
- [ ] Collection confirmation
- [ ] All 8 screens implemented

---

### Frontend — Dashboard & Analytics

#### LIS-127: Build Main Dashboard and Status screens
**Labels:** `frontend`
**Description:**
Dashboard screens (Screens #6-13):
- Main dashboard with summary cards (orders, results, revenue)
- Branch overview (multi-branch comparison)
- Pending actions dashboard
- TAT compliance dashboard (charts)
- Instrument status dashboard (live status)
- QC dashboard (today's QC status)
- Critical values dashboard
- Financial dashboard

**Acceptance Criteria:**
- [ ] Summary cards with real-time data
- [ ] Chart components (Chart.js or ngx-charts)
- [ ] TAT compliance charts
- [ ] Instrument status with live indicators
- [ ] All 8 dashboard screens

---

#### LIS-128: Build Analytics and Reporting screens
**Labels:** `frontend`
**Description:**
Analytics screens (Screens #186-194):
- Test volume report with charts (bar, line, pie)
- Revenue report with date range picker
- TAT analysis report with trend lines
- QC performance report
- Doctor-wise referral report
- Patient demographics report
- Instrument utilization report
- Audit trail report (admin only, searchable log)
- Custom report builder (stretch goal)

**Acceptance Criteria:**
- [ ] Report screens with interactive charts
- [ ] Date range selectors
- [ ] Export to CSV/Excel
- [ ] Audit trail search and filter
- [ ] All 9 analytics screens

---

### Infrastructure — Production Readiness

#### LIS-129: Implement Performance Testing
**Labels:** `testing`, `infrastructure`
**Description:**
Performance testing and optimization:
- JMeter/k6 load test scripts
- Target SLAs:
  - Patient search p95 < 200ms
  - Order creation p95 < 500ms
  - Result entry p95 < 500ms
  - Report generation p95 < 5s
  - Dashboard load p95 < 1s
- Concurrent users: 50 per branch
- Database query optimization (explain analyze)
- Redis caching for hot paths (dashboard, test catalog, reference ranges)
- Connection pool tuning

**Acceptance Criteria:**
- [ ] Load test scripts for all critical paths
- [ ] SLA verification report
- [ ] Query optimization (slow query identification)
- [ ] Caching implementation
- [ ] Connection pool configuration

---

#### LIS-130: Conduct Security Audit
**Labels:** `security`
**Description:**
Security review per security-architecture.md:
- OWASP Top 10 review
- Penetration testing (critical endpoints)
- JWT token security (expiry, refresh, revocation)
- PHI logging audit (ensure no PHI in logs)
- SQL injection prevention verification
- XSS prevention verification
- Security headers verification
- Rate limiting verification
- HIPAA compliance checklist review
- Dependency vulnerability scan (OWASP Dependency-Check)

**Acceptance Criteria:**
- [ ] OWASP Top 10 review complete
- [ ] Penetration test report
- [ ] PHI logging audit passed
- [ ] Dependency scan clean
- [ ] HIPAA checklist completed
- [ ] Security findings remediated

---

#### LIS-131: Prepare Kubernetes Deployment Configuration
**Labels:** `infrastructure`
**Description:**
Production K8s deployment:
- Kubernetes manifests for all services
- Helm charts (optional)
- Resource limits and requests
- HPA (Horizontal Pod Autoscaler) configuration
- Ingress configuration with TLS
- ConfigMaps and Secrets management
- Persistent volume claims for PostgreSQL, MinIO
- Monitoring: Prometheus + Grafana
- Log aggregation: ELK stack or Loki

**Acceptance Criteria:**
- [ ] K8s manifests for all services
- [ ] Ingress with TLS
- [ ] HPA configuration
- [ ] Monitoring setup
- [ ] Log aggregation
- [ ] Deployment tested on staging

---

### Documentation

#### LIS-132: Complete All Documentation
**Labels:** `documentation`
**Description:**
Final documentation:
- API documentation (OpenAPI for all endpoints)
- User manual (per role)
- Admin guide (master data setup, branch provisioning)
- Deployment runbook
- Troubleshooting guide
- Training materials (presentations, videos)
- Release notes

**Acceptance Criteria:**
- [ ] Complete OpenAPI documentation
- [ ] User manual for each role
- [ ] Admin setup guide
- [ ] Deployment runbook
- [ ] Training materials

---

### Database

#### LIS-133: Create Flyway migrations for Phase 8 tables
**Labels:** `backend`, `database`
**Description:**
Phase 8 database migrations:
- HomeCollectionRequest, CollectionSlot, PhlebotomistSchedule tables
- DoctorPortalSession table (optional)
- AnalyticsCache table (materialized views)
- Custom report configuration table

**Acceptance Criteria:**
- [ ] All migrations created
- [ ] Performance indexes
- [ ] Materialized view scripts for analytics

---

### Testing

#### LIS-134: End-to-end integration test: Complete LIS workflow
**Labels:** `backend`, `testing`
**Description:**
Full system integration test covering all phases:
1. Register patient → Create order → Pay
2. Collect samples → Receive → Accept
3. Instrument sends results → Auto-map → Auto-validate
4. Pathologist authorizes → Report generated → Delivered
5. Doctor views on portal → Patient downloads from portal
6. QC daily run → Westgard evaluation
7. Dashboard shows correct metrics
8. Analytics reports generate correctly

**Acceptance Criteria:**
- [ ] Complete happy path test
- [ ] Multi-branch isolation verified
- [ ] All state machines traversed
- [ ] Performance within SLA
- [ ] No PHI in logs

---

#### LIS-135: Comprehensive test coverage review and gap filling
**Labels:** `testing`
**Description:**
Final testing push:
- Backend coverage: minimum 80% overall, 90% services
- Frontend coverage: minimum 75% overall, 85% services
- E2E tests (Cypress): 50+ scenarios covering all critical paths
- Multi-branch isolation tests
- Data validation tests
- Error handling tests
- Security test cases

**Acceptance Criteria:**
- [ ] Backend coverage ≥ 80%
- [ ] Frontend coverage ≥ 75%
- [ ] 50+ Cypress E2E scenarios
- [ ] All critical paths covered
- [ ] Security test cases passing

---

## Completion Criteria

- [ ] Doctor portal live with Google SSO
- [ ] Patient portal live with OTP login
- [ ] Home collection module functional
- [ ] All 8 dashboard screens with live data
- [ ] All 9 analytics reports functional
- [ ] Performance testing passed (all SLAs met)
- [ ] Security audit completed and findings remediated
- [ ] Kubernetes deployment configured and tested
- [ ] All documentation complete
- [ ] Overall test coverage targets met
- [ ] Production deployment checklist complete
- [ ] **Total: All 194 screens implemented across all phases**
