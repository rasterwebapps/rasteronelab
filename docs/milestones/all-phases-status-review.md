# RasterOneLab LIS — All Phases Status Review

> **Review Date:** 2026-03-19 (updated after PRs #15, #16, #17)
> **Scope:** All 8 development phases (LIS-001 through LIS-135)
> **Reviewed By:** Automated codebase analysis

---

## Executive Summary

| Phase | Issues | Status | Progress | Key Blocker |
|-------|--------|--------|----------|-------------|
| Phase 1 — Foundation | 15 | ✅ Complete | **100%** | — |
| Phase 2 — Administration Module | 18 | 🟡 In Progress | **~97%** | Frontend unit tests; OpenAPI annotations on admin controllers |
| Phase 3 — Patient & Ordering | 21 | 🟡 In Progress | **~65%** | State machine partial; Spring Events not wired; panel expansion missing |
| Phase 4 — Sample Management | 14 | ⬜ Not Started | **0%** | Blocked by Phase 3 state machine |
| Phase 5 — Result Entry & Validation | 20 | ⬜ Not Started | **0%** | Blocked by Phase 4 |
| Phase 6 — Instrument Interface | 12 | ⬜ Not Started | **0%** | Can overlap with Phase 5 |
| Phase 7 — Reports, QC & Notifications | 17 | ⬜ Not Started | **0%** | Blocked by Phase 5 |
| Phase 8 — Portals, Analytics & Launch | 18 | ⬜ Not Started | **0%** | Blocked by Phase 7 |
| **Total** | **135** | | | |

**Overall project completion: ~40%** (Phases 1–2 complete, Phase 3 ~65%, all others 0%)

---

## Phase 1 — Foundation ✅ COMPLETE (100%)

**Timeline:** Months 1–2 | **Issues:** 15 (LIS-001 to LIS-015)

### Implementation Summary

| Layer | Component | Status |
|-------|-----------|--------|
| Core | BaseEntity, ApiResponse, PagedResponse, exceptions | ✅ |
| Core | BranchContextHolder, BranchInterceptor | ✅ |
| Core | BranchAwareRepository | ✅ |
| Auth | Keycloak realm (10 roles, JWT claims, test users) | ✅ |
| Auth | Spring Security OAuth2 Resource Server | ✅ |
| Gateway | Spring Cloud Gateway routing + JWT validation + rate limiting | ✅ |
| Frontend | Angular 19 scaffold, auth flow, BranchInterceptor, layout | ✅ |
| Infrastructure | Docker Compose (Postgres, Redis, RabbitMQ, Keycloak, ES, MinIO) | ✅ |
| Infrastructure | Jenkins CI/CD pipeline (multi-branch) | ✅ |
| Infrastructure | Dockerfiles for backend and frontend | ✅ |
| Database | Flyway framework, core tables | ✅ |
| Tests | lis-core (8 tests), lis-auth (1 test), lis-gateway (1 test) | ✅ |

### Issue Status

| Issue | Title | Status |
|-------|-------|--------|
| LIS-001 | BaseEntity with UUID v7, audit fields, soft delete | ✅ Done |
| LIS-002 | ApiResponse and PagedResponse | ✅ Done |
| LIS-003 | Global exception handler and custom exceptions | ✅ Done |
| LIS-004 | BranchContextHolder and BranchInterceptor | ✅ Done |
| LIS-005 | BranchAwareRepository base interface | ✅ Done |
| LIS-006 | Keycloak realm with roles and custom claims | ✅ Done |
| LIS-007 | Spring Security OAuth2 Resource Server | ✅ Done |
| LIS-008 | Spring Cloud Gateway routing and JWT validation | ✅ Done |
| LIS-009 | Angular 19 application with authentication flow | ✅ Done |
| LIS-010 | Angular BranchInterceptor and BranchService | ✅ Done |
| LIS-011 | Shared Angular layout and navigation components | ✅ Done |
| LIS-012 | Docker Compose for all services | ✅ Done |
| LIS-013 | CI/CD pipeline with Jenkins | ✅ Done |
| LIS-014 | Dockerfiles for backend and frontend | ✅ Done |
| LIS-015 | Flyway migration framework and core tables | ✅ Done |

> **Phase 1 verdict: ✅ 100% complete. No action required.**

---

## Phase 2 — Administration Module 🟡 IN PROGRESS (~97%)

**Timeline:** Months 2–4 | **Issues:** 18 (LIS-016 to LIS-033)

### Implementation Metrics

| Layer | Implemented | Notes |
|-------|-------------|-------|
| Backend Java files | 200+ | `lis-admin` module |
| JPA Entities | 30 | All master data entities (incl. NotificationTemplate, ReportTemplate, DiscountScheme, InsuranceTariff, Role — added PR #15) |
| Repositories | 28 | Full persistence layer |
| Services | 26 | CRUD + business logic |
| Controllers | 26 | REST endpoints with `@PreAuthorize` |
| DTOs (Request/Response) | 52 | MapStruct-mapped |
| MapStruct Mappers | 26 | Entity ↔ DTO |
| Flyway Migrations | 50+ | Forward + rollback + 12 repeatable seed migrations (R__001–R__012) |
| Service Unit Tests | **21 of 26 (81%)** | **✅ 80% target met** — 5 untested: AntibioticOrganismMapping, AutoValidationRule, DeltaCheckConfig, Holiday, Microorganism |
| Frontend Components | 42 | 21 list + 21 form pairs — **inline templates (Tailwind + Angular Material)** |
| Frontend Tests | 0 | No `.spec.ts` files |

### Issue Status

| Issue | Title | Status | Gap |
|-------|-------|--------|-----|
| LIS-016 | Organization CRUD API | ✅ Complete | Tests exist (OrganizationServiceTest) |
| LIS-017 | Branch Management CRUD API | ✅ Complete | Tests exist (BranchServiceTest) |
| LIS-018 | Department Management CRUD API | ✅ Complete | Tests exist (DepartmentServiceTest, BranchDepartmentServiceTest); seed data ✅ (PR #16 — R__001) |
| LIS-019 | Test Master CRUD API | ✅ Complete | Tests exist (TestMasterServiceTest) |
| LIS-020 | Parameter Master CRUD API | ✅ Complete | Tests exist (ParameterServiceTest) |
| LIS-021 | Reference Range Config API | ✅ Complete | Tests exist (ReferenceRangeServiceTest) |
| LIS-022 | Test Panel Management API | ✅ Complete | Tests exist (TestPanelServiceTest) |
| LIS-023 | Pricing Catalog API | ✅ Complete | Tests exist (PriceCatalogServiceTest) |
| LIS-024 | Doctor Management API | ✅ Complete | Tests exist (DoctorServiceTest) |
| LIS-025 | User Management API | ✅ Complete | Tests exist (AppUserServiceTest) |
| LIS-026 | Number Series Config API | ✅ Complete | Tests exist (NumberSeriesServiceTest) |
| LIS-027 | TAT Configuration API | ✅ Complete | Tests exist (TATConfigurationServiceTest) |
| LIS-028 | Working Hours & Holiday Config API | ✅ Complete | Tests exist (WorkingHoursServiceTest); Holiday service lacks test |
| LIS-029 | Critical Value Config API | ✅ Complete | Tests exist (CriticalValueConfigServiceTest) |
| LIS-030 | Delta Check & Auto-validation Config API | ✅ Complete | No tests for DeltaCheckConfig/AutoValidationRule services |
| LIS-031 | Antibiotic & Microorganism Masters API | ✅ Complete | Tests exist (AntibioticServiceTest); AntibioticOrganismMapping + Microorganism lack tests |
| LIS-032 | Configuration Screens (Notification/Report/Discount/Insurance) | ✅ Complete | **All 4 entities added in PR #15**: NotificationTemplate, ReportTemplate, DiscountScheme, InsuranceTariff — full CRUD stack + tests |
| LIS-033 | Role & Permission Management API | ✅ Complete | Full Java stack (entity, repo, service, controller, DTOs, mapper); DB schema ✅ (V20260318_0016); seed data ✅ (PR #16 — R__008); tests (RoleServiceTest) |

### 🟢 Remaining Gaps in Phase 2

1. **Frontend unit tests** — 42 Angular components have no `.spec.ts` files (TASK-P2-08)
2. **OpenAPI annotations** — admin controllers have no `@Tag`/`@Operation` annotations; Swagger UI shows auto-generated names only (TASK-P2-09)
3. **5 services without tests** — AntibioticOrganismMappingService, AutoValidationRuleService, DeltaCheckConfigService, HolidayService, MicroorganismService (coverage is 81% — above threshold, but these are still untested)

> ✅ **Resolved (PR #15):** All 5 missing backend entities — NotificationTemplate, ReportTemplate, DiscountScheme, InsuranceTariff, Role — added with full CRUD stacks and service tests.
> ✅ **Resolved (PR #15 + PR #16):** Seed data — V20260317_0019–V20260317_0021 + 12 repeatable migrations (R__001–R__012).
> ✅ **Resolved (PR #15):** Backend test coverage — 21 of 26 services tested = 81% (target: 80% ✅).

> **Phase 2 verdict: 🟡 ~97% — Only frontend unit tests (TASK-P2-08) and OpenAPI annotations (TASK-P2-09) remain. Estimated effort: 3–5 days.**
> See [pending-tasks.md](pending-tasks.md) for the full task breakdown with file checklists.

---

## Phase 3 — Patient & Ordering 🟡 IN PROGRESS (~65%)

**Timeline:** Months 4–6 | **Issues:** 21 (LIS-034 to LIS-054)

### Implementation Metrics

| Module | Entities | Repos | Services | Controllers | Tests | Migrations | OpenAPI |
|--------|----------|-------|----------|-------------|-------|------------|---------|
| `lis-patient` | 5 | 3 | 2 | 2 | 1 | 4 | ✅ |
| `lis-order` | 5 | 2 | 1 | 1 | 1 | 2 | ✅ |
| `lis-billing` | 10 | 5 | 4 | 4 | 2 | 5 | ✅ |
| `lis-core` | — | — | — | — | 8 | — | — |
| **Total** | **20** | **10** | **7** | **7** | **12** | **11** | **7 controllers** |

### Issue Status

| Issue | Title | Status | Notes |
|-------|-------|--------|-------|
| LIS-034 | Patient CRUD API with UHID generation | ✅ Complete | PatientService + PatientController; `generateUhid()` implemented |
| LIS-035 | Duplicate patient detection and merge | ⚠️ Partial | `PatientMergeAudit` entity exists; detection algorithm **not yet implemented** |
| LIS-036 | Patient Visit Management | ✅ Complete | PatientVisitService + PatientVisitController |
| LIS-037 | Patient Demographics & PHI Audit | ❌ Not Done | No PHI audit logging |
| LIS-038 | Patient Search (multi-criteria) | ✅ Complete | DB-based search via `patientRepository.searchPatients()`; no Elasticsearch |
| LIS-039 | Patient Angular screens | ✅ Complete | patient-list, patient-form, patient-detail — inline Tailwind templates |
| LIS-040 | Test Order API with panel expansion | ⚠️ Partial | TestOrder + OrderLineItem CRUD; panel expansion **not implemented** |
| LIS-041 | Order State Machine | ⚠️ Partial | `placeOrder()` (DRAFT→PLACED) and `cancelOrder()` implemented; full lifecycle (PAID→SAMPLE_COLLECTED→…→COMPLETED) **not done** |
| LIS-042 | Barcode Generation for Orders | ⚠️ Partial | `BarcodeGeneratorUtil` implemented in `lis-core`; barcode field in `TestOrder`; **not wired** in TestOrderService |
| LIS-043 | Order Search & Worklist API | ✅ Complete | `getByStatus()` and `getByPatient()` with pagination |
| LIS-044 | Order Angular screens | ✅ Complete | order-list, order-create, order-detail — inline Tailwind templates (PR #17) |
| LIS-045 | Invoice Generation API | ✅ Complete | InvoiceService with `generateInvoice()` and `generateInvoiceNumber()` |
| LIS-046 | Payment Recording API | ✅ Complete | PaymentService with CRUD |
| LIS-047 | Discount & Concession Application | ❌ Not Done | No discount application logic in InvoiceService |
| LIS-048 | Insurance Billing API | ❌ Not Done | No insurance claim logic |
| LIS-049 | Receipt Generation | ❌ Not Done | No receipt PDF |
| LIS-050 | Corporate Billing Aggregation | ❌ Not Done | No corporate billing logic |
| LIS-051 | Billing Angular screens | ✅ Complete | invoice-list, invoice-detail, payment-form — inline Tailwind templates (PR #17) |
| LIS-052 | Credit Management API | ✅ Complete | CreditAccountService + CreditAccountController |
| LIS-053 | Financial Reports API (basic) | ❌ Not Done | No aggregate report queries |
| LIS-054 | Refund & Cancellation API | ✅ Complete | RefundService + RefundController |

### 🔴 Remaining Gaps in Phase 3

1. **Order State Machine incomplete** — only DRAFT→PLACED and →CANCELLED implemented. PAID, SAMPLE_COLLECTED, IN_PROGRESS, RESULTED, AUTHORISED, COMPLETED transitions are missing. This blocks Phase 4.
2. **Spring Events not wired** — `OrderPlacedEvent`, `OrderCancelledEvent`, `PaymentReceivedEvent` classes exist in `lis-core` but no service calls `publishEvent()`. Invoice auto-generation on order placement and order status update on payment are not connected.
3. **Panel expansion not implemented** — test orders don't split test panels into constituent tests by tube type.
4. **Barcode not wired** — `BarcodeGeneratorUtil` is implemented in `lis-core` and `TestOrder.barcode` field exists, but `TestOrderService.create()` does not call the util to generate a barcode.
5. **PHI audit logging absent** — no demographic change audit trail.
6. **Discount/Insurance/Receipt/Corporate billing** — 4 LIS issues (LIS-047–LIS-050) not yet implemented.
7. **No E2E integration tests** — no Testcontainers-based happy-path flow.

> ✅ **Completed since last review:** Patient CRUD + UHID, PatientVisit, DB migrations, invoice/payment/refund/credit-account CRUD, all 3 frontend modules (patient/order/billing) with inline Tailwind templates, OpenAPI annotations on all 7 Phase 3 controllers (PR #17), order search/worklist.

> **Phase 3 verdict: 🟡 ~65% — Core CRUD and frontend are done. Critical domain logic (full state machine, Spring Events, panel expansion, barcode) still needed. Estimated effort to complete: ~3–4 weeks.**

---

## Phase 4 — Sample Management ⬜ NOT STARTED (0%)

**Timeline:** Months 6–8 | **Issues:** 14 (LIS-055 to LIS-068)

### Planned Deliverables

| Issue | Title | Dependency |
|-------|-------|------------|
| LIS-055 | Sample Collection Recording API | Order state machine (LIS-041) |
| LIS-056 | Sample State Machine | LIS-055 |
| LIS-057 | Sample Barcode Scanning Integration | LIS-056 |
| LIS-058 | Sample Rejection & Recollection Workflow | LIS-056 |
| LIS-059 | Sample Receiving at Lab | LIS-057 |
| LIS-060 | Sample Aliquoting | LIS-059 |
| LIS-061 | Sample Storage & Location Tracking | LIS-059 |
| LIS-062 | Sample Disposal Workflow | LIS-061 |
| LIS-063 | Inter-branch Sample Transfer | LIS-059 |
| LIS-064 | TAT Monitoring & Alerts | LIS-060 |
| LIS-065 | Sample Inventory Tracking | LIS-061 |
| LIS-066 | Sample Module Angular screens (14 screens) | LIS-055 |
| LIS-067 | Barcode Scanner Hardware Integration | LIS-057 |
| LIS-068 | Sample SLA & KPI Reporting | LIS-064 |

**Backend module:** `lis-sample` (currently empty — 0 files)
**Frontend:** Scaffolded (`frontend/src/app/features/sample/`)

> **Phase 4 verdict: ⬜ 0% — Cannot start until LIS-041 (Order State Machine) is complete.**
> **Estimated effort: 4 weeks once Phase 3 is unblocked.**

---

## Phase 5 — Result Entry & Validation ⬜ NOT STARTED (0%)

**Timeline:** Months 8–12 | **Issues:** 20 (LIS-069 to LIS-088)

### Planned Deliverables

| Issue | Title | Notes |
|-------|-------|-------|
| LIS-069 | Result Entry Core API and State Machine | Foundation for all departments |
| LIS-070 | Biochemistry Result Entry | Numeric + calculated parameters |
| LIS-071 | Hematology Result Entry | CBC with 5-part diff; histograms |
| LIS-072 | Microbiology Result Entry | Culture + antibiogram (S/I/R) |
| LIS-073 | Histopathology Result Entry | Narrative + image attachments |
| LIS-074 | Clinical Pathology Result Entry | Urine routine, stool microscopy |
| LIS-075 | Serology/Immunology Result Entry | Qualitative + titre results |
| LIS-076 | Molecular Biology Result Entry | PCR CT values; gene targets |
| LIS-077 | Auto-calculation Engine | Derived parameters (e.g., eGFR, LDL) |
| LIS-078 | Delta Check Implementation | Compare with previous results |
| LIS-079 | Critical Value Detection & Notification | Trigger notification pipeline |
| LIS-080 | Auto-validation Engine | Rule-based auto-release |
| LIS-081 | Result Verification Workflow | Dual-sign for high-risk tests |
| LIS-082 | Result Authorization (e-signature) | Senior pathologist approval |
| LIS-083 | Result Amendment Workflow | Amended report tracking |
| LIS-084 | External Result Entry (referred tests) | External lab integration |
| LIS-085 | Result Entry Angular screens (20 screens) | Dept-specific UIs |
| LIS-086 | Authorization Angular screens (8 screens) | Pathologist dashboard |
| LIS-087 | Micro Antibiogram UI | S/I/R matrix |
| LIS-088 | Result Worklist API | Pending results by department |

**Backend module:** `lis-result` (currently empty — 0 files)
**Frontend:** Scaffolded (`frontend/src/app/features/result/`)

> **Phase 5 verdict: ⬜ 0% — Most complex phase (7 departments, 20 issues). Blocked by Phase 4.**
> **Estimated effort: 8 weeks.**

---

## Phase 6 — Instrument Interface ⬜ NOT STARTED (0%)

**Timeline:** Months 10–13 | **Issues:** 12 (LIS-089 to LIS-100)

### Planned Deliverables

| Issue | Title | Notes |
|-------|-------|-------|
| LIS-089 | ASTM E1381 TCP/IP Connection Manager | Netty or NIO; multi-instrument |
| LIS-090 | ASTM E1394 Frame Parser | STX/ETX framing; checksum |
| LIS-091 | Instrument Message Handler | Host Query, Result, Patient records |
| LIS-092 | Bidirectional Order Upload to Instruments | LIS → instrument worklist |
| LIS-093 | Auto-result Import from Instruments | Instrument → LIS via RabbitMQ |
| LIS-094 | Roche Cobas c311 Integration | Driver + test mapping |
| LIS-095 | Sysmex XN-1000 Integration | Driver + test mapping |
| LIS-096 | Instrument QC Data Collection | Level I/II/III auto-import |
| LIS-097 | Instrument Error Handling & Alerts | Connection drops; checksum errors |
| LIS-098 | Instrument Log & Audit Trail | All messages archived |
| LIS-099 | Instrument Management Angular screens (8 screens) | Config + status dashboard |
| LIS-100 | ASTM/HL7 Message Archive | MinIO storage for raw messages |

**Backend module:** `lis-instrument` (currently empty — 0 files)
**Frontend:** Scaffolded (`frontend/src/app/features/`)

> **Phase 6 verdict: ⬜ 0% — Can overlap with Phase 5 (starts month 10). Requires ASTM protocol expertise.**
> **Estimated effort: 4 weeks.**

---

## Phase 7 — Reports, QC & Notifications ⬜ NOT STARTED (0%)

**Timeline:** Months 12–16 | **Issues:** 17 (LIS-101 to LIS-117)

### Planned Deliverables

| Issue | Title | Module |
|-------|-------|--------|
| LIS-101 | PDF Report Generation Engine (OpenPDF) | `lis-report` |
| LIS-102 | Department-specific Report Layouts | `lis-report` |
| LIS-103 | Report Header & Branding (per branch) | `lis-report` |
| LIS-104 | Report Delivery (WhatsApp, Email, Portal) | `lis-notification` |
| LIS-105 | Report Angular screens (10 screens) | Frontend |
| LIS-106 | QC Material & Level Management | `lis-qc` |
| LIS-107 | QC Result Entry & Westgard Rules | `lis-qc` |
| LIS-108 | Levey-Jennings Chart Generation | `lis-qc` |
| LIS-109 | External QA / EQA Program Integration | `lis-qc` |
| LIS-110 | QC Angular screens (10 screens) | Frontend |
| LIS-111 | SMS/Email/WhatsApp Notification Engine | `lis-notification` |
| LIS-112 | Critical Value Notification (call + SMS) | `lis-notification` |
| LIS-113 | Report Ready Notification | `lis-notification` |
| LIS-114 | Notification Template Management | `lis-notification` |
| LIS-115 | Notification Angular screens (5 screens) | Frontend |
| LIS-116 | Inventory Management (reagents) | `lis-inventory` |
| LIS-117 | Inventory Angular screens (12 screens) | Frontend |

**Backend modules:** `lis-report`, `lis-qc`, `lis-notification`, `lis-inventory` (all empty — 0 files)
**Frontend:** Scaffolded

> **Phase 7 verdict: ⬜ 0% — High integration complexity (PDF + QC + multi-channel). Blocked by Phase 5.**
> **Estimated effort: 6 weeks.**

---

## Phase 8 — Portals, Analytics & Launch ⬜ NOT STARTED (0%)

**Timeline:** Months 16–22 | **Issues:** 18 (LIS-118 to LIS-135)

### Planned Deliverables

| Issue | Title | Notes |
|-------|-------|-------|
| LIS-118 | Doctor Portal Backend API | OAuth2; referral-scoped access |
| LIS-119 | Doctor Portal Angular App | Separate lazy-loaded module |
| LIS-120 | Patient Portal Backend API | OTP login; self-registration |
| LIS-121 | Patient Portal Angular App | Report download; appointment |
| LIS-122 | Home Collection Module | Route assignment; GPS tracking |
| LIS-123 | Analytics Dashboard APIs | Test TAT, revenue, popular tests |
| LIS-124 | Operational Reports | Branch, department, user reports |
| LIS-125 | Analytics Angular screens (dashboards) | Charts + KPIs |
| LIS-126 | Performance Testing | JMeter; 200 concurrent users |
| LIS-127 | Security Audit | OWASP Top 10; pen test |
| LIS-128 | FHIR R4 Integration | `lis-integration` |
| LIS-129 | HL7 v2 Integration | External labs |
| LIS-130 | Multi-language Support (i18n) | Frontend; 3 languages |
| LIS-131 | Accessibility (WCAG 2.1 AA) | Screen readers; keyboard nav |
| LIS-132 | Production Kubernetes Deployment | Helm charts; HPA |
| LIS-133 | Disaster Recovery & Backup | RTO 4h; RPO 1h |
| LIS-134 | User Acceptance Testing (UAT) | 2-branch pilot |
| LIS-135 | Production Launch & Handover | Go-live checklist |

**Backend module:** `lis-integration` (currently empty — 0 files)
**Frontend:** Portals scaffolded (`doctor-portal`, `patient-portal`)

> **Phase 8 verdict: ⬜ 0% — Final phase. Includes performance testing, security audit, and production launch.**
> **Estimated effort: 6 weeks + 6-8 weeks for UAT and launch preparation.**

---

## Implementation Metrics Summary

### Backend Module Status

```
Module             Files    Entities   Services   Controllers   Tests   Phase   Status
─────────────────────────────────────────────────────────────────────────────────────────
lis-core            9          1          0            0          8     1     ✅ Done
lis-auth            4          0          0            0          1     1     ✅ Done
lis-gateway         3          0          0            0          1     1     ✅ Done
lis-admin          200+       30         26           26         21     2     🟡 ~97%
lis-patient         20         5          2            2          1     3     🟡 ~65%
lis-order           16         5          1            1          1     3     🟡 ~65%
lis-billing         35        10          4            4          2     3     🟡 ~65%
lis-sample           0         0          0            0          0     4     ⬜ 0%
lis-result           0         0          0            0          0     5     ⬜ 0%
lis-instrument       0         0          0            0          0     6     ⬜ 0%
lis-report           0         0          0            0          0     7     ⬜ 0%
lis-qc               0         0          0            0          0     7     ⬜ 0%
lis-notification     0         0          0            0          0     7     ⬜ 0%
lis-inventory        0         0          0            0          0     7     ⬜ 0%
lis-integration      0         0          0            0          0     8     ⬜ 0%
─────────────────────────────────────────────────────────────────────────────────────────
TOTAL              287+       51         33           33         35
```

### Frontend Feature Status

```
Feature           Components   Models   Services   Tests   Status
─────────────────────────────────────────────────────────────────
admin                 42         25        3          0    🟡 Inline templates ✅; no unit tests
patient                3          3        1          0    🟡 Inline templates ✅; no unit tests
order                  3          3        1          0    🟡 Inline templates ✅; no unit tests
billing                3          3        1          0    🟡 Inline templates ✅; no unit tests
sample                 1          0        0          0    ⬜ Scaffold only
result                 1          0        0          0    ⬜ Scaffold only
report                 1          0        0          0    ⬜ Scaffold only
qc                     1          0        0          0    ⬜ Scaffold only
inventory              1          0        0          0    ⬜ Scaffold only
dashboard              1          0        0          0    ⬜ Scaffold only
doctor-portal          1          0        0          0    ⬜ Scaffold only
patient-portal         1          0        0          0    ⬜ Scaffold only
─────────────────────────────────────────────────────────────────
TOTAL                 59         34        6          0
```

### Test Coverage

| Module | Test Files | Services Covered | Target | Gap |
|--------|-----------|-----------------|--------|-----|
| `lis-core` | 8 | N/A (utility tests) | — | — |
| `lis-admin` | 21 | **21 of 26 (81%)** | 80% | **✅ TARGET MET** — 5 services still untested |
| `lis-patient` | 1 | 1 of 2 (50%) | 80% | 30% gap |
| `lis-order` | 1 | 1 of 1 (100%) | 80% | — |
| `lis-billing` | 2 | 2 of 4 (50%) | 80% | 30% gap |
| `lis-auth` | 1 | Security config | — | — |
| `lis-gateway` | 1 | Gateway config | — | — |
| `lis-sample–lis-integration` | 0 | 0 | 80% | ∞ |
| **Frontend** | **0** | **0 of 59 components** | 70% | **∞** |

---

## Critical Path & Blockers

```
Phase 3 (Order State Machine) ──→ Phase 4 (Sample Lifecycle)
                                      │
                              Phase 5 (Result Entry)
                                 │          │
                          Phase 6 (Instruments, overlaps)
                                      │
                              Phase 7 (Reports, QC, Notifications)
                                      │
                              Phase 8 (Portals, Analytics, Launch)
```

### 🔴 Blocker 1 — Order State Machine (LIS-041)
Orders can only transition DRAFT→PLACED and →CANCELLED. The full lifecycle (PAID→SAMPLE_COLLECTED→IN_PROGRESS→RESULTED→AUTHORISED→COMPLETED) and Spring Event publishing are missing. Phase 4 cannot begin until resolved.
**Effort:** 3–5 days | **Owner:** Phase 3 team

### ✅ Blocker 2 — Phase 2 Missing Entities — RESOLVED (PR #15)
All 5 entities (NotificationTemplate, ReportTemplate, DiscountScheme, InsuranceTariff, Role) were added with full CRUD stacks and service tests.

### ✅ Risk 1 — Test Coverage (Phase 2) — RESOLVED (PR #15)
21 of 26 services now tested = 81%, above the 80% target.

### 🟡 Risk 2 — Frontend Unit Tests Missing (Phase 2 + Phase 3)
Admin: 42 components, 0 spec.ts. Patient/Order/Billing: 9 components, 0 spec.ts.
**Effort:** 3–5 days (admin), 2 days (Phase 3)

### ✅ Risk 3 — Seed Data — RESOLVED (PR #16)
12 repeatable Flyway migrations (R__001–R__012) added: departments, sample types, antibiotics, microorganisms, CLSI breakpoints, units, roles/permissions, report templates, rejection reasons, number series, critical values.

---

## Effort Estimates (Remaining Work)

| Phase | Current | Target | Effort | Key Remaining Work |
|-------|---------|--------|--------|--------------------|
| Phase 2 | 97% | 100% | ~3 days | Frontend unit tests (TASK-P2-08), OpenAPI admin annotations (TASK-P2-09) |
| Phase 3 | 65% | 100% | ~3–4 wks | Full state machine, Spring Events, panel expansion, barcode wiring, 6 missing LIS issues |
| Phase 4 | 0% | 100% | 4 wks | Full sample lifecycle, barcode scanning, state machine |
| Phase 5 | 0% | 100% | 8 wks | 7 depts, 20 issues, auto-calc, delta check, critical values |
| Phase 6 | 0% | 100% | 4 wks | ASTM TCP, frame parser, 2 instrument drivers, RabbitMQ |
| Phase 7 | 0% | 100% | 6 wks | PDF engine, Westgard QC, SMS/Email/WhatsApp, inventory |
| Phase 8 | 0% | 100% | 12 wks | Portals, analytics, perf test, security audit, UAT, launch |
| **Total remaining** | | | **~37 wks** | **~9–10 months** |

---

## Recommended Action Plan

### Immediate (Next 3 Days) — Close Phase 2

1. **Add frontend unit tests** for 4+ critical admin components (TASK-P2-08)
2. **Add OpenAPI `@Operation` annotations** to 26 admin controllers (TASK-P2-09)
   > ✅ All 5 missing entities (PR #15), seed data (PR #15 + PR #16), test coverage ≥80% (PR #15) — already done.

### Short Term (Next 3–4 Weeks) — Complete Phase 3

3. **Implement full Order State Machine** — add PAID, SAMPLE_COLLECTED, IN_PROGRESS, RESULTED, AUTHORISED, COMPLETED transitions
4. **Wire Spring Events** — `OrderPlacedEvent` (auto-generate invoice), `PaymentReceivedEvent` (mark order PAID)
5. **Implement panel expansion** — expand TestPanel → constituent OrderLineItems grouped by tube type
6. **Wire barcode generation** — call `BarcodeGeneratorUtil.generateOrderNumber()` in `TestOrderService.create()`
7. **Implement 6 remaining LIS issues** — Discount (LIS-047), Insurance (LIS-048), Receipt PDF (LIS-049), Corporate Billing (LIS-050), Financial Reports (LIS-053), PHI Audit (LIS-037)
8. **Add E2E integration tests** — Testcontainers happy-path flow

### Medium Term (Weeks 10–22) — Phase 4 + Phase 5 + Phase 6

10. Complete sample lifecycle (LIS-055 to LIS-068)
11. Implement result entry for all 7 departments (LIS-069 to LIS-088) — assign domain experts
12. Instrument ASTM integration (LIS-089 to LIS-100, overlapping with Phase 5)

### Long Term (Months 6–11) — Phase 7 + Phase 8

13. PDF reports, Westgard QC, notification engine, inventory (LIS-101 to LIS-117)
14. Doctor portal, patient portal, analytics dashboards (LIS-118 to LIS-125)
15. Performance test, security audit, UAT, production launch (LIS-126 to LIS-135)

---

## References

| Document | Location |
|----------|----------|
| Phase 1 issues | [phase-1-foundation.md](phase-1-foundation.md) |
| Phase 2 issues | [phase-2-administration.md](phase-2-administration.md) |
| Phase 2 detailed gap analysis | [phase-2-status-review.md](phase-2-status-review.md) |
| Phase 3 issues | [phase-3-patient-ordering.md](phase-3-patient-ordering.md) |
| Phase 4 issues | [phase-4-sample-management.md](phase-4-sample-management.md) |
| Phase 5 issues | [phase-5-result-entry.md](phase-5-result-entry.md) |
| Phase 6 issues | [phase-6-instrument-interface.md](phase-6-instrument-interface.md) |
| Phase 7 issues | [phase-7-reports-qc-notifications.md](phase-7-reports-qc-notifications.md) |
| Phase 8 issues | [phase-8-portals-analytics.md](phase-8-portals-analytics.md) |
| Pending task checklist (Phases 1–3) | [pending-tasks.md](pending-tasks.md) |
| Milestones README | [README.md](README.md) |
