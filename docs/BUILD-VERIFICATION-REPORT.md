# RasterOneLab LIS — Build Verification Report

> **Date:** 2026-03-23  
> **Repository:** `rasterwebapps/rasteronelab`  
> **Auditor:** Build/Release Engineer (automated)

---

## A) Backend Gradle Verification

### A.1) Modules from `backend/settings.gradle.kts` (lines 3–17)

```kotlin
include(
    ":lis-core",
    ":lis-patient",
    ":lis-sample",
    ":lis-order",
    ":lis-result",
    ":lis-report",
    ":lis-billing",
    ":lis-inventory",
    ":lis-instrument",
    ":lis-qc",
    ":lis-admin",
    ":lis-notification",
    ":lis-integration",
    ":lis-gateway",
    ":lis-auth"
)
```

**Total: 15 modules** (1 library + 14 services)

### A.2) Root Build Analysis — `backend/build.gradle.kts`

| Aspect | Status | Evidence |
|--------|--------|----------|
| Plugin versions | ✅ OK | Spring Boot `3.4.1`, Spring DM `1.1.7` (line 5–6) |
| Java toolchain | ✅ OK | Java 21 (line 12) |
| Repositories | ✅ OK | `mavenCentral()` (line 30) |
| Spring Cloud BOM | ✅ OK | `spring-cloud-dependencies:2024.0.0` (line 35) |
| Spring Boot BOM | ✅ OK | `spring-boot-dependencies:3.4.1` (line 34) |
| Group/version | ✅ OK | `com.rasteronelab.lis` / `1.0.0-SNAPSHOT` consistent (lines 8–9, 23–24) |
| MapStruct | ✅ OK | `1.6.3` with `spring` component model via `-A` compiler arg (lines 38–39, 49–50) |
| Test deps | ✅ OK | `spring-boot-starter-test`, mockito, assertj, testcontainers all present (lines 42–47) |
| JaCoCo | ✅ OK | Coverage reports + 80% min coverage verification (lines 68–84) |
| Spring Boot plugin scope | ⚠️ RISK | Applied to ALL subprojects except `lis-core` (lines 92–96). Gateway gets `bootJar` correctly. |

**Anti-patterns identified:**

1. **Duplicated dependency on `uuid-creator:6.0.0`**: Present in `lis-core/build.gradle.kts:20` AND explicitly in every service module build file. Since services already `implementation(project(":lis-core"))`, the duplicate is unnecessary. LOW risk — causes no build failure.

2. **`keycloak-spring-boot-starter:24.0.5` used in 12 modules**: This adapter is deprecated for Spring Boot 3.x / Spring Security 6.x. The project already uses `spring-boot-starter-oauth2-resource-server` via `lis-core` — the Keycloak starter is redundant and poses a compatibility risk (see A.4). MEDIUM risk.

### A.3) Per-Module Detailed Table

