# Phase 4: Sample Management

> Complete sample lifecycle from collection to disposal, including rejection workflow and barcode scanning.

## Milestone Goals

- Complete sample lifecycle management
- Barcode scanner integration
- Sample rejection and recollection workflow
- Inter-branch sample transfer
- Angular screens: Sample (14 screens)

## Documentation References

- [Sample Collection Flow](../process-flows/03-sample-collection.md)
- [Sample Receiving Flow](../process-flows/04-sample-receiving.md)
- [Workflow State Machines — Sample](../architecture/workflow-state-machines.md)
- [Sample Types & Tubes](../domain/sample-types-tubes.md)
- [Barcode Strategy](../domain/barcode-strategy.md)
- [Screen List — Sample](../screens/screen-list-complete.md) — Screens 36-49

---

## Issues

### Backend — Sample Module (`lis-sample`)

#### LIS-055: Implement Sample Collection Recording API
**Labels:** `backend`, `database`
**Description:**
Sample collection core:
- SampleCollection entity: order, patient, tubeType, sampleType, barcode, collectedBy, collectedAt, collectionSite, quantity
- Test-to-tube mapping: group order tests by tube type (RED, PURPLE/EDTA, BLUE, GREY, GREEN)
- Sample barcode generation: `SMP-{BranchCode}-{YYYYMMDD}-{sequence}`
- Multiple tubes per order (one per tube type)
- Collection recording updates order status to SAMPLE_COLLECTED
- Endpoints: `/api/v1/samples` (POST, GET, list)

**Acceptance Criteria:**
- [ ] SampleCollection entity, DTO, mapper
- [ ] Test-to-tube mapping logic
- [ ] Sample barcode generation
- [ ] Order status update on collection
- [ ] Flyway migration
- [ ] Unit + integration tests

---

