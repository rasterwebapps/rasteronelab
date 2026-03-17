# Phase 1: Foundation (Months 1-2)

> Project setup, authentication, multi-branch infrastructure, and CI/CD pipeline.

## Milestone Goals

- Project setup (Gradle multi-module, Angular, Docker)
- Authentication working end-to-end (Keycloak → Spring Security → Angular)
- Multi-branch infrastructure proven with row-level filtering
- CI/CD pipeline operational

## Key Technical Decisions

- Row-level filtering for branch isolation (branch_id column)
- Java 21, Spring Boot 3.4, Angular 19
- Keycloak 24 for OAuth2/OIDC
- PostgreSQL 17, Redis 7, RabbitMQ 3.13

## Documentation References

- [System Architecture](../architecture/system-architecture.md)
- [Multi-Branch Implementation](../architecture/multi-branch-implementation.md)
- [Security Architecture](../architecture/security-architecture.md)
- [Project Scaffolding](../development/project-scaffolding.md)
- [Coding Standards](../development/coding-standards.md)

---

## Issues

### Backend — Core Module (`lis-core`)

#### LIS-001: Create BaseEntity with UUID v7, audit fields, and soft delete
**Labels:** `backend`, `database`
**Status:** ✅ **DONE**
**Description:**
Create `BaseEntity` as `@MappedSuperclass` with:
- `id` (UUID v7, auto-generated with `@PrePersist`)
- `branchId` (UUID, NOT NULL)
- `createdAt`, `updatedAt` (LocalDateTime, auto-managed)
- `createdBy`, `updatedBy` (String, from SecurityContext)
- `isDeleted` (Boolean, default false), `deletedAt` (LocalDateTime)
- Enable `@EntityListeners(AuditingEntityListener.class)`

**Acceptance Criteria:**
- [x] BaseEntity class in `lis-core/domain/model/`
- [x] UUID v7 generation on persist
- [x] Audit fields auto-populated
- [x] Soft delete support

---

#### LIS-002: Create ApiResponse wrapper and PagedResponse
**Labels:** `backend`
**Status:** ✅ **DONE**
**Description:**
Create standardized API response wrapper:
- `ApiResponse<T>` with `success`, `message`, `data`, `errorCode`, `timestamp`
- Static factory methods: `success(data)`, `success(data, message)`, `error(message, errorCode)`
- `PagedResponse<T>` extending ApiResponse for paginated results
- `@JsonInclude(NON_NULL)` for clean JSON output

**Acceptance Criteria:**
- [x] ApiResponse class with all fields
- [x] PagedResponse with pagination metadata
- [x] Unit tests for serialization

---

#### LIS-003: Create global exception handler and custom exceptions
**Labels:** `backend`
**Status:** ✅ **DONE**
**Description:**
Create `@RestControllerAdvice` global exception handler:
- `NotFoundException` → 404
- `ValidationException` → 400
- `BranchAccessDeniedException` → 403
- `DuplicateResourceException` → 409
- `BusinessRuleException` → 422
- Error codes: LIS-SYS-001 (internal), LIS-SEC-001 (auth), LIS-VAL-001 (validation)
- Log exceptions with correlation ID

**Acceptance Criteria:**
- [x] Exception hierarchy in `lis-core`
- [x] GlobalExceptionHandler mapping all exceptions
- [x] Error codes documented
- [x] Unit tests

---

#### LIS-004: Implement BranchContextHolder and BranchInterceptor
**Labels:** `backend`, `security`
**Status:** ✅ **DONE**
**Description:**
Implement multi-branch context management:
- `BranchContextHolder` using ThreadLocal to store current branch UUID
- `BranchInterceptor` (HandlerInterceptor) to extract `X-Branch-Id` header
- Validate user has access to requested branch (from JWT `branchIds` claim)
- Clear ThreadLocal in `afterCompletion` to prevent leaks
- `requireCurrentBranchId()` method that throws if not set

**Acceptance Criteria:**
- [x] BranchContextHolder with set/get/clear/require methods
- [x] BranchInterceptor registered in WebMvcConfigurer
- [x] JWT claim validation for branch access
- [x] ThreadLocal cleanup verified
- [x] Unit tests with mock JWT

---

#### LIS-005: Create BranchAwareRepository base interface
**Labels:** `backend`, `database`
**Status:** ✅ **DONE**
**Description:**
Create base repository interface that enforces branch-scoped queries:
- `BranchAwareRepository<T extends BaseEntity>` extending JpaRepository
- Default methods: `findByIdAndBranchId()`, `findAllByBranchId()`
- `@Query` annotations with branch_id filtering
- Soft-delete aware: `is_deleted = false` in all queries