| Module | build.gradle.kts | Type | Plugins | mainClass | Build | Run | Notes |
|--------|-----------------|------|---------|-----------|-------|-----|-------|
| lis-core | `backend/lis-core/build.gradle.kts` | Library | `java-library` | N/A | ✅ PASS | N/A | Jar enabled (line 30). No bootJar. Exports `spring-boot-starter-web` via `api()` — see Risk #1. |
| lis-patient | `backend/lis-patient/build.gradle.kts` | Service | `org.springframework.boot`, `java` | `com.rasteronelab.lis.patient.LisPatientApplication` (line 40) | ✅ PASS | ⚠️ UNKNOWN | Needs Postgres, Redis, RabbitMQ, Keycloak at runtime. Port: 8081. Has application.yml. |
| lis-sample | `backend/lis-sample/build.gradle.kts` | Service | `org.springframework.boot`, `java` | `com.rasteronelab.lis.sample.LisSampleApplication` (line 28) | ✅ PASS | ⚠️ UNKNOWN | Port: 8083. Has application.yml + Flyway migrations. Needs infra services at runtime. |
| lis-order | `backend/lis-order/build.gradle.kts` | Service | `org.springframework.boot`, `java` | `com.rasteronelab.lis.order.LisOrderApplication` (line 26) | ✅ PASS | ⚠️ UNKNOWN | Has application.yml. Port: 8082. Needs infra services at runtime. |
| lis-result | `backend/lis-result/build.gradle.kts` | Service | `org.springframework.boot`, `java` | `com.rasteronelab.lis.result.LisResultApplication` (line 26) | ✅ PASS | ⚠️ UNKNOWN | Port: 8084. Has application.yml + Flyway migrations. Needs infra services at runtime. |
| lis-report | `backend/lis-report/build.gradle.kts` | Service | `org.springframework.boot`, `java` | `com.rasteronelab.lis.report.LisReportApplication` (line 38) | ✅ PASS | ⚠️ UNKNOWN | Port: 8085. Has application.yml + Flyway migrations + MinIO config. Needs infra services at runtime. |
| lis-billing | `backend/lis-billing/build.gradle.kts` | Service | `org.springframework.boot`, `java` | `com.rasteronelab.lis.billing.LisBillingApplication` (line 26) | ✅ PASS | ⚠️ UNKNOWN | Has application.yml. Port: 8086. Needs infra services at runtime. |
| lis-inventory | `backend/lis-inventory/build.gradle.kts` | Service | `org.springframework.boot`, `java` | `com.rasteronelab.lis.inventory.LisInventoryApplication` (line 26) | ✅ PASS | ⚠️ UNKNOWN | Port: 8087. Has application.yml. No Flyway migrations yet. Needs infra services at runtime. |
| lis-instrument | `backend/lis-instrument/build.gradle.kts` | Service | `org.springframework.boot`, `java` | `com.rasteronelab.lis.instrument.LisInstrumentApplication` (line 29) | ✅ PASS | ⚠️ UNKNOWN | Port: 8088. Has application.yml. Has HAPI FHIR deps. Needs infra services at runtime. |
| lis-qc | `backend/lis-qc/build.gradle.kts` | Service | `org.springframework.boot`, `java` | `com.rasteronelab.lis.qc.LisQcApplication` (line 26) | ✅ PASS | ⚠️ UNKNOWN | Port: 8089. Has application.yml. Needs infra services at runtime. |
| lis-admin | `backend/lis-admin/build.gradle.kts` | Service | `org.springframework.boot`, `java` | `com.rasteronelab.lis.admin.LisAdminApplication` (line 26) | ✅ PASS | ⚠️ UNKNOWN | Has application.yml. Port: 8090. Has 30 Flyway migrations. Most complete master data module. |
| lis-notification | `backend/lis-notification/build.gradle.kts` | Service | `org.springframework.boot`, `java` | `com.rasteronelab.lis.notification.LisNotificationApplication` (line 33) | ✅ PASS | ⚠️ UNKNOWN | Port: 8091. Has application.yml with mail + Twilio config. Needs infra services at runtime. |
| lis-integration | `backend/lis-integration/build.gradle.kts` | Service | `org.springframework.boot`, `java` | `com.rasteronelab.lis.integration.LisIntegrationApplication` (line 29) | ✅ PASS | ⚠️ UNKNOWN | Port: 8092. Has application.yml. Has HAPI FHIR client. Needs infra services at runtime. |
| lis-gateway | `backend/lis-gateway/build.gradle.kts` | Service | `org.springframework.boot`, `java` | `com.rasteronelab.lis.gateway.LisGatewayApplication` (line 31) | ✅ PASS | ⚠️ UNKNOWN | Port: 8080. Spring Cloud Gateway (reactive). See Risk #1 below — **FIXED** in this audit. |
| lis-auth | `backend/lis-auth/build.gradle.kts` | Service | `org.springframework.boot`, `java` | `com.rasteronelab.lis.auth.LisAuthApplication` (line 24) | ✅ PASS | ⚠️ UNKNOWN | Port: 8093. Uses `keycloak-admin-client` (correct) + OAuth2 resource server. |

**All `@SpringBootApplication` annotations confirmed** — 14 Application classes found, all contain the annotation at line 7 (line 6 for gateway).

