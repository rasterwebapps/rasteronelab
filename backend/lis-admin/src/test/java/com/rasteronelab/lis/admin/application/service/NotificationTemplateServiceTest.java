package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.NotificationTemplateRequest;
import com.rasteronelab.lis.admin.api.dto.NotificationTemplateResponse;
import com.rasteronelab.lis.admin.api.mapper.NotificationTemplateMapper;
import com.rasteronelab.lis.admin.domain.model.NotificationTemplate;
import com.rasteronelab.lis.admin.domain.repository.NotificationTemplateRepository;
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

@DisplayName("NotificationTemplateService")
@ExtendWith(MockitoExtension.class)
class NotificationTemplateServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private NotificationTemplateRepository notificationTemplateRepository;

    @Mock
    private NotificationTemplateMapper notificationTemplateMapper;

    @InjectMocks
    private NotificationTemplateService notificationTemplateService;

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
    @DisplayName("create should save and return notification template")
    void create_shouldSaveAndReturnNotificationTemplate() {
        NotificationTemplateRequest request = new NotificationTemplateRequest();
        request.setTemplateCode("TPL-001");
        NotificationTemplate template = new NotificationTemplate();
        NotificationTemplate saved = new NotificationTemplate();
        NotificationTemplateResponse response = new NotificationTemplateResponse();

        when(notificationTemplateRepository.existsByTemplateCodeAndBranchIdAndIsDeletedFalse("TPL-001", BRANCH_ID)).thenReturn(false);
        when(notificationTemplateMapper.toEntity(request)).thenReturn(template);
        when(notificationTemplateRepository.save(template)).thenReturn(saved);
        when(notificationTemplateMapper.toResponse(saved)).thenReturn(response);

        NotificationTemplateResponse result = notificationTemplateService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(template.getBranchId()).isEqualTo(BRANCH_ID);
        verify(notificationTemplateRepository).save(template);
    }

    @Test
    @DisplayName("create with duplicate templateCode should throw DuplicateResourceException")
    void create_withDuplicateTemplateCode_shouldThrowDuplicateResourceException() {
        NotificationTemplateRequest request = new NotificationTemplateRequest();
        request.setTemplateCode("TPL-001");

        when(notificationTemplateRepository.existsByTemplateCodeAndBranchIdAndIsDeletedFalse("TPL-001", BRANCH_ID)).thenReturn(true);

        assertThatThrownBy(() -> notificationTemplateService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("templateCode");
    }

    @Test
    @DisplayName("getById should return notification template")
    void getById_shouldReturnNotificationTemplate() {
        UUID templateId = UUID.randomUUID();
        NotificationTemplate template = new NotificationTemplate();
        NotificationTemplateResponse response = new NotificationTemplateResponse();

        when(notificationTemplateRepository.findByIdAndBranchIdAndIsDeletedFalse(templateId, BRANCH_ID)).thenReturn(Optional.of(template));
        when(notificationTemplateMapper.toResponse(template)).thenReturn(response);

        NotificationTemplateResponse result = notificationTemplateService.getById(templateId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID templateId = UUID.randomUUID();

        when(notificationTemplateRepository.findByIdAndBranchIdAndIsDeletedFalse(templateId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificationTemplateService.getById(templateId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("NotificationTemplate");
    }

    @Test
    @DisplayName("getAll should return paged results")
    void getAll_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        NotificationTemplate template = new NotificationTemplate();
        NotificationTemplateResponse response = new NotificationTemplateResponse();
        Page<NotificationTemplate> templatePage = new PageImpl<>(List.of(template));

        when(notificationTemplateRepository.findAllByBranchIdAndIsDeletedFalse(BRANCH_ID, pageable)).thenReturn(templatePage);
        when(notificationTemplateMapper.toResponse(template)).thenReturn(response);

        Page<NotificationTemplateResponse> result = notificationTemplateService.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("update should update and return notification template")
    void update_shouldUpdateAndReturnNotificationTemplate() {
        UUID templateId = UUID.randomUUID();
        NotificationTemplateRequest request = new NotificationTemplateRequest();
        request.setTemplateCode("TPL-002");
        NotificationTemplate template = new NotificationTemplate();
        template.setTemplateCode("TPL-001");
        NotificationTemplate saved = new NotificationTemplate();
        NotificationTemplateResponse response = new NotificationTemplateResponse();

        when(notificationTemplateRepository.findByIdAndBranchIdAndIsDeletedFalse(templateId, BRANCH_ID)).thenReturn(Optional.of(template));
        when(notificationTemplateRepository.existsByTemplateCodeAndBranchIdAndIsDeletedFalse("TPL-002", BRANCH_ID)).thenReturn(false);
        when(notificationTemplateRepository.save(template)).thenReturn(saved);
        when(notificationTemplateMapper.toResponse(saved)).thenReturn(response);

        NotificationTemplateResponse result = notificationTemplateService.update(templateId, request);

        assertThat(result).isEqualTo(response);
        verify(notificationTemplateMapper).updateEntity(request, template);
    }

    @Test
    @DisplayName("delete should soft delete notification template")
    void delete_shouldSoftDelete() {
        UUID templateId = UUID.randomUUID();
        NotificationTemplate template = new NotificationTemplate();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(notificationTemplateRepository.findByIdAndBranchIdAndIsDeletedFalse(templateId, BRANCH_ID)).thenReturn(Optional.of(template));

        notificationTemplateService.delete(templateId);

        assertThat(template.getIsDeleted()).isTrue();
        assertThat(template.getDeletedAt()).isNotNull();
        verify(notificationTemplateRepository).save(template);
    }
}
