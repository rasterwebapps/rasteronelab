# Testing Strategy — RasterOneLab LIS

## Testing Pyramid

```
         ╱──────────────────────╲
        ╱   E2E Tests (Cypress)   ╲  ← Slow, few, high confidence
       ╱      ~50 scenarios        ╲
      ╱────────────────────────────╲
     ╱   Integration Tests          ╲ ← Medium, Testcontainers
    ╱   (MockMvc + Testcontainers)   ╲ ~300 tests
   ╱─────────────────────────────────╲
  ╱   Unit Tests (JUnit + Mockito)    ╲ ← Fast, many, small scope
 ╱          ~1000 tests               ╲ 80% coverage target
╱──────────────────────────────────────╲
```

## Coverage Targets

| Layer | Coverage Target |
|-------|----------------|
| Service layer (business logic) | 90% |
| Controller layer | 80% |
| Repository layer | 70% |
| Utility/Helper classes | 85% |
| **Overall Backend** | **80%** |
| Angular components | 75% |
| Angular services | 85% |
| **Overall Frontend** | **75%** |

## Backend Testing

### Unit Tests (JUnit 5 + Mockito)

```java
@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock PatientRepository patientRepository;
    @Mock PatientMapper patientMapper;
    @Mock AuditService auditService;
    @InjectMocks PatientServiceImpl patientService;

    @BeforeEach
    void setup() {
        // Set branch context for each test
        BranchContextHolder.setCurrentBranchId(UUID.randomUUID());
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
    }

    @Test
    @DisplayName("Should return patient when found for current branch")
    void getPatient_WhenFound_ReturnsResponse() {
        // Arrange
        UUID patientId = UUID.randomUUID();
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Patient patient = TestDataFactory.buildPatient(patientId, branchId);
        PatientResponse expected = TestDataFactory.buildPatientResponse(patientId);

        when(patientRepository.findByIdAndBranchIdAndIsDeletedFalse(patientId, branchId))
            .thenReturn(Optional.of(patient));
        when(patientMapper.toResponse(patient)).thenReturn(expected);

        // Act
        PatientResponse result = patientService.getPatient(patientId);

        // Assert
        assertThat(result.id()).isEqualTo(patientId);
        verify(auditService).logView("PATIENT", patientId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when patient not found")
    void getPatient_WhenNotFound_ThrowsNotFoundException() {
        UUID id = UUID.randomUUID();
        when(patientRepository.findByIdAndBranchIdAndIsDeletedFalse(any(), any()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.getPatient(id))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Patient");
    }
}
```

### Integration Tests (Testcontainers + MockMvc)

```java
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class PatientControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
        .withDatabaseName("lis_test");

    @Container
    static RedisContainer redis = new RedisContainer("redis:7");

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "RECEPTIONIST")
    void registerPatient_WithValidData_Returns201() throws Exception {
        PatientRequest request = new PatientRequest("John Doe", "MALE", 
            LocalDate.of(1979, 3, 15), "+919876543210");

        mockMvc.perform(post("/api/v1/patients")
                .header("X-Branch-Id", TEST_BRANCH_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.uhid").isNotEmpty())
            .andExpect(jsonPath("$.data.fullName").value("John Doe"));
    }
}
```

### Test Data Factory

```java
public class TestDataFactory {

    public static Patient buildPatient(UUID id, UUID branchId) {
        Patient p = new Patient();
        p.setId(id);
        p.setBranchId(branchId);
        p.setUhid("TST-001234");
        p.setFullName("John Doe");
        p.setGender(Gender.MALE);
        p.setDateOfBirth(LocalDate.of(1979, 3, 15));
        p.setPhoneNumber("+919876543210");
        return p;
    }
}
```

## Frontend Testing

### Angular Component Tests (Jasmine + TestBed)

```typescript
describe('PatientListComponent', () => {
  let component: PatientListComponent;
  let fixture: ComponentFixture<PatientListComponent>;
  let patientService: jasmine.SpyObj<PatientService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('PatientService', ['getAll']);

    await TestBed.configureTestingModule({
      imports: [PatientListComponent],
      providers: [{ provide: PatientService, useValue: spy }]
    }).compileComponents();

    patientService = TestBed.inject(PatientService) as jasmine.SpyObj<PatientService>;
    fixture = TestBed.createComponent(PatientListComponent);
    component = fixture.componentInstance;
  });

  it('should display patients when loaded', fakeAsync(() => {
    patientService.getAll.and.returnValue(of(mockApiResponse([mockPatient])));
    fixture.detectChanges();
    tick();
    expect(component.patients().length).toBe(1);
  }));
});
```

### E2E Tests (Cypress)

```typescript
// cypress/e2e/patient-registration.cy.ts
describe('Patient Registration', () => {
  it('should register a new patient', () => {
    cy.login('receptionist@lab.com', 'password');
    cy.visit('/patients/new');
    
    cy.get('[data-cy="full-name"]').type('John Doe');
    cy.get('[data-cy="dob"]').type('1979-03-15');
    cy.get('[data-cy="gender"]').select('MALE');
    cy.get('[data-cy="phone"]').type('+919876543210');
    cy.get('[data-cy="submit"]').click();
    
    cy.get('[data-cy="uhid"]').should('match', /DEL-\d{6}/);
    cy.url().should('include', '/patients/');
  });
});
```

## Multi-Branch Isolation Testing

```java
@Test
void getPatient_ShouldNotReturnPatientFromOtherBranch() {
    UUID branch1 = UUID.randomUUID();
    UUID branch2 = UUID.randomUUID();

    // Create patient in branch 1
    BranchContextHolder.setCurrentBranchId(branch1);
    Patient patient = patientService.createPatient(validRequest());

    // Try to access from branch 2
    BranchContextHolder.setCurrentBranchId(branch2);

    assertThatThrownBy(() -> patientService.getPatient(patient.getId()))
        .isInstanceOf(NotFoundException.class);
}
```

## Performance Testing (JMeter / k6)

Target SLAs for production:
- Patient search: p95 < 200ms
- Order creation: p95 < 500ms
- Report generation: p95 < 5s (async, so p95 of queue processing)
- Concurrent users: 50 simultaneous users per branch
