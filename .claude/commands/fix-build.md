# Fix Build Failure — /fix-build

Diagnose and fix build, test, and deployment failures.

## Usage
```
/fix-build {type}
```

Types: `gradle`, `angular`, `docker`, `liquibase`, `test`, `checkstyle`

## Common Issues & Fixes

### Gradle Build Failures

#### Compilation Error: "Cannot find symbol"
```bash
# 1. Check imports
# 2. Verify Gradle module dependencies in build.gradle.kts
# 3. Run clean build
./gradlew clean :lis-patient:build

# If MapStruct issue:
./gradlew :lis-patient:compileJava  # Check annotation processor output
```

#### Dependency Version Conflict
```bash
./gradlew :lis-patient:dependencies | grep -A5 "CONFLICT"
# Check BOM in root build.gradle.kts
# Use dependencyInsight to trace:
./gradlew :lis-patient:dependencyInsight --dependency spring-boot-starter
```

### Test Failures

#### Testcontainers Docker Not Found
```bash
# Ensure Docker is running
docker ps
# Set Testcontainers docker host
export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=/var/run/docker.sock
```

#### Multi-Branch Test Isolation Failure
```java
// Ensure tearDown clears BranchContextHolder
@AfterEach
void tearDown() {
    BranchContextHolder.clear(); // CRITICAL - prevents thread contamination
}
```

### Angular Build Failures

#### TypeScript Compilation Error
```bash
cd frontend
npx tsc --noEmit  # Check types without building
ng build 2>&1 | head -50  # Detailed errors
```

#### NgModule in Standalone Component Error
```
Error: 'XComponent' is declared in a NgModule but is used in a standalone component
```
**Fix**: Remove NgModule imports, ensure component is standalone, use importProvidersFrom in app.config.ts

### Liquibase Failures

#### Checksum Mismatch
```sql
-- NEVER edit existing changesets!
-- Instead, add a new changeset to fix the issue
-- If absolutely necessary in dev only:
UPDATE databasechangelog SET md5sum = NULL WHERE id = 'changeset-id';
```

#### Lock Stuck
```sql
DELETE FROM databasechangeloglock;
UPDATE databasechangeloglock SET locked = false, lockgranted = null, lockedby = null;
```

### Docker Issues

#### Container Health Check Failing
```bash
# Check logs
docker-compose logs lis-patient

# Check health endpoint manually
docker exec lis-patient wget -q --spider http://localhost:8081/actuator/health

# Common fix: increase start-period for slow-starting Java apps
```

#### Keycloak Connection Refused
```bash
# Ensure Keycloak is healthy before starting backend
docker-compose up -d keycloak
docker-compose logs -f keycloak | grep "Keycloak.*started"
# Then start backend
docker-compose up -d lis-gateway
```

### Checkstyle Failures
```bash
# Run checkstyle
./gradlew checkstyleMain 2>&1 | grep "error"

# Common fixes:
# - Line too long (>120 chars): break into multiple lines
# - Missing Javadoc: add /** */ comment
# - Import order: organize imports (Ctrl+Alt+O in IntelliJ)
# - Unused imports: remove them
```
