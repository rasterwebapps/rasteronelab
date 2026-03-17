# Coding Standards — RasterOneLab LIS

## Java / Backend Standards

### Package Structure (Hexagonal Architecture)
```
com.rasteronelab.lis.{module}/
├── api/
│   ├── rest/           # @RestController classes
│   ├── dto/            # Request + Response DTOs (ONLY these are exposed)
│   └── mapper/         # MapStruct interfaces
├── application/
│   ├── port/
│   │   └── in/         # Use case interfaces
│   └── service/        # @Service implementations
├── domain/
│   ├── model/          # JPA @Entity classes (NEVER expose in API)
│   └── repository/     # Spring Data JPA repository interfaces
└── infrastructure/     # External concerns: messaging, storage, etc.
```

### Naming Conventions

| Element | Convention | Example |
|---------|-----------|---------|
| Classes | PascalCase | `PatientServiceImpl` |
| Methods | camelCase | `findByUhid()` |
| Fields | camelCase | `branchId` |
| Constants | UPPER_SNAKE | `MAX_RETRY_COUNT` |
| REST endpoints | kebab-case | `/patients/{id}/test-orders` |
| DB tables | snake_case | `test_order` |
| DB columns | snake_case | `branch_id` |
| DB indexes | `idx_{table}_{column}` | `idx_patient_branch_id` |

### Entity Rules (CRITICAL)

```java
@Entity
@Table(name = "patient")
public class Patient extends BaseEntity {
    // ✅ All entities extend BaseEntity (has id, branchId, audit fields, soft delete)
    // ✅ UUID primary key (auto-generated)
    // ✅ branchId MUST be present
    // ✅ No @JsonIgnore needed — entities NEVER returned in API
    // ✅ Lombok: @Getter @Setter @NoArgsConstructor (NO @Data for entities)
    // ✅ Use BigDecimal for monetary values (NEVER double or float)
    // ✅ Use LocalDate/LocalDateTime (NEVER java.util.Date)
}
```

### Service Rules

```java
@Service
@Transactional  // ← ALWAYS on service class (or individual methods)
@RequiredArgsConstructor  // ← Constructor injection (NEVER @Autowired)
public class PatientServiceImpl implements PatientUseCase {
    
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final AuditService auditService;
    
    @Override
    @Transactional(readOnly = true)  // ← Read operations
    public PatientResponse getPatient(UUID id) {
        UUID branchId = BranchContextHolder.requireCurrentBranchId();  // ← Always
        return patientRepository
            .findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
            .map(patientMapper::toResponse)
            .orElseThrow(() -> new NotFoundException("Patient", id));
    }
}
```

### Controller Rules

```java
@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Tag(name = "Patient", description = "Patient management APIs")
public class PatientController {

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'LAB_TECHNICIAN', 'PATHOLOGIST', 'ADMIN')")
    @Operation(summary = "Get patient by ID")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatient(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(patientUseCase.getPatient(id)));
    }
}
```

### Error Handling

```java
// NEVER return raw exceptions
// ALWAYS use LIS exception hierarchy:
throw new NotFoundException("Patient", id);           // 404
throw new ValidationException("LIS-PAT-003", "...");  // 422
throw new UnauthorizedException("LIS-SEC-001", "..."); // 403
throw new BranchAccessDeniedException(branchId);       // 403

// Global exception handler in lis-core catches all and wraps in ApiResponse
```

### Monetary Values

```java
// ✅ CORRECT
BigDecimal amount = new BigDecimal("1200.50");
BigDecimal tax = amount.multiply(new BigDecimal("0.18")).setScale(2, RoundingMode.HALF_UP);

// ❌ WRONG — floating point precision errors
double amount = 1200.50;
float tax = (float)(amount * 0.18);
```

### Logging

```java
@Slf4j  // Lombok
public class PatientServiceImpl {
    
    // ✅ CORRECT
    log.info("Creating patient for branch {}", branchId);
    log.error("Failed to create patient: {}", e.getMessage(), e);
    
    // ❌ NEVER log PHI (patient data)
    log.info("Patient name: {}", patient.getFullName());  // WRONG
}
```

