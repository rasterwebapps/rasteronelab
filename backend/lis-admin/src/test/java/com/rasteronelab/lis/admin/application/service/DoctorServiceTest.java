package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.DoctorRequest;
import com.rasteronelab.lis.admin.api.dto.DoctorResponse;
import com.rasteronelab.lis.admin.api.mapper.DoctorMapper;
import com.rasteronelab.lis.admin.domain.model.Doctor;
import com.rasteronelab.lis.admin.domain.repository.DoctorRepository;
import com.rasteronelab.lis.core.common.exception.DuplicateResourceException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("DoctorService")
@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        BranchContextHolder.setCurrentBranchId(BRANCH_ID);
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("create should save and return doctor")
    void create_shouldSaveAndReturnDoctor() {
        DoctorRequest request = new DoctorRequest();
        request.setCode("DOC-001");
        Doctor doctor = new Doctor();
        Doctor saved = new Doctor();
        DoctorResponse response = new DoctorResponse();

        when(doctorRepository.existsByCodeAndBranchIdAndIsDeletedFalse("DOC-001", BRANCH_ID)).thenReturn(false);
        when(doctorMapper.toEntity(request)).thenReturn(doctor);
        when(doctorRepository.save(doctor)).thenReturn(saved);
        when(doctorMapper.toResponse(saved)).thenReturn(response);

        DoctorResponse result = doctorService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(doctor.getBranchId()).isEqualTo(BRANCH_ID);
        verify(doctorRepository).save(doctor);
    }

    @Test
    @DisplayName("create with duplicate code should throw DuplicateResourceException")
    void create_withDuplicateCode_shouldThrowDuplicateResourceException() {
        DoctorRequest request = new DoctorRequest();
        request.setCode("DOC-001");

        when(doctorRepository.existsByCodeAndBranchIdAndIsDeletedFalse("DOC-001", BRANCH_ID)).thenReturn(true);

        assertThatThrownBy(() -> doctorService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("code");
    }

    @Test
    @DisplayName("getById should return doctor")
    void getById_shouldReturnDoctor() {
        UUID doctorId = UUID.randomUUID();
        Doctor doctor = new Doctor();
        DoctorResponse response = new DoctorResponse();

        when(doctorRepository.findByIdAndBranchIdAndIsDeletedFalse(doctorId, BRANCH_ID)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toResponse(doctor)).thenReturn(response);

        DoctorResponse result = doctorService.getById(doctorId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID doctorId = UUID.randomUUID();

        when(doctorRepository.findByIdAndBranchIdAndIsDeletedFalse(doctorId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> doctorService.getById(doctorId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Doctor");
    }

    @Test
    @DisplayName("getAll should return paged results")
    void getAll_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Doctor doctor = new Doctor();
        DoctorResponse response = new DoctorResponse();
        Page<Doctor> doctorPage = new PageImpl<>(List.of(doctor));

        when(doctorRepository.findAllByBranchIdAndIsDeletedFalse(BRANCH_ID, pageable)).thenReturn(doctorPage);
        when(doctorMapper.toResponse(doctor)).thenReturn(response);

        Page<DoctorResponse> result = doctorService.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("update should update and return doctor")
    void update_shouldUpdateAndReturnDoctor() {
        UUID doctorId = UUID.randomUUID();
        DoctorRequest request = new DoctorRequest();
        request.setCode("DOC-002");
        Doctor doctor = new Doctor();
        doctor.setCode("DOC-001");
        Doctor saved = new Doctor();
        DoctorResponse response = new DoctorResponse();

        when(doctorRepository.findByIdAndBranchIdAndIsDeletedFalse(doctorId, BRANCH_ID)).thenReturn(Optional.of(doctor));
        when(doctorRepository.existsByCodeAndBranchIdAndIsDeletedFalse("DOC-002", BRANCH_ID)).thenReturn(false);
        when(doctorRepository.save(doctor)).thenReturn(saved);
        when(doctorMapper.toResponse(saved)).thenReturn(response);

        DoctorResponse result = doctorService.update(doctorId, request);

        assertThat(result).isEqualTo(response);
        verify(doctorMapper).updateEntity(request, doctor);
    }

    @Test
    @DisplayName("delete should soft delete doctor")
    void delete_shouldSoftDelete() {
        UUID doctorId = UUID.randomUUID();
        Doctor doctor = new Doctor();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(doctorRepository.findByIdAndBranchIdAndIsDeletedFalse(doctorId, BRANCH_ID)).thenReturn(Optional.of(doctor));

        doctorService.delete(doctorId);

        assertThat(doctor.getIsDeleted()).isTrue();
        assertThat(doctor.getDeletedAt()).isNotNull();
        verify(doctorRepository).save(doctor);
    }
}