**Acceptance Criteria:**
- [x] BranchAwareRepository interface
- [x] Soft-delete filtering
- [x] Integration tests with test database

---

### Backend — Authentication (`lis-auth`)

#### LIS-006: Configure Keycloak realm with roles and custom claims
**Labels:** `backend`, `security`, `infrastructure`
**Status:** ✅ **DONE**
**Description:**
Set up Keycloak 24 realm `rasteronelab` with:
- 10 roles: SUPER_ADMIN, ORG_ADMIN, BRANCH_ADMIN, PATHOLOGIST, LAB_TECHNICIAN, RECEPTIONIST, PHLEBOTOMIST, BILLING_STAFF, DOCTOR, PATIENT
- Custom JWT claims: `organizationId` (UUID), `branchIds` (UUID[]), `employeeId` (String)
- Protocol mapper for custom claims
- Client: `lis-frontend` (public, PKCE), `lis-backend` (confidential)
- Realm import JSON for reproducible setup

**Acceptance Criteria:**
- [x] Keycloak realm JSON export
- [x] All 10 roles created
- [x] Custom claims in JWT tokens
- [x] Realm import in Docker Compose
- [x] Test users for each role

---

#### LIS-007: Implement Spring Security OAuth2 Resource Server
**Labels:** `backend`, `security`
**Status:** ✅ **DONE**
**Description:**
Configure Spring Security for all backend modules:
- OAuth2 Resource Server with JWT validation
- Custom `JwtAuthenticationConverter` to extract roles + custom claims
- `@PreAuthorize` support enabled
- CORS configuration for Angular dev server
- Security filter chain with endpoint-level rules
- Method-level security with `@PreAuthorize("hasRole('...')")`

**Acceptance Criteria:**
- [x] SecurityConfig class in lis-auth
- [x] JWT validation working with Keycloak
- [x] Role extraction from JWT
- [x] CORS configuration
- [x] Integration tests with mock JWT

---

### Backend — API Gateway (`lis-gateway`)

#### LIS-008: Set up Spring Cloud Gateway with routing and JWT validation
**Labels:** `backend`, `infrastructure`
**Status:** ✅ **DONE**
**Description:**
Configure API Gateway:
- Route definitions for all backend modules (8081-8093)
- JWT token validation at gateway level
- Rate limiting (100 req/s general, 5 req/min login)
- Request/response logging
- Health check aggregation
- Circuit breaker for downstream services

**Acceptance Criteria:**
- [x] Gateway routes for all modules
- [x] JWT validation filter
- [x] Rate limiting configuration
- [x] Health check endpoint
- [x] Integration tests

---

### Frontend — Angular Setup

#### LIS-009: Scaffold Angular 19 application with authentication flow
**Labels:** `frontend`
**Status:** ✅ **DONE**
**Description:**
Set up Angular 19 SPA with:
- Standalone components architecture
- Angular Material 19 + Tailwind CSS 4
- OAuth2/OIDC integration with Keycloak (angular-oauth2-oidc or similar)
- Login, logout, token refresh flow
- Auth guard for protected routes
- HTTP interceptor for JWT token injection

**Acceptance Criteria:**
- [x] Angular 19 project with standalone components
- [x] Material + Tailwind configured
- [x] Login/logout flow working
- [x] Auth guard protecting routes
- [x] Token refresh mechanism

---

#### LIS-010: Implement Angular BranchInterceptor and BranchService
**Labels:** `frontend`
**Status:** ✅ **DONE**
**Description:**
Create branch context management in Angular:
- `BranchService` to hold current branch (signal-based)
- `BranchInterceptor` (HttpInterceptor) to inject `X-Branch-Id` header
- Branch selector component in top navigation
- Persist selected branch in localStorage
- Branch switch triggers data reload

**Acceptance Criteria:**
- [x] BranchService with signal-based state
- [x] HTTP interceptor injecting branch header
- [x] Branch selector UI component
- [x] Branch persistence across sessions
- [x] Unit tests

---

#### LIS-011: Create shared Angular layout and navigation components
**Labels:** `frontend`
**Status:** ✅ **DONE**
**Description:**
Build application shell:
- Main layout with sidebar navigation + top bar
- Responsive sidebar (collapsible)
- User profile menu (role display, logout)
- Branch selector in header
- Breadcrumb component
- Loading spinner/skeleton screens
- Toast notification service

