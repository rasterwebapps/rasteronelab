package com.rasteronelab.lis.patient.application.service;

import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.patient.api.dto.DuplicateCheckRequest;
import com.rasteronelab.lis.patient.api.dto.PatientRequest;
import com.rasteronelab.lis.patient.api.dto.PatientResponse;
import com.rasteronelab.lis.patient.api.mapper.PatientMapper;
import com.rasteronelab.lis.patient.domain.model.Gender;
import com.rasteronelab.lis.patient.domain.model.Patient;
import com.rasteronelab.lis.patient.domain.repository.PatientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("PatientService")
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    private PatientService patientService;

    @BeforeEach
    void setUp() {
        patientService = new PatientService(patientRepository, patientMapper, 40);
        BranchContextHolder.setCurrentBranchId(BRANCH_ID);
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("create should generate UHID and save patient")
    void create_shouldGenerateUhidAndSavePatient() {
        PatientRequest request = new PatientRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhone("1234567890");
        request.setGender(Gender.MALE);

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
        assertThat(patient.getBranchId()).isEqualTo(BRANCH_ID);
        assertThat(patient.getIsActive()).isTrue();
        assertThat(patient.getUhid()).isEqualTo("BR-000001");
        verify(patientRepository).save(patient);
    }

    @Test
    @DisplayName("getById should return patient when found")
    void getById_shouldReturnPatient() {
        UUID id = UUID.randomUUID();
        Patient patient = new Patient();
        PatientResponse response = new PatientResponse();

        when(patientRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(patient));
        when(patientMapper.toResponse(patient)).thenReturn(response);

        PatientResponse result = patientService.getById(id);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById should throw NotFoundException when not found")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();

        when(patientRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Patient");
    }

    @Test
    @DisplayName("getAll should return paginated patients")
    void getAll_shouldReturnPaginatedPatients() {
        Pageable pageable = PageRequest.of(0, 10);
        Patient patient = new Patient();
        PatientResponse response = new PatientResponse();
        Page<Patient> page = new PageImpl<>(List.of(patient));

        when(patientRepository.findAllByBranchIdAndIsDeletedFalse(BRANCH_ID, pageable)).thenReturn(page);
        when(patientMapper.toResponse(patient)).thenReturn(response);

        Page<PatientResponse> result = patientService.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(response);
    }

    @Test
    @DisplayName("search should return matching patients")
    void search_shouldReturnMatchingPatients() {
        Pageable pageable = PageRequest.of(0, 10);
        String searchTerm = "John";
        Patient patient = new Patient();
        PatientResponse response = new PatientResponse();
        Page<Patient> page = new PageImpl<>(List.of(patient));

        when(patientRepository.searchPatients(BRANCH_ID, searchTerm, pageable)).thenReturn(page);
        when(patientMapper.toResponse(patient)).thenReturn(response);

        Page<PatientResponse> result = patientService.search(searchTerm, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(response);
    }

    @Test
    @DisplayName("update should update and return patient")
    void update_shouldUpdateAndReturnPatient() {
        UUID id = UUID.randomUUID();
        PatientRequest request = new PatientRequest();
        request.setFirstName("Jane");
        request.setPhone("9876543210");

        Patient patient = new Patient();
        Patient saved = new Patient();
        PatientResponse response = new PatientResponse();

        when(patientRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(saved);
        when(patientMapper.toResponse(saved)).thenReturn(response);

        PatientResponse result = patientService.update(id, request);

        assertThat(result).isEqualTo(response);
        verify(patientMapper).updateEntity(request, patient);
        verify(patientRepository).save(patient);
    }

    @Test
    @DisplayName("update should throw NotFoundException when patient not found")
    void update_notFound_shouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();
        PatientRequest request = new PatientRequest();

        when(patientRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.update(id, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Patient");
    }

    @Test
    @DisplayName("delete should soft delete patient")
    void delete_shouldSoftDelete() {
        UUID id = UUID.randomUUID();
        Patient patient = new Patient();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(patientRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(patient));

        patientService.delete(id);

        assertThat(patient.getIsDeleted()).isTrue();
        assertThat(patient.getDeletedAt()).isNotNull();
        verify(patientRepository).save(patient);
    }

    @Test
    @DisplayName("delete should throw NotFoundException when patient not found")
    void delete_notFound_shouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();

        when(patientRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.delete(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Patient");
    }

    @Test
    @DisplayName("findDuplicates should return potential duplicate patients")
    void findDuplicates_shouldReturnDuplicates() {
        String firstName = "John";
        String lastName = "Doe";
        String phone = "1234567890";
        LocalDate dob = LocalDate.of(1990, 1, 1);

        Patient patient = new Patient();
        PatientResponse response = new PatientResponse();

        when(patientRepository.findDuplicates(BRANCH_ID, firstName, lastName, phone, dob))
                .thenReturn(List.of(patient));
        when(patientMapper.toResponse(patient)).thenReturn(response);

        List<PatientResponse> result = patientService.findDuplicates(firstName, lastName, phone, dob);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(response);
    }

    @Test
    @DisplayName("findDuplicates should return empty list when no duplicates found")
    void findDuplicates_noDuplicates_shouldReturnEmptyList() {
        String firstName = "John";
        String lastName = "Doe";
        String phone = "1234567890";
        LocalDate dob = LocalDate.of(1990, 1, 1);

        when(patientRepository.findDuplicates(BRANCH_ID, firstName, lastName, phone, dob))
                .thenReturn(List.of());

        List<PatientResponse> result = patientService.findDuplicates(firstName, lastName, phone, dob);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("calculateDuplicateScore should give 100 for full match")
    void calculateDuplicateScore_fullMatch_shouldGive100() {
        Patient candidate = new Patient();
        candidate.setFirstName("John");
        candidate.setLastName("Doe");
        candidate.setDateOfBirth(LocalDate.of(1990, 1, 1));
        candidate.setPhone("1234567890");
        candidate.setEmail("john@example.com");
        candidate.setGender(Gender.MALE);

        int score = patientService.calculateDuplicateScore(candidate,
                "John", "Doe", "1234567890", "john@example.com", "MALE",
                LocalDate.of(1990, 1, 1));

        assertThat(score).isEqualTo(100);
    }

    @Test
    @DisplayName("calculateDuplicateScore should give 40 for name+DOB match only")
    void calculateDuplicateScore_nameDobMatch_shouldGive40() {
        Patient candidate = new Patient();
        candidate.setFirstName("John");
        candidate.setLastName("Doe");
        candidate.setDateOfBirth(LocalDate.of(1990, 1, 1));
        candidate.setPhone("9999999999");
        candidate.setGender(Gender.FEMALE);

        int score = patientService.calculateDuplicateScore(candidate,
                "John", "Doe", "1234567890", null, "MALE",
                LocalDate.of(1990, 1, 1));

        assertThat(score).isEqualTo(40);
    }

    @Test
    @DisplayName("calculateDuplicateScore should give 30 for phone match only")
    void calculateDuplicateScore_phoneMatch_shouldGive30() {
        Patient candidate = new Patient();
        candidate.setFirstName("Jane");
        candidate.setLastName("Smith");
        candidate.setDateOfBirth(LocalDate.of(2000, 6, 15));
        candidate.setPhone("1234567890");

        int score = patientService.calculateDuplicateScore(candidate,
                "John", "Doe", "1234567890", null, null,
                LocalDate.of(1990, 1, 1));

        assertThat(score).isEqualTo(30);
    }

    @Test
    @DisplayName("calculateDuplicateScore should give 20 for name match without DOB")
    void calculateDuplicateScore_nameMatchOnly_shouldGive20() {
        Patient candidate = new Patient();
        candidate.setFirstName("John");
        candidate.setLastName("Doe");
        candidate.setDateOfBirth(LocalDate.of(2000, 6, 15));

        int score = patientService.calculateDuplicateScore(candidate,
                "John", "Doe", null, null, null,
                LocalDate.of(1990, 1, 1));

        assertThat(score).isEqualTo(20);
    }

    @Test
    @DisplayName("findDuplicatesWithScore should filter candidates below threshold")
    void findDuplicatesWithScore_shouldFilterBelowThreshold() {
        Patient highScorePatient = new Patient();
        highScorePatient.setFirstName("John");
        highScorePatient.setLastName("Doe");
        highScorePatient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        highScorePatient.setPhone("1234567890");

        Patient lowScorePatient = new Patient();
        lowScorePatient.setFirstName("Jane");
        lowScorePatient.setLastName("Smith");
        lowScorePatient.setDateOfBirth(LocalDate.of(2000, 6, 15));
        lowScorePatient.setPhone("9999999999");

        PatientResponse highResponse = new PatientResponse();
        PatientResponse lowResponse = new PatientResponse();

        when(patientRepository.findDuplicates(eq(BRANCH_ID), any(), any(), any(), any()))
                .thenReturn(List.of(highScorePatient, lowScorePatient));
        when(patientMapper.toResponse(highScorePatient)).thenReturn(highResponse);

        DuplicateCheckRequest request = new DuplicateCheckRequest(
                "John", "Doe", "1234567890", null, null, LocalDate.of(1990, 1, 1));
        List<java.util.Map<String, Object>> results = patientService.findDuplicatesWithScore(request);

        // High score patient (name+DOB=40 + phone=30 = 70) should be included
        // Low score patient (no matches significant enough) should be filtered out
        assertThat(results).hasSize(1);
        assertThat(results.get(0).get("patient")).isEqualTo(highResponse);
        assertThat((int) results.get(0).get("score")).isGreaterThanOrEqualTo(40);
    }
}