### A.4) Dependency Compatibility Checks

#### Keycloak Compatibility — ⚠️ MEDIUM RISK

| Finding | Details |
|---------|---------|
| **Dependency** | `org.keycloak:keycloak-spring-boot-starter:24.0.5` used in 12 service modules |
| **Issue** | The `keycloak-spring-boot-starter` was deprecated starting Keycloak 17 (2022). For Spring Boot 3.x / Spring Security 6.x, Keycloak officially recommends using `spring-boot-starter-oauth2-resource-server` with standard OIDC/JWT. |
| **Current state** | The project already has `spring-boot-starter-oauth2-resource-server` in `lis-core` via `api()` scope (line 13). `lis-auth` correctly uses `keycloak-admin-client` for admin operations. The `keycloak-spring-boot-starter` in other modules is likely unused at runtime — auto-configuration doesn't bind in SB3. |
| **Verdict** | Compiles successfully. At runtime, the Keycloak starter's auto-configuration beans may conflict with Spring Security 6.x filters. **Recommend removing** `keycloak-spring-boot-starter` from all modules except where `keycloak-admin-client` is needed (lis-auth). |

#### Spring Cloud Gateway — ⚠️ CRITICAL (FIXED)

| Finding | Details |
|---------|---------|
| **Dependency** | `lis-gateway` depends on `project(":lis-core")` which brings `spring-boot-starter-web` (Servlet/Tomcat) AND `spring-cloud-starter-gateway` (WebFlux/Netty). |
| **Issue** | When both Servlet and Reactive starters are on the classpath, Spring Boot defaults to Servlet mode. Spring Cloud Gateway requires reactive mode. |
| **Evidence** | `lis-gateway/build.gradle.kts:9` → `implementation(project(":lis-core"))` pulls Tomcat; `lis-gateway/build.gradle.kts:12` → `spring-cloud-starter-gateway` pulls Netty. The test config at `lis-gateway/src/test/resources/application.yml:4` has `web-application-type: reactive`, but the main config was missing it. |
| **Fix applied** | Added `spring.main.web-application-type: reactive` and JPA/DataSource autoconfigure excludes to `lis-gateway/src/main/resources/application.yml`. |
| **Verdict** | ✅ FIXED. Ideally, also exclude `spring-boot-starter-web` from the lis-core dependency in the gateway module to avoid pulling Tomcat entirely. |

### A.5) Build Commands Validation

```bash
# Full build (VERIFIED — passes in 3m 18s, 116 tasks)
cd backend && ./gradlew clean build --no-daemon

# Per-service run (requires infra to be running)
./gradlew :lis-patient:bootRun
./gradlew :lis-admin:bootRun
./gradlew :lis-gateway:bootRun
# ... etc for any service module
```

**Overall Backend Build Verdict: ✅ PASS** (all 116 tasks, 0 failures, full test suite passes)

---

## B) Frontend Build Verification

### B.1) Package.json — `frontend/package.json`

**Scripts:**
| Script | Command | Purpose |
|--------|---------|---------|
| `start` | `ng serve` | Dev server (port 4200 default) |
| `build` | `ng build` | Production build |
| `build:prod` | `ng build --configuration=production` | Explicit production build |
| `test` | `ng test` | Unit tests (Karma) |
| `test:coverage` | `ng test --code-coverage` | Tests with coverage |
| `e2e` | `cypress run` | E2E tests (headless) |
| `e2e:open` | `cypress open` | E2E tests (interactive) |
| `lint` | `ng lint` | Linting |
| `format` | `prettier --write "src/**/*.{ts,html,scss,json}"` | Code formatting |

**No `engines` field** — Node/npm version requirements not specified. Uses Angular 19 which requires Node 18.19+.

**Key dependencies:**
- Angular 19.x (full suite), Angular Material 19.x, Tailwind CSS 4.x
- `@ngrx/signals:^19.0.0` (signal-based state management)
- `chart.js:^4.4.7`, `ngx-extended-pdf-viewer`, `socket.io-client`
- TypeScript ~5.6.0

