# Deployment Guide — RasterOneLab LIS

## Environments

| Environment | Infrastructure | Purpose |
|------------|---------------|---------|
| Local | Docker Compose | Developer workstation |
| Staging | Kubernetes (small cluster) | UAT, QA testing |
| Production | Kubernetes (HA cluster) | Live lab operations |

## Local Development Setup

### Prerequisites
- Java 21 (SDKMAN: `sdk install java 21.0.3-ms`)
- Node.js 22 LTS
- Docker Desktop
- Angular CLI 19: `npm install -g @angular/cli@19`

### Start Infrastructure

```bash
cd infrastructure/docker
cp .env.example .env  # Edit with your settings
docker-compose up -d

# Services started:
# PostgreSQL: localhost:5432
# Redis: localhost:6379
# Keycloak: localhost:8180
# RabbitMQ: localhost:15672 (admin/admin)
# Elasticsearch: localhost:9200
# MinIO: localhost:9001 (minioadmin/minioadmin)
# Mailhog: localhost:8025
```

### Start Backend Modules

```bash
cd backend
./gradlew :lis-core:build  # Build core first
./gradlew :lis-admin:bootRun   # Port 8090
./gradlew :lis-patient:bootRun # Port 8081
# ... start other modules as needed
./gradlew :lis-gateway:bootRun # Port 8080 (start last)
```

### Start Frontend

```bash
cd frontend
npm install
ng serve  # http://localhost:4200
```

## Jenkins CI/CD Pipeline

```groovy
// infrastructure/jenkins/Jenkinsfile stages:
pipeline {
    stages {
        stage('Checkout') { ... }
        stage('Build') {
            steps {
                sh './gradlew build -x test'
                sh 'cd frontend && npm ci && ng build --configuration=production'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew test jacocoTestReport'
                sh 'cd frontend && ng test --watch=false --browsers=ChromeHeadless'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                sh './gradlew sonarqube'
            }
        }
        stage('Docker Build') {
            steps {
                sh 'docker build -f infrastructure/docker/Dockerfile.backend -t rasteronelab/backend:${BUILD_NUMBER} .'
                sh 'docker build -f infrastructure/docker/Dockerfile.frontend -t rasteronelab/frontend:${BUILD_NUMBER} frontend/'
            }
        }
        stage('Docker Push') {
            steps {
                sh 'docker push rasteronelab/backend:${BUILD_NUMBER}'
                sh 'docker push rasteronelab/frontend:${BUILD_NUMBER}'
            }
        }
        stage('Deploy to Staging') {
            when { branch 'develop' }
            steps {
                sh 'kubectl set image deployment/lis-backend backend=rasteronelab/backend:${BUILD_NUMBER} -n staging'
            }
        }
        stage('Deploy to Production') {
            when { branch 'main' }
            input { message 'Deploy to production?' }
            steps {
                sh 'kubectl set image deployment/lis-backend backend=rasteronelab/backend:${BUILD_NUMBER} -n production'
            }
        }
    }
}
```

## Kubernetes Deployment (Production)

### Resource Requirements (per module)

| Module | CPU Request | CPU Limit | Memory Request | Memory Limit | Replicas |
|--------|------------|-----------|----------------|-------------|---------|
| lis-gateway | 500m | 1000m | 512Mi | 1Gi | 2 |
| lis-patient | 250m | 500m | 512Mi | 1Gi | 2 |
| lis-result | 500m | 1000m | 512Mi | 1Gi | 2 |
| lis-report | 500m | 1000m | 1Gi | 2Gi | 2 |
| lis-notification | 250m | 500m | 256Mi | 512Mi | 1 |

### Health Checks

```yaml
livenessProbe:
  httpGet:
    path: /actuator/health/liveness
    port: 8080
  initialDelaySeconds: 60
  periodSeconds: 30

readinessProbe:
  httpGet:
    path: /actuator/health/readiness
    port: 8080
  initialDelaySeconds: 30
  periodSeconds: 10
```

## Database Migrations (Liquibase)

```bash
# Run pending migrations (auto-runs on startup)
./gradlew :lis-patient:bootRun

# Manual migration check
./gradlew :lis-patient:liquibaseStatus

# Rollback last changeset
./gradlew :lis-patient:liquibaseRollback -PliquibaseCommandValue=1
```

## Monitoring Stack

| Tool | Purpose | URL |
|------|---------|-----|
| Grafana | Dashboards | :3000 |
| Prometheus | Metrics collection | :9090 |
| Loki | Log aggregation | :3100 |
| Jaeger | Distributed tracing | :16686 |

### Key Metrics to Monitor
- Request rate (req/min per module)
- Error rate (5xx responses)
- P95/P99 response time
- JVM heap usage
- Database connection pool usage
- RabbitMQ queue depths
- Report generation queue depth
- Instrument connection status

## Go-Live Checklist

### Pre-Go-Live (1 week before)
- [ ] All servers provisioned and secured
- [ ] SSL certificates installed
- [ ] Database backup configured (daily automated)
- [ ] Keycloak realm exported/backed up
- [ ] MinIO bucket lifecycle policies configured
- [ ] All staff users created in Keycloak
- [ ] All masters configured and validated
- [ ] ASTM instrument connections tested
- [ ] Notification gateways tested (SMS delivered, email received)
- [ ] Report PDF generation validated for all departments
- [ ] Staff training completed

### Go-Live Day
- [ ] Switch DNS to production servers
- [ ] Monitor error rates for first 2 hours
- [ ] Support team on standby
- [ ] Rollback plan ready
- [ ] First sample patient processed end-to-end

### Post-Go-Live (1 week after)
- [ ] Review error logs daily
- [ ] Monitor TAT compliance
- [ ] Verify QC data being recorded
- [ ] Check all instruments are consistently connected
- [ ] Gather staff feedback
