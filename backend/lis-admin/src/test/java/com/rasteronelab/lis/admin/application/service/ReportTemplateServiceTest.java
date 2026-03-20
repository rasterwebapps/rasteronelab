package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.ReportTemplateRequest;
import com.rasteronelab.lis.admin.api.dto.ReportTemplateResponse;
import com.rasteronelab.lis.admin.api.mapper.ReportTemplateMapper;
import com.rasteronelab.lis.admin.domain.model.ReportTemplate;
import com.rasteronelab.lis.admin.domain.repository.ReportTemplateRepository;
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

@DisplayName("ReportTemplateService")
@ExtendWith(MockitoExtension.class)
class ReportTemplateServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private ReportTemplateRepository reportTemplateRepository;

    @Mock
    private ReportTemplateMapper reportTemplateMapper;

    @InjectMocks
    private ReportTemplateService reportTemplateService;

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
    @DisplayName("create should save and return report template")
    void create_shouldSaveAndReturnReportTemplate() {
        ReportTemplateRequest request = new ReportTemplateRequest();
        request.setTemplateName("Default Template");
        ReportTemplate reportTemplate = new ReportTemplate();
        ReportTemplate saved = new ReportTemplate();
        ReportTemplateResponse response = new ReportTemplateResponse();

        when(reportTemplateRepository.existsByTemplateNameAndBranchIdAndIsDeletedFalse("Default Template", BRANCH_ID)).thenReturn(false);
        when(reportTemplateMapper.toEntity(request)).thenReturn(reportTemplate);
        when(reportTemplateRepository.save(reportTemplate)).thenReturn(saved);
        when(reportTemplateMapper.toResponse(saved)).thenReturn(response);

        ReportTemplateResponse result = reportTemplateService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(reportTemplate.getBranchId()).isEqualTo(BRANCH_ID);
        verify(reportTemplateRepository).save(reportTemplate);
    }

    @Test
    @DisplayName("create with duplicate template name should throw DuplicateResourceException")
    void create_withDuplicateTemplateName_shouldThrowDuplicateResourceException() {
        ReportTemplateRequest request = new ReportTemplateRequest();
        request.setTemplateName("Default Template");

        when(reportTemplateRepository.existsByTemplateNameAndBranchIdAndIsDeletedFalse("Default Template", BRANCH_ID)).thenReturn(true);

        assertThatThrownBy(() -> reportTemplateService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("templateName");
    }

    @Test
    @DisplayName("getById should return report template")
    void getById_shouldReturnReportTemplate() {
        UUID templateId = UUID.randomUUID();
        ReportTemplate reportTemplate = new ReportTemplate();
        ReportTemplateResponse response = new ReportTemplateResponse();

        when(reportTemplateRepository.findByIdAndBranchIdAndIsDeletedFalse(templateId, BRANCH_ID)).thenReturn(Optional.of(reportTemplate));
        when(reportTemplateMapper.toResponse(reportTemplate)).thenReturn(response);

        ReportTemplateResponse result = reportTemplateService.getById(templateId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID templateId = UUID.randomUUID();

        when(reportTemplateRepository.findByIdAndBranchIdAndIsDeletedFalse(templateId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reportTemplateService.getById(templateId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("ReportTemplate");
    }

    @Test
    @DisplayName("getAll should return paged results")
    void getAll_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        ReportTemplate reportTemplate = new ReportTemplate();
        ReportTemplateResponse response = new ReportTemplateResponse();
        Page<ReportTemplate> templatePage = new PageImpl<>(List.of(reportTemplate));

        when(reportTemplateRepository.findAllByBranchIdAndIsDeletedFalse(BRANCH_ID, pageable)).thenReturn(templatePage);
        when(reportTemplateMapper.toResponse(reportTemplate)).thenReturn(response);

        Page<ReportTemplateResponse> result = reportTemplateService.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("update should update and return report template")
    void update_shouldUpdateAndReturnReportTemplate() {
        UUID templateId = UUID.randomUUID();
        ReportTemplateRequest request = new ReportTemplateRequest();
        request.setTemplateName("Updated Template");
        ReportTemplate reportTemplate = new ReportTemplate();
        reportTemplate.setTemplateName("Default Template");
        ReportTemplate saved = new ReportTemplate();
        ReportTemplateResponse response = new ReportTemplateResponse();

        when(reportTemplateRepository.findByIdAndBranchIdAndIsDeletedFalse(templateId, BRANCH_ID)).thenReturn(Optional.of(reportTemplate));
        when(reportTemplateRepository.existsByTemplateNameAndBranchIdAndIsDeletedFalse("Updated Template", BRANCH_ID)).thenReturn(false);
        when(reportTemplateRepository.save(reportTemplate)).thenReturn(saved);
        when(reportTemplateMapper.toResponse(saved)).thenReturn(response);

        ReportTemplateResponse result = reportTemplateService.update(templateId, request);

        assertThat(result).isEqualTo(response);
        verify(reportTemplateMapper).updateEntity(request, reportTemplate);
    }

    @Test
    @DisplayName("delete should soft delete report template")
    void delete_shouldSoftDelete() {
        UUID templateId = UUID.randomUUID();
        ReportTemplate reportTemplate = new ReportTemplate();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(reportTemplateRepository.findByIdAndBranchIdAndIsDeletedFalse(templateId, BRANCH_ID)).thenReturn(Optional.of(reportTemplate));

        reportTemplateService.delete(templateId);

        assertThat(reportTemplate.getIsDeleted()).isTrue();
        assertThat(reportTemplate.getDeletedAt()).isNotNull();
        verify(reportTemplateRepository).save(reportTemplate);
    }
}
