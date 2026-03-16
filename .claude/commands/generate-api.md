# Generate API Endpoint — /generate-api

Generate a complete REST API endpoint with all layers.

## Usage
```
/generate-api {module} {resource} {HTTP_METHOD} {path} {description}
```

## Example
```
/generate-api patient patient-search GET /api/v1/patients/search "Search patients by name, phone, UHID with branch filtering"
```

## What Gets Generated

### 1. Controller Method
```java
@GetMapping("/search")
@PreAuthorize("hasAnyRole('RECEPTIONIST', 'LAB_TECHNICIAN', 'PATHOLOGIST', 'ADMIN')")
@Operation(summary = "Search patients", description = "Search patients by name, phone, or UHID within branch")
public ResponseEntity<ApiResponse<PagedResponse<PatientResponse>>> searchPatients(
    @RequestParam(required = false) String query,
    @RequestParam(required = false) String uhid,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
) {
    UUID branchId = BranchContextHolder.getCurrentBranchId();
    Pageable pageable = PageRequest.of(page, size);
    PagedResponse<PatientResponse> result = patientUseCase.searchPatients(branchId, query, uhid, pageable);
    return ResponseEntity.ok(ApiResponse.success(result));
}
```

### 2. Use Case Interface Method
```java
PagedResponse<PatientResponse> searchPatients(UUID branchId, String query, String uhid, Pageable pageable);
```

### 3. Service Implementation
```java
@Override
@Transactional(readOnly = true)
public PagedResponse<PatientResponse> searchPatients(UUID branchId, String query, String uhid, Pageable pageable) {
    Page<Patient> patients = patientRepository.searchByBranch(branchId, query, uhid, pageable);
    return PagedResponse.of(patients.map(patientMapper::toResponse));
}
```

### 4. Repository Query
```java
@Query("""
    SELECT p FROM Patient p 
    WHERE p.branchId = :branchId 
    AND p.isDeleted = false
    AND (:query IS NULL OR LOWER(p.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
         OR p.phoneNumber LIKE CONCAT('%', :query, '%'))
    AND (:uhid IS NULL OR p.uhid = :uhid)
    ORDER BY p.createdAt DESC
    """)
Page<Patient> searchByBranch(
    @Param("branchId") UUID branchId,
    @Param("query") String query,
    @Param("uhid") String uhid,
    Pageable pageable
);
```

### 5. OpenAPI Documentation
- `@Operation` with summary, description, security requirements
- `@Parameter` for each query/path param
- `@ApiResponse` for 200, 400, 401, 403, 404

### 6. Test
```java
@Test
void searchPatients_withValidBranchId_returnsFilteredResults() { ... }

@Test  
void searchPatients_withoutBranchId_throwsBranchAccessDeniedException() { ... }
```
