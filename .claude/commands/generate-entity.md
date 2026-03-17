# Generate Entity Stack — /generate-entity

Generate a complete entity stack following RasterOneLab hexagonal architecture.

## Usage
```
/generate-entity {EntityName} {module} {fields...}
```

## Example
```
/generate-entity TestParameter admin "name:String, code:String, unit:String, dataType:ParameterDataType, decimalPlaces:Integer, branchId:UUID"
```

## What Gets Generated (10 artifacts)

### 1. Domain Entity (`domain/model/{EntityName}.java`)
- Extends `BaseEntity` (includes id, branchId, createdAt, updatedAt, createdBy, updatedBy, isDeleted, deletedAt)
- `@Entity`, `@Table(name = "snake_case_table_name")`
- All fields with proper JPA annotations
- Validation annotations (`@NotNull`, `@NotBlank`, etc.)
- `branchId` MUST be present

### 2. Repository (`domain/repository/{EntityName}Repository.java`)
- Extends `JpaRepository<{EntityName}, UUID>`
- `findAllByBranchIdAndIsDeletedFalse(UUID branchId, Pageable pageable)`
- `findByIdAndBranchIdAndIsDeletedFalse(UUID id, UUID branchId)`
- Custom JPQL queries as needed

### 3. Request DTO (`api/dto/{EntityName}Request.java`)
- All fields from entity (except audit fields)
- Bean Validation annotations
- Lombok `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`

### 4. Response DTO (`api/dto/{EntityName}Response.java`)
- All entity fields including audit fields
- Lombok `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`

### 5. MapStruct Mapper (`api/mapper/{EntityName}Mapper.java`)
- `@Mapper(componentModel = "spring", uses = {})`
- `toEntity({EntityName}Request)` method
- `toResponse({EntityName})` method
- `toResponseList(List<{EntityName}>)` method

### 6. Use Case Interface (`application/port/in/{EntityName}UseCase.java`)
- `create{EntityName}(UUID branchId, {EntityName}Request request)`
- `update{EntityName}(UUID branchId, UUID id, {EntityName}Request request)`
- `get{EntityName}(UUID branchId, UUID id)`
- `getAll{EntityName}s(UUID branchId, Pageable pageable)`
- `delete{EntityName}(UUID branchId, UUID id)`

### 7. Service Implementation (`application/service/{EntityName}ServiceImpl.java`)
- `@Service`, `@Transactional`
- Constructor injection only
- `BranchContextHolder` usage
- Spring Events for cross-module communication
- Proper exception handling (NotFoundException, ValidationException)

### 8. REST Controller (`api/rest/{EntityName}Controller.java`)
- `@RestController`, `@RequestMapping("/api/v1/{kebab-case-plural}")`
- `@PreAuthorize` on each method
- `ApiResponse<T>` wrapper for all responses
- Full CRUD: GET all (paginated), GET by id, POST, PUT, DELETE
- OpenAPI `@Operation`, `@ApiResponse` annotations

### 9. Flyway Migration
- File: `resources/db/migration/V{YYYYMMDD_HHmm}__create_{table_name}.sql`
- `CREATE TABLE` with all columns (snake_case)
- UUID PK, branch_id, all entity fields
- Audit columns (created_at, updated_at, created_by, updated_by, is_deleted, deleted_at)
- Indexes: branch_id, is_deleted, unique constraints

### 10. Tests
- **Unit test** (`{EntityName}ServiceTest.java`): Test all service methods with Mockito
- **Integration test** (`{EntityName}IntegrationTest.java`): Testcontainers + MockMvc full flow

## Template Code

### BaseEntity (reference)
```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
public abstract class BaseEntity {
    @Id
    private UUID id = UuidCreator.getTimeOrderedEpoch(); // UUID v7
    
    @Column(nullable = false)
    private UUID branchId;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    @CreatedBy
    private String createdBy;
    
    @LastModifiedBy
    private String updatedBy;
    
    @Column(nullable = false)
    private Boolean isDeleted = false;
    
    private LocalDateTime deletedAt;
}
```

### ApiResponse wrapper (reference)
```java
@Data @Builder
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String timestamp = LocalDateTime.now().toString();
    
    public static <T> ApiResponse<T> success(T data) { ... }
    public static <T> ApiResponse<T> success(String message, T data) { ... }
    public static <T> ApiResponse<T> error(String message) { ... }
}
```
