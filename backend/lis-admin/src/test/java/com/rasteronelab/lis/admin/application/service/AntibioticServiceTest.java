package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.AntibioticRequest;
import com.rasteronelab.lis.admin.api.dto.AntibioticResponse;
import com.rasteronelab.lis.admin.api.mapper.AntibioticMapper;
import com.rasteronelab.lis.admin.domain.model.Antibiotic;
import com.rasteronelab.lis.admin.domain.repository.AntibioticRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("AntibioticService")
@ExtendWith(MockitoExtension.class)
class AntibioticServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private AntibioticRepository antibioticRepository;

    @Mock
    private AntibioticMapper antibioticMapper;

    @InjectMocks
    private AntibioticService antibioticService;

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
    @DisplayName("create should save and return antibiotic")
    void create_shouldSaveAndReturnAntibiotic() {
        AntibioticRequest request = new AntibioticRequest();
        request.setCode("AMX");
        Antibiotic entity = new Antibiotic();
        Antibiotic saved = new Antibiotic();
        AntibioticResponse response = new AntibioticResponse();

        when(antibioticRepository.existsByCodeAndBranchIdAndIsDeletedFalse("AMX", BRANCH_ID)).thenReturn(false);
        when(antibioticMapper.toEntity(request)).thenReturn(entity);
        when(antibioticRepository.save(entity)).thenReturn(saved);
        when(antibioticMapper.toResponse(saved)).thenReturn(response);

        AntibioticResponse result = antibioticService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(entity.getBranchId()).isEqualTo(BRANCH_ID);
        verify(antibioticRepository).save(entity);
    }

    @Test
    @DisplayName("create with duplicate code should throw DuplicateResourceException")
    void create_withDuplicateCode_shouldThrowDuplicateResourceException() {
        AntibioticRequest request = new AntibioticRequest();
        request.setCode("AMX");

        when(antibioticRepository.existsByCodeAndBranchIdAndIsDeletedFalse("AMX", BRANCH_ID)).thenReturn(true);

        assertThatThrownBy(() -> antibioticService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("code");
    }

    @Test
    @DisplayName("getById should return antibiotic")
    void getById_shouldReturnAntibiotic() {
        UUID id = UUID.randomUUID();
        Antibiotic entity = new Antibiotic();
        AntibioticResponse response = new AntibioticResponse();

        when(antibioticRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(entity));
        when(antibioticMapper.toResponse(entity)).thenReturn(response);

        AntibioticResponse result = antibioticService.getById(id);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();

        when(antibioticRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> antibioticService.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Antibiotic");
    }

    @Test
    @DisplayName("search should return matching results")
    void search_shouldReturnMatchingResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Antibiotic entity = new Antibiotic();
        AntibioticResponse response = new AntibioticResponse();
        Page<Antibiotic> page = new PageImpl<>(List.of(entity));

        when(antibioticRepository.findAllByBranchIdAndIsDeletedFalseAndNameContainingIgnoreCase(BRANCH_ID, "amox", pageable)).thenReturn(page);
        when(antibioticMapper.toResponse(entity)).thenReturn(response);

        Page<AntibioticResponse> result = antibioticService.search("amox", pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("delete should soft delete antibiotic")
    void delete_shouldSoftDelete() {
        UUID id = UUID.randomUUID();
        Antibiotic entity = new Antibiotic();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(antibioticRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(entity));

        antibioticService.delete(id);

        assertThat(entity.getIsDeleted()).isTrue();
        assertThat(entity.getDeletedAt()).isNotNull();
        verify(antibioticRepository).save(entity);
    }
}