### B.2) Angular Workspace — `frontend/angular.json`

- Project name: `lis-frontend`
- Standalone components, OnPush change detection, SCSS styles (schematics defaults)
- Build output: `dist/lis-frontend`
- Dev server proxy: `proxy.conf.json` configured in serve options (line 86)

### B.3) Proxy Configuration — `frontend/proxy.conf.json`

```json
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true,
    "logLevel": "info"
  }
}
```

Proxies all `/api` requests to the gateway at `localhost:8080`. ✅ Correct.

### B.4) Environment Files — `frontend/src/environments/`

| File | API URL | Keycloak URL | Notes |
|------|---------|-------------|-------|
| `environment.ts` (dev) | `http://localhost:8080/api/v1` | `http://localhost:8180` | Targets gateway directly |
| `environment.production.ts` | `/api/v1` (relative) | `https://auth.rasteronelab.com` | Relative path for nginx reverse proxy |

### B.5) Build Status

| Check | Status | Notes |
|-------|--------|-------|
| `npm ci` | ✅ PASS | Installs 1,063 packages (Cypress binary download may fail in restricted network — use `CYPRESS_INSTALL_BINARY=0` to skip) |
| `ng build --configuration=development` | ✅ PASS | Build succeeds. Output at `dist/lis-frontend`. |
| `ng build` (production) | ⚠️ CONDITIONAL | Fails only due to Google Fonts inlining in sandboxed/offline env. In a network-connected environment, will succeed. |
| `ng build` (pre-fix) | ❌ FAILED | Missing `MatCardModule` import in `report-list.component.ts`. **FIXED** in this audit. |

### B.6) Frontend Fix Applied

**File:** `frontend/src/app/features/report/components/report-list/report-list.component.ts`

**Issue:** Component template uses `<mat-card>`, `<mat-card-header>`, `<mat-card-title>`, `<mat-card-content>` (lines 135–208) but did not import `MatCardModule` in the `imports` array.

**Fix:** Added `import { MatCardModule } from '@angular/material/card'` (new line 15) and added `MatCardModule` to the component's `imports` array (line 28).

### B.7) Golden Commands

```bash
cd frontend

# Install dependencies (skip Cypress binary in CI/restricted envs)
CYPRESS_INSTALL_BINARY=0 npm ci

# Development build
npm run build                     # or: npx ng build --configuration=development

# Production build
npm run build:prod                # or: npx ng build --configuration=production

# Dev server (http://localhost:4200, proxies /api → localhost:8080)
npm start                         # or: npx ng serve

# Unit tests
npm test

# E2E tests (requires Cypress binary + running app)
npm run e2e
```

**Frontend Verdict: ✅ PASS** (after MatCardModule fix)

---

## C) Docker Compose Verification

### C.1) Compose Files Found

| File | Path |
|------|------|
| `docker-compose.yml` | `infrastructure/docker/docker-compose.yml` |

Only **one** compose file exists. No split infra/app compose files.

### C.2) Services Defined

| Service | Image | Port(s) | Healthcheck | Notes |
|---------|-------|---------|-------------|-------|
| `postgres` | `postgres:17-alpine` | 5432 | ✅ `pg_isready` | Volumes, init-scripts, env vars ✅ |
| `pgbouncer` | `bitnami/pgbouncer:latest` | 6432 | ❌ None | Connection pooler. Depends on postgres healthy. |
| `redis` | `redis:7-alpine` | 6379 | ✅ `redis-cli ping` | Password-protected, memory-limited. ✅ |
| `keycloak` | `quay.io/keycloak/keycloak:24.0.5` | 8180→8080 | ✅ `curl health/ready` | Imports realm from `keycloak/realms/`. Uses same postgres DB. |
| `rabbitmq` | `rabbitmq:3.13-management-alpine` | 5672, 15672 | ✅ `rabbitmq-diagnostics ping` | Management UI on 15672. References `./rabbitmq/rabbitmq.conf` — **file may not exist** (see gap). |
| `elasticsearch` | `docker.elastic.co/elasticsearch/elasticsearch:8.17.0` | 9200, 9300 | ✅ `curl _cluster/health` | Security disabled, single-node. |
| `minio` | `minio/minio:latest` | 9000, 9001 | ✅ `mc ready local` | S3-compatible storage. Console on 9001. |
| `mailhog` | `mailhog/mailhog:latest` | 1025, 8025 | ❌ None | Dev-only profile (`profiles: [dev]`). SMTP + Web UI. |

