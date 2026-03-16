# Security Audit — /security-audit

Perform a security review of LIS code changes.

## Usage
```
/security-audit {file-or-module}
```

## Security Checklist

### 1. SQL Injection Prevention
- [ ] All DB queries use parameterized queries or JPA (no String concatenation)
- [ ] JPQL uses `:namedParams` (not string interpolation)
- [ ] Native queries use `?1` or `:param` (not `+` concatenation)

### 2. XSS Prevention
- [ ] HTML output is escaped (Thymeleaf auto-escapes by default)
- [ ] Angular templates are safe (no `[innerHTML]` with user data)
- [ ] User inputs sanitized before storage

### 3. Authentication & Authorization
- [ ] All endpoints have `@PreAuthorize` annotations
- [ ] JWT tokens validated on every request
- [ ] Token expiry enforced
- [ ] Role hierarchy properly configured
- [ ] Keycloak realm settings verified (token lifetime, etc.)

### 4. HIPAA / PHI Protection
- [ ] Patient data encrypted in transit (HTTPS only)
- [ ] Patient data encrypted at rest (database encryption)
- [ ] PHI not logged in application logs
- [ ] PHI not included in error messages
- [ ] Minimum necessary data principle applied
- [ ] Access to PHI requires appropriate role

### 5. Multi-Branch Data Isolation
- [ ] All queries filter by `branchId`
- [ ] `BranchContextHolder` used consistently
- [ ] Cross-branch access only for SUPER_ADMIN
- [ ] Branch token validated in JWT claims

### 6. Audit Trail
- [ ] All CRUD operations logged to audit_trail
- [ ] Login/logout events logged
- [ ] Result authorization events logged
- [ ] Report access logged
- [ ] Critical value notifications logged

### 7. Input Validation
- [ ] All DTOs have Bean Validation annotations
- [ ] File uploads validated (type, size)
- [ ] Date ranges validated
- [ ] Numeric ranges validated

### 8. Sensitive Data Handling
- [ ] Passwords never stored in plain text
- [ ] API keys stored in environment variables (not source code)
- [ ] No secrets in git history
- [ ] `.env` files in `.gitignore`

### 9. Rate Limiting
- [ ] API gateway rate limiting configured
- [ ] Login endpoint rate limited
- [ ] Report download rate limited

### 10. CORS Configuration
- [ ] CORS origins whitelisted (not `*` in production)
- [ ] Credentials included only for trusted origins
