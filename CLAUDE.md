# RasterOneLab — LIS Project Memory File

> **For Claude Code AI Agent**: This file provides complete project context for autonomous code generation.
> Always read this file at the start of every session.

## Project Overview

**RasterOneLab** is an enterprise-grade, multi-branch **Lab Information System (LIS)** built for diagnostic laboratory chains.

- **Type**: Multi-tenant SaaS (branch_id column-level filtering, NOT schema per tenant)
- **Use Case**: Complete lab operations — patient registration → sample → testing → results → billing → reports
- **Scale**: Designed for 10-500 branches, 10,000+ daily tests, 1M+ patient records

---

## Tech Stack

| Component | Technology |
|-----------|-----------|
| Backend | Java 21, Spring Boot 3.4, Gradle Kotlin DSL |
| Frontend | Angular 19, TypeScript 5.x, Angular Material 19, Tailwind CSS 4 |
| Database | PostgreSQL 17 |
| Auth | Keycloak 24 (OAuth2 + OpenID Connect) |
| Cache | Redis 7.x |
| Queue | RabbitMQ 3.13 |
| Search | Elasticsearch 8 |
| Storage | MinIO (S3-compatible) |
| CI/CD | Jenkins, Docker, Kubernetes |

---

## Monorepo Structure

```
rasteronelab/
├── backend/                    # Gradle multi-module
│   ├── settings.gradle.kts
│   ├── build.gradle.kts
│   ├── lis-core/              # Shared: BaseEntity, ApiResponse, BranchContextHolder, exceptions, enums
│   ├── lis-patient/           # Patient registration & demographics
│   ├── lis-sample/            # Sample collection & tracking
│   ├── lis-order/             # Test order management
│   ├── lis-result/            # Result entry & validation
│   ├── lis-report/            # PDF report generation
│   ├── lis-billing/           # Invoicing & payments
│   ├── lis-inventory/         # Reagents & consumables
│   ├── lis-instrument/        # ASTM/HL7 instrument interface
│   ├── lis-qc/                # Quality control (Westgard, EQA)
│   ├── lis-admin/             # Master data & configuration
│   ├── lis-notification/      # SMS, Email, WhatsApp alerts
│   ├── lis-integration/       # HL7 FHIR, external systems
│   ├── lis-gateway/           # Spring Cloud Gateway
│   └── lis-auth/              # Keycloak OAuth2 adapter
├── frontend/                   # Angular 19 SPA
│   └── src/app/
│       ├── core/              # Guards, interceptors, services
│       ├── shared/            # Shared components
│       ├── layouts/           # Layouts
│       └── features/          # Lazy-loaded feature modules
├── infrastructure/
│   ├── docker/                # docker-compose, Dockerfiles
│   ├── nginx/
│   ├── jenkins/
│   └── k8s/
└── docs/                      # Full documentation
```

---

## Essential Commands

### Backend
```bash
cd backend
./gradlew build                          # Build all modules
./gradlew test                           # Run all tests
./gradlew :lis-patient:bootRun           # Run patient service
./gradlew :lis-gateway:bootRun           # Run API gateway
./gradlew :lis-patient:test              # Run module tests
./gradlew jacocoTestReport               # Generate coverage report
./gradlew checkstyleMain                 # Run checkstyle
./gradlew :lis-patient:dependencies      # Show dependency tree
```

### Frontend
```bash
cd frontend
ng serve                                 # Dev server (http://localhost:4200)
ng build --configuration=production      # Production build
ng test                                  # Unit tests (Karma)
ng e2e                                   # E2E tests (Cypress)
ng generate component features/patient/components/patient-list --standalone
```

### Infrastructure
```bash
cd infrastructure/docker
docker-compose up -d                     # Start all services
docker-compose up -d postgres redis      # Start specific services
docker-compose logs -f keycloak          # Tail logs
docker-compose down -v                   # Stop & remove volumes
```

---

## Architecture Rules

### 🏢 Multi-Branch Rules (CRITICAL)
- **EVERY** entity MUST have `branchId` (UUID)
- **EVERY** API endpoint MUST be branch-scoped
- Use `BranchContextHolder` (ThreadLocal) to hold current branch in request context
- `BranchInterceptor` sets branch from `X-Branch-Id` header
- Repository queries MUST filter by `branchId`
- Redis keys MUST be prefixed with `branch:{branchId}:`
- Cross-branch access only for SUPER_ADMIN role

