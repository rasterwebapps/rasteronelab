#!/usr/bin/env bash
#
# create-github-milestones.sh
#
# Creates GitHub Milestones and Issues for RasterOneLab LIS based on the
# development plan documented in docs/milestones/.
#
# Prerequisites:
#   - gh CLI installed and authenticated (gh auth login)
#   - Repository: rasterdevapps/rasteronelab
#
# Usage:
#   ./scripts/create-github-milestones.sh [--dry-run] [--phase N]
#
# Options:
#   --dry-run    Print commands without executing
#   --phase N    Only create milestone and issues for phase N (1-8)

set -euo pipefail

REPO="rasterdevapps/rasteronelab"
DRY_RUN=false
PHASE_FILTER=""

# Parse arguments
while [[ $# -gt 0 ]]; do
    case "$1" in
        --dry-run)
            DRY_RUN=true
            shift
            ;;
        --phase)
            PHASE_FILTER="$2"
            shift 2
            ;;
        *)
            echo "Unknown option: $1"
            exit 1
            ;;
    esac
done

create_label() {
    local name="$1"
    local color="$2"
    local description="$3"
    if [ "$DRY_RUN" = true ]; then
        echo "[DRY RUN] gh label create '$name' --color '$color' --description '$description' --repo '$REPO' --force"
    else
        gh label create "$name" --color "$color" --description "$description" --repo "$REPO" --force 2>/dev/null || true
    fi
}

create_milestone() {
    local title="$1"
    local description="$2"
    if [ "$DRY_RUN" = true ]; then
        echo "[DRY RUN] gh api repos/$REPO/milestones -f title='$title' -f description='$description' -f state='open'"
    else
        gh api "repos/$REPO/milestones" -f title="$title" -f description="$description" -f state="open" 2>/dev/null || echo "Milestone may already exist: $title"
    fi
}

create_issue() {
    local title="$1"
    local body="$2"
    local milestone="$3"
    local labels="$4"
    if [ "$DRY_RUN" = true ]; then
        echo "[DRY RUN] gh issue create --repo '$REPO' --title '$title' --milestone '$milestone' --label '$labels'"
    else
        gh issue create --repo "$REPO" --title "$title" --body "$body" --milestone "$milestone" --label "$labels"
    fi
}

echo "========================================="
echo " RasterOneLab LIS — Milestone Creator"
echo "========================================="
echo ""
echo "Repository: $REPO"
echo "Dry Run: $DRY_RUN"
[ -n "$PHASE_FILTER" ] && echo "Phase Filter: $PHASE_FILTER"
echo ""

# -------------------------------------------------------------------
# Step 1: Create Labels
# -------------------------------------------------------------------
echo "--- Creating Labels ---"
create_label "backend"        "0075ca" "Java/Spring Boot backend work"
create_label "frontend"       "7057ff" "Angular 19 frontend work"
create_label "infrastructure" "e3dbed" "Docker, CI/CD, Kubernetes"
create_label "database"       "fbca04" "Schema, migrations, queries"
create_label "security"       "d93f0b" "Authentication, authorization, encryption"
create_label "testing"        "0e8a16" "Unit, integration, E2E tests"
create_label "documentation"  "c5def5" "Documentation and API specs"
create_label "phase-1"        "b60205" "Phase 1: Foundation"
create_label "phase-2"        "d93f0b" "Phase 2: Administration"
create_label "phase-3"        "e36209" "Phase 3: Patient & Ordering"
create_label "phase-4"        "fbca04" "Phase 4: Sample Management"
create_label "phase-5"        "0e8a16" "Phase 5: Result Entry"
create_label "phase-6"        "006b75" "Phase 6: Instrument Interface"
create_label "phase-7"        "0075ca" "Phase 7: Reports, QC & Notifications"
create_label "phase-8"        "5319e7" "Phase 8: Portals & Analytics"
echo ""

# -------------------------------------------------------------------
# Step 2: Create Milestones
# -------------------------------------------------------------------
echo "--- Creating Milestones ---"

declare -A MILESTONES
MILESTONES=(
    ["Phase 1: Foundation"]="Months 1-2 | Project setup, authentication (Keycloak), multi-branch infrastructure, CI/CD pipeline, BaseEntity, ApiResponse, BranchContextHolder"
    ["Phase 2: Administration Module"]="Months 2-4 | Master data CRUD (125 masters across 5 levels), admin screens (25), branch/department/test/parameter management"
    ["Phase 3: Patient & Ordering"]="Months 4-6 | Patient registration (UHID), test ordering (panel expansion), basic billing (invoicing, payments). 34 screens"
    ["Phase 4: Sample Management"]="Months 6-8 | Sample collection, receiving, aliquoting, tracking, rejection workflow, inter-branch transfer, barcode scanning. 14 screens"
    ["Phase 5: Result Entry & Validation"]="Months 8-12 | Result entry for all 7 departments, auto-calculation, delta check, critical values, auto-validation, authorization. 28 screens"
    ["Phase 6: Instrument Interface"]="Months 10-13 | ASTM E1381/E1394 bidirectional communication, TCP/IP connection manager, result mapper, RabbitMQ integration. 8 screens"
    ["Phase 7: Reports, QC & Notifications"]="Months 12-16 | PDF reports (OpenPDF), QC (Westgard rules, Levey-Jennings), multi-channel notifications, inventory management. 37 screens"
    ["Phase 8: Portals, Analytics & Launch"]="Months 16-22 | Doctor portal, patient portal, home collection, dashboards, analytics, performance testing, security audit, production deployment. 35 screens"
)

# Create milestones in order
MILESTONE_ORDER=(
    "Phase 1: Foundation"
    "Phase 2: Administration Module"
    "Phase 3: Patient & Ordering"
    "Phase 4: Sample Management"
    "Phase 5: Result Entry & Validation"
    "Phase 6: Instrument Interface"
    "Phase 7: Reports, QC & Notifications"
    "Phase 8: Portals, Analytics & Launch"
)

for ms in "${MILESTONE_ORDER[@]}"; do
    phase_num="${ms:6:1}"
    if [ -n "$PHASE_FILTER" ] && [ "$PHASE_FILTER" != "$phase_num" ]; then
        continue
    fi
    echo "Creating milestone: $ms"
    create_milestone "$ms" "${MILESTONES[$ms]}"
done
echo ""

# -------------------------------------------------------------------
# Step 3: Create Issues
# -------------------------------------------------------------------
echo "--- Creating Issues ---"

# Helper function to check phase filter
should_create() {
    local phase="$1"
    if [ -n "$PHASE_FILTER" ] && [ "$PHASE_FILTER" != "$phase" ]; then
        return 1
    fi
    return 0
}

# ==================== PHASE 1 ====================
if should_create "1"; then
echo "Creating Phase 1 issues..."

create_issue \
    "LIS-001: Create BaseEntity with UUID v7, audit fields, and soft delete" \
    "Create \`BaseEntity\` as \`@MappedSuperclass\` with id (UUID v7), branchId, createdAt, updatedAt, createdBy, updatedBy, isDeleted, deletedAt. Enable AuditingEntityListener.

**Module:** lis-core
**Ref:** docs/development/project-scaffolding.md" \
    "Phase 1: Foundation" \
    "backend,database,phase-1"

create_issue \
    "LIS-002: Create ApiResponse wrapper and PagedResponse" \
    "Create standardized \`ApiResponse<T>\` with success, message, data, errorCode, timestamp. Static factory methods. PagedResponse for pagination. \`@JsonInclude(NON_NULL)\`.

