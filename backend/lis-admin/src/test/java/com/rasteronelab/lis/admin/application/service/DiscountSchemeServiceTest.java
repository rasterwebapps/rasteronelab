package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.DiscountSchemeRequest;
import com.rasteronelab.lis.admin.api.dto.DiscountSchemeResponse;
import com.rasteronelab.lis.admin.api.mapper.DiscountSchemeMapper;
import com.rasteronelab.lis.admin.domain.model.DiscountScheme;
import com.rasteronelab.lis.admin.domain.repository.DiscountSchemeRepository;
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

@DisplayName("DiscountSchemeService")
@ExtendWith(MockitoExtension.class)
class DiscountSchemeServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private DiscountSchemeRepository discountSchemeRepository;

    @Mock
    private DiscountSchemeMapper discountSchemeMapper;

    @InjectMocks
    private DiscountSchemeService discountSchemeService;

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
    @DisplayName("create should save and return discount scheme")
    void create_shouldSaveAndReturnDiscountScheme() {
        DiscountSchemeRequest request = new DiscountSchemeRequest();
        request.setSchemeCode("DISC-001");
        DiscountScheme discountScheme = new DiscountScheme();
        DiscountScheme saved = new DiscountScheme();
        DiscountSchemeResponse response = new DiscountSchemeResponse();

        when(discountSchemeRepository.existsBySchemeCodeAndBranchIdAndIsDeletedFalse("DISC-001", BRANCH_ID)).thenReturn(false);
        when(discountSchemeMapper.toEntity(request)).thenReturn(discountScheme);
        when(discountSchemeRepository.save(discountScheme)).thenReturn(saved);
        when(discountSchemeMapper.toResponse(saved)).thenReturn(response);

        DiscountSchemeResponse result = discountSchemeService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(discountScheme.getBranchId()).isEqualTo(BRANCH_ID);
        verify(discountSchemeRepository).save(discountScheme);
    }

    @Test
    @DisplayName("create with duplicate scheme code should throw DuplicateResourceException")
    void create_withDuplicateSchemeCode_shouldThrowDuplicateResourceException() {
        DiscountSchemeRequest request = new DiscountSchemeRequest();
        request.setSchemeCode("DISC-001");

        when(discountSchemeRepository.existsBySchemeCodeAndBranchIdAndIsDeletedFalse("DISC-001", BRANCH_ID)).thenReturn(true);

        assertThatThrownBy(() -> discountSchemeService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("schemeCode");
    }

    @Test
    @DisplayName("getById should return discount scheme")
    void getById_shouldReturnDiscountScheme() {
        UUID schemeId = UUID.randomUUID();
        DiscountScheme discountScheme = new DiscountScheme();
        DiscountSchemeResponse response = new DiscountSchemeResponse();

        when(discountSchemeRepository.findByIdAndBranchIdAndIsDeletedFalse(schemeId, BRANCH_ID)).thenReturn(Optional.of(discountScheme));
        when(discountSchemeMapper.toResponse(discountScheme)).thenReturn(response);

        DiscountSchemeResponse result = discountSchemeService.getById(schemeId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID schemeId = UUID.randomUUID();

        when(discountSchemeRepository.findByIdAndBranchIdAndIsDeletedFalse(schemeId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> discountSchemeService.getById(schemeId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("DiscountScheme");
    }

    @Test
    @DisplayName("getAll should return paged results")
    void getAll_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        DiscountScheme discountScheme = new DiscountScheme();
        DiscountSchemeResponse response = new DiscountSchemeResponse();
        Page<DiscountScheme> schemePage = new PageImpl<>(List.of(discountScheme));

        when(discountSchemeRepository.findAllByBranchIdAndIsDeletedFalse(BRANCH_ID, pageable)).thenReturn(schemePage);
        when(discountSchemeMapper.toResponse(discountScheme)).thenReturn(response);

        Page<DiscountSchemeResponse> result = discountSchemeService.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("update should update and return discount scheme")
    void update_shouldUpdateAndReturnDiscountScheme() {
        UUID schemeId = UUID.randomUUID();
        DiscountSchemeRequest request = new DiscountSchemeRequest();
        request.setSchemeCode("DISC-002");
        DiscountScheme discountScheme = new DiscountScheme();
        discountScheme.setSchemeCode("DISC-001");
        DiscountScheme saved = new DiscountScheme();
        DiscountSchemeResponse response = new DiscountSchemeResponse();

        when(discountSchemeRepository.findByIdAndBranchIdAndIsDeletedFalse(schemeId, BRANCH_ID)).thenReturn(Optional.of(discountScheme));
        when(discountSchemeRepository.existsBySchemeCodeAndBranchIdAndIsDeletedFalse("DISC-002", BRANCH_ID)).thenReturn(false);
        when(discountSchemeRepository.save(discountScheme)).thenReturn(saved);
        when(discountSchemeMapper.toResponse(saved)).thenReturn(response);

        DiscountSchemeResponse result = discountSchemeService.update(schemeId, request);

        assertThat(result).isEqualTo(response);
        verify(discountSchemeMapper).updateEntity(request, discountScheme);
    }

    @Test
    @DisplayName("delete should soft delete discount scheme")
    void delete_shouldSoftDelete() {
        UUID schemeId = UUID.randomUUID();
        DiscountScheme discountScheme = new DiscountScheme();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(discountSchemeRepository.findByIdAndBranchIdAndIsDeletedFalse(schemeId, BRANCH_ID)).thenReturn(Optional.of(discountScheme));

        discountSchemeService.delete(schemeId);

        assertThat(discountScheme.getIsDeleted()).isTrue();
        assertThat(discountScheme.getDeletedAt()).isNotNull();
        verify(discountSchemeRepository).save(discountScheme);
    }
}