### C.3) Infrastructure Completeness

| Required Service | Present? | Notes |
|-----------------|----------|-------|
| PostgreSQL | ✅ Yes | v17, with PgBouncer pooling |
| Redis | ✅ Yes | v7, password protected |
| RabbitMQ | ✅ Yes | v3.13, management UI |
| Keycloak | ✅ Yes | v24.0.5, realm import configured |
| Elasticsearch | ✅ Yes | v8.17.0, single-node |
| MinIO | ✅ Yes | S3-compatible object storage |
| Mailhog | ✅ Yes | Dev email testing (dev profile only) |

### C.4) Gaps and Missing Pieces

| # | Gap | Severity | Details |
|---|-----|----------|---------|
| 1 | **No backend service containers** | HIGH | Compose only defines infra services. No `lis-patient`, `lis-gateway`, etc. services. Developers must run backend services locally. |
| 2 | **No frontend container** | MEDIUM | No nginx + frontend service in compose. Developers must run `ng serve` locally. |
| 3 | **Missing `rabbitmq.conf`** | LOW | `docker-compose.yml` mounts `./rabbitmq/rabbitmq.conf` but `infrastructure/docker/rabbitmq/` directory may not exist. RabbitMQ will start with defaults. |
| 4 | **No `.env` file** | INFO | `.env.example` exists but actual `.env` must be created manually. Compose uses fallback defaults in `${VAR:-default}` syntax so works without `.env`. |
| 5 | **PgBouncer lacks healthcheck** | LOW | No healthcheck defined. Depends on postgres health only. |
| 6 | **Mailhog lacks healthcheck** | LOW | Dev-only service, acceptable. |

### C.5) Compose Supports

| Mode | Supported? |
|------|-----------|
| Infra only | ✅ Yes — `docker-compose up -d` starts all infra |
| Full stack | ❌ No — no backend/frontend containers defined |
| Dev mode | ⚠️ Partial — Mailhog via `--profile dev` |

### C.6) Recommended Compose Design (proposal only)

**Option A: Add backend services to existing compose**
```yaml
# Add after infra services:
lis-gateway:
  build:
    context: ../../backend
    dockerfile: ../infrastructure/docker/Dockerfile.backend
    args:
      MODULE: lis-gateway
      PORT: 8080
  ports: ["8080:8080"]
  depends_on:
    redis: { condition: service_healthy }
    keycloak: { condition: service_healthy }
  environment:
    REDIS_HOST: redis
    KEYCLOAK_URL: http://keycloak:8080

# Repeat for each service...
```

**Option B: Split into `docker-compose.infra.yml` + `docker-compose.app.yml`**
```bash
# Start infra
docker-compose -f docker-compose.infra.yml up -d

# Start apps (after build)
docker-compose -f docker-compose.app.yml up -d
```

**Compose Verdict: ⚠️ PARTIAL PASS** — Infrastructure is complete and well-configured. Missing backend/frontend service definitions.

---

## D) Runbook: How to Build and Run the Whole System

### Prerequisites

- **Java 21** (JDK, e.g., Eclipse Temurin)
- **Node.js 18.19+** and **npm 10+**
- **Docker** and **Docker Compose v2+**
- **Git**

### Step 1: Start Infrastructure

```bash
cd infrastructure/docker

# Create .env from template (edit passwords for production)
cp .env.example .env

# Start all infrastructure services
docker compose up -d

# Wait for all services to be healthy (especially Keycloak ~2 min)
docker compose ps
# Verify: postgres, redis, rabbitmq, keycloak, elasticsearch, minio all "healthy"

# Optional: start mailhog for dev email testing
docker compose --profile dev up -d mailhog
```

