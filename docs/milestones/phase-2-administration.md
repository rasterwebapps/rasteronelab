# Phase 2: Administration Module (Months 2-4)

> All master data CRUD operational. Admin screens functional across all 5 master data levels.

## Milestone Goals

- Complete `lis-admin` module with all master data CRUD
- Admin Angular screens (25 screens)
- Flyway migrations for all admin tables
- Master data seeding for initial setup

## Documentation References

- [Master Data Overview](../domain/masters/00-master-data-overview.md) — 125 masters across 5 levels
- [Global Masters (L1)](../domain/masters/01-global-masters.md)
- [Organization Masters (L2)](../domain/masters/02-organization-masters.md)
- [Branch Masters (L3)](../domain/masters/03-branch-masters.md)
- [Operational Masters (L4)](../domain/masters/04-operational-masters.md)
- [External Masters (L5)](../domain/masters/05-external-masters.md)
- [Master Data Lifecycle](../domain/masters/06-master-data-lifecycle.md)
- [API Specification — Admin](../architecture/api-specification.md)
- [Database Schema — Admin Tables](../architecture/database-schema.md)
- [Screen List — Administration](../screens/screen-list-complete.md) — Screens 130-154

---

## Issues

### Backend — Organization & Branch Management

#### LIS-016: Implement Organization CRUD API
**Labels:** `backend`, `database`
**Description:**
Create Organization entity and CRUD operations:
- Organization entity with name, code, address, contact, logo, license
- Organization uses `org_id` (NOT branch_id) since orgs span branches
- CRUD endpoints: POST/GET/PUT/DELETE `/api/v1/organizations`
- Only SUPER_ADMIN can manage organizations
- Flyway migration for `organizations` table

**Acceptance Criteria:**
- [ ] Organization entity, DTO, mapper
- [ ] OrganizationService with CRUD
- [ ] OrganizationController with @PreAuthorize
- [ ] Flyway migration
- [ ] Unit + integration tests

---

#### LIS-017: Implement Branch Management CRUD API
**Labels:** `backend`, `database`
**Description:**
Branch entity and management:
- Branch entity with name, code, address, phone, email, organization reference
- Branch provisioning workflow (create → copy org masters → configure)
- Branch-specific settings: working hours, number series prefix, report template
- Endpoints: `/api/v1/branches` (CRUD)
- Branch activation/deactivation

**Acceptance Criteria:**
- [ ] Branch entity, DTO, mapper (MapStruct)
- [ ] BranchService with CRUD + provisioning
- [ ] BranchController with @PreAuthorize(SUPER_ADMIN, ORG_ADMIN)
- [ ] Flyway migration
- [ ] Branch provisioning integration test

---

#### LIS-018: Implement Department Management CRUD API
**Labels:** `backend`, `database`
**Description:**
Department master data:
- 11 departments: Biochemistry, Hematology, Microbiology, Histopathology, Clinical Pathology, Serology/Immunology, Molecular Biology, Cytology, Toxicology, Genetics, Immunohematology
- Department entity with name, code, description, isActive, displayOrder
- Branch-department mapping (which departments are active per branch)
- Endpoints: `/api/v1/departments` (CRUD)

**Acceptance Criteria:**
- [ ] Department entity, DTO, mapper
- [ ] DepartmentService with CRUD
- [ ] Branch-Department mapping
- [ ] Seed data for 11 departments
- [ ] Unit tests

---

### Backend — Test & Parameter Masters

#### LIS-019: Implement Test Master CRUD API
**Labels:** `backend`, `database`
**Description:**
Test master with full configuration:
- Test entity: name, code, department, sampleType, tubeType, reportSection, TAT
- Test-Parameter mapping (one test → many parameters)
- Test pricing (base price, branch overrides)
- Test activation/deactivation per branch
- Search/filter: by department, name, code, status
- Endpoints: `/api/v1/tests` (CRUD + search)

**Acceptance Criteria:**
- [ ] Test entity with all relationships
- [ ] TestService with CRUD + search
- [ ] TestController with pagination
- [ ] Flyway migration with indexes
- [ ] Unit + integration tests

---

#### LIS-020: Implement Parameter Master CRUD API
**Labels:** `backend`, `database`
**Description:**
Parameter configuration as per parameter-configuration.md:
- Parameter entity: name, code, unit, dataType (NUMERIC, TEXT, QUALITATIVE, SEMI_QUANTITATIVE)
- Display configuration: displayOrder, printName, decimalPlaces
- Method reference, LOINC code mapping
- Parameter grouping for result entry display
- Endpoints: `/api/v1/parameters` (CRUD)

