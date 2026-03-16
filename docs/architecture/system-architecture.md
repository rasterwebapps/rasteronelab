# System Architecture — RasterOneLab LIS

## Overview

RasterOneLab LIS is a **multi-module, multi-branch** laboratory information system built with a microservices-inspired modular monolith architecture. Each module (patient, order, sample, result, etc.) is independently deployable but shares common infrastructure.

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                          Clients                                 │
│  Angular SPA    │  Doctor Portal  │  Patient Portal  │  Mobile   │
└────────┬────────┴────────┬────────┴────────┬─────────┴──────────┘
         └─────────────────┼─────────────────┘
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Nginx (Reverse Proxy)                         │
│              Rate Limiting │ SSL Termination │ CORS               │
└────────────────────────────┬────────────────────────────────────┘
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│              Spring Cloud Gateway (lis-gateway :8080)             │
│    JWT Validation │ Route to Module │ Rate Limiting │ Logging      │
└──┬────────┬───────┬─────────┬───────┬────────┬──────────────────┘
   │        │       │         │       │        │
   ▼        ▼       ▼         ▼       ▼        ▼
┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐
│:8081 │ │:8082 │ │:8083 │ │:8084 │ │:8085 │ │:8090 │
│patient│ │order │ │sample│ │result│ │report│ │admin │
└──────┘ └──────┘ └──────┘ └──────┘ └──────┘ └──────┘
         ┌─────────────────────────────────────────────┐
         │         Shared Infrastructure                │
         │  PostgreSQL 17 │ Redis 7 │ RabbitMQ │ MinIO  │
         └─────────────────────────────────────────────┘
```

## Technology Stack

| Concern | Technology | Rationale |
|---------|-----------|-----------|
| Backend Framework | Spring Boot 3.4 | Production-proven, Java 21 virtual threads |
| Language | Java 21 | LTS, records, sealed classes, virtual threads |
| Build | Gradle Kotlin DSL | Better IDE support, Kotlin type safety |
| Frontend | Angular 19 | Enterprise SPA, Signals, standalone components |
| Database | PostgreSQL 17 | JSONB, partitioning, UUID v7, excellent performance |
| Auth | Keycloak 24 | OAuth2/OIDC, RBAC, multi-realm |
| Cache | Redis 7 | High-performance cache, pub/sub |
| Queue | RabbitMQ 3.13 | Reliable messaging, DLQ, management UI |
| Search | Elasticsearch 8 | Patient search, audit trail search |
| Storage | MinIO | S3-compatible, on-premise, PDF/image storage |
| PDF | OpenPDF | Java PDF generation for reports |
| Mapping | MapStruct | Compile-time mapper, zero overhead |

## Module Responsibilities

| Module | Port | Responsibility |
|--------|------|---------------|
| lis-gateway | 8080 | Routing, auth validation, rate limiting |
| lis-patient | 8081 | Patient demographics, UHID, visit history |
| lis-order | 8082 | Test ordering, panels, pricing |
| lis-sample | 8083 | Sample collection, tracking, aliquoting |
| lis-result | 8084 | Result entry, validation, delta check, critical values |
| lis-report | 8085 | PDF generation, report delivery |
| lis-billing | 8086 | Invoicing, payments, credit management |
| lis-inventory | 8087 | Reagents, consumables, stock management |
| lis-instrument | 8088 | ASTM/HL7 interface, auto-validation |
| lis-qc | 8089 | Westgard rules, Levey-Jennings, EQA |
| lis-admin | 8090 | Master data, branches, users, configuration |
| lis-notification | 8091 | SMS, Email, WhatsApp, Push notifications |
| lis-integration | 8092 | HL7 FHIR, external system integration |
| lis-auth | 8093 | Keycloak adapter, user management |

## Communication Patterns

### 1. REST (Synchronous)
- Client ↔ Gateway ↔ Module
- Format: JSON with `ApiResponse<T>` wrapper
- Auth: JWT Bearer token, Branch: `X-Branch-Id` header

### 2. Spring Events (In-Process Async)
- Within same JVM instance
- Used for: Cross-module notification without tight coupling
- Example: `OrderPlacedEvent` → billing module auto-generates invoice

### 3. RabbitMQ (Async Message Queue)
- Cross-service, durable with retry
- Used for: Instrument results, report generation, notifications, audit
- Exchanges: `lis.direct`, `lis.topic`, `lis.fanout`

### 4. WebSocket (Real-time Push)
- Used for: Live instrument results, critical value alerts, dashboard updates
- Implementation: Spring WebSocket + STOMP + socket.io-client

## Data Flow: Complete Test Order

```
Patient arrives
  → Receptionist creates order (lis-order)
  → Billing auto-generates invoice (via OrderPlacedEvent)
  → Phlebotomist collects sample (lis-sample)
  → Sample received at lab, QC check
  → Lab technician enters result OR instrument sends via ASTM
  → Auto-validation runs (QC check, delta check, critical value check)
  → Pathologist authorizes (lis-result)
  → Report generated (lis-report, triggered by AuthorizationEvent)
  → SMS/Email delivered (lis-notification, via ReportGeneratedEvent)
  → Patient/Doctor portal updated (WebSocket push)
```