**Module:** lis-core
**Ref:** docs/development/project-scaffolding.md" \
    "Phase 1: Foundation" \
    "backend,phase-1"

create_issue \
    "LIS-003: Create global exception handler and custom exceptions" \
    "Create \`@RestControllerAdvice\` with NotFoundException (404), ValidationException (400), BranchAccessDeniedException (403), DuplicateResourceException (409), BusinessRuleException (422). Error codes: LIS-SYS-001, LIS-SEC-001, LIS-VAL-001.

**Module:** lis-core
**Ref:** docs/architecture/error-codes.md" \
    "Phase 1: Foundation" \
    "backend,phase-1"

create_issue \
    "LIS-004: Implement BranchContextHolder and BranchInterceptor" \
    "ThreadLocal-based BranchContextHolder for multi-branch context. BranchInterceptor extracts X-Branch-Id header, validates JWT branchIds claim. Clear ThreadLocal in afterCompletion.

**Module:** lis-core
**Ref:** docs/architecture/multi-branch-implementation.md" \
    "Phase 1: Foundation" \
    "backend,security,phase-1"

create_issue \
    "LIS-005: Create BranchAwareRepository base interface" \
    "Base repository with branch-scoped queries: findByIdAndBranchId(), findAllByBranchId(). Soft-delete filtering (is_deleted = false).

**Module:** lis-core
**Ref:** docs/architecture/multi-branch-implementation.md" \
    "Phase 1: Foundation" \
    "backend,database,phase-1"

create_issue \
    "LIS-006: Configure Keycloak realm with roles and custom claims" \
    "Keycloak realm 'rasteronelab' with 10 roles (SUPER_ADMIN through PATIENT). Custom JWT claims: organizationId, branchIds, employeeId. Clients: lis-frontend (public PKCE), lis-backend (confidential). Realm import JSON.

**Module:** lis-auth
**Ref:** docs/architecture/security-architecture.md" \
    "Phase 1: Foundation" \
    "backend,security,infrastructure,phase-1"

create_issue \
    "LIS-007: Implement Spring Security OAuth2 Resource Server" \
    "SecurityConfig with JWT validation, custom JwtAuthenticationConverter for roles + claims. @PreAuthorize support. CORS for Angular dev server.

**Module:** lis-auth
**Ref:** docs/architecture/security-architecture.md" \
    "Phase 1: Foundation" \
    "backend,security,phase-1"

create_issue \
    "LIS-008: Set up Spring Cloud Gateway with routing and JWT validation" \
    "API Gateway with routes for all modules (8081-8093). JWT validation filter, rate limiting (100 req/s general, 5/min login), health check aggregation.

**Module:** lis-gateway
**Ref:** docs/architecture/system-architecture.md" \
    "Phase 1: Foundation" \
    "backend,infrastructure,phase-1"

create_issue \
    "LIS-009: Scaffold Angular 19 application with authentication flow" \
    "Angular 19 SPA with standalone components, Material 19 + Tailwind CSS 4. OAuth2/OIDC with Keycloak. Login/logout/refresh flow. Auth guard and JWT interceptor.

**Module:** frontend
**Ref:** docs/development/project-scaffolding.md" \
    "Phase 1: Foundation" \
    "frontend,phase-1"

create_issue \
    "LIS-010: Implement Angular BranchInterceptor and BranchService" \
    "Signal-based BranchService. HTTP interceptor injecting X-Branch-Id header. Branch selector in top nav. LocalStorage persistence.

**Module:** frontend
**Ref:** docs/architecture/multi-branch-implementation.md" \
    "Phase 1: Foundation" \
    "frontend,phase-1"

create_issue \
    "LIS-011: Create shared Angular layout and navigation components" \
    "Main layout with sidebar + top bar. Responsive sidebar, user profile menu, breadcrumbs, loading spinners, toast notifications.

**Module:** frontend
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 1: Foundation" \
    "frontend,phase-1"

create_issue \
    "LIS-012: Create Docker Compose for all services" \
    "Docker Compose with PostgreSQL 17, Redis 7, RabbitMQ 3.13, Keycloak 24, Elasticsearch 8, MinIO. Health checks, volumes, network configuration.

**Module:** infrastructure
**Ref:** docs/development/deployment-guide.md" \
    "Phase 1: Foundation" \
    "infrastructure,phase-1"

create_issue \
    "LIS-013: Set up CI/CD pipeline with Jenkins" \
    "Multibranch pipeline: checkout → build → test → quality → Docker → deploy. Gradle cache, test reporting, JaCoCo coverage (80% threshold).

**Module:** infrastructure
**Ref:** docs/development/deployment-guide.md" \
    "Phase 1: Foundation" \
    "infrastructure,phase-1"

create_issue \
    "LIS-014: Create Dockerfiles for backend and frontend" \
    "Multi-stage Dockerfiles: Backend (Gradle → JRE 21 slim), Frontend (Node → Nginx). Non-root user, health checks, env vars.

**Module:** infrastructure
**Ref:** docs/development/deployment-guide.md" \
    "Phase 1: Foundation" \
    "infrastructure,phase-1"

create_issue \
    "LIS-015: Set up Flyway migration framework and core tables" \
    "Flyway versioned SQL migrations per module. Core tables: branch, organization, audit_trail (monthly partitioned). Naming: V{YYYYMMDD_HHmm}__{description}.sql.

**Module:** lis-core, all modules
**Ref:** docs/development/project-scaffolding.md, docs/architecture/database-schema.md" \
    "Phase 1: Foundation" \
    "backend,database,phase-1"

fi

# ==================== PHASE 2 ====================
if should_create "2"; then
echo "Creating Phase 2 issues..."

create_issue \
    "LIS-016: Implement Organization CRUD API" \
    "Organization entity with name, code, address, contact, logo. Uses org_id (NOT branch_id). SUPER_ADMIN only. Flyway migration.

**Module:** lis-admin
**Ref:** docs/domain/masters/02-organization-masters.md" \
    "Phase 2: Administration Module" \
    "backend,database,phase-2"

create_issue \
    "LIS-017: Implement Branch Management CRUD API" \
    "Branch entity with name, code, address, org reference. Provisioning workflow. Branch-specific settings: working hours, number series, report template.

**Module:** lis-admin
**Ref:** docs/architecture/multi-branch-implementation.md" \
    "Phase 2: Administration Module" \
    "backend,database,phase-2"

create_issue \
    "LIS-018: Implement Department Management CRUD API" \
    "11 departments with name, code, description, isActive, displayOrder. Branch-department mapping. Seed data.

**Module:** lis-admin
**Ref:** docs/domain/departments/00-department-overview.md" \
    "Phase 2: Administration Module" \
    "backend,database,phase-2"

create_issue \
    "LIS-019: Implement Test Master CRUD API" \
    "Test entity with department, sampleType, tubeType, TAT, pricing. Test-Parameter mapping. Search/filter. Activation per branch.

**Module:** lis-admin
**Ref:** docs/domain/masters/02-organization-masters.md" \
    "Phase 2: Administration Module" \
    "backend,database,phase-2"

create_issue \
    "LIS-020: Implement Parameter Master CRUD API" \
    "Parameter entity with name, code, unit, dataType (NUMERIC/TEXT/QUALITATIVE/SEMI_QUANTITATIVE). Display config, LOINC mapping.

**Module:** lis-admin
**Ref:** docs/domain/parameters/parameter-configuration.md" \
    "Phase 2: Administration Module" \
    "backend,database,phase-2"

create_issue \
    "LIS-021: Implement Reference Range Configuration API" \
    "Reference ranges per parameter: gender, ageMin/Max, normalMin/Max, criticalLow/High. Pregnancy-specific. Overlap validation.

