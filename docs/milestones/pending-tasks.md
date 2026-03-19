# RasterOneLab LIS — Pending Task List (Phases 1–3)

> **Review Date:** 2026-03-19 (updated after PRs #15, #16, #17, #21)
> **Scope:** Phases 1, 2, and 3 only
> **Overall Phase Status:** Phase 1 ✅ Done · Phase 2 🟡 ~97% · Phase 3 🟡 ~80%

---

## Summary

| Phase | Total Issues | Done | Pending | Blocked |
|-------|-------------|------|---------|---------|
| Phase 1 — Foundation | 15 | 15 | **0** | — |
| Phase 2 — Administration | 18 | 17 | **1 (frontend tests)** | — |
| Phase 3 — Patient & Ordering | 21 | 17 | **4 partial/missing** | — |
| **Total** | **54** | **49** | **5** | — |

---

## ✅ Phase 1 — Foundation (COMPLETE — No Pending Tasks)

All 15 issues (LIS-001 to LIS-015) are fully implemented and verified.

| Issue | Title | Status |
|-------|-------|--------|
| LIS-001 | BaseEntity with UUID v7, audit fields, soft delete | ✅ Done |
| LIS-002 | ApiResponse and PagedResponse | ✅ Done |
| LIS-003 | Global exception handler and custom exceptions | ✅ Done |
| LIS-004 | BranchContextHolder and BranchInterceptor | ✅ Done |
| LIS-005 | BranchAwareRepository base interface | ✅ Done |
| LIS-006 | Keycloak realm configuration (10 roles, JWT claims, test users) | ✅ Done |
| LIS-007 | Spring Security OAuth2 Resource Server | ✅ Done |
| LIS-008 | Spring Cloud Gateway routing and JWT validation | ✅ Done |
| LIS-009 | Angular 19 application scaffold with authentication | ✅ Done |
| LIS-010 | Angular BranchInterceptor and BranchService | ✅ Done |
| LIS-011 | Shared Angular layout and navigation components | ✅ Done |
| LIS-012 | Docker Compose for all services | ✅ Done |
| LIS-013 | Jenkins CI/CD pipeline | ✅ Done |
| LIS-014 | Dockerfiles for backend and frontend | ✅ Done |
| LIS-015 | Flyway migration framework and core tables | ✅ Done |

> **Phase 1 verdict:** ✅ **100% complete. No action required.**

---

## 🟡 Phase 2 — Administration Module (~97% — 2 tasks remaining)

> **Context:** All 26 backend services + controllers + entities implemented; 21 of 26 services tested (81% — target met); 42 inline-template Angular components; comprehensive seed data via R__001–R__012. Only TASK-P2-08 (frontend unit tests) and TASK-P2-09 (OpenAPI annotations) remain.

### 🟡 P1 — Remaining (Phase 2 Completion)

#### ✅ TASK-P2-01 · `NotificationTemplate` — RESOLVED (PR #15)
Full CRUD stack added: entity, repository, service, controller, DTOs, MapStruct mapper, Flyway migration. `NotificationTemplateServiceTest` added.

#### ✅ TASK-P2-02 · `ReportTemplate` — RESOLVED (PR #15)
Full CRUD stack added. `ReportTemplateServiceTest` added.

#### ✅ TASK-P2-03 · `DiscountScheme` — RESOLVED (PR #15)
Full CRUD stack added. `DiscountSchemeServiceTest` added.

#### ✅ TASK-P2-04 · `InsuranceTariff` — RESOLVED (PR #15)
Full CRUD stack added. `InsuranceTariffServiceTest` added.

---

#### ✅ TASK-P2-05 · `Role` and `Permission` — RESOLVED (PR #15 + PR #16)

Java CRUD stack (Role entity, repository, service, controller, DTOs, mapper) added in PR #15. DB schema (`V20260318_0016`) and seed data (`R__008_seed_roles_permissions.sql`: 10 system roles, 23 permissions, full role-permission matrix) added in PR #16. `RoleServiceTest` added in PR #15.

---

#### ✅ TASK-P2-06 · Seed data migrations — RESOLVED (PR #16)

**12 repeatable Flyway seed migrations merged in PR #16:**
- [x] `R__001_seed_departments.sql` — 11 standard lab departments
- [x] `R__002_seed_sample_types.sql` — 15 sample/tube types
- [x] `R__003_seed_antibiotics.sql` — 60+ antibiotics across 17 CLSI drug classes
- [x] `R__004_seed_microorganisms.sql` — 80+ microorganisms
- [x] `R__005_seed_organism_antibiotic_panels.sql` — 11 default antibiotic panels
- [x] `R__006_seed_clsi_breakpoints.sql` — 47 CLSI M100 2024 breakpoints
- [x] `R__007_seed_units.sql` — 46 measurement units
- [x] `R__008_seed_roles_permissions.sql` — 10 roles, 23 permissions, full matrix
- [x] `R__009_seed_report_templates.sql` — default templates for all 11 departments
- [x] `R__010_seed_rejection_reasons.sql` — 15 rejection reasons with severity flags
- [x] `R__011_seed_number_series.sql` — 7 number series patterns (UHID, ORDER, SAMPLE, etc.)
- [x] `R__012_seed_critical_values.sql` — 18 critical value references

All seeds use `ON CONFLICT … DO NOTHING` or `WHERE NOT EXISTS` for idempotency.

---

### 🟡 P1 — Required for Phase 2 Closure

#### TASK-P2-08 · Add frontend unit tests for critical admin components

**Issue:** 0 `.spec.ts` files exist for 42 frontend components.

**Work required (minimum viable coverage — highest-risk components):**
- [ ] `branch-list.component.spec.ts` — test data table, search, pagination
- [ ] `branch-form.component.spec.ts` — test form validation, create/edit flows
- [ ] `test-master-list.component.spec.ts` — test filters, data binding
- [ ] `test-master-form.component.spec.ts` — test parameter assignment
- [ ] `user-list.component.spec.ts` — test role/branch display
- [ ] `user-form.component.spec.ts` — test Keycloak role integration
- [ ] `role-list.component.spec.ts` — test permission matrix rendering

---

#### ✅ TASK-P2-07 · Backend test coverage ≥80% — RESOLVED (PR #15)

21 of 26 services now have unit tests = **81%** (target 80% met).
Services still without tests: AntibioticOrganismMappingService, AutoValidationRuleService, DeltaCheckConfigService, HolidayService, MicroorganismService.

---

### 🟢 P2 — Nice to Have (Should Complete Before Phase 3 Sign-off)

#### TASK-P2-09 · Add OpenAPI `@Operation` annotations to all 26 controllers

**Issue:** SpringDoc is configured and auto-generates from code, but no `@Tag`, `@Operation`, or `@ApiResponse` annotations provide human-readable documentation.

**Work required:**
- [ ] Add `@Tag(name = "...", description = "...")` on each controller class
- [ ] Add `@Operation(summary = "...", description = "...")` on each endpoint method
- [ ] Add `@ApiResponse` for common status codes (200, 400, 404, 409)
- [ ] Add example request/response bodies via `@Schema` on DTOs

---

#### TASK-P2-10 · Implement branch provisioning wizard

**Issue:** LIS-029. There is no guided multi-step dialog for setting up a new branch (working hours, departments, number series).

**Work required:**
- [ ] Multi-step Angular Material `MatStepper` dialog component
- [ ] Step 1: Branch basic info (name, code, type, address)
- [ ] Step 2: Assign departments (checkbox list from org departments)
- [ ] Step 3: Configure working hours (reuse `working-hours-form`)
- [ ] Step 4: Set number series prefixes per document type
- [ ] Confirmation step with summary

---

#### TASK-P2-11 · Verify reference range overlap validation

**Issue:** LIS-021. Overlapping age/gender reference ranges for the same parameter should be rejected.

**Work required:**
- [ ] Review `ReferenceRangeService.create()` for overlap check
- [ ] If missing: add validation that no two ranges for the same `parameterId` + `gender` overlap in age range
- [ ] Add unit test covering overlap scenario

---

#### TASK-P2-12 · Verify branch override pricing logic in `PriceCatalogService`

**Issue:** LIS-023. Branch-level price overrides (branch price takes precedence over org-level price) should be verified.

**Work required:**
- [ ] Review `PriceCatalogService` for branch override logic
- [ ] If missing: implement price resolution order (branch → org → default)
- [ ] Add unit test: branch price overrides org price for same test

---

## 🟡 Phase 3 — Patient & Ordering (~80% — Integration tests and validation remaining)

> **Context (as of 2026-03-19):** lis-patient, lis-order, lis-billing backend CRUD is fully implemented; all 3 frontend modules have inline-template components (PR #17); all 7 Phase 3 controllers have OpenAPI annotations (PR #17); DB migrations are complete. Full order state machine with VALID_TRANSITIONS map implemented (PR #20). Spring Events wired: OrderPlacedEvent→BillingEventListener auto-generates invoice, PaymentReceivedEvent→OrderEventListener transitions to PAID, SampleCollectedEvent→transitions to SAMPLE_COLLECTED (PR #20 + #21). Barcode generation wired in TestOrderService.create() (PR #21). Weighted duplicate patient scoring algorithm implemented (PR #21). Remaining gaps: order validation/sample grouping, discount application logic, E2E integration tests.

### Backend — Patient Module (`lis-patient`)

#### ✅ TASK-P3-01 · Patient CRUD + UHID — RESOLVED
`Patient` entity, `PatientService`, `PatientController`, DTOs, mapper, `PatientRepository`, `PatientVisitRepository`, `PatientMergeAuditRepository`. UHID auto-generated via `generateUhid()` in PatientService. Flyway migrations present. `PatientServiceTest` added.

#### ✅ TASK-P3-02 · LIS-035: Duplicate Patient Detection and Merge — RESOLVED (PR #21)

- [x] Duplicate detection: weighted scoring algorithm (name+DOB=40pts, phone=30pts, email=15pts, gender=15pts)
- [x] `PatientMergeService`: select primary patient, fill missing fields from duplicate, soft-delete duplicate
- [x] Merge audit trail: `PatientMergeAudit` entity logs merged records
- [x] Endpoints: `GET /api/v1/patients/duplicates`, `GET /api/v1/patients/duplicates/scored`, `POST /api/v1/patients/merge`
- [x] `PatientMergeServiceTest` (5 tests), duplicate scoring tests in `PatientServiceTest` (5 tests)

---

#### ✅ TASK-P3-03 · Patient Visit Management — RESOLVED
`PatientVisit` entity, `PatientVisitService`, `PatientVisitController`, DTOs, mapper. Flyway migrations present.

---

### Backend — Order Module (`lis-order`)

#### ✅ TASK-P3-04 · Test Order CRUD — RESOLVED
`TestOrder` + `OrderLineItem` entities, `TestOrderService`, `TestOrderController`, DTOs, mappers. `placeOrder()` (DRAFT→PLACED) and `cancelOrder()` implemented. `TestOrderServiceTest` added.

#### ✅ TASK-P3-05 · LIS-038: Full Order State Machine + Barcode Wiring — RESOLVED (PR #20 + #21)

- [x] Complete state machine: `VALID_TRANSITIONS` map with all transitions DRAFT→PLACED→PAID→SAMPLE_COLLECTED→IN_PROGRESS→RESULTED→AUTHORISED→COMPLETED + CANCELLED from any non-terminal state
- [x] Wire `BarcodeGeneratorUtil.generateOrderNumber()` in `TestOrderService.create()` — barcode auto-generated on order creation
- [x] `updateStatus()` method enforces valid transitions via `VALID_TRANSITIONS` map
- [x] Unit tests for each state transition (15 tests in `TestOrderServiceTest`)
- [x] `OrderEventListener` handles `PaymentReceivedEvent` (→PAID) and `SampleCollectedEvent` (→SAMPLE_COLLECTED)
- [x] `OrderEventListenerTest` (5 tests)

---

#### TASK-P3-06 · LIS-039: Order Validation and Sample Requirements

- [ ] Sample grouping logic: group order line items by sample type and tube type
- [ ] TAT calculation per test based on `TATConfiguration`
- [ ] Pending collection list generation per tube type
- [ ] Unit tests for validation scenarios

---

### Backend — Billing Module (`lis-billing`)

#### ✅ TASK-P3-07 · Invoice Generation — RESOLVED
`Invoice`, `InvoiceLineItem` entities, `InvoiceService` with `generateInvoice()` + `generateInvoiceNumber()`, `InvoiceController`, DTOs, mappers. Flyway migrations present. `InvoiceServiceTest` added.

#### ✅ TASK-P3-08 · Payment Recording — RESOLVED
`Payment` entity, `PaymentService`, `PaymentController`, DTOs, mappers. Flyway migrations present. `PaymentServiceTest` added.

#### ✅ TASK-P3-09 · Refund + Credit Management — RESOLVED
`Refund` entity, `RefundService`, `RefundController`; `CreditAccount` entity, `CreditAccountService`, `CreditAccountController`. Flyway migrations present.

#### TASK-P3-09b · Discount Application (remaining)

- [ ] Discount application: apply `DiscountScheme` to invoice — no discount logic in `InvoiceService`
- [ ] Outstanding invoice tracking: `GET /api/v1/invoices/outstanding`

---

### Frontend

#### ✅ TASK-P3-10 · Patient screens — RESOLVED (PR #17)
`patient-list`, `patient-form`, `patient-detail` components with inline Tailwind templates, `patient.service.ts`, `patient.model.ts`, lazy-loaded routes.

#### ✅ TASK-P3-11 · Order screens — RESOLVED (PR #17)
`order-list`, `order-create`, `order-detail` components with inline Tailwind templates, `order.service.ts`, `order.model.ts`, lazy-loaded routes.

#### ✅ TASK-P3-12 · Billing screens — RESOLVED (PR #17)
`invoice-list`, `invoice-detail`, `payment-form` components with inline Tailwind templates, `billing.service.ts`, `billing.model.ts`, lazy-loaded routes.

---

### Cross-Cutting (Phase 3)

#### ✅ TASK-P3-16 · LIS-049: Spring Events for Order → Invoice auto-generation — RESOLVED (PR #20 + #21)

Events classes exist in `lis-core` (`OrderPlacedEvent`, `OrderCancelledEvent`, `PaymentReceivedEvent`, `SampleCollectedEvent`) and are now fully wired.

- [x] `TestOrderService.placeOrder()` publishes `OrderPlacedEvent`
- [x] `BillingEventListener` listens for `OrderPlacedEvent` → auto-generates invoice
- [x] `PaymentService.recordPayment()` publishes `PaymentReceivedEvent`
- [x] `OrderEventListener` handles `PaymentReceivedEvent` → updates order to PAID
- [x] `OrderEventListener` handles `SampleCollectedEvent` → updates order to SAMPLE_COLLECTED
- [x] `TestOrderService.cancelOrder()` publishes `OrderCancelledEvent`
- [x] `BillingEventListenerTest` (2 tests), `OrderEventListenerTest` (5 tests)

---

#### ✅ TASK-P3-17 · Barcode Wiring — RESOLVED (PR #21)

`BarcodeGeneratorUtil` is fully implemented in `lis-core` and now wired.

- [x] `TestOrderService.create()` calls `BarcodeGeneratorUtil.generateOrderNumber(sequence)` to set barcode
- [x] Barcode returned in `TestOrderResponse`

---

#### ✅ TASK-P3-18 · Flyway migrations — RESOLVED
All migration files present: lis-patient (4 files), lis-order (2 files), lis-billing (5 files).

---

#### TASK-P3-19 · LIS-052: End-to-end integration test: Patient → Order → Invoice → Payment

- [ ] Testcontainers setup: PostgreSQL + Keycloak
- [ ] Happy path test:
  1. Register new patient → assert UHID generated
  2. Create order with CBC + Lipid Panel → assert panel expanded to constituent tests
  3. Place order → assert `OrderPlacedEvent` fired → assert invoice auto-generated
  4. Record payment (full CASH) → assert invoice status `PAID`, order status `PAID`
- [ ] Partial payment test (split CASH + UPI)
- [ ] Multi-branch isolation test (orders from branch A not visible from branch B)
- [ ] Error scenarios: duplicate patient, cancelled order, refund flow

---

#### ✅ TASK-P3-20 · OpenAPI documentation for Phase 3 APIs — RESOLVED (PR #17)
`@Tag`, `@Operation`, `@ApiResponse`, `@Parameter` annotations added to all 7 Phase 3 controllers (PatientController, PatientVisitController, TestOrderController, InvoiceController, PaymentController, RefundController, CreditAccountController).

---

#### TASK-P3-21 · LIS-054: Implement Complete Lipid + CBC Walkthrough integration test

Based on `docs/process-flows/complete-lipid-cbc-walkthrough.md`:

- [ ] Register patient Rajesh Kumar (male, 45 years)
- [ ] Order Lipid Profile + CBC + ESR (panel expansion: Lipid → TC/TG/HDL/LDL/VLDL)
- [ ] Apply WALK_IN pricing
- [ ] Record CASH payment
- [ ] Assert all intermediate states match documentation walkthrough

---

## 📋 Master Pending Task Checklist

### Phase 2 Pending (~97% — 1 critical task)

**P1 — Required for Closure**
- [ ] TASK-P2-08: Frontend unit tests for 7 critical admin components

**P2 — Nice to Have**
- [ ] TASK-P2-09: OpenAPI `@Operation` annotations on 26 admin controllers
- [ ] TASK-P2-10: Branch provisioning wizard (multi-step `MatStepper` dialog)
- [ ] TASK-P2-11: Reference range overlap validation + test
- [ ] TASK-P2-12: Branch override pricing logic verification + test

**✅ All P0 Blockers Resolved:**
- [x] ~~TASK-P2-01: Backend entity `NotificationTemplate`~~ ✅ **DONE (PR #15)**
- [x] ~~TASK-P2-02: Backend entity `ReportTemplate`~~ ✅ **DONE (PR #15)**
- [x] ~~TASK-P2-03: Backend entity `DiscountScheme`~~ ✅ **DONE (PR #15)**
- [x] ~~TASK-P2-04: Backend entity `InsuranceTariff`~~ ✅ **DONE (PR #15)**
- [x] ~~TASK-P2-05: Backend entities `Role` and `Permission`~~ ✅ **DONE (PR #15 + PR #16)**
- [x] ~~TASK-P2-06: Seed data migrations~~ ✅ **DONE (PR #15 + PR #16 — R__001–R__012)**
- [x] ~~TASK-P2-07: Backend test coverage 29% → 80%~~ ✅ **DONE (PR #15 — 81% achieved)**

### Phase 3 Pending (~80% — integration tests and nice-to-haves remaining)

**P0 — Blockers**
- [x] ~~TASK-P3-05: Full Order State Machine transitions + barcode wiring~~ ✅ **DONE (PR #20 + PR #21)**
- [x] ~~TASK-P3-16: Spring Events: Order → Invoice auto-generation (wiring)~~ ✅ **DONE (PR #20 + PR #21)**

**P1 — High Priority**
- [x] ~~TASK-P3-02: Duplicate patient detection algorithm~~ ✅ **DONE (PR #21)**
- [ ] TASK-P3-06: Order validation and sample requirements
- [ ] TASK-P3-09b: Discount application logic
- [x] ~~TASK-P3-17: Barcode wiring in TestOrderService~~ ✅ **DONE (PR #21)**
- [ ] TASK-P3-19: E2E integration test
- [ ] TASK-P3-21: Lipid + CBC walkthrough integration test

**P2 — Nice to Have**
- [ ] Add frontend unit tests for patient/order/billing components

**✅ Completed Phase 3 Tasks:**
- [x] ~~TASK-P3-01: Patient CRUD + UHID~~ ✅
- [x] ~~TASK-P3-03: Patient Visit Management~~ ✅
- [x] ~~TASK-P3-04: Test Order CRUD~~ ✅
- [x] ~~TASK-P3-07: Invoice Generation~~ ✅
- [x] ~~TASK-P3-08: Payment Recording~~ ✅
- [x] ~~TASK-P3-09: Refund + Credit Management~~ ✅
- [x] ~~TASK-P3-10: Patient frontend screens~~ ✅
- [x] ~~TASK-P3-11: Order frontend screens~~ ✅
- [x] ~~TASK-P3-12: Billing frontend screens~~ ✅
- [x] ~~TASK-P3-18: Flyway migrations (Patient/Order/Billing)~~ ✅
- [x] ~~TASK-P3-20: OpenAPI for Phase 3 APIs~~ ✅ (PR #17)

**P2 — Nice to Have**
- [ ] TASK-P2-09: OpenAPI `@Operation` annotations on 26 admin controllers
- [ ] TASK-P2-10: Branch provisioning wizard (multi-step `MatStepper` dialog)
- [ ] TASK-P2-11: Reference range overlap validation + test
- [ ] TASK-P2-12: Branch override pricing logic verification + test

---

## 🗓️ Suggested Sprint Plan

### Sprint 1 (Days 1-3): Close Phase 2

| Task | Owner | Effort |
|------|-------|--------|
| ~~TASK-P2-01 to P2-07~~ | ✅ Done (PRs #15, #16) | — |
| TASK-P2-08 (Frontend tests) | Frontend Dev | 3 days |
| **Sprint 1 Total** | | **~3 frontend days** |

### Sprint 2 (Week 2-3): Complete Phase 3 Core Domain Logic

| Task | Owner | Effort |
|------|-------|--------|
| TASK-P3-05 (Full state machine + barcode) | Backend Dev | 3 days |
| TASK-P3-16 (Spring Events wiring) | Backend Dev | 2 days |
| TASK-P3-17 (Barcode wiring) | Backend Dev | 1 day |
| TASK-P3-02 (Duplicate detection algorithm) | Backend Dev | 2 days |
| TASK-P3-06 (Order validation) | Backend Dev | 2 days |
| TASK-P3-09b (Discount logic) | Backend Dev | 2 days |
| TASK-P2-09 to P2-12 (polish) | Both | 3 days |
| **Sprint 2 Total** | | **~15 backend days** |

### Sprint 3 (Week 4-5): Phase 3 Integration Tests + Remaining Issues

| Task Group | Owner | Effort |
|------------|-------|--------|
| TASK-P3-19 (E2E integration test) | Backend Dev | 3 days |
| TASK-P3-21 (Lipid + CBC walkthrough) | Backend Dev | 2 days |
| PHI audit, Discount, Insurance, Receipt, Corporate, Financial reports | Backend Dev | 5 days |
| **Sprint 3 Total** | | **~10 backend days** |