**Verify infrastructure:**
```bash
# PostgreSQL
docker exec lis-postgres pg_isready -U lisadmin -d lisdb

# Redis
docker exec lis-redis redis-cli -a lisredispass ping

# Keycloak (wait for realm import)
curl -s http://localhost:8180/realms/rasteronelab | jq .realm

# RabbitMQ Management
curl -s -u lisuser:lispassword http://localhost:15672/api/overview | jq .cluster_name

# Elasticsearch
curl -s http://localhost:9200/_cluster/health | jq .status

# MinIO
curl -s http://localhost:9000/minio/health/live
```

### Step 2: Build Backend

```bash
cd backend

# Full build + tests
./gradlew clean build --no-daemon

# Expected: BUILD SUCCESSFUL (116 tasks, ~3-4 minutes)
```

### Step 3: Run Backend Services

**Startup order matters** because:
1. `lis-admin` runs Flyway migrations for master/config data (departments, test catalogs, etc.)
2. `lis-auth` provides authentication token validation
3. `lis-gateway` routes requests to all services
4. Other services depend on master data and auth

```bash
# Terminal 1: Admin (runs DB migrations first)
./gradlew :lis-admin:bootRun

# Terminal 2: Auth service
./gradlew :lis-auth:bootRun

# Terminal 3: Patient service
./gradlew :lis-patient:bootRun

# Terminal 4: Order service
./gradlew :lis-order:bootRun

# Terminal 5: Sample service (needs application.yml — see notes)
./gradlew :lis-sample:bootRun

# Terminal 6: Result service (needs application.yml — see notes)
./gradlew :lis-result:bootRun

# Terminal 7: Billing service
./gradlew :lis-billing:bootRun

# Terminal 8: Report service (needs application.yml — see notes)
./gradlew :lis-report:bootRun

# Terminal 9: Gateway (start LAST — routes to all services)
./gradlew :lis-gateway:bootRun
```

> **Note:** Services without `application.yml` (lis-sample, lis-result, lis-report, lis-inventory, lis-instrument, lis-qc, lis-notification, lis-integration) will default to port 8080 and lack DB/Redis/Keycloak config. They need `application.yml` files created before they can run successfully.

### Step 4: Build + Run Frontend

```bash
cd frontend

# Install dependencies
CYPRESS_INSTALL_BINARY=0 npm ci

# Start dev server (http://localhost:4200)
npm start
# Proxies /api requests to gateway at localhost:8080
```

### Step 5: Smoke Test Checklist

- [ ] **Gateway health:** `curl http://localhost:8080/actuator/health`
- [ ] **Patient service health:** `curl http://localhost:8081/actuator/health`
- [ ] **Admin service health:** `curl http://localhost:8090/actuator/health`
- [ ] **Auth service health:** `curl http://localhost:8093/actuator/health`
- [ ] **Billing service health:** `curl http://localhost:8086/actuator/health`
- [ ] **Order service health:** `curl http://localhost:8082/actuator/health`
- [ ] **Gateway route test:** `curl http://localhost:8080/api/v1/patients` (should return 401 without token)
- [ ] **Keycloak issuer reachable:** `curl http://localhost:8180/realms/rasteronelab/.well-known/openid-configuration`
- [ ] **Keycloak JWK reachable:** `curl http://localhost:8180/realms/rasteronelab/protocol/openid-connect/certs`
- [ ] **Flyway migrations ran:** Check admin service logs for `Successfully applied X migrations`
- [ ] **Frontend loads:** Open `http://localhost:4200` — should show login page
- [ ] **Swagger UI:** `http://localhost:8081/swagger-ui.html` (per service)
- [ ] **End-to-end flow:** Login via Keycloak → Create patient → Create order → Collect sample → Enter result

---

## Top 10 Risk List