**Module:** lis-admin
**Ref:** docs/domain/parameters/parameter-configuration.md" \
    "Phase 2: Administration Module" \
    "backend,database,phase-2"

create_issue \
    "LIS-022: Implement Test Panel Management API" \
    "Panel entity with constituent tests. Expansion logic (recursive). Panel pricing. Nested panels.

**Module:** lis-admin
**Ref:** docs/domain/masters/02-organization-masters.md" \
    "Phase 2: Administration Module" \
    "backend,database,phase-2"

create_issue \
    "LIS-023: Implement Price Catalog Management API" \
    "Price catalog with rate list types (WALK_IN, CORPORATE, INSURANCE, DOCTOR_REF). Branch overrides. Effective dates. Bulk update.

**Module:** lis-admin
**Ref:** docs/process-flows/09-billing-payment.md" \
    "Phase 2: Administration Module" \
    "backend,database,phase-2"

create_issue \
    "LIS-024: Implement Doctor Management CRUD API" \
    "Doctor entity with name, specialization, phone, registrationNumber, referralCommission. Branch mapping. Search.

**Module:** lis-admin
**Ref:** docs/domain/masters/05-external-masters.md" \
    "Phase 2: Administration Module" \
    "backend,database,phase-2"

create_issue \
    "LIS-025: Implement User Management with Keycloak integration" \
    "CRUD users in Keycloak via Admin API. Role/branch assignment. Local DB for extended attributes. Password reset.

**Module:** lis-admin, lis-auth
**Ref:** docs/architecture/security-architecture.md" \
    "Phase 2: Administration Module" \
    "backend,security,phase-2"

create_issue \
    "LIS-026: Implement Number Series, TAT, and Working Hours Configuration" \
    "Branch-level config: UHID/Order/Sample prefixes, TAT per test (routine vs STAT), working hours with holiday calendar.

**Module:** lis-admin
**Ref:** docs/domain/masters/03-branch-masters.md" \
    "Phase 2: Administration Module" \
    "backend,database,phase-2"

create_issue \
    "LIS-027: Implement Critical Value and Delta Check Configuration" \
    "Critical value thresholds per parameter. Delta check rules (percentage, time window). Auto-validation rules.

**Module:** lis-admin
**Ref:** docs/domain/critical-values.md" \
    "Phase 2: Administration Module" \
    "backend,database,phase-2"

create_issue \
    "LIS-028: Implement Antibiotic and Microorganism Masters" \
    "Antibiotic master with CLSI breakpoints. Microorganism master with gram type. Antibiotic-organism mapping. Seed data.

**Module:** lis-admin
**Ref:** docs/domain/departments/03-microbiology.md" \
    "Phase 2: Administration Module" \
    "backend,database,phase-2"

create_issue \
    "LIS-029: Build Branch Configuration admin screen" \
    "Branch list with search/filter. Create/Edit form. Activation toggle. Provisioning wizard. Working hours UI.

