package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.PriceCatalogRequest;
import com.rasteronelab.lis.admin.api.dto.PriceCatalogResponse;
import com.rasteronelab.lis.admin.api.mapper.PriceCatalogMapper;
import com.rasteronelab.lis.admin.domain.model.PriceCatalog;
import com.rasteronelab.lis.admin.domain.model.TestMaster;
import com.rasteronelab.lis.admin.domain.model.TestPanel;
import com.rasteronelab.lis.admin.domain.repository.PriceCatalogRepository;
import com.rasteronelab.lis.admin.domain.repository.TestMasterRepository;
import com.rasteronelab.lis.admin.domain.repository.TestPanelRepository;
import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
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
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("PriceCatalogService")
@ExtendWith(MockitoExtension.class)
class PriceCatalogServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private PriceCatalogRepository priceCatalogRepository;

    @Mock
    private TestMasterRepository testMasterRepository;

    @Mock
    private TestPanelRepository testPanelRepository;

    @Mock
    private PriceCatalogMapper priceCatalogMapper;

    @InjectMocks
    private PriceCatalogService priceCatalogService;

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
    @DisplayName("create with testId should save successfully")
    void create_withTestId_shouldSaveSuccessfully() {
        UUID testId = UUID.randomUUID();
        PriceCatalogRequest request = new PriceCatalogRequest();
        request.setTestId(testId);
        request.setPanelId(null);
        PriceCatalog priceCatalog = new PriceCatalog();
        PriceCatalog saved = new PriceCatalog();
        PriceCatalogResponse response = new PriceCatalogResponse();
        TestMaster testMaster = new TestMaster();

        when(priceCatalogMapper.toEntity(request)).thenReturn(priceCatalog);
        when(testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(testId, BRANCH_ID)).thenReturn(Optional.of(testMaster));
        when(priceCatalogRepository.save(priceCatalog)).thenReturn(saved);
        when(priceCatalogMapper.toResponse(saved)).thenReturn(response);

        PriceCatalogResponse result = priceCatalogService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(priceCatalog.getBranchId()).isEqualTo(BRANCH_ID);
        verify(priceCatalogRepository).save(priceCatalog);
    }

    @Test
    @DisplayName("create with panelId should save successfully")
    void create_withPanelId_shouldSaveSuccessfully() {
        UUID panelId = UUID.randomUUID();
        PriceCatalogRequest request = new PriceCatalogRequest();
        request.setTestId(null);
        request.setPanelId(panelId);
        PriceCatalog priceCatalog = new PriceCatalog();
        PriceCatalog saved = new PriceCatalog();
        PriceCatalogResponse response = new PriceCatalogResponse();
        TestPanel testPanel = new TestPanel();

        when(priceCatalogMapper.toEntity(request)).thenReturn(priceCatalog);
        when(testPanelRepository.findByIdAndBranchIdAndIsDeletedFalse(panelId, BRANCH_ID)).thenReturn(Optional.of(testPanel));
        when(priceCatalogRepository.save(priceCatalog)).thenReturn(saved);
        when(priceCatalogMapper.toResponse(saved)).thenReturn(response);

        PriceCatalogResponse result = priceCatalogService.create(request);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("create with both testId and panelId should throw BusinessRuleException")
    void create_withBothTestAndPanel_shouldThrowBusinessRuleException() {
        PriceCatalogRequest request = new PriceCatalogRequest();
        request.setTestId(UUID.randomUUID());
        request.setPanelId(UUID.randomUUID());

        assertThatThrownBy(() -> priceCatalogService.create(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Only one of testId or panelId");
    }

    @Test
    @DisplayName("create with neither testId nor panelId should throw BusinessRuleException")
    void create_withNeitherTestNorPanel_shouldThrowBusinessRuleException() {
        PriceCatalogRequest request = new PriceCatalogRequest();
        request.setTestId(null);
        request.setPanelId(null);

        assertThatThrownBy(() -> priceCatalogService.create(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Either testId or panelId must be provided");
    }

    @Test
    @DisplayName("getById should return price catalog")
    void getById_shouldReturnPriceCatalog() {
        UUID id = UUID.randomUUID();
        PriceCatalog priceCatalog = new PriceCatalog();
        PriceCatalogResponse response = new PriceCatalogResponse();

        when(priceCatalogRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(priceCatalog));
        when(priceCatalogMapper.toResponse(priceCatalog)).thenReturn(response);

        PriceCatalogResponse result = priceCatalogService.getById(id);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();

        when(priceCatalogRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> priceCatalogService.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("PriceCatalog");
    }

    @Test
    @DisplayName("delete should soft delete price catalog")
    void delete_shouldSoftDelete() {
        UUID id = UUID.randomUUID();
        PriceCatalog priceCatalog = new PriceCatalog();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(priceCatalogRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(priceCatalog));

        priceCatalogService.delete(id);

        assertThat(priceCatalog.getIsDeleted()).isTrue();
        assertThat(priceCatalog.getDeletedAt()).isNotNull();
        verify(priceCatalogRepository).save(priceCatalog);
    }
}
