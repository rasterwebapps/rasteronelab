# RasterOneLab LIS — Local Development Setup

## Prerequisites

| Tool | Version | Install |
|------|---------|---------|
| Docker & Docker Compose | 24+ / v2+ | [docker.com](https://docs.docker.com/get-docker/) |
| JDK | 21 (Temurin) | `sdk install java 21-tem` or [adoptium.net](https://adoptium.net/) |
| Node.js | 22 LTS | `nvm install 22` or [nodejs.org](https://nodejs.org/) |
| Gradle | 8.x (wrapper included) | Bundled via `./gradlew` |
| Angular CLI | 19.x | `npm install -g @angular/cli@19` |

## 1. Clone the Repository

```bash
git clone https://github.com/rasterwebapps/rasteronelab.git
cd rasteronelab
```

## 2. Start Infrastructure Services

```bash
cd infrastructure/docker

# Create your local env file from the template
cp .env.example .env
# Edit .env and set secure passwords for local dev

# Start all infrastructure services
docker-compose up -d
```

This starts PostgreSQL, Redis, Keycloak, RabbitMQ, Elasticsearch, MinIO, and Mailhog.

Verify all services are healthy:

```bash
docker-compose ps
```

### Service URLs

| Service | URL | Purpose |
|---------|-----|---------|
| PostgreSQL | `localhost:5432` | Database |
| PgBouncer | `localhost:6432` | Connection pooler |
| Redis | `localhost:6379` | Cache |
| Keycloak | `http://localhost:8180` | Auth (admin console) |
| RabbitMQ | `http://localhost:15672` | Message queue management UI |
| Elasticsearch | `http://localhost:9200` | Search engine |
| MinIO Console | `http://localhost:9001` | Object storage UI |
| MinIO API | `http://localhost:9000` | S3-compatible API |
| Mailhog | `http://localhost:8025` | Dev email inbox |

## 3. Build and Run Backend

```bash
cd backend

# Build all modules
./gradlew clean build

# Terminal 1 - run the API gateway
./gradlew :lis-gateway:bootRun

# Terminal 2 - run a specific backend service (example: patient service)
./gradlew :lis-patient:bootRun

# Run tests
./gradlew test

# Run tests with coverage
./gradlew test jacocoTestReport
```

Backend services start on ports 8081–8090. The API gateway runs on port 8080.

## 4. Start Frontend

```bash
# Terminal 3 - start the Angular app
cd frontend

# Install dependencies
npm ci

# Start dev server
ng serve
```

The Angular dev server starts at **http://localhost:4200** and proxies API requests to the gateway.

### Running frontend and backend together

For local development, keep these processes running at the same time:

1. Docker infrastructure from `infrastructure/docker`
2. API gateway on `http://localhost:8080`
3. At least one backend module you are actively using (for example `:lis-patient:bootRun`)
4. Angular frontend on `http://localhost:4200`

The frontend forwards `/api` traffic to the gateway using `frontend/proxy.conf.json`, so the gateway must be running before you start testing screens that call the backend.

## 5. Default Credentials

### Infrastructure Services

| Service | Username | Password |
|---------|----------|----------|
| PostgreSQL | `lisadmin` | *(from .env — DB_PASSWORD)* |
| Keycloak Admin | `admin` | *(from .env — KEYCLOAK_ADMIN_PASSWORD)* |
| RabbitMQ | `lisuser` | *(from .env — RABBITMQ_PASSWORD)* |
| MinIO | `minioadmin` | *(from .env — MINIO_ROOT_PASSWORD)* |

### Application Test Users (Keycloak)

All test users share the password: **`Test@1234`**

| Username | Role |
|----------|------|
| `superadmin` | SUPER_ADMIN |
| `orgadmin` | ORG_ADMIN |
| `branchadmin` | BRANCH_ADMIN |
| `pathologist` | PATHOLOGIST |
| `labtechnician` | LAB_TECHNICIAN |
| `receptionist` | RECEPTIONIST |
| `phlebotomist` | PHLEBOTOMIST |
| `billingstaff` | BILLING_STAFF |
| `doctor` | DOCTOR |
| `patient` | PATIENT |

## 6. Troubleshooting

### Port conflicts

If a port is already in use, stop the conflicting process or edit `docker-compose.yml` to remap:

```bash
# Find what's using a port
lsof -i :5432
```

### Keycloak not starting

Keycloak requires PostgreSQL to be fully ready. If it fails on first start:

```bash
docker-compose restart keycloak
```

### Gradle build fails with OOM

Increase Gradle JVM memory in `backend/gradle.properties`:

```properties
org.gradle.jvmargs=-Xmx4g
```

### Elasticsearch exits immediately

Elasticsearch requires `vm.max_map_count >= 262144`. On Linux:

```bash
sudo sysctl -w vm.max_map_count=262144
```

### Frontend proxy errors (502/504)

Ensure the backend gateway (`lis-gateway`) is running on port 8080 before starting the frontend.

### Docker Compose "no space left on device"

```bash
docker system prune -a --volumes
```

### Reset everything

```bash
cd infrastructure/docker
docker-compose down -v    # Stops containers AND removes volumes (data loss!)
docker-compose up -d      # Fresh start
```