---

## Angular / TypeScript Standards

### Component Structure (Angular 19 Standalone)

```typescript
// ✅ CORRECT — standalone, Signals, inject()
@Component({
  selector: 'app-patient-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, RouterLink],  // Only what's needed
  template: `
    @if (isLoading()) {
      <app-loading-spinner />
    } @else {
      <table mat-table [dataSource]="patients()">
        @for (patient of patients(); track patient.id) {
          <tr mat-row></tr>
        }
      </table>
    }
  `
})
export class PatientListComponent {
  // inject() instead of constructor injection
  private patientService = inject(PatientService);
  
  // Signals instead of properties
  patients = signal<Patient[]>([]);
  isLoading = signal(false);
  
  // computed() for derived state
  totalPatients = computed(() => this.patients().length);
  
  // NO constructor injection
  // NO *ngIf / *ngFor
  // NO Moment.js (use date-fns)
  // NO RxJS Subject for state (use Signals)
  // NO any type
}
```

### Signal Store Pattern

```typescript
// patient.store.ts
export const PatientStore = signalStore(
  withState<PatientState>({ patients: [], loading: false, error: null }),
  withComputed(({ patients }) => ({
    totalCount: computed(() => patients().length),
  })),
  withMethods((store, patientService = inject(PatientService)) => ({
    loadPatients: rxMethod<void>(pipe(
      tap(() => patchState(store, { loading: true })),
      switchMap(() => patientService.getAll()),
      tapResponse(
        (patients) => patchState(store, { patients, loading: false }),
        (error) => patchState(store, { error: error.message, loading: false })
      )
    )),
  }))
);
```

### HTTP Service Pattern

```typescript
@Injectable({ providedIn: 'root' })
export class PatientService {
  private http = inject(HttpClient);
  
  getAll(): Observable<ApiResponse<Patient[]>> {
    return this.http.get<ApiResponse<Patient[]>>('/api/v1/patients');
  }
  
  getById(id: string): Observable<ApiResponse<Patient>> {
    return this.http.get<ApiResponse<Patient>>(`/api/v1/patients/${id}`);
  }
}
```

---

## SQL Standards

### Table Naming
- snake_case: `test_order`, `order_line_item`, `sample_collection`
- NO abbreviations (except established: `id`, `TAT`, `QC`)
- Plural for entities: `patients`, `orders`

### Migration File Naming
`{YYYYMMDD}{HH}{NNN}-{action}-{entity}.xml`  
Example: `2024011501001-create-patient-table.xml`

### Column Rules
- ALL tables: `id UUID`, `branch_id UUID NOT NULL`, `created_at TIMESTAMPTZ`, `updated_at TIMESTAMPTZ`, `created_by VARCHAR(100)`, `updated_by VARCHAR(100)`, `is_deleted BOOLEAN DEFAULT FALSE`, `deleted_at TIMESTAMPTZ`
- Monetary: `NUMERIC(12,2)` — NEVER `FLOAT` or `REAL`
- Text without limit: `TEXT`; bounded text: `VARCHAR(N)`
- Boolean: `BOOLEAN NOT NULL DEFAULT FALSE`

---

## Git Standards

### Branch Naming
```
feature/LIS-{ticket-number}-{kebab-description}
bugfix/LIS-{ticket-number}-{description}
hotfix/LIS-{ticket-number}-{description}
```

### Commit Messages
```
feat(patient): add UHID duplicate detection [LIS-123]
fix(result): fix delta check % calculation [LIS-456]
refactor(core): extract BranchContextHolder to core [LIS-789]
docs(api): update patient API specification
test(sample): add sample rejection integration tests
```

### PR Checklist
- [ ] `branchId` filter in all repository queries
- [ ] `@Transactional` on service methods
- [ ] No JPA entities exposed in API responses
- [ ] MapStruct mapper used (no manual mapping)
- [ ] `@PreAuthorize` on all controller methods
- [ ] `BigDecimal` for monetary values
- [ ] Unit tests for service layer
- [ ] Integration test (Testcontainers) for repository
- [ ] Flyway migration if entity changed
- [ ] Audit logging for PHI access
