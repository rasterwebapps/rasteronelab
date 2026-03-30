# Infrastructure — RasterOneLab LIS

## Quick Start (Local Development)

```bash
cd infrastructure/docker
cp .env.example .env
# Edit .env with your passwords

# Start all infrastructure services
docker-compose up -d

# Verify all services are healthy
docker-compose ps

# Stop all services
docker-compose down

# Stop and remove volumes (DESTRUCTIVE - loses all data)
docker-compose down -v
```

## Production Deployment (Staging / Production)

```bash
cd infrastructure/docker

# Copy and fill in ALL env vars (especially KC_HOSTNAME, ELASTIC_PASSWORD etc.)
cp .env.example .env
vi .env

# Start with production overrides (Keycloak start mode, ES xpack security)
docker-compose \
  -f docker-compose.yml \
  -f docker-compose.services.yml \
  -f docker-compose.prod.yml \
  up -d
```

> **Note**: `docker-compose.prod.yml` switches Keycloak from `start-dev` to `start --optimized`
> and enables Elasticsearch xpack security. Never run `start-dev` on staging/production.

## Service URLs (Local)

| Service | URL | Credentials |
|---------|-----|-------------|
| PostgreSQL | localhost:5432 | See .env |
| PgBouncer | localhost:6432 | (uses postgres credentials) |
| Redis | localhost:6379 | See .env |
| Keycloak Admin | http://localhost:8180/admin | See .env |
| RabbitMQ Management | http://localhost:15672 | See .env |
| Elasticsearch | http://localhost:9200 | See .env |
| MinIO Console | http://localhost:9001 | See .env |
| Mailhog | http://localhost:8025 | No auth |

## Keycloak Setup (First Time)

1. Access http://localhost:8180/admin
2. Login with KEYCLOAK_ADMIN credentials
3. Create realm: `rasteronelab`
4. Create client: `lis-frontend` (public, SPA)
5. Create client: `lis-backend` (confidential, for service accounts)
6. Create roles: SUPER_ADMIN, ORG_ADMIN, BRANCH_ADMIN, PATHOLOGIST, LAB_TECHNICIAN, RECEPTIONIST, BILLING_STAFF, INVENTORY_STAFF, DOCTOR, PATIENT
7. Add custom attributes: `organizationId`, `branchIds` (multivalued)
8. Configure token mappers to include custom claims in JWT

## MinIO Setup (First Time)

```bash
# Create required buckets
docker exec lis-minio mc alias set local http://localhost:9000 minioadmin minioadmin123
docker exec lis-minio mc mb local/lis-reports
docker exec lis-minio mc mb local/lis-images
docker exec lis-minio mc mb local/lis-documents
docker exec lis-minio mc mb local/lis-logos
docker exec lis-minio mc mb local/lis-signatures
```

## Docker Build

```bash
# Build specific backend module
docker build \
  --file infrastructure/docker/Dockerfile.backend \
  --build-arg MODULE=lis-patient \
  --tag rasteronelab/lis-patient:latest \
  backend/

# Build frontend
docker build \
  --file infrastructure/docker/Dockerfile.frontend \
  --tag rasteronelab/lis-frontend:latest \
  frontend/
```

## Jenkins Pipeline

Pipeline is defined in `jenkins/Jenkinsfile`. Configure Jenkins with:
- Credentials: `nexus-credentials`, `docker-registry`, `k8s-config`
- Plugins: Docker Pipeline, Kubernetes, SonarQube Scanner, JaCoCo
- SonarQube server configured as `SonarQube`
