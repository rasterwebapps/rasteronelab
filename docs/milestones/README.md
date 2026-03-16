# RasterOneLab LIS — Development Milestones

> 8-Phase roadmap derived from project documentation. Each milestone maps to a development phase with detailed GitHub issues.

## Timeline Overview

| Milestone | Phase | Timeline | Status | Issues |
|-----------|-------|----------|--------|--------|
| [Phase 1: Foundation](phase-1-foundation.md) | Infrastructure & Auth | Months 1-2 | 🟡 In Progress | 15 |
| [Phase 2: Administration Module](phase-2-administration.md) | Master Data & Admin | Months 2-4 | ⬜ Not Started | 18 |
| [Phase 3: Patient & Ordering](phase-3-patient-ordering.md) | Registration, Orders, Billing | Months 4-6 | ⬜ Not Started | 21 |
| [Phase 4: Sample Management](phase-4-sample-management.md) | Sample Lifecycle | Months 6-8 | ⬜ Not Started | 14 |
| [Phase 5: Result Entry & Validation](phase-5-result-entry.md) | All Departments | Months 8-12 | ⬜ Not Started | 20 |
| [Phase 6: Instrument Interface](phase-6-instrument-interface.md) | ASTM/HL7 Integration | Months 10-13 | ⬜ Not Started | 12 |
| [Phase 7: Reports, QC & Notifications](phase-7-reports-qc-notifications.md) | PDF, QC, Alerts | Months 12-16 | ⬜ Not Started | 17 |
| [Phase 8: Portals, Analytics & Launch](phase-8-portals-analytics.md) | Portals, Dashboards | Months 16-22 | ⬜ Not Started | 18 |

**Total Issues: 135 (LIS-001 through LIS-135)**

---

## Milestone Dependencies

```
Phase 1 ──→ Phase 2 ──→ Phase 3 ──→ Phase 4 ──→ Phase 5
                                                    │
                                          Phase 6 ←─┘ (overlaps)
                                            │
                                          Phase 7 ←── (overlaps with 5-6)
                                            │
                                          Phase 8
```

## Documentation Sources

Each milestone's issues are derived from the following project documentation:

| Category | Documents | Key Content |
|----------|-----------|-------------|
| Architecture | 11 docs | System design, API spec, DB schema, security, workflows |
| Domain | 21 docs | Master data (5 levels), departments (7), parameters |
| Process Flows | 12 docs | End-to-end workflows for all operations |
| Screens | 1 doc | 194 application screens across 18 modules |
| Development | 7 docs | Tech stack, scaffolding, coding standards, testing |

## Issue Labeling Convention

| Label | Description |
|-------|-------------|
| `backend` | Java/Spring Boot work |
| `frontend` | Angular 19 work |
| `infrastructure` | Docker, CI/CD, K8s |
| `database` | Schema, migrations, queries |
| `security` | Auth, RBAC, encryption |
| `testing` | Unit, integration, E2E tests |
| `documentation` | Docs, API specs |
| `bug` | Bug fix |
| `enhancement` | Feature improvement |

## Module Reference

| Module | Port | Primary Phase |
|--------|------|---------------|
| lis-core | — | Phase 1 |
| lis-auth | 8093 | Phase 1 |
| lis-gateway | 8080 | Phase 1 |
| lis-admin | 8090 | Phase 2 |
| lis-patient | 8081 | Phase 3 |
| lis-order | 8082 | Phase 3 |
| lis-billing | 8086 | Phase 3 |
| lis-sample | 8083 | Phase 4 |
| lis-result | 8084 | Phase 5 |
| lis-instrument | 8088 | Phase 6 |
| lis-report | 8085 | Phase 7 |
| lis-qc | 8089 | Phase 7 |
| lis-notification | 8091 | Phase 7 |
| lis-inventory | 8087 | Phase 7 |
| lis-integration | 8092 | Phase 8 |

## Automation

Use the provided script to create milestones and issues on GitHub:

```bash
# Ensure gh CLI is authenticated
gh auth login

# Create all milestones and issues
./scripts/create-github-milestones.sh
```

See [scripts/create-github-milestones.sh](../../scripts/create-github-milestones.sh) for details.
