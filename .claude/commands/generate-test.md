# Generate Test Suite — /generate-test

Generate comprehensive test suite for a given class or module.

## Usage
```
/generate-test {type} {ClassName} {module}
```

Types: `service`, `controller`, `repository`, `component`, `store`, `e2e`, `migration`

## Java Test Templates

### Service Unit Test
```java
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    @Mock PatientRepository patientRepository;
    @Mock PatientMapper patientMapper;
    @InjectMocks PatientServiceImpl patientService;

    @BeforeEach
    void setUp() {
        BranchContextHolder.setCurrentBranchId(TEST_BRANCH_ID);
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
    }

    @Test
    @DisplayName("createPatient: success with valid request")
    void createPatient_withValidRequest_returnsPatientResponse() {
        // Given
        var request = PatientRequestBuilder.valid().build();
        var entity = PatientBuilder.valid().build();
        var response = PatientResponseBuilder.valid().build();
        
        when(patientMapper.toEntity(request)).thenReturn(entity);
        when(patientRepository.save(entity)).thenReturn(entity);
        when(patientMapper.toResponse(entity)).thenReturn(response);
        
        // When
        var result = patientService.createPatient(TEST_BRANCH_ID, request);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(response.getId());
        verify(patientRepository).save(entity);
    }
    
    @Test
    @DisplayName("getPatient: throws NotFoundException when not found")
    void getPatient_withInvalidId_throwsNotFoundException() {
        when(patientRepository.findByIdAndBranchIdAndIsDeletedFalse(any(), any()))
            .thenReturn(Optional.empty());
            
        assertThatThrownBy(() -> patientService.getPatient(TEST_BRANCH_ID, INVALID_ID))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Patient not found");
    }
}
```

### Controller Integration Test
```java
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class PatientControllerIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
        .withDatabaseName("lis_test");
    
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    
    @Test
    void createPatient_withValidRequest_returns201() throws Exception {
        var request = PatientRequestBuilder.valid().build();
        
        mockMvc.perform(post("/api/v1/patients")
            .header("X-Branch-Id", TEST_BRANCH_ID)
            .header("Authorization", "Bearer " + getTestToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.fullName").value(request.getFullName()));
    }
}
```

### Test Data Builders
```java
public class PatientRequestBuilder {
    private String fullName = "John Doe";
    private Gender gender = Gender.MALE;
    private String dateOfBirth = "1990-01-15";
    private String phoneNumber = "+1234567890";
    
    public static PatientRequestBuilder valid() { return new PatientRequestBuilder(); }
    public PatientRequestBuilder fullName(String name) { this.fullName = name; return this; }
    public PatientRequest build() { return new PatientRequest(fullName, gender, dateOfBirth, phoneNumber); }
}
```

## TypeScript Test Templates

### Component Test
```typescript
describe('PatientListComponent', () => {
  let component: PatientListComponent;
  let fixture: ComponentFixture<PatientListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PatientListComponent, HttpClientTestingModule],
    }).compileComponents();
    
    fixture = TestBed.createComponent(PatientListComponent);
    component = fixture.componentInstance;
  });

  it('should display patients when loaded', () => {
    const patients = [mockPatient()];
    component.patients = patients;
    fixture.detectChanges();
    
    const rows = fixture.nativeElement.querySelectorAll('[data-testid="patient-row"]');
    expect(rows.length).toBe(1);
  });
});
```

### Multi-Branch Test Isolation
```java
@Test
void getPatients_cannotAccessOtherBranch_throwsException() {
    // Branch A patient
    var branchAPatient = createPatientInBranch(BRANCH_A_ID);
    
    // Set context to Branch B
    BranchContextHolder.setCurrentBranchId(BRANCH_B_ID);
    
    // Should not see Branch A patient
    var result = patientService.getPatient(BRANCH_B_ID, branchAPatient.getId());
    assertThatThrownBy(() -> result).isInstanceOf(NotFoundException.class);
}
```