**Module:** frontend (Screen #130)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 2: Administration Module" \
    "frontend,phase-2"

create_issue \
    "LIS-030: Build Test Master and Parameter Master admin screens" \
    "Test list with department filter. Test create/edit with parameter mapping. Parameter list/form. Reference range matrix. Panel management.

**Module:** frontend (Screens #132-138)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 2: Administration Module" \
    "frontend,phase-2"

create_issue \
    "LIS-031: Build Doctor, User, and Role Management admin screens" \
    "Doctor management: list, create, edit, search. User management with Keycloak. Role/permission matrix.

**Module:** frontend (Screens #139-141)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 2: Administration Module" \
    "frontend,phase-2"

create_issue \
    "LIS-032: Build remaining admin configuration screens" \
    "Holiday calendar, notification/report templates, discount schemes, insurance tariffs, number series, critical value config, delta check config, auto-validation rules.

**Module:** frontend (Screens #142-154)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 2: Administration Module" \
    "frontend,phase-2"

create_issue \
    "LIS-033: Create Flyway migrations for all Phase 2 tables" \
    "Migrations for Organization, Branch, Department, Test, Parameter, ReferenceRange, Panel, PriceCatalog, Doctor, Configuration tables, Antibiotic, Microorganism. All with BaseEntity columns + indexes.

**Module:** lis-admin
**Ref:** docs/architecture/database-schema.md" \
    "Phase 2: Administration Module" \
    "backend,database,phase-2"

fi

# ==================== PHASE 3 ====================
if should_create "3"; then
echo "Creating Phase 3 issues..."

create_issue \
    "LIS-034: Implement Patient CRUD API with UHID generation" \
    "Patient entity with UHID format {BranchCode}-{6-digit-sequence}. Search by phone/name/UHID. PHI audit logging. Soft delete.

**Module:** lis-patient
**Ref:** docs/process-flows/01-patient-registration.md" \
    "Phase 3: Patient & Ordering" \
    "backend,database,phase-3"

create_issue \
    "LIS-035: Implement Duplicate Patient Detection and Merge" \
    "Duplicate detection by name+DOB, phone. Scoring algorithm. Merge workflow with data transfer and audit trail.

**Module:** lis-patient
**Ref:** docs/process-flows/01-patient-registration.md" \
    "Phase 3: Patient & Ordering" \
    "backend,phase-3"

create_issue \
    "LIS-036: Implement Patient Visit Management" \
    "Visit entity with patient, visitDate, visitType, referringDoctor. Auto-create on order. Visit history with order summary.

**Module:** lis-patient
**Ref:** docs/process-flows/01-patient-registration.md" \
    "Phase 3: Patient & Ordering" \
    "backend,database,phase-3"

create_issue \
    "LIS-037: Implement Test Order Creation API with state machine" \
    "Order entity with patient, visit, doctor, priority, status. OrderLineItem. State machine: DRAFT → PLACED → ... → COMPLETED. Barcode: ORD-{BranchCode}-{YYYYMMDD}-{seq}.

**Module:** lis-order
**Ref:** docs/process-flows/02-test-ordering.md, docs/architecture/workflow-state-machines.md" \
    "Phase 3: Patient & Ordering" \
    "backend,database,phase-3"

create_issue \
    "LIS-038: Implement Panel Expansion and Test Search" \
    "Test search with autocomplete. Panel expansion (recursive). Nested panels. De-duplication. Reflex test rules.

**Module:** lis-order
**Ref:** docs/process-flows/02-test-ordering.md" \
    "Phase 3: Patient & Ordering" \
    "backend,phase-3"

create_issue \
    "LIS-039: Implement Order Validation and Sample Requirements" \
    "Validate required fields. Check sample requirements (tubes, volumes). Group by tube type. Calculate estimated TAT.

**Module:** lis-order
**Ref:** docs/process-flows/02-test-ordering.md" \
    "Phase 3: Patient & Ordering" \
    "backend,phase-3"

create_issue \
    "LIS-040: Implement Invoice Generation API" \
    "Invoice entity with pricing logic: base → branch override → rate list → discount. State machine: DRAFT → GENERATED → PAID. Invoice number format.

**Module:** lis-billing
**Ref:** docs/process-flows/09-billing-payment.md" \
    "Phase 3: Patient & Ordering" \
    "backend,database,phase-3"

create_issue \
    "LIS-041: Implement Payment Recording API" \
    "Payment entity with methods: CASH, CARD, UPI, INSURANCE, CREDIT, ONLINE. Split payment. Partial payment with balance. Receipt generation.

**Module:** lis-billing
**Ref:** docs/process-flows/09-billing-payment.md" \
    "Phase 3: Patient & Ordering" \
    "backend,database,phase-3"

create_issue \
    "LIS-042: Implement Discount, Refund, and Outstanding Management" \
    "Discount schemes (percentage, flat, coupon). Corporate/insurance credit limits. Refund with supervisor approval. Outstanding tracking.

**Module:** lis-billing
**Ref:** docs/process-flows/09-billing-payment.md" \
    "Phase 3: Patient & Ordering" \
    "backend,database,phase-3"

create_issue \
    "LIS-043: Build Patient Search and List screen" \
    "Data table with UHID, Name, Age/Gender, Phone, Last Visit. Multi-criteria search. Filters. Quick actions. Pagination.

**Module:** frontend (Screen #14)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 3: Patient & Ordering" \
    "frontend,phase-3"

create_issue \
    "LIS-044: Build Patient Registration and Edit screens" \
    "Registration form with mandatory/optional fields. Age/DOB calculator. Duplicate detection warning. Edit form.

**Module:** frontend (Screens #15-16)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 3: Patient & Ordering" \
    "frontend,phase-3"

create_issue \
    "LIS-045: Build Patient Detail, Visit History, and related screens" \
    "Patient detail view (tabbed). Visit history. Order/Report/Billing history. Patient merge UI.

**Module:** frontend (Screens #17-25)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 3: Patient & Ordering" \
    "frontend,phase-3"

create_issue \
    "LIS-046: Build Create Order wizard (3-step)" \
    "Step 1: Patient search/select. Step 2: Test selection with panel expansion. Step 3: Review, pricing, submit. Barcode display.

**Module:** frontend (Screens #26-28)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 3: Patient & Ordering" \
    "frontend,phase-3"

create_issue \
    "LIS-047: Build Order List, Detail, and Management screens" \
    "Order list with status filters. Detail with timeline. Edit (DRAFT only). Cancel. Barcode print. TAT monitor.

**Module:** frontend (Screens #29-35)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 3: Patient & Ordering" \
    "frontend,phase-3"

create_issue \
    "LIS-048: Build Invoice and Payment screens" \
    "Invoice list/detail. Payment form (multi-method). Receipt print. Refund form. Outstanding list. Collection report.

**Module:** frontend (Screens #88-99)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 3: Patient & Ordering" \
    "frontend,phase-3"

create_issue \
    "LIS-049: Implement Spring Events for Order → Invoice auto-generation" \
    "OrderPlacedEvent → InvoiceService listener. PaymentReceivedEvent → order status update. Event audit logging.

**Module:** lis-order, lis-billing
**Ref:** docs/architecture/system-architecture.md" \
    "Phase 3: Patient & Ordering" \
    "backend,phase-3"

create_issue \
    "LIS-050: Implement Barcode Generation Service" \
    "Centralized barcode generation for orders, samples, UHID. Code128/QR image rendering. Branch-specific number series.

**Module:** lis-core
**Ref:** docs/domain/barcode-strategy.md" \
    "Phase 3: Patient & Ordering" \
    "backend,phase-3"

create_issue \
    "LIS-051: Create Flyway migrations for Patient, Order, and Billing tables" \
    "Patient, PatientVisit, TestOrder, OrderLineItem, Invoice, Payment, Refund, CreditAccount, NumberSeries tables.

**Module:** lis-patient, lis-order, lis-billing
**Ref:** docs/architecture/database-schema.md" \
    "Phase 3: Patient & Ordering" \
    "backend,database,phase-3"

create_issue \
    "LIS-052: End-to-end test: Patient → Order → Invoice → Payment flow" \
    "Integration test: register patient → create order → verify invoice → record payment. Test split/partial payments. Multi-branch isolation.

**Module:** lis-patient, lis-order, lis-billing
**Ref:** docs/process-flows/complete-lipid-cbc-walkthrough.md" \
    "Phase 3: Patient & Ordering" \
    "backend,testing,phase-3"

create_issue \
    "LIS-053: Create OpenAPI documentation for Patient, Order, and Billing APIs" \
    "OpenAPI 3.0 annotations. Request/response examples. Swagger UI at /swagger-ui.html. Postman collection.

**Module:** lis-patient, lis-order, lis-billing
**Ref:** docs/architecture/api-specification.md" \
    "Phase 3: Patient & Ordering" \
    "documentation,phase-3"

create_issue \
    "LIS-054: Complete Lipid + CBC Walkthrough integration test" \
    "Implement exact scenario from docs: Patient Rajesh Kumar → Lipid Profile + CBC + ESR → Panel expansion → WALK_IN pricing → Payment.

**Module:** all
**Ref:** docs/process-flows/complete-lipid-cbc-walkthrough.md" \
    "Phase 3: Patient & Ordering" \
    "backend,testing,phase-3"

fi

# ==================== PHASE 4 ====================
if should_create "4"; then
echo "Creating Phase 4 issues..."

create_issue \
    "LIS-055: Implement Sample Collection Recording API" \
    "SampleCollection entity. Test-to-tube mapping (RED, PURPLE/EDTA, BLUE, GREY, GREEN). Sample barcode: SMP-{BranchCode}-{YYYYMMDD}-{seq}. Multiple tubes per order.

**Module:** lis-sample
**Ref:** docs/process-flows/03-sample-collection.md, docs/domain/sample-types-tubes.md" \
    "Phase 4: Sample Management" \
    "backend,database,phase-4"

create_issue \
    "LIS-056: Implement Sample State Machine" \
    "States: COLLECTED → RECEIVED → ACCEPTED/REJECTED → ALIQUOTED → PROCESSING → COMPLETED → STORED → DISPOSED. Rejection reasons enum. State change events.

**Module:** lis-sample
**Ref:** docs/architecture/workflow-state-machines.md" \
    "Phase 4: Sample Management" \
    "backend,phase-4"

create_issue \
    "LIS-057: Implement Sample Receiving (Accept/Reject) API" \
    "Scan barcode to identify. Accept/reject with reason. Batch receiving. Recollection notification trigger. Order status auto-update.

**Module:** lis-sample
**Ref:** docs/process-flows/04-sample-receiving.md" \
    "Phase 4: Sample Management" \
    "backend,phase-4"

create_issue \
    "LIS-058: Implement Sample Aliquoting API" \
    "Aliquot entity with parent-child tracking. Auto-generated aliquot barcodes. Volume tracking. Only from accepted samples.

**Module:** lis-sample
**Ref:** docs/process-flows/03-sample-collection.md" \
    "Phase 4: Sample Management" \
    "backend,database,phase-4"

create_issue \
    "LIS-059: Implement Sample Tracking and Storage API" \
    "Sample timeline (collection → dispose). Storage location (rack/shelf/position). Auto-dispose reminders. Tracking query by barcode/patient/order.

**Module:** lis-sample
**Ref:** docs/process-flows/03-sample-collection.md" \
    "Phase 4: Sample Management" \
    "backend,database,phase-4"

create_issue \
    "LIS-060: Implement Inter-Branch Sample Transfer" \
    "Transfer request with source/dest branch. Dual branch context writing. Transfer status tracking. BRANCH_ADMIN+ authorization.

**Module:** lis-sample
**Ref:** docs/architecture/multi-branch-implementation.md" \
    "Phase 4: Sample Management" \
    "backend,security,phase-4"

create_issue \
    "LIS-061: Implement Pending Collection and Pending Receipt Lists" \
    "Pending collection: orders PAID but not collected. Pending receipt: samples COLLECTED but not received. Priority and TAT sorting.

**Module:** lis-sample
**Ref:** docs/process-flows/03-sample-collection.md" \
    "Phase 4: Sample Management" \
    "backend,phase-4"

create_issue \
    "LIS-062: Build Sample Collection screen with barcode scanning" \
    "Scan order barcode to load patient/tests. Display tubes required (color-coded). Record collection per tube. Print tube labels.

**Module:** frontend (Screen #36-37)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 4: Sample Management" \
    "frontend,phase-4"

create_issue \
    "LIS-063: Build Sample Receive/Reject screens" \
    "Receive screen with barcode scan. Batch receive. Reject dialog with reason. Visual condition indicators.

**Module:** frontend (Screens #38-40)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 4: Sample Management" \
    "frontend,phase-4"

create_issue \
    "LIS-064: Build Sample List, Detail, and Tracking screens" \
    "Sample list with status filters. Detail view. Visual tracking timeline. Status badges.

**Module:** frontend (Screens #41-42, 46)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 4: Sample Management" \
    "frontend,phase-4"

create_issue \
    "LIS-065: Build Aliquoting, Storage, Transfer, and Disposal screens" \
    "Aliquot form. Storage location picker. Transfer request form. Disposal confirmation. Pending collection/receipt lists.

**Module:** frontend (Screens #43-45, 47-49)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 4: Sample Management" \
    "frontend,phase-4"

create_issue \
    "LIS-066: Implement barcode scanner integration (WebHID API)" \
    "WebHID API for USB scanners. Keyboard wedge fallback. Scan event handling. Audible feedback. Error handling.

**Module:** frontend
**Ref:** docs/domain/barcode-strategy.md" \
    "Phase 4: Sample Management" \
    "frontend,infrastructure,phase-4"

create_issue \
    "LIS-067: Create Flyway migrations for Sample Management tables" \
    "SampleCollection, SampleTest, SampleAliquot, SampleStorage, SampleTransfer, SampleTracking, RejectionReason tables.

**Module:** lis-sample
**Ref:** docs/architecture/database-schema.md" \
    "Phase 4: Sample Management" \
    "backend,database,phase-4"

create_issue \
    "LIS-068: End-to-end test: Order → Collection → Receive → Accept/Reject flow" \
    "Integration test: create order → collect samples → receive → accept/reject. Test aliquoting. Test inter-branch transfer. State machine verification.

**Module:** lis-sample
**Ref:** docs/process-flows/03-sample-collection.md, docs/process-flows/04-sample-receiving.md" \
    "Phase 4: Sample Management" \
    "backend,testing,phase-4"

fi

# ==================== PHASE 5 ====================
if should_create "5"; then
echo "Creating Phase 5 issues..."

create_issue \
    "LIS-069: Implement Result Entry Core API and State Machine" \
    "TestResult entity with parameter, value, unit, abnormalFlag. State machine: PENDING → ENTERED → VALIDATED → AUTHORIZED → RELEASED. Department worklist.

**Module:** lis-result
**Ref:** docs/process-flows/06-result-entry-validation.md, docs/architecture/workflow-state-machines.md" \
    "Phase 5: Result Entry & Validation" \
    "backend,database,phase-5"

create_issue \
    "LIS-070: Implement Biochemistry Numeric Result Entry" \
    "Numeric entry with auto-calculations: LDL, VLDL, eGFR, A:G Ratio, Globulin. Abnormal flagging (CRITICAL_LOW/LOW/NORMAL/HIGH/CRITICAL_HIGH).

**Module:** lis-result
**Ref:** docs/domain/departments/01-biochemistry.md" \
    "Phase 5: Result Entry & Validation" \
    "backend,phase-5"

create_issue \
    "LIS-071: Implement Hematology Result Entry (CBC + Differential)" \
    "CBC numeric entry. Auto-calculations: MCV, MCH, MCHC. Differential count. Absolute counts from percentage × WBC. Peripheral smear narrative. Scatter plot upload.

**Module:** lis-result
**Ref:** docs/domain/departments/02-hematology.md" \
    "Phase 5: Result Entry & Validation" \
    "backend,phase-5"

create_issue \
    "LIS-072: Implement Microbiology Culture and Antibiogram Entry" \
    "Multi-day workflow: Day 1 preliminary → Day 2-3 organism ID → Day 3-5 antibiogram (S/I/R + MIC). Multi-organism (up to 3 isolates). CLSI breakpoints.

**Module:** lis-result
**Ref:** docs/domain/departments/03-microbiology.md" \
    "Phase 5: Result Entry & Validation" \
    "backend,phase-5"

create_issue \
    "LIS-073: Implement Histopathology Narrative and Image Entry" \
    "Gross/microscopic narrative. Diagnosis with ICD-10. Image upload to MinIO. Template-based synoptic reports.

**Module:** lis-result
**Ref:** docs/domain/departments/04-histopathology.md" \
    "Phase 5: Result Entry & Validation" \
    "backend,phase-5"

create_issue \
    "LIS-074: Implement Clinical Pathology, Serology, and Molecular Biology Entry" \
    "Semi-quantitative (ABSENT/TRACE/1+/2+/3+/4+). Qualitative (REACTIVE/NON-REACTIVE) + titer. CT values with DETECTED/NOT_DETECTED.

**Module:** lis-result
**Ref:** docs/domain/departments/05-clinical-pathology.md, 06-serology-immunology.md, 07-molecular-biology.md" \
    "Phase 5: Result Entry & Validation" \
    "backend,phase-5"

create_issue \
    "LIS-075: Implement Delta Check Engine" \
    "Compare current with previous result. Percentage calculation. Configurable thresholds (20-50% warning, >50% critical). Time window. Block auto-validation on failure.

**Module:** lis-result
**Ref:** docs/domain/critical-values.md" \
    "Phase 5: Result Entry & Validation" \
    "backend,phase-5"

create_issue \
    "LIS-076: Implement Critical Value Detection and Notification" \
    "Detect critical values on entry. Immediate notification (Spring Event). Physician acknowledgment workflow. Critical value log. Block auto-authorization.

**Module:** lis-result
**Ref:** docs/process-flows/15-critical-value-workflow.md, docs/domain/critical-values.md" \
    "Phase 5: Result Entry & Validation" \
    "backend,phase-5"

create_issue \
    "LIS-077: Implement Auto-Validation Engine" \
    "Rule-based: normal range + delta pass + QC pass + no critical = auto-VALIDATED. Configurable per parameter. Audit trail (SYSTEM vs userId).

**Module:** lis-result
**Ref:** docs/process-flows/06-result-entry-validation.md" \
    "Phase 5: Result Entry & Validation" \
    "backend,phase-5"

create_issue \
    "LIS-078: Implement Result Authorization Workflow" \
    "PATHOLOGIST role only. Batch authorization. Pre-auth validation (QC, critical values). Report generation trigger. Amendment workflow with reason.

**Module:** lis-result
**Ref:** docs/process-flows/07-result-authorization.md" \
    "Phase 5: Result Entry & Validation" \
    "backend,phase-5"

create_issue \
    "LIS-079: Build Pending Results Dashboard" \
    "Department-wise pending count cards. TAT countdown. STAT priority highlighting. Quick navigation to worklists.

**Module:** frontend (Screen #50)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 5: Result Entry & Validation" \
    "frontend,phase-5"

create_issue \
    "LIS-080: Build Biochemistry Result Entry Grid" \
    "Spreadsheet-like grid: Parameter, Result, Unit, Range, Flag columns. Color-coded flags. Auto-calculations. Previous result column. Keyboard navigation.

**Module:** frontend (Screen #51)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 5: Result Entry & Validation" \
    "frontend,phase-5"

create_issue \
    "LIS-081: Build Hematology Result Entry screens" \
    "CBC grid. Differential form (percentage/absolute). Scatter plot upload. Peripheral smear rich text editor.

**Module:** frontend (Screens #52-55)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 5: Result Entry & Validation" \
    "frontend,phase-5"

create_issue \
    "LIS-082: Build Microbiology Result Entry screens" \
    "Multi-day entry. Organism search/select. Antibiogram matrix (organism × antibiotic, S/I/R color-coded). MIC entry. Multi-organism tabs.

**Module:** frontend (Screens #56-58)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 5: Result Entry & Validation" \
    "frontend,phase-5"

create_issue \
    "LIS-083: Build Histopathology and remaining department entry screens" \
    "Histopathology multi-section editor + image upload. Clinical Path semi-quantitative. Serology qualitative + titer. Molecular Biology CT values. Bulk entry.

**Module:** frontend (Screens #59-66, 69)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 5: Result Entry & Validation" \
    "frontend,phase-5"

create_issue \
    "LIS-084: Build Result Validation and Authorization screens" \
    "Pending validation/authorization lists. Validation/authorization screens. Batch authorize. Critical value acknowledgment. Amendment with reason. Amendment history.

**Module:** frontend (Screens #70-77)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 5: Result Entry & Validation" \
    "frontend,phase-5"

create_issue \
    "LIS-085: Build Delta Check Review and Critical Value Documentation screens" \
    "Delta check comparison display. Color-coded indicators. Override with reason. Critical value documentation form.

**Module:** frontend (Screens #67-68)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 5: Result Entry & Validation" \
    "frontend,phase-5"

create_issue \
    "LIS-086: Create Flyway migrations for Result module tables" \
    "TestResult (LIST partitioned by branch_id), ResultValidation, CriticalValueLog, DeltaCheckResult, ResultAmendment, CultureResult, AntibioticSensitivity, HistopathologyResult tables.

**Module:** lis-result
**Ref:** docs/architecture/database-schema.md" \
    "Phase 5: Result Entry & Validation" \
    "backend,database,phase-5"

create_issue \
    "LIS-087: End-to-end test: Sample → Result Entry → Validation → Authorization" \
    "Integration test: accept samples → enter results → delta check → critical values → auto-validate → authorize → report trigger. Amendment flow.

**Module:** lis-result
**Ref:** docs/process-flows/06-result-entry-validation.md, 07-result-authorization.md" \
    "Phase 5: Result Entry & Validation" \
    "backend,testing,phase-5"

create_issue \
    "LIS-088: Department-specific result calculation tests" \
    "Unit tests for all auto-calculations: Lipid (LDL, VLDL), Renal (eGFR, BUN), Liver (A:G, Globulin), Hematology (MCV, MCH, MCHC). BigDecimal precision. Edge cases.

**Module:** lis-result
**Ref:** docs/domain/departments/" \
    "Phase 5: Result Entry & Validation" \
    "backend,testing,phase-5"

fi

# ==================== PHASE 6 ====================
if should_create "6"; then
echo "Creating Phase 6 issues..."

create_issue \
    "LIS-089: Implement ASTM E1381 TCP/IP Connection Manager" \
    "TCP server socket for instruments. Connection config entity. State: DISCONNECTED → CONNECTED → ERROR. Heartbeat, auto-reconnect (exponential backoff). Multi-connection.

**Module:** lis-instrument
**Ref:** docs/domain/instrument-interface-spec.md" \
    "Phase 6: Instrument Interface" \
    "backend,infrastructure,phase-6"

create_issue \
    "LIS-090: Implement ASTM E1394 Frame Parser" \
    "Parse ASTM frames: STX [Frame#][Data] ETX [Checksum] CR LF. Record types: H, P, O, R, C, Q, L. Checksum verification. Multi-frame reassembly. ENQ/ACK/NAK handshake.

**Module:** lis-instrument
**Ref:** docs/domain/instrument-interface-spec.md" \
    "Phase 6: Instrument Interface" \
    "backend,phase-6"

create_issue \
    "LIS-091: Implement Result Mapper (Instrument Code → Parameter Code)" \
    "InstrumentParameterMapping entity. Mapping config. Unit conversion. Roche Cobas mapping (GLU→Glucose). Sysmex mapping (WBC→WBC Count).

**Module:** lis-instrument
**Ref:** docs/domain/instrument-interface-spec.md" \
    "Phase 6: Instrument Interface" \
    "backend,database,phase-6"

create_issue \
    "LIS-092: Implement Result Download (Instrument → LIS) Flow" \
    "Complete flow: ENQ/ACK → H → P (match patient) → O (match order) → R (map results) → L. Publish to RabbitMQ. Process and store in test_result.

**Module:** lis-instrument
**Ref:** docs/domain/instrument-interface-spec.md" \
    "Phase 6: Instrument Interface" \
    "backend,phase-6"

create_issue \
    "LIS-093: Implement Host Query Mode (LIS → Instrument Orders)" \
    "Instrument sends Q record (query by sample ID). LIS responds with H, P, O records. Filter tests by instrument config.

**Module:** lis-instrument
**Ref:** docs/domain/instrument-interface-spec.md" \
    "Phase 6: Instrument Interface" \
    "backend,phase-6"

create_issue \
    "LIS-094: Implement Serial Port Support for Legacy Instruments" \
    "RS-232 via jSerialComm. Config: COM port, baud rate (9600), data bits (8), stop bits (1), parity (None). ASTM over serial.

**Module:** lis-instrument
**Ref:** docs/domain/instrument-interface-spec.md" \
    "Phase 6: Instrument Interface" \
    "backend,phase-6"

create_issue \
    "LIS-095: Implement RabbitMQ Integration for Instrument Results" \
    "Exchange: instrument.results (topic). Queue with DLQ. Consumer: validate, map, store results. Retry (3x exponential backoff). Processing log.

**Module:** lis-instrument
**Ref:** docs/architecture/async-processing.md" \
    "Phase 6: Instrument Interface" \
    "backend,infrastructure,phase-6"

create_issue \
    "LIS-096: Build Instrument List and Connection Setup screens" \
    "Instrument list with status (green/red/yellow). WebSocket real-time status. Connection setup form. Test connection. Connection logs.

**Module:** frontend (Screens #110-111)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 6: Instrument Interface" \
    "frontend,phase-6"

create_issue \
    "LIS-097: Build ASTM Parameter Mapping and Result Queue screens" \
    "Two-column mapping UI (instrument ↔ LIS). Auto-suggest. Result queue with status. Unmatched result alerts.

**Module:** frontend (Screens #112-113)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 6: Instrument Interface" \
    "frontend,phase-6"

create_issue \
    "LIS-098: Build Result Processing Log, Error Log, and Import screens" \
    "Processing log with search/filter. Error log by severity. CSV import with column mapping. Calibration schedule calendar.

**Module:** frontend (Screens #114-117)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 6: Instrument Interface" \
    "frontend,phase-6"

create_issue \
    "LIS-099: Create Flyway migrations for Instrument module tables" \
    "InstrumentConnection, InstrumentParameterMapping, InstrumentResultQueue, ProcessingLog, ErrorLog, Calibration tables.

**Module:** lis-instrument
**Ref:** docs/architecture/database-schema.md" \
    "Phase 6: Instrument Interface" \
    "backend,database,phase-6"

create_issue \
    "LIS-100: ASTM protocol integration tests with mock instruments" \
    "Mock TCP instrument. ENQ/ACK/NAK handshake test. Roche Cobas result parsing. Sysmex result parsing. Host query. Reconnection. Malformed message handling.

**Module:** lis-instrument
**Ref:** docs/domain/instrument-interface-spec.md" \
    "Phase 6: Instrument Interface" \
    "backend,testing,phase-6"

fi

# ==================== PHASE 7 ====================
if should_create "7"; then
echo "Creating Phase 7 issues..."

create_issue \
    "LIS-101: Implement PDF Report Generation Engine (OpenPDF)" \
    "OpenPDF report generation. Branch header (logo, name, address). Department-specific layouts (tabular, antibiogram, narrative). QR code. Watermark for draft/amended.

**Module:** lis-report
**Ref:** docs/architecture/report-generation-engine.md" \
    "Phase 7: Reports, QC & Notifications" \
    "backend,phase-7"

create_issue \
    "LIS-102: Implement Report Storage in MinIO and Versioning" \
    "MinIO path: reports/{branchId}/{year}/{month}/{orderId}/report-vN.pdf. Version management (v1, v2, v3). Amendment with reason. SHA-256 verification hash.

**Module:** lis-report
**Ref:** docs/architecture/report-generation-engine.md" \
    "Phase 7: Reports, QC & Notifications" \
    "backend,infrastructure,phase-7"

create_issue \
    "LIS-103: Implement Report Delivery (SMS, Email, WhatsApp) Integration" \
    "Email with PDF attachment. SMS with download link. WhatsApp via BSP. Delivery tracking. Retry (3x). Bulk delivery.

**Module:** lis-report, lis-notification
**Ref:** docs/process-flows/08-report-generation-delivery.md" \
    "Phase 7: Reports, QC & Notifications" \
    "backend,phase-7"

create_issue \
    "LIS-104: Implement Branch-Specific Report Branding" \
    "Branch logo, colors, pathologist signature, NABL logo, custom disclaimer. Report template config API. Preview functionality.

**Module:** lis-report
**Ref:** docs/architecture/report-generation-engine.md" \
    "Phase 7: Reports, QC & Notifications" \
    "backend,phase-7"

create_issue \
    "LIS-105: Implement QC Daily Entry and Westgard Rules Engine" \
    "QC material management. Daily QC result entry. Westgard rules (1-2s, 1-3s, 2-2s, R-4s, 4-1s, 10x). QC status: ACCEPTED/WARNING/REJECTED. Block patient authorization on failure.

**Module:** lis-qc
**Ref:** docs/process-flows/10-quality-control.md" \
    "Phase 7: Reports, QC & Notifications" \
    "backend,database,phase-7"

create_issue \
    "LIS-106: Implement Levey-Jennings Chart Data API" \
    "Data aggregation (30/60/90 days). Mean, SD, CV, z-score calculation. Trend detection (7+ points). Chart data endpoint.

**Module:** lis-qc
**Ref:** docs/process-flows/10-quality-control.md" \
    "Phase 7: Reports, QC & Notifications" \
    "backend,phase-7"

create_issue \
    "LIS-107: Implement EQA Submission and Corrective Actions" \
    "EQA program enrollment and result submission. Corrective action documentation (problem, root cause, action, resolution). Link to QC rejections.

**Module:** lis-qc
**Ref:** docs/process-flows/10-quality-control.md" \
    "Phase 7: Reports, QC & Notifications" \
    "backend,phase-7"

create_issue \
    "LIS-108: Implement Multi-Channel Notification Engine" \
    "SMS (Twilio/SNS), Email (SMTP/SES), WhatsApp (Meta BSP). Template engine with {{variables}}. DND rules (8AM-9PM patients, 24/7 critical). Retry: immediate → 5min → 15min → 1hr → escalate.

**Module:** lis-notification
**Ref:** docs/architecture/notification-architecture.md" \
    "Phase 7: Reports, QC & Notifications" \
    "backend,infrastructure,phase-7"

create_issue \
    "LIS-109: Implement Notification Events and Delivery Tracking" \
    "Event listeners: patient registered, report ready, critical value, TAT breach, payment received. Notification log entity. Delivery status tracking. Retry management.

**Module:** lis-notification
**Ref:** docs/architecture/notification-architecture.md" \
    "Phase 7: Reports, QC & Notifications" \
    "backend,database,phase-7"

create_issue \
    "LIS-110: Implement Basic Inventory Management" \
    "Reagent entity. Stock movement (IN/OUT/ADJUST). Low stock alerts. Expiry monitoring. Supplier management. Purchase order workflow. GRN processing.

**Module:** lis-inventory
**Ref:** docs/architecture/system-architecture.md" \
    "Phase 7: Reports, QC & Notifications" \
    "backend,database,phase-7"

create_issue \
    "LIS-111: Build Report Management screens" \
    "Pending reports. PDF preview. Manual generation. Release confirmation. Multi-channel delivery. Amendment with reason. Version history. Bulk delivery.

**Module:** frontend (Screens #78-87)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 7: Reports, QC & Notifications" \
    "frontend,phase-7"

create_issue \
    "LIS-112: Build QC Entry, Levey-Jennings, and Westgard screens" \
    "QC daily entry form. Interactive Levey-Jennings chart (Chart.js). Westgard violations list. Accept/reject. Corrective action form. EQA management. QC calendar.

**Module:** frontend (Screens #100-109)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 7: Reports, QC & Notifications" \
    "frontend,phase-7"

create_issue \
    "LIS-113: Build Notification History and Settings screens" \
    "Notification history with status filters. Detail view. Manual retry. Alert management. Channel preferences per event type.

**Module:** frontend (Screens #155-159)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 7: Reports, QC & Notifications" \
    "frontend,phase-7"

create_issue \
    "LIS-114: Build Inventory Management screens" \
    "Dashboard, reagent list, stock entry/adjustment, usage log, low stock alerts, supplier management, purchase orders, GRN, expiry monitor, movement history.

**Module:** frontend (Screens #118-129)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 7: Reports, QC & Notifications" \
    "frontend,phase-7"

create_issue \
    "LIS-115: Create Flyway migrations for Report, QC, Notification, and Inventory tables" \
    "Report/ReportSection/ReportDelivery. QCMaterial/QCResult/QCRule/CorrectiveAction/EQA. NotificationLog/Template (partitioned). Reagent/StockMovement/Supplier/PurchaseOrder/GRN.

**Module:** lis-report, lis-qc, lis-notification, lis-inventory
**Ref:** docs/architecture/database-schema.md" \
    "Phase 7: Reports, QC & Notifications" \
    "backend,database,phase-7"

create_issue \
    "LIS-116: End-to-end test: Authorization → Report Generation → Delivery" \
    "Integration test: authorize results → verify PDF generated → release → deliver SMS/email → verify tracking. Test amendment and versioning.

**Module:** lis-report
**Ref:** docs/process-flows/08-report-generation-delivery.md" \
    "Phase 7: Reports, QC & Notifications" \
    "backend,testing,phase-7"

create_issue \
    "LIS-117: Westgard rules engine comprehensive tests" \
    "Test all 6 rules with known data: 1-2s, 1-3s, 2-2s, R-4s, 4-1s, 10x. Combined scenarios. Edge cases. Performance (batch evaluation).

**Module:** lis-qc
**Ref:** docs/process-flows/10-quality-control.md" \
    "Phase 7: Reports, QC & Notifications" \
    "backend,testing,phase-7"

fi

# ==================== PHASE 8 ====================
if should_create "8"; then
echo "Creating Phase 8 issues..."

create_issue \
    "LIS-118: Implement Doctor Portal Backend API" \
    "Doctor auth (Google SSO / username). Dashboard: referred patients' reports. Patient search (referral-scoped). Report viewing. Critical value alerts. E-referral ordering.

**Module:** lis-integration
**Ref:** docs/screens/screen-list-complete.md (Screens #160-169)" \
    "Phase 8: Portals, Analytics & Launch" \
    "backend,security,phase-8"

create_issue \
    "LIS-119: Implement E-Referral Order API" \
    "Doctor-initiated remote ordering. Simplified flow (no payment). Appears at branch reception. Branch notification. Track referral orders.

**Module:** lis-integration
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 8: Portals, Analytics & Launch" \
    "backend,phase-8"

create_issue \
    "LIS-120: Implement Patient Portal Backend API" \
    "OTP login (phone). Dashboard: my reports. Report download (own only). Invoice history. Profile update. Resource Owner Password flow.

**Module:** lis-integration
**Ref:** docs/screens/screen-list-complete.md (Screens #170-177)" \
    "Phase 8: Portals, Analytics & Launch" \
    "backend,security,phase-8"

create_issue \
    "LIS-121: Implement Home Collection Module" \
    "Collection request entity. Slot booking. Phlebotomist schedule. Route planning (basic). Collection confirmation. Failed collection management.

**Module:** lis-sample (or lis-integration)
**Ref:** docs/screens/screen-list-complete.md (Screens #178-185)" \
    "Phase 8: Portals, Analytics & Launch" \
    "backend,database,phase-8"

create_issue \
    "LIS-122: Implement Dashboard APIs" \
    "Summary cards (orders, results, revenue). TAT compliance by department. Pending actions count. Critical values pending. Instrument status. Redis caching.

**Module:** lis-admin (or dedicated)
**Ref:** docs/screens/screen-list-complete.md (Screens #6-13)" \
    "Phase 8: Portals, Analytics & Launch" \
    "backend,phase-8"

create_issue \
    "LIS-123: Implement Analytics and Operational Report APIs" \
    "Test volume, revenue, TAT analysis, QC performance, doctor referral, patient demographics, instrument utilization, audit trail reports. Custom report builder (stretch).

**Module:** lis-admin
**Ref:** docs/screens/screen-list-complete.md (Screens #186-194)" \
    "Phase 8: Portals, Analytics & Launch" \
    "backend,phase-8"

create_issue \
    "LIS-124: Build Doctor Portal Angular screens" \
    "Doctor login (Google SSO). Dashboard. Patient search. Report list/viewer/download. E-referral order form. Critical alerts. Profile. Help. 10 screens.

**Module:** frontend
**Ref:** docs/screens/screen-list-complete.md (Screens #160-169)" \
    "Phase 8: Portals, Analytics & Launch" \
    "frontend,phase-8"

create_issue \
    "LIS-125: Build Patient Portal Angular screens" \
    "OTP login. Dashboard. Report list/viewer/download. Invoice history. Payment (stretch). Profile update. Help. 8 screens.

**Module:** frontend
**Ref:** docs/screens/screen-list-complete.md (Screens #170-177)" \
    "Phase 8: Portals, Analytics & Launch" \
    "frontend,phase-8"

create_issue \
    "LIS-126: Build Home Collection Angular screens" \
    "Request form. Slot booking calendar. Phlebotomist schedule. Route planning. Collection confirmation. Report. Failed collection. 8 screens.

**Module:** frontend
**Ref:** docs/screens/screen-list-complete.md (Screens #178-185)" \
    "Phase 8: Portals, Analytics & Launch" \
    "frontend,phase-8"

create_issue \
    "LIS-127: Build Main Dashboard and Status screens" \
    "Summary cards. Branch overview. Pending actions. TAT compliance charts. Instrument status. QC dashboard. Critical values. Financial dashboard. 8 screens.

**Module:** frontend (Screens #6-13)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 8: Portals, Analytics & Launch" \
    "frontend,phase-8"

create_issue \
    "LIS-128: Build Analytics and Reporting screens" \
    "Test volume, revenue, TAT analysis, QC performance, referral, demographics, instrument utilization, audit trail, custom builder. Charts. CSV/Excel export. 9 screens.

**Module:** frontend (Screens #186-194)
**Ref:** docs/screens/screen-list-complete.md" \
    "Phase 8: Portals, Analytics & Launch" \
    "frontend,phase-8"

create_issue \
    "LIS-129: Implement Performance Testing" \
    "JMeter/k6 load tests. SLAs: patient search p95<200ms, order creation p95<500ms, report gen p95<5s. 50 concurrent users/branch. Query optimization. Redis caching. Pool tuning.

**Module:** all
**Ref:** docs/development/testing-strategy.md" \
    "Phase 8: Portals, Analytics & Launch" \
    "testing,infrastructure,phase-8"

create_issue \
    "LIS-130: Conduct Security Audit" \
    "OWASP Top 10. Penetration testing. JWT security. PHI logging audit. SQL injection. XSS. Security headers. Rate limiting. HIPAA checklist. Dependency scan.

**Module:** all
**Ref:** docs/architecture/security-architecture.md" \
    "Phase 8: Portals, Analytics & Launch" \
    "security,phase-8"

create_issue \
    "LIS-131: Prepare Kubernetes Deployment Configuration" \
    "K8s manifests for all services. Ingress with TLS. HPA. ConfigMaps/Secrets. PVCs. Prometheus + Grafana monitoring. Log aggregation (ELK/Loki).

**Module:** infrastructure
**Ref:** docs/development/deployment-guide.md" \
    "Phase 8: Portals, Analytics & Launch" \
    "infrastructure,phase-8"

create_issue \
    "LIS-132: Complete All Documentation" \
    "OpenAPI for all endpoints. User manuals per role. Admin setup guide. Deployment runbook. Troubleshooting guide. Training materials.

**Module:** all
**Ref:** docs/" \
    "Phase 8: Portals, Analytics & Launch" \
    "documentation,phase-8"

create_issue \
    "LIS-133: Create Flyway migrations for Phase 8 tables" \
    "HomeCollectionRequest, CollectionSlot, PhlebotomistSchedule. AnalyticsCache (materialized views). Custom report config.

**Module:** various
**Ref:** docs/architecture/database-schema.md" \
    "Phase 8: Portals, Analytics & Launch" \
    "backend,database,phase-8"

create_issue \
    "LIS-134: End-to-end integration test: Complete LIS workflow" \
    "Full system test: Register → Order → Pay → Collect → Receive → Instrument results → Auto-validate → Authorize → Report → Deliver → Portal access. Multi-branch. All state machines. SLA performance.

**Module:** all
**Ref:** docs/process-flows/complete-lipid-cbc-walkthrough.md" \
    "Phase 8: Portals, Analytics & Launch" \
    "backend,testing,phase-8"

create_issue \
    "LIS-135: Comprehensive test coverage review and gap filling" \
    "Backend ≥80% (services 90%). Frontend ≥75% (services 85%). 50+ Cypress E2E scenarios. Multi-branch isolation. Security test cases.

**Module:** all
**Ref:** docs/development/testing-strategy.md" \
    "Phase 8: Portals, Analytics & Launch" \
    "testing,phase-8"

fi

echo ""
echo "========================================="
echo " Done! Milestones and issues created."
echo "========================================="
echo ""
echo "Summary:"
echo "  Milestones: 8"
echo "  Issues: 135 (LIS-001 through LIS-135)"
echo ""
echo "View milestones: gh api repos/$REPO/milestones --jq '.[].title'"
echo "View issues: gh issue list --repo $REPO --state open"