#### LIS-056: Implement Sample State Machine
**Labels:** `backend`
**Description:**
Sample lifecycle state machine:
- States: COLLECTED → RECEIVED → ACCEPTED/REJECTED → ALIQUOTED → PROCESSING → COMPLETED → STORED → DISPOSED
- Transition validations (e.g., can't accept already accepted sample)
- Rejection reasons enum: HEMOLYZED, CLOTTED, INSUFFICIENT, WRONG_TUBE, UNLABELED, LIPEMIC, ICTERIC, DETERIORATED, OLD
- State change events published for downstream processing
- Audit trail for all state transitions

**Acceptance Criteria:**
- [ ] Sample state machine implementation
- [ ] All 9 states with transitions
- [ ] Rejection reasons enum
- [ ] State change event publishing
- [ ] Unit tests for all transitions

---

#### LIS-057: Implement Sample Receiving (Accept/Reject) API
**Labels:** `backend`
**Description:**
Lab receiving workflow:
- Scan sample barcode to identify
- Accept: validate sample condition, update status to ACCEPTED
- Reject: select rejection reason, add comment, trigger recollection notification
- Batch receiving: accept/reject multiple samples at once
- Auto-update order status to SAMPLE_RECEIVED
- Endpoints: `/api/v1/samples/{id}/receive`, `/api/v1/samples/{id}/reject`

**Acceptance Criteria:**
- [ ] Sample receive/accept service
- [ ] Sample rejection with reason
- [ ] Batch receiving support
- [ ] Recollection notification trigger (Spring Event)
- [ ] Order status auto-update
- [ ] Unit + integration tests

---

#### LIS-058: Implement Sample Aliquoting API
**Labels:** `backend`, `database`
**Description:**
Sample splitting for multi-department testing:
- Aliquot entity: parentSample, childBarcode, volume, department
- Auto-generate aliquot barcodes
- Track parent-child sample relationships
- Aliquot from accepted samples only
- Endpoints: `/api/v1/samples/{id}/aliquot`

**Acceptance Criteria:**
- [ ] SampleAliquot entity
- [ ] Parent-child tracking
- [ ] Aliquot barcode generation
- [ ] Volume tracking
- [ ] Flyway migration
- [ ] Unit tests

---

#### LIS-059: Implement Sample Tracking and Storage API
**Labels:** `backend`, `database`
**Description:**
Sample tracking throughout lifecycle:
- Sample timeline: collection → transport → receive → aliquot → process → store → dispose
- Storage location management: rack, shelf, position
- Storage duration tracking with auto-dispose reminders
- Sample tracking query by barcode, patient, order
- Endpoints: `/api/v1/samples/{id}/tracking`, `/api/v1/samples/{id}/storage`

**Acceptance Criteria:**
- [ ] Sample timeline/tracking service
- [ ] Storage location entity
- [ ] Auto-dispose reminder logic
- [ ] Tracking query endpoints
- [ ] Unit tests

---

#### LIS-060: Implement Inter-Branch Sample Transfer
**Labels:** `backend`, `security`
**Description:**
Transfer samples between branches:
- Transfer request: source branch, destination branch, reason
- Dual write: create record in both source and destination branch context
- Transfer status tracking (IN_TRANSIT, RECEIVED_AT_DEST)
- Only BRANCH_ADMIN+ can initiate transfers
- Transfer audit trail
- Endpoints: `/api/v1/samples/{id}/transfer`

**Acceptance Criteria:**
- [ ] Sample transfer service
- [ ] Dual branch context writing
- [ ] Transfer status tracking
- [ ] BRANCH_ADMIN authorization
- [ ] Integration tests with multi-branch

---

#### LIS-061: Implement Pending Collection and Pending Receipt Lists
**Labels:** `backend`
**Description:**
Operational worklists:
- Pending collection list: orders with status PAID but not SAMPLE_COLLECTED
- Pending receipt list: samples with status COLLECTED but not RECEIVED
- Filter by date, department, priority (STAT first)
- Sort by TAT (urgent samples first)
- Endpoints: `/api/v1/samples/pending-collection`, `/api/v1/samples/pending-receipt`

**Acceptance Criteria:**
- [ ] Pending collection query
- [ ] Pending receipt query
- [ ] Priority-based sorting
- [ ] TAT-based sorting
- [ ] Unit tests

---

### Frontend — Sample Screens

#### LIS-062: Build Sample Collection screen with barcode scanning
**Labels:** `frontend`
**Description:**
Sample collection screen (Screen #36):
- Scan order barcode to load patient/tests
- Display tubes required per test (color-coded)
- Record collection for each tube
- Print tube labels (Zebra printer integration or browser print)
- Mark all collected to proceed

**Acceptance Criteria:**
- [ ] Barcode scanner input (WebHID or keyboard wedge)
- [ ] Tube requirement display (color-coded)
- [ ] Collection recording per tube
- [ ] Label print functionality
- [ ] Signal-based state

---

#### LIS-063: Build Sample Receive/Reject screens
**Labels:** `frontend`
**Description:**
Lab receiving screens (Screens #38-40):
- Sample receive screen: scan barcode, display sample details, accept button
- Batch receive: scan multiple barcodes, bulk accept
- Reject screen: select rejection reason, add comment, submit
- Visual indicators for sample condition

**Acceptance Criteria:**
- [ ] Receive screen with barcode scan
- [ ] Batch receive functionality
- [ ] Reject dialog with reason selection
- [ ] Success/error notifications

---

#### LIS-064: Build Sample List, Detail, and Tracking screens
**Labels:** `frontend`
**Description:**
Sample management screens (Screens #41-42, 46):
- Sample list with status filters, date range, department
- Sample detail view with all metadata
- Sample tracking timeline (visual timeline component)
- Sample status badges (color-coded)

**Acceptance Criteria:**
- [ ] Sample list with Material data table
- [ ] Sample detail component
- [ ] Visual timeline component
- [ ] Status filter chips

---

#### LIS-065: Build Aliquoting, Storage, Transfer, and Disposal screens
**Labels:** `frontend`
**Description:**
Advanced sample screens (Screens #43-45, 47):
- Aliquoting screen: select parent sample, define aliquots
- Storage location selector (rack/shelf/position)
- Inter-branch transfer request form
- Sample disposal confirmation screen
- Pending collection and receipt lists (Screens #48-49)

**Acceptance Criteria:**
- [ ] Aliquoting form component
- [ ] Storage location picker
- [ ] Transfer request form
- [ ] Disposal confirmation dialog
- [ ] Pending lists with priority highlighting

---

### Integration

#### LIS-066: Implement barcode scanner integration (WebHID API)
**Labels:** `frontend`, `infrastructure`
**Description:**
Barcode scanner hardware integration:
- WebHID API integration for USB barcode scanners
- Keyboard wedge mode fallback (scanner types as keyboard)
- Scan event handling in Angular
- Audible feedback on successful scan
- Error handling for invalid/unknown barcodes

**Acceptance Criteria:**
- [ ] WebHID scanner service
- [ ] Keyboard wedge fallback
- [ ] Scan event bus (Angular service)
- [ ] Audio feedback
- [ ] Error handling

---

### Database

#### LIS-067: Create Flyway migrations for Sample Management tables
**Labels:** `backend`, `database`
**Description:**
Phase 4 database migrations:
- SampleCollection, SampleTest tables
- SampleAliquot table
- SampleStorage table
- SampleTransfer table
- SampleTracking (timeline events) table
- RejectionReason reference table
- All with standard BaseEntity columns + indexes

**Acceptance Criteria:**
- [ ] All migrations created
- [ ] Foreign keys to Order, Patient tables
- [ ] Indexes on barcode, status, branch_id columns
- [ ] Rejection reason seed data

---

### Testing

#### LIS-068: End-to-end test: Order → Collection → Receive → Accept/Reject flow
**Labels:** `backend`, `testing`
**Description:**
Integration test for sample lifecycle:
1. Create order (from Phase 3)
2. Record sample collection (generate barcodes)
3. Receive samples at lab
4. Accept valid samples → verify order status update
5. Reject hemolyzed sample → verify recollection trigger
6. Test aliquoting for multi-department order
7. Test inter-branch transfer

**Acceptance Criteria:**
- [ ] Happy path: collection → receipt → acceptance
- [ ] Rejection and recollection flow
- [ ] Aliquoting flow
- [ ] Multi-branch transfer test
- [ ] State machine transition verification

---

## Completion Criteria

- [ ] Sample collection with barcode generation working
- [ ] Sample receiving (accept/reject) workflow functional
- [ ] Aliquoting and storage tracking operational
- [ ] Inter-branch transfer working with dual branch context
- [ ] Barcode scanning integrated in frontend
- [ ] All 14 sample Angular screens implemented
- [ ] Sample state machine transitions fully tested
- [ ] 80% test coverage on lis-sample module
