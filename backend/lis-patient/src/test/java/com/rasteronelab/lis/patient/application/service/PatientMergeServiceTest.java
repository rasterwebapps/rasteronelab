package com.rasteronelab.lis.patient.application.service;

import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.patient.api.dto.PatientMergeAuditResponse;
import com.rasteronelab.lis.patient.api.mapper.PatientMergeAuditMapper;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("PatientMergeService")
@ExtendWith(MockitoExtension.class)
class PatientMergeServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMergeAuditRepository mergeAuditRepository;

    @Mock
    private PatientMergeAuditMapper mergeAuditMapper;

    @InjectMocks
    private PatientMergeService patientMergeService;

    @BeforeEach
    void setUp() {
        BranchContextHolder.setCurrentBranchId(BRANCH_ID);
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("mergePatients should merge duplicate into primary and soft-delete duplicate")
    void mergePatients_shouldMergeAndSoftDeleteDuplicate() {
        UUID primaryId = UUID.randomUUID();
        UUID mergedId = UUID.randomUUID();

        Patient primary = createPatient(primaryId, "John", "Doe", "1234567890", null, null);
        Patient merged = createPatient(mergedId, "John", "Doe", "1234567890",
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
        verify(patientRepository).save(primary);
        verify(patientRepository).save(merged);
        verify(mergeAuditRepository).save(any(PatientMergeAudit.class));
    }

    @Test
    @DisplayName("mergePatients should throw when merging patient into themselves")
    void mergePatients_samePatient_shouldThrow() {
        UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> patientMergeService.mergePatients(id, id))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Cannot merge a patient into themselves");
    }

    @Test
    @DisplayName("mergePatients should throw when primary patient not found")
    void mergePatients_primaryNotFound_shouldThrow() {
        UUID primaryId = UUID.randomUUID();
        UUID mergedId = UUID.randomUUID();

        when(patientRepository.findByIdAndBranchIdAndIsDeletedFalse(primaryId, BRANCH_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientMergeService.mergePatients(primaryId, mergedId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("mergePatients should throw when merged patient not found")
    void mergePatients_mergedNotFound_shouldThrow() {
        UUID primaryId = UUID.randomUUID();
        UUID mergedId = UUID.randomUUID();
        Patient primary = createPatient(primaryId, "John", "Doe", "1234567890", null, null);

        when(patientRepository.findByIdAndBranchIdAndIsDeletedFalse(primaryId, BRANCH_ID))
                .thenReturn(Optional.of(primary));
        when(patientRepository.findByIdAndBranchIdAndIsDeletedFalse(mergedId, BRANCH_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientMergeService.mergePatients(primaryId, mergedId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("mergePatients should not overwrite existing primary fields")
    void mergePatients_shouldNotOverwriteExistingPrimaryFields() {
        UUID primaryId = UUID.randomUUID();
        UUID mergedId = UUID.randomUUID();

        Patient primary = createPatient(primaryId, "John", "Doe", "1111111111",
                "primary@example.com", "B+");
        Patient merged = createPatient(mergedId, "John", "Doe", "2222222222",
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

    private Patient createPatient(UUID id, String firstName, String lastName,
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
