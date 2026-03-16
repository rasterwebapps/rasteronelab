# Technology Stack Evaluation — RasterOneLab LIS

## Backend: Java 21 + Spring Boot 3.4

### Why Java 21?
- **LTS release**: Long-term support until 2031
- **Virtual Threads (Project Loom)**: Dramatically improves throughput for I/O-bound operations (DB queries, instrument communication)
- **Records**: Immutable data classes for DTOs
- **Sealed Classes**: Better domain modeling (result types)
- **Pattern Matching**: Cleaner code for `instanceof` checks
- **Mature ecosystem**: Extensive libraries for healthcare (HL7, FHIR)

### Why Spring Boot 3.4?
- **Spring Data JPA**: Repository pattern, reduces boilerplate
- **Spring Security**: OAuth2 Resource Server for Keycloak integration
- **Spring WebSocket**: Real-time instrument results and alerts
- **Spring Events**: Loose coupling between modules
- **Spring Actuator**: Health checks, metrics out of the box
- **Spring Cloud Gateway**: API routing without overhead of full microservices

### Alternatives Considered
| Alternative | Why Rejected |
|------------|-------------|
| Quarkus | Smaller community, fewer LIS-specific libraries |
| Micronaut | Less developer familiarity, smaller ecosystem |
| Node.js | Not ideal for CPU-intensive tasks (report PDF generation) |
| Python (Django) | Performance concerns for high-volume labs |

---

## Database: PostgreSQL 17

### Why PostgreSQL?
- **JSONB**: Flexible fields without schema changes (extra_info, qualitative_options)
- **UUID support**: `gen_random_uuid()`, UUID v7 via uuid-creator library
- **Table partitioning**: Essential for test_result and audit_trail (millions of rows)
- **Full-text search**: Patient name search without Elasticsearch (fallback)
- **Excellent JSON operators**: Query inside JSONB columns
- **Row-level security**: Possible future enhancement for branch isolation
- **TIMESTAMPTZ**: Timezone-aware timestamps (critical for multi-timezone operations)
- **ACID compliance**: Essential for financial transactions (billing)
- **No licensing cost**: Open source

### Alternatives Considered
| Alternative | Why Rejected |
|------------|-------------|
| MySQL 8.x | Weaker JSONB support, limited partitioning |
| Oracle | Expensive licensing |
| MongoDB | ACID limitations for billing, less suitable for structured lab data |
| SQL Server | Windows-centric, licensing cost |

---

## Auth: Keycloak 24

### Why Keycloak?
- **OAuth2 + OIDC**: Industry standard, not proprietary
- **Multi-realm**: Separate realms for lab staff vs. doctor/patient portal
- **Custom JWT claims**: Add branchIds, organizationId to token
- **Admin REST API**: Programmatic user management
- **Social login**: Google login for doctor portal (optional)
- **MFA**: TOTP, email OTP
- **No per-user licensing**

### Alternatives Considered
| Alternative | Why Rejected |
|------------|-------------|
| Auth0 | Per-user pricing (expensive for large labs) |
| AWS Cognito | Vendor lock-in, limited custom claims |
| Custom JWT | Security risk, reinventing the wheel |

---

## Frontend: Angular 19

### Why Angular?
- **Enterprise-grade**: Strong typing, strict structure, best for large teams
- **Angular Material**: Professional UI components (tables, forms, dialogs)
- **Signals**: Modern reactive state management without RxJS complexity
- **Standalone components**: No NgModule boilerplate
- **Angular CDK**: Virtual scrolling for large patient lists
- **TypeScript-first**: Catches errors at compile time

### Why Not React or Vue?
| Framework | Consideration |
|-----------|-------------|
| React | More flexible but less opinionated — harder to maintain consistency across team |
| Vue 3 | Smaller enterprise adoption, fewer UI component libraries |
| Angular | More prescriptive — easier for large teams to maintain standards |

---

## Message Queue: RabbitMQ

### Why RabbitMQ?
- **Reliable delivery**: Durable queues, message persistence
- **Dead Letter Queue**: Failed messages don't disappear
- **Priority queues**: STAT results processed before ROUTINE
- **Management UI**: Visual queue monitoring (`localhost:15672`)
- **AMQP standard**: Well-understood protocol

---

## PDF Generation: OpenPDF

### Why OpenPDF?
- **LGPL license**: Free for commercial use
- **iText 2.x fork**: Familiar API, extensive documentation
- **Java-native**: No external process/service needed
- **Features**: Tables, images, fonts, headers/footers, page numbers

### Alternatives Considered
| Alternative | Why Rejected |
|------------|-------------|
| iText 7 | Dual license: AGPL (open source) or commercial ($$$) |
| Apache PDFBox | Mostly for reading PDFs, limited generation |
| JasperReports | Heavy, XML template-based (complex for custom layouts) |
| Flying Saucer | HTML to PDF (harder to control layout precisely) |

---

## Object Storage: MinIO

### Why MinIO?
- **S3-compatible**: Use AWS SDK, easy migration to AWS S3 if needed
- **Self-hosted**: Patient data stays on-premise (HIPAA)
- **High performance**: Object storage for PDF reports and images
- **Versioning**: Keep old report versions
- **Lifecycle policies**: Auto-delete after retention period
- **Web UI**: Built-in browser for ops team

---

## Summary: Decision Matrix

| Concern | Choice | Key Reason |
|---------|--------|-----------|
| Language | Java 21 | LTS, virtual threads, mature ecosystem |
| Framework | Spring Boot 3.4 | JPA, Security, Events, Gateway |
| Build | Gradle Kotlin | Type-safe DSL, faster than Maven |
| Frontend | Angular 19 | Enterprise, TypeScript, Material |
| Database | PostgreSQL 17 | JSONB, partitioning, ACID |
| Auth | Keycloak 24 | OAuth2/OIDC, no per-user cost |
| Cache | Redis 7 | Performance, pub/sub |
| Queue | RabbitMQ | Reliable, DLQ, management UI |
| Storage | MinIO | S3-compatible, self-hosted, HIPAA |
| PDF | OpenPDF | Free, Java-native, sufficient features |
| Mapping | MapStruct | Compile-time, zero overhead |
| Search | Elasticsearch 8 | Full-text patient/audit search |
