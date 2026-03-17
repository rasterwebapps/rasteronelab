# Phase 3: Patient & Ordering (Months 4-6)

> Complete patient registration through order placement and basic billing.

## Milestone Goals

- Complete patient registration with UHID generation and duplicate detection
- Test ordering with panel expansion and barcode generation
- Basic billing with invoice generation and payment recording
- Angular screens: Patient (12) + Order (10) + Billing (12) = 34 screens

## Documentation References

- [Patient Registration Flow](../process-flows/01-patient-registration.md)
- [Test Ordering Flow](../process-flows/02-test-ordering.md)
- [Billing & Payment Flow](../process-flows/09-billing-payment.md)
- [Complete Lipid + CBC Walkthrough](../process-flows/complete-lipid-cbc-walkthrough.md)
- [Barcode Strategy](../domain/barcode-strategy.md)
- [API Specification](../architecture/api-specification.md)
- [Workflow State Machines — Order](../architecture/workflow-state-machines.md)
- [Screen List — Patient, Order, Billing](../screens/screen-list-complete.md) — Screens 14-25, 26-35, 88-99

---

## Issues

### Backend — Patient Module (`lis-patient`)

#### LIS-034: Implement Patient CRUD API with UHID generation
**Labels:** `backend`, `database`
**Description:**
Patient management core:
- Patient entity: name, DOB, age, gender, phone, email, address, bloodGroup, UHID
- UHID generation format: `{BranchCode}-{6-digit-sequence}` with pessimistic locking
- Patient search: by phone, name, UHID (Elasticsearch integration optional)
- Patient update with audit logging (PHI access)
- Soft delete (deactivation, not hard delete)
- Endpoints: `/api/v1/patients` (POST, GET, PUT, DELETE, search)

**Acceptance Criteria:**
- [ ] Patient entity, DTO, mapper (MapStruct)
- [ ] UHID generation with sequence and locking
- [ ] Patient search with multiple criteria
- [ ] PHI audit logging
- [ ] Flyway migration
- [ ] Unit + integration tests

---

#### LIS-035: Implement Duplicate Patient Detection and Merge
**Labels:** `backend`
**Description:**
Prevent and resolve duplicate patients:
- Duplicate detection: name + DOB match, phone number match
- Duplicate scoring algorithm (weighted match on fields)
- Patient merge workflow: select primary, merge demographics, transfer orders/results
- Merge audit trail (which records were merged, by whom)
- Endpoints: `/api/v1/patients/duplicates`, `/api/v1/patients/merge`

**Acceptance Criteria:**
- [ ] Duplicate detection algorithm
- [ ] Merge service with data transfer
- [ ] Merge audit trail
- [ ] Integration tests for merge scenarios

---

#### LIS-036: Implement Patient Visit Management
**Labels:** `backend`, `database`
**Description:**
Visit tracking for each patient encounter:
- Visit entity: patient, visitDate, visitType (WALK_IN, APPOINTMENT, HOME_COLLECTION), referringDoctor
- Auto-create visit on new order
- Visit history with order/result summary
- Endpoints: `/api/v1/patients/{id}/visits` (CRUD)

**Acceptance Criteria:**
- [ ] PatientVisit entity, DTO, mapper
- [ ] Visit creation on order placement
- [ ] Visit history with linked orders
- [ ] Flyway migration
- [ ] Unit tests

---

### Backend — Order Module (`lis-order`)

#### LIS-037: Implement Test Order Creation API with state machine
**Labels:** `backend`, `database`
**Description:**
Order management with state machine:
- Order entity: patient, visit, doctor, priority (ROUTINE, STAT, URGENT), status, orderDate
- OrderLineItem: test, parameters, sampleType, tubeType
- Order state machine: DRAFT → PLACED → PAID → SAMPLE_COLLECTED → ... → COMPLETED
- Order barcode format: `ORD-{BranchCode}-{YYYYMMDD}-{sequence}`
- Endpoints: `/api/v1/orders` (POST, GET, PUT, place, cancel)

