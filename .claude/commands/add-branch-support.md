# Add Multi-Branch Support — /add-branch-support

Add multi-branch support to existing code that's missing it.

## Usage
```
/add-branch-support {module} {entityName}
```

## Checklist Applied

### 1. Entity: Add branchId Field
```java
// Before
@Entity
public class Department {
    @Id private UUID id;
    private String name;
}

// After
@Entity
public class Department extends BaseEntity {  // BaseEntity includes branchId
    private String name;
    // branchId inherited from BaseEntity
}
```

### 2. Repository: Add Branch Filters
```java
// Add branch-filtered methods
Optional<Department> findByIdAndBranchIdAndIsDeletedFalse(UUID id, UUID branchId);
Page<Department> findAllByBranchIdAndIsDeletedFalse(UUID branchId, Pageable pageable);
List<Department> findAllByBranchIdAndIsDeletedFalse(UUID branchId);
boolean existsByCodeAndBranchIdAndIsDeletedFalse(String code, UUID branchId);
```

### 3. Service: Add BranchContextHolder Usage
```java
@Override
@Transactional(readOnly = true)
public DepartmentResponse getDepartment(UUID id) {
    UUID branchId = BranchContextHolder.getCurrentBranchId(); // Get from ThreadLocal
    return departmentRepository
        .findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
        .map(departmentMapper::toResponse)
        .orElseThrow(() -> new NotFoundException("Department", id));
}
```

### 4. Controller: Add X-Branch-Id Header
```java
// BranchInterceptor handles this automatically, but add to OpenAPI docs
@Operation(parameters = {
    @Parameter(name = "X-Branch-Id", in = ParameterIn.HEADER, required = true,
               description = "Branch UUID for multi-branch context")
})
```

### 5. Frontend: BranchInterceptor Already Adds Header
```typescript
// BranchInterceptor automatically adds X-Branch-Id to all HTTP requests
// Verify branch is set in BranchService before making API calls
private readonly branchService = inject(BranchService);

if (!this.branchService.currentBranch()) {
    this.router.navigate(['/select-branch']);
    return;
}
```

### 6. Database Migration: Add branch_id Column
```xml
<changeSet id="2024-01-15-add-branch-id-to-department" author="lis-system">
    <addColumn tableName="department">
        <column name="branch_id" type="UUID">
            <constraints nullable="false"/>
        </column>
    </addColumn>
    <createIndex indexName="idx_department_branch_id" tableName="department">
        <column name="branch_id"/>
    </createIndex>
    <rollback>
        <dropColumn tableName="department" columnName="branch_id"/>
    </rollback>
</changeSet>
```

### 7. Branch Access Validation
```java
// In service layer, validate cross-branch access
private void validateBranchAccess(UUID requestedBranchId) {
    UUID currentBranchId = BranchContextHolder.getCurrentBranchId();
    if (!currentBranchId.equals(requestedBranchId)) {
        SecurityContext security = SecurityContextHolder.getContext();
        boolean isSuperAdmin = security.getAuthentication().getAuthorities()
            .stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"));
        if (!isSuperAdmin) {
            throw new BranchAccessDeniedException(requestedBranchId);
        }
    }
}
```