**Acceptance Criteria:**
- [ ] Parameter entity with all fields
- [ ] ParameterService with CRUD
- [ ] Parameter data types enum
- [ ] Flyway migration
- [ ] Unit tests

---

#### LIS-021: Implement Reference Range Configuration API
**Labels:** `backend`, `database`
**Description:**
Reference range management per parameter:
- Reference range entity: parameter, gender, ageMin, ageMax, ageUnit, normalMin, normalMax, criticalLow, criticalHigh
- Pregnancy-specific ranges
- Unit conversion support
- Validation: no overlapping age ranges per parameter/gender
- Endpoints: `/api/v1/reference-ranges` (CRUD)

**Acceptance Criteria:**
- [ ] ReferenceRange entity with all fields
- [ ] Age/gender matching logic
- [ ] Overlap validation
- [ ] Pregnancy-specific range support
- [ ] Unit + integration tests

---

#### LIS-022: Implement Test Panel Management API
**Labels:** `backend`, `database`
**Description:**
Test panels (groups of tests):
- Panel entity: name, code, department, constituent tests
- Panel expansion logic (Lipid Panel → TC, TG, HDL, LDL, VLDL)
- Panel pricing (can differ from sum of individual tests)
- Nested panels support
- Endpoints: `/api/v1/panels` (CRUD)

**Acceptance Criteria:**
- [ ] Panel entity with test mappings
- [ ] Panel expansion logic
- [ ] Panel pricing
- [ ] PanelController with CRUD
- [ ] Unit tests

---

### Backend — Price & Doctor Masters

#### LIS-023: Implement Price Catalog Management API
**Labels:** `backend`, `database`
**Description:**
Pricing configuration:
- Price catalog entity: test, rateListType (WALK_IN, CORPORATE, INSURANCE, DOCTOR_REF), price
- Branch-level price overrides
- Effective date range for time-based pricing
- Bulk price update support
- Endpoints: `/api/v1/price-catalog` (CRUD + bulk update)

**Acceptance Criteria:**
- [ ] PriceCatalog entity
- [ ] Rate list type enum
- [ ] Branch override logic
- [ ] Bulk update endpoint
- [ ] Unit tests

---

#### LIS-024: Implement Doctor Management CRUD API
**Labels:** `backend`, `database`
**Description:**
Referring doctor management:
- Doctor entity: name, specialization, phone, email, registrationNumber, referralCommission
- Doctor-branch mapping (active at which branches)
- Doctor search by name, specialization, registration number
- Endpoints: `/api/v1/doctors` (CRUD + search)

**Acceptance Criteria:**
- [ ] Doctor entity, DTO, mapper
- [ ] DoctorService with CRUD + search
- [ ] Doctor-branch mapping
- [ ] Flyway migration
- [ ] Unit tests

---

#### LIS-025: Implement User Management with Keycloak integration
**Labels:** `backend`, `security`
**Description:**
User management synced with Keycloak:
- Create/update/deactivate users in Keycloak via Admin API
- Assign roles and branch access
- User entity in local DB for extended attributes (employeeId, department, designation)
- Password reset trigger
- Endpoints: `/api/v1/users` (CRUD)

**Acceptance Criteria:**
- [ ] Keycloak Admin API integration
- [ ] User entity with local attributes
- [ ] Role assignment
- [ ] Branch access management
- [ ] Integration tests

---

### Backend — Configuration Masters

#### LIS-026: Implement Number Series, TAT, and Working Hours Configuration
**Labels:** `backend`, `database`
**Description:**
Branch-level configuration masters:
- Number series: UHID prefix/format, Order prefix, Sample prefix per branch
- TAT configuration: per test/department with routine vs STAT timelines
- Working hours: per branch with holiday calendar
- Endpoints: `/api/v1/config/number-series`, `/api/v1/config/tat`, `/api/v1/config/working-hours`

**Acceptance Criteria:**
- [ ] NumberSeries entity and CRUD
- [ ] TATConfiguration entity and CRUD
- [ ] WorkingHours entity with holiday calendar
- [ ] Branch-scoped configuration
- [ ] Unit tests

---

#### LIS-027: Implement Critical Value and Delta Check Configuration
**Labels:** `backend`, `database`
**Description:**
Clinical configuration masters:
- Critical value thresholds per parameter (low, high)
- Delta check rules per parameter (percentage threshold, time window)
- Auto-validation rules (conditions for automatic result verification)
- Endpoints: `/api/v1/config/critical-values`, `/api/v1/config/delta-check`, `/api/v1/config/auto-validation`