**Acceptance Criteria:**
- [x] Sidebar navigation component
- [x] Top bar with user profile
- [x] Responsive layout
- [x] Breadcrumb component
- [x] Notification toast service

---

### Infrastructure

#### LIS-012: Create Docker Compose for all services
**Labels:** `infrastructure`
**Status:** ✅ **DONE**
**Description:**
Docker Compose configuration for local development:
- PostgreSQL 17 (port 5432)
- Redis 7 (port 6379)
- RabbitMQ 3.13 (ports 5672/15672)
- Keycloak 24 (port 8180) with realm import
- Elasticsearch 8 (port 9200)
- MinIO (ports 9000/9001)
- Volume mounts for data persistence
- Network configuration
- Health checks for all services

**Acceptance Criteria:**
- [x] docker-compose.yml with all services
- [x] Health checks for startup ordering
- [x] Volume configuration
- [x] `.env` template for configuration
- [x] Documentation for first-time setup

---

#### LIS-013: Set up CI/CD pipeline with Jenkins
**Labels:** `infrastructure`
**Status:** ✅ **DONE**
**Description:**
Jenkins pipeline configuration:
- Multibranch pipeline
- Build stages: checkout → build → test → code quality → Docker image → deploy
- Gradle build with caching
- Test result publishing
- JaCoCo coverage reporting
- Docker image build and push
- Deployment to staging/production

**Acceptance Criteria:**
- [x] Jenkinsfile with all stages
- [x] Gradle build cache configuration
- [x] Test result reporting
- [x] Coverage threshold enforcement (80%)
- [x] Docker build stage

---

#### LIS-014: Create Dockerfiles for backend and frontend
**Labels:** `infrastructure`
**Status:** ✅ **DONE**
**Description:**
Multi-stage Dockerfiles:
- Backend: Gradle build → JRE 21 slim runtime
- Frontend: Node build → Nginx static serving
- Security: non-root user, minimal base images
- Health check endpoints
- Environment variable configuration

**Acceptance Criteria:**
- [x] Backend Dockerfile (multi-stage)
- [x] Frontend Dockerfile (multi-stage)
- [x] .dockerignore files
- [x] Images build and run successfully

---

### Database

#### LIS-015: Set up Flyway migration framework and core tables
**Labels:** `backend`, `database`
**Status:** ✅ **DONE**
**Description:**
Initialize database migration framework:
- Flyway versioned SQL migrations per module
- Naming convention: `V{YYYYMMDD_HHmm}__{description}.sql`
- Core tables: `branch`, `organization`, `audit_trail`
- Standard column pattern on all tables
- Index naming convention: `idx_{table}_{column}`
- Partition setup for audit_trail (monthly by changed_at)

**Acceptance Criteria:**
- [x] Flyway configuration in each module
- [x] Core tables migration scripts
- [x] Audit trail table with partitioning
- [x] Migration runs cleanly on fresh database
- [x] Rollback scripts included

---

## Completion Criteria

- [x] All 14 backend modules compile and start
- [x] Keycloak authentication flow works end-to-end (login → JWT → API call)
- [x] BranchContextHolder correctly filters data by branch
- [x] Angular app authenticates and displays dashboard shell
- [x] Docker Compose starts all services with `docker-compose up`
- [x] CI pipeline builds, tests, and reports coverage
- [x] At least 80% test coverage on lis-core module

---

## Phase 1 Progress Summary

| Issue | Title | Status |
|-------|-------|--------|
| LIS-001 | BaseEntity with UUID v7 | ✅ Done |
| LIS-002 | ApiResponse and PagedResponse | ✅ Done |
| LIS-003 | Global exception handler | ✅ Done |
| LIS-004 | BranchContextHolder and BranchInterceptor | ✅ Done |
| LIS-005 | BranchAwareRepository | ✅ Done |
| LIS-006 | Keycloak realm configuration | ✅ Done |
| LIS-007 | Spring Security OAuth2 Resource Server | ✅ Done |
| LIS-008 | Spring Cloud Gateway | ✅ Done |
| LIS-009 | Angular 19 application scaffold | ✅ Done |
| LIS-010 | Angular BranchInterceptor and BranchService | ✅ Done |
| LIS-011 | Shared Angular layout | ✅ Done |
| LIS-012 | Docker Compose | ✅ Done |
| LIS-013 | Jenkins CI/CD pipeline | ✅ Done |
| LIS-014 | Dockerfiles | ✅ Done |
| LIS-015 | Flyway migrations | ✅ Done |

**Phase 1 Overall: ✅ 100% complete (15/15 issues done)**
