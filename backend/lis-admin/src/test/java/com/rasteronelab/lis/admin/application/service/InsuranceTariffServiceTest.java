package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.InsuranceTariffRequest;
import com.rasteronelab.lis.admin.api.dto.InsuranceTariffResponse;
import com.rasteronelab.lis.admin.api.mapper.InsuranceTariffMapper;
import com.rasteronelab.lis.admin.domain.model.InsuranceTariff;
import com.rasteronelab.lis.admin.domain.repository.InsuranceTariffRepository;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("InsuranceTariffService")
@ExtendWith(MockitoExtension.class)
class InsuranceTariffServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private InsuranceTariffRepository insuranceTariffRepository;

    @Mock
    private InsuranceTariffMapper insuranceTariffMapper;

    @InjectMocks
    private InsuranceTariffService insuranceTariffService;

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
    @DisplayName("create should save and return insurance tariff")
    void create_shouldSaveAndReturnInsuranceTariff() {
        InsuranceTariffRequest request = new InsuranceTariffRequest();
        request.setInsuranceName("HDFC Ergo");
        request.setPlanName("Gold Plan");
        request.setTariffRate(new BigDecimal("500.00"));
        InsuranceTariff insuranceTariff = new InsuranceTariff();
        InsuranceTariff saved = new InsuranceTariff();
        InsuranceTariffResponse response = new InsuranceTariffResponse();

        when(insuranceTariffRepository.existsByInsuranceNameAndPlanNameAndBranchIdAndIsDeletedFalse("HDFC Ergo", "Gold Plan", BRANCH_ID)).thenReturn(false);
        when(insuranceTariffMapper.toEntity(request)).thenReturn(insuranceTariff);
        when(insuranceTariffRepository.save(insuranceTariff)).thenReturn(saved);
        when(insuranceTariffMapper.toResponse(saved)).thenReturn(response);

        InsuranceTariffResponse result = insuranceTariffService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(insuranceTariff.getBranchId()).isEqualTo(BRANCH_ID);
        verify(insuranceTariffRepository).save(insuranceTariff);
    }

    @Test
    @DisplayName("create with duplicate insuranceName+planName should throw DuplicateResourceException")
    void create_withDuplicateInsuranceNameAndPlanName_shouldThrowDuplicateResourceException() {
        InsuranceTariffRequest request = new InsuranceTariffRequest();
        request.setInsuranceName("HDFC Ergo");
        request.setPlanName("Gold Plan");
        request.setTariffRate(new BigDecimal("500.00"));

        when(insuranceTariffRepository.existsByInsuranceNameAndPlanNameAndBranchIdAndIsDeletedFalse("HDFC Ergo", "Gold Plan", BRANCH_ID)).thenReturn(true);

        assertThatThrownBy(() -> insuranceTariffService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("insuranceName+planName");
    }

    @Test
    @DisplayName("getById should return insurance tariff")
    void getById_shouldReturnInsuranceTariff() {
        UUID tariffId = UUID.randomUUID();
        InsuranceTariff insuranceTariff = new InsuranceTariff();
        InsuranceTariffResponse response = new InsuranceTariffResponse();

        when(insuranceTariffRepository.findByIdAndBranchIdAndIsDeletedFalse(tariffId, BRANCH_ID)).thenReturn(Optional.of(insuranceTariff));
        when(insuranceTariffMapper.toResponse(insuranceTariff)).thenReturn(response);

        InsuranceTariffResponse result = insuranceTariffService.getById(tariffId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID tariffId = UUID.randomUUID();

        when(insuranceTariffRepository.findByIdAndBranchIdAndIsDeletedFalse(tariffId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> insuranceTariffService.getById(tariffId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("InsuranceTariff");
    }

    @Test
    @DisplayName("getAll should return paged results")
    void getAll_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        InsuranceTariff insuranceTariff = new InsuranceTariff();
        InsuranceTariffResponse response = new InsuranceTariffResponse();
        Page<InsuranceTariff> tariffPage = new PageImpl<>(List.of(insuranceTariff));

        when(insuranceTariffRepository.findAllByBranchIdAndIsDeletedFalse(BRANCH_ID, pageable)).thenReturn(tariffPage);
        when(insuranceTariffMapper.toResponse(insuranceTariff)).thenReturn(response);

        Page<InsuranceTariffResponse> result = insuranceTariffService.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("update should update and return insurance tariff")
    void update_shouldUpdateAndReturnInsuranceTariff() {
        UUID tariffId = UUID.randomUUID();
        InsuranceTariffRequest request = new InsuranceTariffRequest();
        request.setInsuranceName("ICICI Lombard");
        request.setPlanName("Silver Plan");
        request.setTariffRate(new BigDecimal("750.00"));
        InsuranceTariff insuranceTariff = new InsuranceTariff();
        insuranceTariff.setInsuranceName("HDFC Ergo");
        insuranceTariff.setPlanName("Gold Plan");
        InsuranceTariff saved = new InsuranceTariff();
        InsuranceTariffResponse response = new InsuranceTariffResponse();

        when(insuranceTariffRepository.findByIdAndBranchIdAndIsDeletedFalse(tariffId, BRANCH_ID)).thenReturn(Optional.of(insuranceTariff));
        when(insuranceTariffRepository.existsByInsuranceNameAndPlanNameAndBranchIdAndIsDeletedFalse("ICICI Lombard", "Silver Plan", BRANCH_ID)).thenReturn(false);
        when(insuranceTariffRepository.save(insuranceTariff)).thenReturn(saved);
        when(insuranceTariffMapper.toResponse(saved)).thenReturn(response);

        InsuranceTariffResponse result = insuranceTariffService.update(tariffId, request);

        assertThat(result).isEqualTo(response);
        verify(insuranceTariffMapper).updateEntity(request, insuranceTariff);
    }

    @Test
    @DisplayName("delete should soft delete insurance tariff")
    void delete_shouldSoftDelete() {
        UUID tariffId = UUID.randomUUID();
        InsuranceTariff insuranceTariff = new InsuranceTariff();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(insuranceTariffRepository.findByIdAndBranchIdAndIsDeletedFalse(tariffId, BRANCH_ID)).thenReturn(Optional.of(insuranceTariff));

        insuranceTariffService.delete(tariffId);

        assertThat(insuranceTariff.getIsDeleted()).isTrue();
        assertThat(insuranceTariff.getDeletedAt()).isNotNull();
        verify(insuranceTariffRepository).save(insuranceTariff);
    }
}