**Acceptance Criteria:**
- [ ] CriticalValueConfig entity and CRUD
- [ ] DeltaCheckConfig entity and CRUD
- [ ] AutoValidationRule entity and CRUD
- [ ] Parameter-linked configuration
- [ ] Unit tests

---

#### LIS-028: Implement Antibiotic and Microorganism Masters
**Labels:** `backend`, `database`
**Description:**
Microbiology-specific masters:
- Antibiotic master: name, code, group, CLSI breakpoints
- Microorganism master: name, code, gram type, clinical significance
- Antibiotic-organism mapping (natural resistance, expected susceptibility)
- Endpoints: `/api/v1/antibiotics` (CRUD), `/api/v1/microorganisms` (CRUD)

**Acceptance Criteria:**
- [ ] Antibiotic entity with CLSI breakpoints
- [ ] Microorganism entity
- [ ] Antibiotic-organism mapping
- [ ] Seed data for common antibiotics/organisms
- [ ] Unit tests

---

### Frontend — Admin Screens

#### LIS-029: Build Branch Configuration admin screen
**Labels:** `frontend`
**Description:**
Angular screen for branch management (Screen #130):
- Branch list with search and filter
- Create/Edit branch form with all fields
- Branch activation/deactivation toggle
- Branch provisioning wizard (copy masters, configure settings)
- Working hours configuration UI

**Acceptance Criteria:**
- [ ] Branch list component with data table
- [ ] Branch form component (create/edit)
- [ ] Branch provisioning wizard
- [ ] Working hours editor
- [ ] Signal-based state management

---

#### LIS-030: Build Test Master and Parameter Master admin screens
**Labels:** `frontend`
**Description:**
Angular screens for test/parameter management (Screens #132-136):
- Test master list with department filter, search, pagination
- Test create/edit form with parameter mapping
- Parameter master list and create/edit form
- Reference range configuration matrix (age × gender grid)
- Test panel management screen

**Acceptance Criteria:**
- [ ] Test list component with filters
- [ ] Test form with parameter assignment
- [ ] Parameter list and form components
- [ ] Reference range matrix editor
- [ ] Panel management component

---

#### LIS-031: Build Doctor, User, and Role Management admin screens
**Labels:** `frontend`
**Description:**
Angular screens (Screens #139-141):
- Doctor management: list, create, edit, search by specialization
- User management: list, create, edit, role assignment, branch access
- Role & permission management: role list, permission matrix

**Acceptance Criteria:**
- [ ] Doctor management components
- [ ] User management with Keycloak integration
- [ ] Role/permission management UI
- [ ] All using Angular Material components

---

#### LIS-032: Build remaining admin configuration screens
**Labels:** `frontend`
**Description:**
Angular screens for configuration masters (Screens #142-154):
- Holiday calendar
- Notification templates
- Report templates
- Discount scheme management
- Insurance tariff management
- Number series configuration
- Critical value configuration
- Delta check configuration
- Auto-validation rules

**Acceptance Criteria:**
- [ ] All configuration screens implemented
- [ ] Consistent UI patterns across screens
- [ ] Form validation
- [ ] Signal-based state

---

### Database

#### LIS-033: Create Flyway migrations for all Phase 2 tables
**Labels:** `backend`, `database`
**Description:**
Database migrations for admin module:
- Organization, Branch, Department tables
- Test, Parameter, ReferenceRange, TestPanel tables
- PriceCatalog, Doctor tables
- Configuration tables (NumberSeries, TAT, WorkingHours, Holiday)
- CriticalValueConfig, DeltaCheckConfig, AutoValidationRule tables
- Antibiotic, Microorganism, AntibioticOrganismMapping tables
- All with standard BaseEntity columns + indexes

**Acceptance Criteria:**
- [ ] All migrations follow naming convention
- [ ] Forward and rollback scripts
- [ ] Indexes on frequently queried columns
- [ ] Foreign key relationships
- [ ] Seed data for essential masters

---

## Completion Criteria

- [ ] All master data CRUD APIs functional (55+ endpoints)
- [ ] All 25 admin Angular screens implemented
- [ ] Master data seeded for development/testing
- [ ] Flyway migrations run cleanly
- [ ] 80% test coverage on lis-admin module
- [ ] API documentation (OpenAPI/Swagger) for all endpoints
- [ ] Admin screens pass accessibility checks
