# Security Architecture — RasterOneLab LIS

## Authentication: Keycloak 24

### Realm Configuration

```
Realm: rasteronelab
├── Clients
│   ├── lis-frontend (public, SPA, PKCE)
│   ├── lis-backend  (confidential, service accounts)
│   └── lis-gateway  (bearer-only)
├── Identity Providers (optional)
│   ├── Google (for doctor portal)
│   └── Active Directory (for enterprise)
└── Roles
    ├── SUPER_ADMIN
    ├── ORG_ADMIN
    ├── BRANCH_ADMIN
    ├── PATHOLOGIST
    ├── LAB_TECHNICIAN
    ├── RECEPTIONIST
    ├── PHLEBOTOMIST
    ├── BILLING_STAFF
    ├── INVENTORY_STAFF
    ├── DOCTOR
    └── PATIENT
```

### JWT Token Custom Claims

```json
{
  "sub": "user-uuid",
  "preferred_username": "john.doe@lab.com",
  "given_name": "John",
  "family_name": "Doe",
  "email": "john.doe@lab.com",
  "realm_access": {
    "roles": ["LAB_TECHNICIAN"]
  },
  "organizationId": "org-uuid",
  "branchIds": ["branch-uuid-1", "branch-uuid-2"],
  "employeeId": "EMP-001",
  "exp": 1700000000,
  "iat": 1699996400
}
```

### OAuth2 Flows

| Client | Flow | Use Case |
|--------|------|---------|
| Angular SPA | Authorization Code + PKCE | Frontend login |
| Doctor Portal | Authorization Code + PKCE | External doctor access |
| Patient Portal | Resource Owner Password (OTP) | Patient OTP login |
| Instrument Interface | Client Credentials | Service-to-service |

## Authorization: Role-Based Access Control (RBAC)

### Permission Matrix

| Permission | SUPER | ORG | BRANCH | PATH | LAB_TECH | RECEPT | PHLEBOTOMIST | BILLING | DOCTOR | PATIENT |
|-----------|-------|-----|--------|------|----------|--------|--------------|---------|--------|---------|
| Register patient | ✓ | ✓ | ✓ | | | ✓ | ✓ | | | |
| Create order | ✓ | ✓ | ✓ | | | ✓ | | | ✓ | |
| Collect sample | ✓ | ✓ | ✓ | | | | ✓ | | | |
| Receive sample | ✓ | ✓ | ✓ | ✓ | ✓ | | | | | |
| Enter result | ✓ | ✓ | ✓ | ✓ | ✓ | | | | | |
| Authorize result | ✓ | ✓ | | ✓ | | | | | | |
| Release report | ✓ | ✓ | ✓ | ✓ | | ✓ | | | | |
| Process payment | ✓ | ✓ | ✓ | | | ✓ | | ✓ | | |
| Manage masters | ✓ | ✓ | ✓ | | | | | | | |
| View own reports | | | | | | | | | ✓ | ✓ |
| Cross-branch access | ✓ | ✓ | | | | | | | | |

## Multi-Branch Security

```java
@Component
public class BranchSecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        String branchIdHeader = req.getHeader("X-Branch-Id");
        if (branchIdHeader == null) return true; // Let controller handle

        UUID branchId = UUID.fromString(branchIdHeader);

        // Extract allowed branches from JWT
        JwtAuthenticationToken jwt = (JwtAuthenticationToken) SecurityContextHolder
            .getContext().getAuthentication();
        List<String> allowedBranches = jwt.getToken()
            .getClaimAsStringList("branchIds");

        boolean isSuperAdmin = jwt.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"));

        if (!isSuperAdmin && !allowedBranches.contains(branchId.toString())) {
            res.setStatus(403);
            throw new BranchAccessDeniedException(branchId);
        }

        BranchContextHolder.setCurrentBranchId(branchId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res,
                                Object handler, Exception ex) {
        BranchContextHolder.clear(); // Prevent ThreadLocal leak
    }
}
```

## Data Protection

### Encryption in Transit
- TLS 1.2+ enforced via Nginx
- HSTS headers: `Strict-Transport-Security: max-age=63072000`
- Certificate management: Let's Encrypt / internal CA

### Encryption at Rest
- PostgreSQL: Transparent Data Encryption (TDE) or filesystem encryption
- MinIO: Server-side encryption (SSE-S3)
- Redis: Encrypted at OS level

### PHI (Protected Health Information) Rules
- PHI never logged in application logs
- PHI never in error messages returned to client
- PHI access logged in audit_trail
- Data export requires SUPER_ADMIN or ORG_ADMIN

## HIPAA Compliance Checklist

- [x] Access controls (Keycloak RBAC)
- [x] Audit controls (audit_trail table, 7-year retention)
- [x] Integrity controls (soft delete, versioning)
- [x] Transmission security (TLS)
- [x] Authentication (OAuth2 + MFA optional)
- [x] Automatic logoff (session timeout)
- [x] Encryption (TLS + at-rest)
- [x] Backup (automated PostgreSQL backup)
- [ ] BAA (Business Associate Agreement — legal document)
- [ ] Risk assessment (annual)

## Security Headers (Nginx)

```nginx
add_header X-Frame-Options "SAMEORIGIN" always;
add_header X-Content-Type-Options "nosniff" always;
add_header X-XSS-Protection "1; mode=block" always;
add_header Referrer-Policy "strict-origin-when-cross-origin" always;
add_header Content-Security-Policy "default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline';" always;
add_header Strict-Transport-Security "max-age=63072000; includeSubDomains" always;
```

## Rate Limiting

| Endpoint | Limit | Window |
|----------|-------|--------|
| Login | 5 requests | 1 minute |
| All API | 100 requests | 1 second |
| Report download | 10 requests | 1 minute |
| OTP generation | 3 requests | 5 minutes |