### 🏗️ Backend Architecture (Hexagonal)
```
com.rasteronelab.lis.{module}/
├── domain/
│   ├── model/          # JPA entities (never expose outside module)
│   └── repository/     # Spring Data JPA repository interfaces
├── application/
│   ├── port/
│   │   └── in/         # Use case interfaces (input ports)
│   └── service/        # Service implementations (@Service)
├── infrastructure/     # External: DB impl, messaging, file storage
└── api/
    ├── rest/           # @RestController classes
    ├── dto/            # Request/Response DTOs (ONLY expose these)
    └── mapper/         # MapStruct mappers (Entity ↔ DTO)
```

### Backend Coding Rules
- **DTOs ONLY** in API responses — NEVER expose JPA entities
- **MapStruct** for all entity ↔ DTO mapping (NO manual mapping)
- **@Transactional** on all service methods that write to DB
- **Spring Events** (`ApplicationEventPublisher`) for cross-module communication
- **BaseEntity** for all entities: `id (UUID v7)`, `branchId`, `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, `isDeleted`, `deletedAt`
- **@PreAuthorize** on all controller methods
- **ApiResponse<T>** wrapper for all REST responses
- **PagedResponse<T>** for paginated responses
- **Flyway** for ALL database schema changes (never modify DB directly)
- **BigDecimal** for all monetary and decimal lab values (NEVER double/float)
- **Constructor injection** only (no @Autowired field injection)
- **JUnit 5 + Mockito + Testcontainers** for testing
- Soft delete: `isDeleted = true` + `deletedAt` timestamp

### 🅰️ Frontend Architecture Rules
- **Angular Signals** for all state (NOT RxJS BehaviorSubject/Subject)
- **Standalone components** ONLY — NO NgModules ever
- **Lazy-loaded routes** for all feature modules
- **Angular Material + Tailwind CSS** for all UI
- **Smart/Dumb component** pattern (pages = smart, components = dumb/presentational)
- **BranchInterceptor** HttpInterceptor adds `X-Branch-Id` header to all requests
- **@ngrx/signals** for complex state management (SignalStore)
- **date-fns** for all date formatting (NO Moment.js)
- **@if / @for** new control flow (NO *ngIf / *ngFor)
- **inject()** function (NOT constructor injection in components)
- **NO `any` type** — always use proper TypeScript types
- **NO NgModules** — everything is standalone

### 🗄️ Database Rules
- `snake_case` for all table and column names
- **UUID v7** for all primary keys (ordered UUIDs)
- **Soft delete**: `is_deleted BOOLEAN DEFAULT FALSE` + `deleted_at TIMESTAMP`
- **JSONB** for flexible/extensible fields
- All tables need: `id`, `branch_id`, `created_at`, `updated_at`, `created_by`, `updated_by`, `is_deleted`, `deleted_at`
- **Partitioning** for high-volume tables (test_result, audit_trail)
- Indexes on: `branch_id`, `is_deleted`, foreign keys, search fields

### 📝 Naming Conventions
- Java: `PascalCase` classes, `camelCase` methods/fields
- REST: `kebab-case` endpoints (`/api/v1/patient-visits`)
- DB: `snake_case` tables/columns (`patient_visit`, `branch_id`)
- TypeScript: `camelCase` properties, `PascalCase` interfaces/classes
- Files: `kebab-case.ts` (Angular), `PascalCase.java` (Java)
- Constants: `UPPER_SNAKE_CASE`

### 🌿 Git Conventions
- Branch: `feature/LIS-{ticket}-description`
- Commit: `feat(module): message [LIS-123]`
- PR: Must have tests, docs updated, no failing checks

---

## Healthcare Domain Notes

- **TAT** — Turnaround Time: time from sample receipt to result release
- **Critical Values** — Panic values requiring immediate physician notification
- **Delta Check** — Comparing current vs previous result for same patient
- **Westgard Rules** — QC statistical rules (1-2s, 1-3s, 2-2s, R-4s, 4-1s, 10x)
- **ASTM Protocol** — E1381/E1394 standard for instrument communication
- **HL7 FHIR** — Health Level 7 Fast Healthcare Interoperability Resources
- **LOINC** — Logical Observation Identifiers Names and Codes (test codes)
- **Reflex Testing** — Auto-ordering additional tests based on result
- **Aliquoting** — Dividing primary sample into sub-samples

---

## Lab Workflow (8 Steps)

```
1. REGISTRATION    → Patient demographics, UHID generation
2. ORDER           → Test selection, priority, doctor, insurance
3. COLLECTION      → Sample collection, barcode label, tube assignment
4. RECEIVING       → Lab receipt, acceptance/rejection, aliquoting
5. PROCESSING      → Centrifugation, preparation, routing to department
6. RESULT ENTRY    → Department-specific entry (numeric/narrative/culture)
7. AUTHORIZATION   → Pathologist review, critical value notification
8. DELIVERY        → PDF report generation, portal/SMS/email delivery
```

---

## Department Result Types

| Department | Result Entry Type | Report Style |
|-----------|------------------|-------------|
| Biochemistry | NUMERIC tabular | Table: Param/Result/Unit/Flag/Range |
| Hematology | NUMERIC + SCATTER_PLOT + NARRATIVE | Multi-section: table + images + narrative |
| Microbiology | CULTURE_SENSITIVITY (multi-stage) | Antibiogram table + narrative |
| Histopathology | NARRATIVE + IMAGES | Pure narrative with images |
| Clinical Pathology | SEMI_QUANTITATIVE | Tabular with +/- scale |
| Serology | QUALITATIVE + RATIO | Reactive/Non-Reactive with titer |
| Molecular Biology | CT_VALUES + QUALITATIVE | Numeric CT + Detected/Not Detected |
| Cytology | NARRATIVE + BETHESDA | Structured narrative |
| Toxicology | QUANTITATIVE + QUALITATIVE | Result vs cutoff |
| Genetics | KARYOTYPE + NARRATIVE | Karyogram + narrative |
| Immunohematology | BLOOD_GROUP + CROSSMATCH | Special format |

---

## Master Data Architecture (125 Masters, 5 Levels)

| Level | Count | Scope | Examples |
|-------|-------|-------|---------|
| L1 Global | 30 | Platform-wide | LOINC codes, ICD-10, tube types, units |
| L2 Organization | 53 | Per organization | Departments, test catalog, panels, reference ranges, pricing |
| L3 Branch | 27 | Per branch | Instruments, price overrides, QC materials, working hours |
| L4 Operational | 10 | Day-to-day | QC frequency, auto-validation rules, escalation rules |
| L5 External | 5 | Portal users | Doctor/patient preferences, consent forms |

---

## Result Type Strategy Pattern

```java
// Strategy pattern for result processing
public interface ResultProcessor {
    ResultType getSupportedType();
    void validate(Result result);
    void process(Result result);
    byte[] generateReportSection(Result result);
}

// Implementations per department
@Component class NumericResultProcessor implements ResultProcessor { ... }
@Component class NarrativeResultProcessor implements ResultProcessor { ... }
@Component class CultureResultProcessor implements ResultProcessor { ... }
```

## Result Entity Inheritance Model

```java
@Entity @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Result extends BaseEntity { ... }

@Entity class NumericResult extends Result { BigDecimal value; ... }
@Entity class NarrativeResult extends Result { String reportText; ... }
@Entity class CultureResult extends Result { List<OrganismResult> organisms; ... }
@Entity class QualitativeResult extends Result { String interpretation; ... }
```

---

## Reference Documentation

- Architecture: `@docs/architecture/system-architecture.md`
- Multi-Branch: `@docs/architecture/multi-branch-implementation.md`
- Database Schema: `@docs/architecture/database-schema.md`
- API Spec: `@docs/architecture/api-specification.md`
- Security: `@docs/architecture/security-architecture.md`
- Masters: `@docs/domain/masters/00-master-data-overview.md`
- Departments: `@docs/domain/departments/00-department-overview.md`
- Error Codes: `@docs/architecture/error-codes.md`
- Coding Standards: `@docs/development/coding-standards.md`
- Testing Strategy: `@docs/development/testing-strategy.md`
