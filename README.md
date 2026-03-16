# RasterOneLab — Lab Information System (LIS)

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-green.svg)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-19-red.svg)](https://angular.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-Proprietary-red.svg)](LICENSE)

> Enterprise-grade, multi-branch Lab Information System (LIS) for managing complete laboratory operations.

## 🧪 Overview

**RasterOneLab** is a comprehensive Laboratory Information System designed for multi-branch diagnostic centers. It manages the complete lab workflow from patient registration to report delivery, including billing, inventory, quality control, and instrument integration.

### Key Modules
- **Patient Management** — Registration, demographics, visit history
- **Test Ordering** — Panel/individual test orders, reflex testing
- **Sample Management** — Collection, tracking, aliquoting, rejection
- **Result Entry** — Department-specific entry (numeric, narrative, culture, images)
- **Report Generation** — PDF reports with branch-specific templates
- **Billing & Finance** — Invoicing, payments, credit management
- **Inventory** — Reagent, consumable stock management
- **QC** — Westgard rules, Levey-Jennings charts, EQA
- **Instrument Interface** — ASTM/HL7 bidirectional communication
- **Administration** — Master data, users, branches, configuration

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Spring Boot 3.4, Gradle (Kotlin DSL) |
| Frontend | Angular 19, TypeScript 5.x, Angular Material 19, Tailwind CSS 4 |
| Database | PostgreSQL 17 |
| Authentication | Keycloak 24 (OAuth2 + OIDC) |
| Cache | Redis 7 |
| Message Queue | RabbitMQ 3.13 |
| Search | Elasticsearch 8 |
| Storage | MinIO (S3-compatible) |
| Containerization | Docker, Kubernetes |

## 🚀 Getting Started

### Prerequisites

- Java 21+
- Node.js 22+
- Docker & Docker Compose
- PostgreSQL 17 (or use Docker)

### 1. Clone & Setup Infrastructure

```bash
git clone https://github.com/rasterdevapps/rasteronelab.git
cd rasteronelab

# Start all infrastructure services
cd infrastructure/docker
cp .env.example .env
# Edit .env with your settings
docker-compose up -d
```

### 2. Run Backend

```bash
cd backend
./gradlew :lis-gateway:bootRun
# Or run individual modules
./gradlew :lis-patient:bootRun
```

### 3. Run Frontend

```bash
cd frontend
npm install
ng serve
# Open http://localhost:4200
```

## 📁 Project Structure

```
rasteronelab/
├── backend/                    # Spring Boot multi-module Gradle project
│   ├── lis-core/              # Shared domain entities, utilities
│   ├── lis-patient/           # Patient management
│   ├── lis-sample/            # Sample tracking
│   ├── lis-order/             # Test ordering
│   ├── lis-result/            # Result entry & validation
│   ├── lis-report/            # Report generation
│   ├── lis-billing/           # Billing & finance
│   ├── lis-inventory/         # Inventory management
│   ├── lis-instrument/        # Instrument interface (ASTM/HL7)
│   ├── lis-qc/                # Quality control
│   ├── lis-admin/             # Administration
│   ├── lis-notification/      # Notifications (SMS/Email/WhatsApp)
│   ├── lis-integration/       # HL7 FHIR integration
│   ├── lis-gateway/           # API Gateway
│   └── lis-auth/              # Auth service (Keycloak adapter)
├── frontend/                   # Angular 19 SPA
│   └── src/app/
│       ├── core/              # Guards, interceptors, services
│       ├── shared/            # Shared components, pipes, directives
│       ├── layouts/           # App layouts
│       └── features/          # Feature modules (lazy-loaded)
├── infrastructure/             # DevOps & deployment
│   ├── docker/                # Docker Compose, Dockerfiles
│   ├── nginx/                 # Nginx config
│   ├── jenkins/               # CI/CD pipeline
│   └── k8s/                   # Kubernetes manifests
└── docs/                      # Project documentation
    ├── architecture/          # System design docs
    ├── domain/                # Domain/business logic docs
    ├── process-flows/         # Workflow documentation
    ├── screens/               # Screen list & specs
    └── development/           # Dev guides & standards
```

## 🔄 Development Workflow

```bash
# Create feature branch
git checkout -b feature/LIS-{ticket}-description

# Backend: generate entity stack
# Use Claude: /generate-entity

# Frontend: generate feature module
# Use Claude: /generate-angular-module

# Run tests
cd backend && ./gradlew test
cd frontend && ng test

# Commit with conventional commits
git commit -m "feat(patient): add patient search by UHID [LIS-123]"
```

## 📚 Documentation

All documentation is in the [`docs/`](docs/README.md) directory.

- [System Architecture](docs/architecture/system-architecture.md)
- [Multi-Branch Implementation](docs/architecture/multi-branch-implementation.md)
- [API Specification](docs/architecture/api-specification.md)
- [Database Schema](docs/architecture/database-schema.md)
- [Domain Masters](docs/domain/masters/00-master-data-overview.md)
- [Development Standards](docs/development/coding-standards.md)

## 🤝 Contributing

1. Follow the [coding standards](docs/development/coding-standards.md)
2. All PRs must include tests (80% coverage)
3. Use conventional commits: `feat(module): message [LIS-XXX]`
4. Update Liquibase migrations for DB changes
5. Update relevant documentation

## 📄 License

Proprietary — © 2024 Raster Dev Apps. All rights reserved.