**Acceptance Criteria:**
- [ ] Order entity with line items
- [ ] State machine implementation (Spring Statemachine or custom)
- [ ] Barcode generation
- [ ] Order validation (sample requirements)
- [ ] Flyway migration
- [ ] Unit + integration tests

---

#### LIS-038: Implement Panel Expansion and Test Search
**Labels:** `backend`
**Description:**
Test selection features:
- Test search by name, code, department with autocomplete
- Panel expansion: selecting a panel auto-adds constituent tests
- Nested panel support (Panel A contains Panel B which contains tests)
- De-duplication: if same test in multiple panels, add once
- Reflex test rules: auto-add follow-up tests based on conditions
- Endpoints: `/api/v1/tests/search`, `/api/v1/panels/{id}/expand`

**Acceptance Criteria:**
- [ ] Test search with partial matching
- [ ] Panel expansion logic (recursive)
- [ ] Reflex test rule engine
- [ ] De-duplication of overlapping panels
- [ ] Unit tests

---

#### LIS-039: Implement Order Validation and Sample Requirements
**Labels:** `backend`
**Description:**
Order validation before placement:
- Validate all required fields present
- Check sample requirements (tube types, volumes)
- Group order line items by sample type/tube
- Calculate estimated TAT per test
- Validate insurance/corporate authorization if applicable
- Generate pending collection list per tube type

**Acceptance Criteria:**
- [ ] Order validation service
- [ ] Sample grouping logic
- [ ] TAT calculation
- [ ] Pending collection list generation
- [ ] Unit tests

---

### Backend — Billing Module (`lis-billing`)

#### LIS-040: Implement Invoice Generation API
**Labels:** `backend`, `database`
**Description:**
Auto-generate invoice on order placement:
- Invoice entity: order, patient, items, subtotal, discount, tax, total, status
- Pricing logic: base price → branch override → rate list → discount
- Rate list types: WALK_IN, CORPORATE, INSURANCE, DOCTOR_REF
- Discount types: percentage, flat, coupon code, scheme
- Invoice state machine: DRAFT → GENERATED → PARTIALLY_PAID → PAID → REFUNDED
- Invoice number format: `INV-{BranchCode}-{YYYYMMDD}-{sequence}`
- Endpoints: `/api/v1/invoices` (generate, get, list)

**Acceptance Criteria:**
- [ ] Invoice entity with line items
- [ ] Pricing calculation engine
- [ ] Discount application logic
- [ ] Invoice state machine
- [ ] Flyway migration
- [ ] Unit + integration tests

---

#### LIS-041: Implement Payment Recording API
**Labels:** `backend`, `database`
**Description:**
Payment processing:
- Payment entity: invoice, amount, paymentMethod, transactionRef, receivedBy
- Payment methods: CASH, CARD, UPI, INSURANCE, CREDIT, ONLINE
- Split payment support (multiple methods per invoice)
- Partial payment with balance tracking
- Cash denomination breakdown (optional)
- Receipt number generation
- Endpoints: `/api/v1/payments` (record, get, list)

**Acceptance Criteria:**
- [ ] Payment entity
- [ ] Split payment logic
- [ ] Partial payment with balance tracking
- [ ] Receipt generation
- [ ] Flyway migration
- [ ] Unit tests

---

#### LIS-042: Implement Discount, Refund, and Outstanding Management
**Labels:** `backend`, `database`
**Description:**
Financial management:
- Discount scheme management (percentage, flat, coupon)
- Corporate/insurance credit management with credit limits
- Refund workflow: request → supervisor approval → credit note
- Outstanding invoice tracking and aging report
- Endpoints: `/api/v1/invoices/{id}/discount`, `/api/v1/invoices/{id}/refund`, `/api/v1/invoices/outstanding`

