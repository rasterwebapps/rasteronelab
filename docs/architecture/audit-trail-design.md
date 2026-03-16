# Audit Trail Design — RasterOneLab LIS

## What to Audit

### Mandatory Audit Events

| Category | Events |
|---------|--------|
| Patient | Create, Update, View (for PHI access), Merge, Delete |
| Order | Create, Modify, Cancel, Status changes |
| Sample | Collection, Receipt, Rejection, Disposal |
| Result | Entry, Validation, Authorization, Amendment |
| Report | Generation, Release, Download, Amendment |
| Billing | Invoice creation, Payment, Refund, Discount |
| Auth | Login, Logout, Failed login, Password change, Role change |
| Critical Values | Detection, Physician notification, Acknowledgment |
| Data Export | Any bulk export of patient data |
| Admin | All master data changes (create/update/delete) |

## audit_trail Table Schema

```sql
CREATE TABLE audit_trail (
    id          UUID NOT NULL,
    branch_id   UUID NOT NULL,
    entity_type VARCHAR(50)  NOT NULL,  -- 'PATIENT', 'ORDER', 'RESULT', etc.
    entity_id   UUID         NOT NULL,
    action      VARCHAR(50)  NOT NULL,  -- 'CREATE', 'UPDATE', 'DELETE', 'VIEW', 'LOGIN'
    old_value   JSONB,                  -- Previous state (null for CREATE)
    new_value   JSONB,                  -- New state (null for DELETE)
    changed_by  VARCHAR(100),           -- username
    changed_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    ip_address  INET,
    user_agent  TEXT,
    session_id  VARCHAR(100),
    PRIMARY KEY (id, changed_at)
) PARTITION BY RANGE (changed_at);
```

## Retention Policy

| Period | Storage | Accessibility |
|--------|---------|--------------|
| 0-90 days (HOT) | PostgreSQL primary | Instant, full-text search |
| 91 days - 1 year (WARM) | PostgreSQL replica | Query with slight delay |
| 1-7 years (ARCHIVE) | Compressed, cold storage | Restore on request |
| > 7 years | Delete | Compliant with regulations |

## Audit Service Implementation

```java
@Service
@Async("auditExecutor")  // Non-blocking audit logging
public class AuditService {

    private final RabbitTemplate rabbitTemplate;

    public void log(AuditEvent event) {
        // Publish to audit queue (non-blocking)
        rabbitTemplate.convertAndSend("lis.audit.log", event);
    }

    // Helper methods
    public void logCreate(String entityType, UUID entityId, Object newValue) {
        log(new AuditEvent(entityType, entityId, "CREATE", null, toJson(newValue)));
    }

    public void logUpdate(String entityType, UUID entityId, Object oldValue, Object newValue) {
        log(new AuditEvent(entityType, entityId, "UPDATE", toJson(oldValue), toJson(newValue)));
    }

    public void logView(String entityType, UUID entityId) {
        log(new AuditEvent(entityType, entityId, "VIEW", null, null));
    }

    public void logDelete(String entityType, UUID entityId, Object oldValue) {
        log(new AuditEvent(entityType, entityId, "DELETE", toJson(oldValue), null));
    }
}
```

## Usage in Services

```java
@Service
@Transactional
public class PatientServiceImpl implements PatientUseCase {

    private final AuditService auditService;

    @Override
    public PatientResponse createPatient(PatientRequest request) {
        Patient patient = patientMapper.toEntity(request);
        Patient saved = patientRepository.save(patient);
        auditService.logCreate("PATIENT", saved.getId(), saved); // ← Audit
        return patientMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getPatient(UUID id) {
        Patient patient = findByIdAndBranch(id);
        auditService.logView("PATIENT", id); // ← Audit PHI access
        return patientMapper.toResponse(patient);
    }
}
```

## Compliance Requirements

| Standard | Requirement |
|---------|-------------|
| HIPAA | Audit logs for 6 years minimum |
| ISO 15189 | Traceability of all test results |
| NABL | Record retention as per accreditation |
| Local regulations | India: 7 years minimum |
