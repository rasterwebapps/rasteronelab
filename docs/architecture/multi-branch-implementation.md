# Multi-Branch Implementation Guide — RasterOneLab LIS

## Strategy: Row-Level Filtering with branch_id Column

Every table has `branch_id UUID NOT NULL` column. All queries filter by current branch_id.

## BranchContextHolder (ThreadLocal)

```java
package com.rasteronelab.lis.core.infrastructure;

public class BranchContextHolder {
    private static final ThreadLocal<UUID> BRANCH_ID = new ThreadLocal<>();
    private static final ThreadLocal<UUID> ORG_ID = new ThreadLocal<>();

    public static void setCurrentBranchId(UUID branchId) { BRANCH_ID.set(branchId); }
    public static UUID getCurrentBranchId() { return BRANCH_ID.get(); }
    public static UUID requireCurrentBranchId() {
        UUID id = BRANCH_ID.get();
        if (id == null) throw new IllegalStateException("No branch context set");
        return id;
    }

    public static void setCurrentOrgId(UUID orgId) { ORG_ID.set(orgId); }
    public static UUID getCurrentOrgId() { return ORG_ID.get(); }

    public static void clear() {
        BRANCH_ID.remove();
        ORG_ID.remove();
    }
}
```

## BranchInterceptor (Spring MVC)

```java
@Component
public class BranchInterceptor implements HandlerInterceptor {

    private static final String BRANCH_HEADER = "X-Branch-Id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String branchIdStr = request.getHeader(BRANCH_HEADER);
        if (branchIdStr == null || branchIdStr.isBlank()) {
            return true; // Some endpoints don't require branch (e.g., /actuator, /public)
        }

        UUID branchId;
        try {
            branchId = UUID.fromString(branchIdStr);
        } catch (IllegalArgumentException e) {
            response.setStatus(400);
            response.getWriter().write("{\"errorCode\":\"LIS-SYS-007\",\"message\":\"Invalid Branch ID format\"}");
            return false;
        }

        // Validate user has access to this branch
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwt) {
            List<String> allowedBranches = jwt.getToken().getClaimAsStringList("branchIds");
            boolean isSuperAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"));

            if (!isSuperAdmin && (allowedBranches == null || !allowedBranches.contains(branchIdStr))) {
                throw new BranchAccessDeniedException(branchId);
            }
        }

        BranchContextHolder.setCurrentBranchId(branchId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        BranchContextHolder.clear(); // CRITICAL: Prevent ThreadLocal memory leaks
    }
}
```

## BranchAwareRepository (Spring Data JPA)

```java
@NoRepositoryBean
public interface BranchAwareRepository<T, ID> extends JpaRepository<T, ID> {

    List<T> findAllByBranchIdAndIsDeletedFalse(UUID branchId);

    Page<T> findAllByBranchIdAndIsDeletedFalse(UUID branchId, Pageable pageable);

    Optional<T> findByIdAndBranchIdAndIsDeletedFalse(ID id, UUID branchId);

    default UUID requireBranchId() {
        return BranchContextHolder.requireCurrentBranchId();
    }
}
```

## Service Layer Pattern

```java
@Service
@Transactional
public class PatientServiceImpl implements PatientUseCase {

    private final PatientRepository patientRepository;

    @Override
    public PatientResponse getPatient(UUID id) {
        UUID branchId = BranchContextHolder.requireCurrentBranchId();  // ← Always filter
        Patient patient = patientRepository
            .findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
            .orElseThrow(() -> new NotFoundException("Patient", id));
        return patientMapper.toResponse(patient);
    }

    @Override
    public Page<PatientResponse> listPatients(Pageable pageable) {
        UUID branchId = BranchContextHolder.requireCurrentBranchId();
        return patientRepository
            .findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
            .map(patientMapper::toResponse);
    }
}
```

## Redis Caching with Branch Prefix

```java
@Service
public class BranchCacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    // Key format: branch:{branchId}:{entityType}:{entityId}
    private String key(String entityType, String id) {
        UUID branchId = BranchContextHolder.requireCurrentBranchId();
        return String.format("branch:%s:%s:%s", branchId, entityType, id);
    }

    public void put(String entityType, String id, Object value) {
        redisTemplate.opsForValue().set(key(entityType, id), value,
            Duration.ofMinutes(30));
    }

    public Optional<Object> get(String entityType, String id) {
        Object value = redisTemplate.opsForValue().get(key(entityType, id));
        return Optional.ofNullable(value);
    }

    public void invalidateBranch(UUID branchId) {
        Set<String> keys = redisTemplate.keys("branch:" + branchId + ":*");
        if (keys != null) redisTemplate.delete(keys);
    }
}
```

## Angular BranchInterceptor

```typescript
@Injectable()
export class BranchInterceptor implements HttpInterceptor {
    private branchService = inject(BranchService);

    intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        const currentBranchId = this.branchService.currentBranchId();
        if (!currentBranchId) return next.handle(req);

        const withBranch = req.clone({
            headers: req.headers.set('X-Branch-Id', currentBranchId)
        });
        return next.handle(withBranch);
    }
}
```

## Cross-Branch Access Rules

| Scenario | Allowed Roles | Implementation |
|---------|-------------|----------------|
| Transfer sample between branches | BRANCH_ADMIN+ | Dual write with source/dest branch_id |
| View reports from another branch | SUPER_ADMIN only | X-Branch-Id: target-branch |
| Consolidate org-level reports | ORG_ADMIN+ | Loop over all branch_ids |
| New branch provisioning | SUPER_ADMIN | Admin API, seeds masters from org defaults |

## New Branch Provisioning Checklist

1. Create branch record in `branch` table
2. Assign branch to organization
3. Copy organization-level masters to branch level
4. Configure branch working hours
5. Assign branch-specific number series (UHID, Order, Sample)
6. Configure report template
7. Assign users to branch in Keycloak (add branchId to branchIds claim)
8. Create branch partition in partitioned tables
9. Configure instrument connections
10. Test end-to-end with test patient

## Branch-Specific Number Series

```sql
CREATE TABLE branch_number_series (
    id          UUID PRIMARY KEY,
    branch_id   UUID NOT NULL UNIQUE,
    uhid_prefix VARCHAR(10) NOT NULL,
    uhid_seq    BIGINT NOT NULL DEFAULT 0,
    order_prefix VARCHAR(10) NOT NULL,
    order_seq   BIGINT NOT NULL DEFAULT 0,
    sample_prefix VARCHAR(10) NOT NULL,
    sample_seq  BIGINT NOT NULL DEFAULT 0,
    invoice_prefix VARCHAR(10) NOT NULL,
    invoice_seq BIGINT NOT NULL DEFAULT 0
);

-- Example: DEL branch
-- UHID: DEL-001234
-- Order: ORD-DEL-20240115-0001
-- Sample: SMP-DEL-20240115-001234
```