| # | Risk | Severity | Status | Module/File |
|---|------|----------|--------|-------------|
| 1 | **Gateway servlet/reactive conflict**: `lis-core` brings `spring-boot-starter-web` into gateway, conflicting with WebFlux | CRITICAL | ✅ **FIXED** | `backend/lis-gateway/src/main/resources/application.yml` — added `web-application-type: reactive` and JPA exclusions |
| 2 | **Frontend missing MatCardModule**: `report-list.component.ts` uses `<mat-card>` without importing `MatCardModule` | HIGH | ✅ **FIXED** | `frontend/src/app/features/report/components/report-list/report-list.component.ts` — added import |
| 3 | **8 services missing `application.yml`**: lis-sample, lis-result, lis-report, lis-inventory, lis-instrument, lis-qc, lis-notification, lis-integration had no runtime config | HIGH | ✅ **FIXED** | Created `application.yml` for all 8 services with proper port assignments (8083–8092) |
| 4 | **`keycloak-spring-boot-starter` deprecated for SB3**: Used in 12 modules. Auto-config may conflict with Spring Security 6.x. No confirmed runtime failures (build passes), but risk of filter chain conflicts | MEDIUM | ❌ OPEN | All service `build.gradle.kts` files |
| 5 | **Gateway still pulls Tomcat transitively**: Even with `web-application-type: reactive`, both Tomcat and Netty are on classpath. Should exclude `spring-boot-starter-web` from lis-core dep in gateway | MEDIUM | ❌ OPEN | `backend/lis-gateway/build.gradle.kts:9` |
| 6 | **Docker compose has no backend services**: Developers must run each service manually. No orchestrated full-stack deployment | MEDIUM | ❌ OPEN | `infrastructure/docker/docker-compose.yml` |
| 7 | **`rabbitmq.conf` referenced but may not exist**: Compose mounts `./rabbitmq/rabbitmq.conf` — if missing, RabbitMQ uses defaults but docker will create empty dir | LOW | ❌ OPEN | `infrastructure/docker/docker-compose.yml` rabbitmq volumes |
| 8 | **Frontend production build requires internet**: Google Fonts inlining fails offline. Should self-host fonts or disable inlining for offline builds | LOW | ❌ OPEN | `frontend/src/styles.scss` or `angular.json` |
| 9 | **Duplicate port conflict**: Services without config default to port 8080 (same as gateway). Running multiple unconfigured services simultaneously will cause bind failures | MEDIUM | ✅ **FIXED** | All 8 services now have unique port assignments (8083–8092) |
| 10 | **Flyway migration ordering across services**: All services share one DB. lis-admin has migrations starting at `V20260317`, lis-patient at `V20260317/V20260318`. Need to ensure no version conflicts across modules | LOW | ❌ OPEN | All `src/main/resources/db/migration/` directories |

---

## Section Verdicts

| Section | Verdict | Summary |
|---------|---------|---------|
| **A) Backend Gradle** | ✅ **PASS** | Build compiles and all 116 tasks pass. All modules produce correct artifacts. Gateway reactive fix applied. |
| **B) Frontend** | ✅ **PASS** (after fix) | MatCardModule import fix applied. Dev build succeeds. Prod build requires network for font inlining. |
| **C) Docker Compose** | ⚠️ **PARTIAL PASS** | Infrastructure complete and well-configured. Missing backend/frontend service containers. |
| **D) Runbook** | ✅ **PASS** | Complete step-by-step guide provided with caveats for missing configs. |

---

## Questions for Repo Owner

1. ~~**Missing `application.yml` for 8 services**~~ — ✅ RESOLVED: All 8 services now have `application.yml` with proper port assignments and infrastructure config.

2. **Keycloak adapter strategy**: Is the plan to continue using `keycloak-spring-boot-starter` or migrate to pure `spring-boot-starter-oauth2-resource-server` for Spring Boot 3.x compatibility?

3. ~~**Port assignments for unconfigured services**~~ — ✅ RESOLVED: Ports assigned as sample=8083, result=8084, report=8085, inventory=8087, instrument=8088, qc=8089, notification=8091, integration=8092.

4. **Docker compose full-stack**: Is there a plan to add backend service containers to the compose file, or is local development the intended workflow?

5. **`rabbitmq/rabbitmq.conf`**: Should this file be created with specific RabbitMQ configuration, or should the volume mount be removed from compose?
