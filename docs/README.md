# RasterOneLab LIS — Documentation Index

> Complete documentation for the RasterOneLab Laboratory Information System

## 📐 Architecture

| Document | Description |
|----------|-------------|
| [System Architecture](architecture/system-architecture.md) | High-level system design, components, communication patterns |
| [Multi-Branch Implementation](architecture/multi-branch-implementation.md) | Row-level filtering, BranchContextHolder, BranchInterceptor |
| [Database Schema](architecture/database-schema.md) | Complete DDL for all ~143 tables |
| [API Specification](architecture/api-specification.md) | 100+ REST endpoints with request/response formats |
| [Security Architecture](architecture/security-architecture.md) | Keycloak, JWT, RBAC, HIPAA compliance |
| [Workflow State Machines](architecture/workflow-state-machines.md) | Order, Sample, Result, Invoice, Report state machines |
| [Notification Architecture](architecture/notification-architecture.md) | SMS, Email, WhatsApp notification system |
| [Report Generation Engine](architecture/report-generation-engine.md) | PDF report generation, templates, MinIO storage |
| [Async Processing](architecture/async-processing.md) | RabbitMQ exchanges, queues, consumers |
| [Audit Trail Design](architecture/audit-trail-design.md) | What to audit, retention, compliance |
| [Error Codes](architecture/error-codes.md) | Standardized LIS error code registry |

## 🧪 Domain Knowledge

### Master Data
| Document | Description |
|----------|-------------|
| [Master Data Overview](domain/masters/00-master-data-overview.md) | 125 masters across 5 levels |
| [Global Masters (L1)](domain/masters/01-global-masters.md) | 30 platform-wide masters |
| [Organization Masters (L2)](domain/masters/02-organization-masters.md) | 53 per-organization masters |
| [Branch Masters (L3)](domain/masters/03-branch-masters.md) | 27 per-branch masters |
| [Operational Masters (L4)](domain/masters/04-operational-masters.md) | 10 operational masters |
| [External Masters (L5)](domain/masters/05-external-masters.md) | 5 external/portal masters |
| [Master Data Lifecycle](domain/masters/06-master-data-lifecycle.md) | Setup phases 1-6 |

### Departments
| Document | Description |
|----------|-------------|
| [Department Overview](domain/departments/00-department-overview.md) | All 11 departments summary |
| [Biochemistry](domain/departments/01-biochemistry.md) | 46+ parameters, numeric tabular entry |
| [Hematology](domain/departments/02-hematology.md) | CBC, differential, scatter plots |
| [Microbiology](domain/departments/03-microbiology.md) | Culture, antibiogram, CLSI |
| [Histopathology](domain/departments/04-histopathology.md) | Narrative + images, CAP synoptic |
| [Clinical Pathology](domain/departments/05-clinical-pathology.md) | Urine/stool semi-quantitative |
| [Serology/Immunology](domain/departments/06-serology-immunology.md) | Qualitative + titer |
| [Molecular Biology](domain/departments/07-molecular-biology.md) | PCR, CT values |
| [Result Entry Types](domain/departments/result-entry-types.md) | All 10 result data types |

### Other Domain Docs
| Document | Description |
|----------|-------------|
| [Parameter Configuration](domain/parameters/parameter-configuration.md) | 137+ parameters with full config |
| [Lab Domain Glossary](domain/lab-domain-glossary.md) | Key medical/lab terms |
| [Sample Types & Tubes](domain/sample-types-tubes.md) | Tube colors, volumes, stability |
| [Critical Values](domain/critical-values.md) | Critical value definitions & workflow |
| [Instrument Interface Spec](domain/instrument-interface-spec.md) | ASTM E1381/E1394 protocol |
| [Barcode Strategy](domain/barcode-strategy.md) | Barcode formats, label designs |

## 🔄 Process Flows

| Document | Description |
|----------|-------------|
| [Complete Lipid + CBC Walkthrough](process-flows/complete-lipid-cbc-walkthrough.md) | End-to-end example |
| [Patient Registration](process-flows/01-patient-registration.md) | Registration workflow |
| [Test Ordering](process-flows/02-test-ordering.md) | Order creation workflow |
| [Sample Collection](process-flows/03-sample-collection.md) | Collection workflow |
| [Sample Receiving](process-flows/04-sample-receiving.md) | Lab receipt workflow |
| [Result Entry & Validation](process-flows/06-result-entry-validation.md) | Result entry workflow |
| [Result Authorization](process-flows/07-result-authorization.md) | Authorization workflow |
| [Report Generation & Delivery](process-flows/08-report-generation-delivery.md) | Report workflow |
| [Billing & Payment](process-flows/09-billing-payment.md) | Billing workflow |
| [Quality Control](process-flows/10-quality-control.md) | QC workflow |
| [Critical Value Workflow](process-flows/15-critical-value-workflow.md) | Critical value notification |
| [Master Data Setup Flow](process-flows/master-data-setup-flow.md) | Onboarding phases |

## 🖥️ Screens

| Document | Description |
|----------|-------------|
| [Screen List (194 screens)](screens/screen-list-complete.md) | Complete list of all screens |

## 👨‍💻 Development

| Document | Description |
|----------|-------------|
| [Tech Stack Evaluation](development/tech-stack-evaluation.md) | Technology choices with rationale |
| [Development Plan](development/development-plan.md) | 8-phase development plan |
| [Prompting Guide](development/prompting-guide.md) | AI prompting for LIS development |
| [Project Scaffolding](development/project-scaffolding.md) | Project skeleton specification |
| [Coding Standards](development/coding-standards.md) | Java, Angular, SQL, Git standards |
| [Testing Strategy](development/testing-strategy.md) | Testing pyramid, coverage targets |
| [Deployment Guide](development/deployment-guide.md) | CI/CD, Docker, Kubernetes, monitoring |
