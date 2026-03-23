package com.rasteronelab.lis.patient.application.service;

import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.patient.api.dto.DuplicateCheckRequest;
import com.rasteronelab.lis.patient.api.dto.PatientMergeAuditResponse;
import com.rasteronelab.lis.patient.api.dto.PatientRequest;
import com.rasteronelab.lis.patient.api.dto.PatientResponse;
import com.rasteronelab.lis.patient.api.mapper.PatientMergeAuditMapper;
import com.rasteronelab.lis.patient.api.mapper.PatientMapper;
import com.rasteronelab.lis.patient.domain.model.Gender;
import com.rasteronelab.lis.patient.domain.model.Patient;
import com.rasteronelab.lis.patient.domain.model.PatientMergeAudit;
import com.rasteronelab.lis.patient.domain.repository.PatientMergeAuditRepository;
import com.rasteronelab.lis.patient.domain.repository.PatientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Flow test verifying the complete patient registration → duplicate detection → merge lifecycle.
 * Exercises both PatientService and PatientMergeService to ensure the end-to-end flow is correct.
 */
@DisplayName("Patient Registration → Duplicate Detection → Merge Flow")
@ExtendWith(MockitoExtension.class)
class PatientBillingFlowTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @Mock
    private PatientMergeAuditRepository mergeAuditRepository;

    @Mock
    private PatientMergeAuditMapper mergeAuditMapper;

    private PatientService patientService;
    private PatientMergeService patientMergeService;

    @BeforeEach
    void setUp() {
        patientService = new PatientService(patientRepository, patientMapper, 40);
        patientMergeService = new PatientMergeService(patientRepository, mergeAuditRepository, mergeAuditMapper);
        BranchContextHolder.setCurrentBranchId(BRANCH_ID);
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
        SecurityContextHolder.clearContext();
    }

    // ── Test 1: Registration ────────────────────────────────────────────

    @Test
    @DisplayName("registerPatient should generate UHID (BR-XXXXXX), set branchId, and default isActive to true")
    void registerPatient_generatesUhid_andSetsDefaults() {
        PatientRequest request = new PatientRequest();
        request.setFirstName("Priya");
        request.setPhone("9876543210");
        request.setGender(Gender.FEMALE);

        Patient patient = new Patient();
        Patient saved = new Patient();
        saved.setUhid("BR-000001");
        PatientResponse response = new PatientResponse();

        when(patientMapper.toEntity(request)).thenReturn(patient);
        when(patientRepository.countByBranchIdAndIsDeletedFalse(BRANCH_ID)).thenReturn(0L);
        when(patientRepository.existsByUhidAndBranchIdAndIsDeletedFalse("BR-000001", BRANCH_ID)).thenReturn(false);
        when(patientRepository.save(patient)).thenReturn(saved);
        when(patientMapper.toResponse(saved)).thenReturn(response);

        PatientResponse result = patientService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(patient.getUhid()).matches("BR-\\d{6}");
        assertThat(patient.getBranchId()).isEqualTo(BRANCH_ID);
        assertThat(patient.getIsActive()).isTrue();
        verify(patientRepository).save(patient);
    }

    // ── Test 2-5: Duplicate Detection (Scored Matching) ─────────────────

    @Test
    @DisplayName("duplicateDetection: all fields match → score = 100")
    void duplicateDetection_scoredMatching_allFieldsMatch_returns100() {
        Patient candidate = createPatient("John", "Doe", LocalDate.of(1990, 1, 1),
                "1234567890", "john@example.com", Gender.MALE);

        int score = patientService.calculateDuplicateScore(candidate,
                "John", "Doe", "1234567890", "john@example.com", "MALE",
                LocalDate.of(1990, 1, 1));

        assertThat(score).isEqualTo(100);
    }

    @Test
    @DisplayName("duplicateDetection: name+DOB match only → score = 40")
    void duplicateDetection_scoredMatching_nameAndDobOnly_returns40() {
        Patient candidate = createPatient("John", "Doe", LocalDate.of(1990, 1, 1),
                "9999999999", "other@example.com", Gender.FEMALE);

        int score = patientService.calculateDuplicateScore(candidate,
                "John", "Doe", "1234567890", "john@example.com", "MALE",
                LocalDate.of(1990, 1, 1));

        assertThat(score).isEqualTo(40);
    }

    @Test
    @DisplayName("duplicateDetection: phone match only → score = 30")
    void duplicateDetection_scoredMatching_phoneOnly_returns30() {
        Patient candidate = createPatient("Jane", "Smith", LocalDate.of(2000, 6, 15),
                "1234567890", null, null);

        int score = patientService.calculateDuplicateScore(candidate,
                "John", "Doe", "1234567890", null, null,
                LocalDate.of(1990, 1, 1));

        assertThat(score).isEqualTo(30);
    }

    @Test
    @DisplayName("duplicateDetection: candidates below threshold (40) are filtered out")
    void duplicateDetection_belowThreshold_filteredOut() {
        // High-score candidate: name+DOB (40) + phone (30) = 70
        Patient highScorePatient = createPatient("John", "Doe", LocalDate.of(1990, 1, 1),
                "1234567890", null, null);

        // Low-score candidate: no significant matches = 0
        Patient lowScorePatient = createPatient("Alice", "Wonder", LocalDate.of(2005, 3, 20),
                "5555555555", "alice@example.com", Gender.FEMALE);

        PatientResponse highResponse = new PatientResponse();

        when(patientRepository.findDuplicates(eq(BRANCH_ID), any(), any(), any(), any()))
                .thenReturn(List.of(highScorePatient, lowScorePatient));
        when(patientMapper.toResponse(highScorePatient)).thenReturn(highResponse);

        DuplicateCheckRequest request = new DuplicateCheckRequest(
                "John", "Doe", "1234567890", null, null, LocalDate.of(1990, 1, 1));
        List<Map<String, Object>> results = patientService.findDuplicatesWithScore(request);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).get("patient")).isEqualTo(highResponse);
        assertThat((int) results.get(0).get("score")).isGreaterThanOrEqualTo(40);
    }

    // ── Test 6-8: Merge Operations ──────────────────────────────────────

    @Test
    @DisplayName("mergePatients should fill missing primary fields, soft-delete duplicate, and create audit")
    void mergePatients_fillsMissingPrimaryFields_softDeletesDuplicate() {
        UUID primaryId = UUID.randomUUID();
        UUID mergedId = UUID.randomUUID();

        Patient primary = createPatientWithId(primaryId, "John", "Doe", "1234567890", null, null);
        Patient merged = createPatientWithId(mergedId, "John", "Doe", "1234567890",
                "john@example.com", "A+");

        PatientMergeAudit audit = new PatientMergeAudit();
        PatientMergeAuditResponse auditResponse = new PatientMergeAuditResponse();

        when(patientRepository.findByIdAndBranchIdAndIsDeletedFalse(primaryId, BRANCH_ID))
                .thenReturn(Optional.of(primary));
        when(patientRepository.findByIdAndBranchIdAndIsDeletedFalse(mergedId, BRANCH_ID))
                .thenReturn(Optional.of(merged));
        when(mergeAuditRepository.save(any(PatientMergeAudit.class))).thenReturn(audit);
        when(mergeAuditMapper.toResponse(audit)).thenReturn(auditResponse);

        PatientMergeAuditResponse result = patientMergeService.mergePatients(primaryId, mergedId);

        // Primary should pick up missing email and blood group from merged
        assertThat(primary.getEmail()).isEqualTo("john@example.com");
        assertThat(primary.getBloodGroup()).isEqualTo("A+");

        // Merged should be soft-deleted
        assertThat(merged.getIsDeleted()).isTrue();
        assertThat(merged.getDeletedAt()).isNotNull();

        verify(patientRepository).save(primary);
        verify(patientRepository).save(merged);

        // Verify audit record is created with correct details
        ArgumentCaptor<PatientMergeAudit> auditCaptor = ArgumentCaptor.forClass(PatientMergeAudit.class);
        verify(mergeAuditRepository).save(auditCaptor.capture());
        PatientMergeAudit capturedAudit = auditCaptor.getValue();
        assertThat(capturedAudit.getPrimaryPatientId()).isEqualTo(primaryId);
        assertThat(capturedAudit.getMergedPatientId()).isEqualTo(mergedId);
        assertThat(capturedAudit.getMergedBy()).isEqualTo("admin");
        assertThat(capturedAudit.getMergeDetails()).isNotNull();
    }

    @Test
    @DisplayName("mergePatients should not overwrite existing primary fields")
    void mergePatients_doesNotOverwriteExistingFields() {
        UUID primaryId = UUID.randomUUID();
        UUID mergedId = UUID.randomUUID();

        Patient primary = createPatientWithId(primaryId, "John", "Doe", "1111111111",
                "primary@example.com", "B+");
        Patient merged = createPatientWithId(mergedId, "John", "Doe", "2222222222",
                "merged@example.com", "A+");

        PatientMergeAudit audit = new PatientMergeAudit();
        PatientMergeAuditResponse auditResponse = new PatientMergeAuditResponse();

        when(patientRepository.findByIdAndBranchIdAndIsDeletedFalse(primaryId, BRANCH_ID))
                .thenReturn(Optional.of(primary));
        when(patientRepository.findByIdAndBranchIdAndIsDeletedFalse(mergedId, BRANCH_ID))
                .thenReturn(Optional.of(merged));
        when(mergeAuditRepository.save(any(PatientMergeAudit.class))).thenReturn(audit);
        when(mergeAuditMapper.toResponse(audit)).thenReturn(auditResponse);

        patientMergeService.mergePatients(primaryId, mergedId);

        // Primary fields should NOT be overwritten
        assertThat(primary.getEmail()).isEqualTo("primary@example.com");
        assertThat(primary.getBloodGroup()).isEqualTo("B+");
        assertThat(primary.getPhone()).isEqualTo("1111111111");
    }

    @Test
    @DisplayName("mergePatients with same patient ID should throw BusinessRuleException")
    void mergePatients_samePatient_throwsException() {
        UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> patientMergeService.mergePatients(id, id))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Cannot merge a patient into themselves");
    }

    // ── Test 9: Search ──────────────────────────────────────────────────

    @Test
    @DisplayName("patientSearch should match by UHID, name, or phone")
    void patientSearch_matchesByUhidOrNameOrPhone() {
        Pageable pageable = PageRequest.of(0, 10);
        Patient patient = new Patient();
        PatientResponse response = new PatientResponse();

        // Search by UHID
        when(patientRepository.searchPatients(BRANCH_ID, "BR-000001", pageable))
                .thenReturn(new PageImpl<>(List.of(patient)));
        when(patientMapper.toResponse(patient)).thenReturn(response);

        Page<PatientResponse> uhidResult = patientService.search("BR-000001", pageable);
        assertThat(uhidResult.getContent()).hasSize(1);
        assertThat(uhidResult.getContent().get(0)).isEqualTo(response);

        // Search by name
        when(patientRepository.searchPatients(BRANCH_ID, "John", pageable))
                .thenReturn(new PageImpl<>(List.of(patient)));

        Page<PatientResponse> nameResult = patientService.search("John", pageable);
        assertThat(nameResult.getContent()).hasSize(1);

        // Search by phone
        when(patientRepository.searchPatients(BRANCH_ID, "9876543210", pageable))
                .thenReturn(new PageImpl<>(List.of(patient)));

        Page<PatientResponse> phoneResult = patientService.search("9876543210", pageable);
        assertThat(phoneResult.getContent()).hasSize(1);
    }

    // ── Test 10: Soft Delete ────────────────────────────────────────────

    @Test
    @DisplayName("deletePatient should soft-delete with audit trail (isDeleted, deletedAt, updatedBy)")
    void deletePatient_softDeletes_withAuditTrail() {
        UUID id = UUID.randomUUID();
        Patient patient = new Patient();

        when(patientRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID))
                .thenReturn(Optional.of(patient));

        patientService.delete(id);

        assertThat(patient.getIsDeleted()).isTrue();
        assertThat(patient.getDeletedAt()).isNotNull();
        assertThat(patient.getUpdatedBy()).isEqualTo("admin");
        verify(patientRepository).save(patient);
    }

    // ── Helpers ─────────────────────────────────────────────────────────

    private Patient createPatient(String firstName, String lastName, LocalDate dob,
                                  String phone, String email, Gender gender) {
        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setDateOfBirth(dob);
        patient.setPhone(phone);
        patient.setEmail(email);
        patient.setGender(gender);
        return patient;
    }

    private Patient createPatientWithId(UUID id, String firstName, String lastName,
                                        String phone, String email, String bloodGroup) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setUhid("BR-" + id.toString().substring(0, 6));
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setPhone(phone);
        patient.setEmail(email);
        patient.setBloodGroup(bloodGroup);
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setGender(Gender.MALE);
        patient.setIsActive(true);
        return patient;
    }
}