**Acceptance Criteria:**
- [ ] Discount application service
- [ ] Credit management service
- [ ] Refund workflow with approval
- [ ] Outstanding tracking
- [ ] Unit tests

---

### Frontend — Patient Screens

#### LIS-043: Build Patient Search and List screen
**Labels:** `frontend`
**Description:**
Patient list screen (Screen #14):
- Data table with columns: UHID, Name, Age/Gender, Phone, Last Visit
- Search by UHID, phone, name (debounced input)
- Filters: date range, gender, status
- Quick actions: view, edit, new order
- Pagination with page size selector

**Acceptance Criteria:**
- [ ] Patient list component with Material data table
- [ ] Multi-criteria search
- [ ] Filter sidebar
- [ ] Responsive design
- [ ] Signal-based state

---

#### LIS-044: Build Patient Registration and Edit screens
**Labels:** `frontend`
**Description:**
Patient forms (Screens #15-16):
- Registration form: mandatory (name, DOB/age, gender, phone), optional fields
- Age calculator from DOB and vice versa
- Duplicate detection warning before save
- Edit form with same fields, pre-populated
- Form validation with error messages

**Acceptance Criteria:**
- [ ] Registration form component
- [ ] Edit form component
- [ ] Age/DOB calculator
- [ ] Duplicate warning dialog
- [ ] Form validation

---

#### LIS-045: Build Patient Detail, Visit History, and related screens
**Labels:** `frontend`
**Description:**
Patient detail screens (Screens #17-25):
- Patient detail view with demographics, visit history, order history
- Visit history list with test summary per visit
- Order history with status indicators
- Report history with download links
- Billing summary with outstanding balance
- Patient merge screen

**Acceptance Criteria:**
- [ ] Patient detail component (tabbed view)
- [ ] Visit history component
- [ ] Order/Report/Billing history components
- [ ] Patient merge UI

---

### Frontend — Order Screens

#### LIS-046: Build Create Order wizard (3-step)
**Labels:** `frontend`
**Description:**
Order creation (Screens #26-28):
- Step 1: Patient search/select or quick register
- Step 2: Test selection with search, department filter, panel expansion
- Step 3: Review, pricing, discount, submit
- Stepper component with validation per step
- Order barcode display/print after submission

**Acceptance Criteria:**
- [ ] 3-step order wizard
- [ ] Test search with autocomplete
- [ ] Panel expansion display
- [ ] Price calculation display
- [ ] Order confirmation and barcode

---

#### LIS-047: Build Order List, Detail, and Management screens
**Labels:** `frontend`
**Description:**
Order management screens (Screens #29-35):
- Order list with status filters, date range, search
- Order detail view with line items, status timeline
- Order edit (DRAFT state only)
- Order cancel with reason
- Barcode print
- Pending orders dashboard
- TAT monitor with color-coded indicators

**Acceptance Criteria:**
- [ ] Order list with filters
- [ ] Order detail with timeline
- [ ] Edit and cancel flows
- [ ] TAT monitor component

---

### Frontend — Billing Screens

#### LIS-048: Build Invoice and Payment screens
**Labels:** `frontend`
**Description:**
Billing screens (Screens #88-99):
- Invoice list with status filters
- Invoice detail view with line items
- Payment recording form (multi-method)
- Payment receipt print
- Refund processing form
- Outstanding invoices list
- Discount application dialog
- Daily collection report
- Financial summary

**Acceptance Criteria:**
- [ ] Invoice list and detail components
- [ ] Payment form with split payment UI
- [ ] Receipt print layout
- [ ] Refund form with approval
- [ ] Outstanding and collection reports

---

### Cross-Cutting

#### LIS-049: Implement Spring Events for Order → Invoice auto-generation
**Labels:** `backend`
**Description:**
Cross-module communication:
- OrderPlacedEvent published when order status changes to PLACED
- InvoiceService listens and auto-generates invoice
- PaymentReceivedEvent triggers order status update to PAID
- Event logging for debugging

**Acceptance Criteria:**
- [ ] OrderPlacedEvent class
- [ ] PaymentReceivedEvent class
- [ ] Event listeners in billing module
- [ ] Event audit logging
- [ ] Integration tests

---

#### LIS-050: Implement Barcode Generation Service
**Labels:** `backend`
**Description:**
Centralized barcode generation:
- Order barcode: `ORD-{BranchCode}-{YYYYMMDD}-{sequence}`
- Sample barcode: `SMP-{BranchCode}-{YYYYMMDD}-{sequence}`
- UHID format: `{BranchCode}-{6-digit-sequence}`
- Barcode image generation (Code128 or QR)
- Number series management per branch

**Acceptance Criteria:**
- [ ] Barcode generation service
- [ ] Number series with atomic increment
- [ ] Barcode image rendering
- [ ] Branch-specific prefixes
- [ ] Unit tests

---

### Database

#### LIS-051: Create Flyway migrations for Patient, Order, and Billing tables
**Labels:** `backend`, `database`
**Description:**
Phase 3 database migrations:
- Patient, PatientVisit tables
- TestOrder, OrderLineItem tables
- Invoice, InvoiceLineItem, Payment, Refund, CreditAccount tables
- Number series table
- Foreign keys and indexes
- Seed data for test/dev environments

**Acceptance Criteria:**
- [ ] All migrations follow naming convention
- [ ] Forward and rollback scripts
- [ ] Performance indexes
- [ ] Referential integrity constraints
- [ ] Dev seed data

---

### Integration Testing

#### LIS-052: End-to-end test: Patient → Order → Invoice → Payment flow
**Labels:** `backend`, `testing`
**Description:**
Complete integration test for the happy path:
1. Register new patient (get UHID)
2. Create order with tests (panel expansion)
3. Verify invoice auto-generated
4. Record payment (full, partial, split)
5. Verify order status transitions
6. Test with different rate lists

**Acceptance Criteria:**
- [ ] Testcontainers setup for PostgreSQL + Keycloak
- [ ] Happy path end-to-end test
- [ ] Partial payment flow test
- [ ] Multi-branch isolation test
- [ ] Error scenario tests

---

### Documentation

#### LIS-053: Create OpenAPI documentation for Patient, Order, and Billing APIs
**Labels:** `documentation`
**Description:**
API documentation:
- OpenAPI 3.0 annotations on all controllers
- Request/response examples
- Error response documentation
- Swagger UI configuration at `/swagger-ui.html`
- Postman collection export

**Acceptance Criteria:**
- [ ] @Tag, @Operation, @ApiResponse annotations
- [ ] Example request/response bodies
- [ ] Swagger UI accessible
- [ ] Postman collection

---

#### LIS-054: Complete Lipid + CBC Walkthrough integration test
**Labels:** `backend`, `testing`
**Description:**
Implement the exact scenario from `complete-lipid-cbc-walkthrough.md`:
1. Patient Rajesh Kumar registration
2. Order Lipid Profile + CBC + ESR
3. Panel expansion (Lipid → TC, TG, HDL, LDL, VLDL)
4. Invoice with WALK_IN pricing
5. Payment recording

**Acceptance Criteria:**
- [ ] Full walkthrough as integration test
- [ ] Matches documentation scenario exactly
- [ ] All intermediate states verified

---

## Completion Criteria

- [ ] Patient registration with UHID works end-to-end
- [ ] Duplicate detection and merge functional
- [ ] Test ordering with panel expansion working
- [ ] Order state machine transitions correct
- [ ] Invoice auto-generation on order placement
- [ ] Payment recording with split payment support
- [ ] All 34 Angular screens functional
- [ ] 80% test coverage on patient, order, billing modules
- [ ] API documentation published
