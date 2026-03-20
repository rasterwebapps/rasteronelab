# Backend — LIS Spring Boot Project

## Build Commands
```bash
./gradlew build                          # Build all modules
./gradlew test                           # Run all tests
./gradlew :lis-patient:bootRun           # Run specific module
./gradlew :lis-patient:test              # Test specific module
./gradlew clean build                    # Clean build
./gradlew jacocoTestReport               # Generate coverage report (target: 80%)
./gradlew checkstyleMain                 # Lint check
./gradlew :lis-patient:dependencies      # Show dependency tree
```

## Key Dependencies
- Spring Boot 3.4.1 (BOM manages versions)
- Spring Data JPA + Hibernate 6
- Spring Security + Keycloak adapter
- Spring Cloud Gateway (lis-gateway)
- Flyway (DB migrations)
- MapStruct 1.6 (DTO mapping)
- UUID Creator (UUID v7 generation)
- Testcontainers (integration tests)
- JUnit 5 + Mockito + AssertJ

## 10-Step Entity Creation Process

When creating a new domain entity, follow these 10 steps in order:

1. **Create Flyway migration** (DB first!)
   - File: `src/main/resources/db/migration/V{YYYYMMDD_HHmm}__{description}.sql`
   - Include: UUID PK, branch_id, domain columns, audit columns, soft delete, indexes

2. **Create JPA Entity** (`domain/model/{Entity}.java`)
   - Extend `BaseEntity` (never add id, branchId, audit fields manually)
   - Add domain-specific fields only

3. **Create Repository** (`domain/repository/{Entity}Repository.java`)
   - Extend `JpaRepository<{Entity}, UUID>`
   - Add `findByIdAndBranchIdAndIsDeletedFalse()` and `findAllByBranchIdAndIsDeletedFalse()`

4. **Create Request DTO** (`api/dto/{Entity}Request.java`)
   - Add all user-provided fields with validation annotations
   - Include: no-arg constructor, all-arg constructor, getters, setters, builder pattern

5. **Create Response DTO** (`api/dto/{Entity}Response.java`)
   - Add all fields users need to see (including audit fields)

6. **Create MapStruct Mapper** (`api/mapper/{Entity}Mapper.java`)
   - `toEntity(Request)`, `toResponse(Entity)`, `toResponseList(List<Entity>)`

7. **Create Use Case Interface** (`application/port/in/{Entity}UseCase.java`)
   - Define all operations as interface methods

8. **Create Service Implementation** (`application/service/{Entity}ServiceImpl.java`)
   - @Service, @Transactional
   - Inject repository + mapper via constructor
   - Use BranchContextHolder.getCurrentBranchId()

9. **Create REST Controller** (`api/rest/{Entity}Controller.java`)
   - @RestController, @RequestMapping, @PreAuthorize on each method
   - Wrap all responses in ApiResponse<T>

10. **Write Tests**
    - Unit: `{Entity}ServiceTest.java` (Mockito, no Spring context)
    - Integration: `{Entity}IntegrationTest.java` (Testcontainers, MockMvc)

## BaseEntity Template
All entities MUST extend BaseEntity:
```java
@Entity
@Table(name = "patient")
public class Patient extends BaseEntity {
    // domain fields only — id, branchId, audit fields come from BaseEntity
    private String fullName;
    private String uhid;
    // ... manual getters and setters
}
```

## ApiResponse Usage
```java
// In controller:
return ResponseEntity.ok(ApiResponse.success(patientMapper.toResponse(patient)));
return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Patient created", response));
return ResponseEntity.badRequest().body(ApiResponse.error("LIS-PAT-001", "Patient not found"));
```

## CRITICAL Rules
1. **NEVER** expose JPA entities in API responses — always use DTOs
2. **ALWAYS** use BigDecimal for monetary values AND lab numeric values
3. **ALWAYS** use constructor injection (not @Autowired field injection)
4. **ALWAYS** set branchId from BranchContextHolder in services
5. **NEVER** use double or float for lab or financial calculations
6. **ALWAYS** add @Transactional on service methods that write to DB
7. **ALWAYS** soft delete (isDeleted=true + deletedAt) — never hard delete
8. **ALWAYS** filter queries by branchId AND isDeleted=false

## Result Type Enum & Strategy
See CLAUDE.md at root for full Result Type Strategy Pattern code.

## Module Dependencies
- All feature modules depend on `:lis-core`
- Modules communicate via Spring Events (ApplicationEventPublisher)
- No direct service calls between modules
- Shared data via events or REST calls through the gateway
