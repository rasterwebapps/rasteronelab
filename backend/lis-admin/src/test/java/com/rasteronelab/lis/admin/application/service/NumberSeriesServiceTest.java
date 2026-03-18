package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.NumberSeriesRequest;
import com.rasteronelab.lis.admin.api.dto.NumberSeriesResponse;
import com.rasteronelab.lis.admin.api.mapper.NumberSeriesMapper;
import com.rasteronelab.lis.admin.domain.model.NumberSeries;
import com.rasteronelab.lis.admin.domain.repository.NumberSeriesRepository;
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
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("NumberSeriesService")
@ExtendWith(MockitoExtension.class)
class NumberSeriesServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private NumberSeriesRepository numberSeriesRepository;

    @Mock
    private NumberSeriesMapper numberSeriesMapper;

    @InjectMocks
    private NumberSeriesService numberSeriesService;

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
    @DisplayName("create should save and return number series")
    void create_shouldSaveAndReturnNumberSeries() {
        NumberSeriesRequest request = new NumberSeriesRequest();
        request.setEntityType("UHID");
        NumberSeries entity = new NumberSeries();
        NumberSeries saved = new NumberSeries();
        NumberSeriesResponse response = new NumberSeriesResponse();

        when(numberSeriesRepository.existsByEntityTypeAndBranchIdAndIsDeletedFalse("UHID", BRANCH_ID)).thenReturn(false);
        when(numberSeriesMapper.toEntity(request)).thenReturn(entity);
        when(numberSeriesRepository.save(entity)).thenReturn(saved);
        when(numberSeriesMapper.toResponse(saved)).thenReturn(response);

        NumberSeriesResponse result = numberSeriesService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(entity.getBranchId()).isEqualTo(BRANCH_ID);
        verify(numberSeriesRepository).save(entity);
    }

    @Test
    @DisplayName("create with duplicate entity type should throw DuplicateResourceException")
    void create_withDuplicateEntityType_shouldThrowDuplicateResourceException() {
        NumberSeriesRequest request = new NumberSeriesRequest();
        request.setEntityType("UHID");

        when(numberSeriesRepository.existsByEntityTypeAndBranchIdAndIsDeletedFalse("UHID", BRANCH_ID)).thenReturn(true);

        assertThatThrownBy(() -> numberSeriesService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("entityType");
    }

    @Test
    @DisplayName("getById should return number series")
    void getById_shouldReturnNumberSeries() {
        UUID id = UUID.randomUUID();
        NumberSeries entity = new NumberSeries();
        NumberSeriesResponse response = new NumberSeriesResponse();

        when(numberSeriesRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(entity));
        when(numberSeriesMapper.toResponse(entity)).thenReturn(response);

        NumberSeriesResponse result = numberSeriesService.getById(id);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();

        when(numberSeriesRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> numberSeriesService.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("NumberSeries");
    }

    @Test
    @DisplayName("delete should soft delete number series")
    void delete_shouldSoftDelete() {
        UUID id = UUID.randomUUID();
        NumberSeries entity = new NumberSeries();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(numberSeriesRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(entity));

        numberSeriesService.delete(id);

        assertThat(entity.getIsDeleted()).isTrue();
        assertThat(entity.getDeletedAt()).isNotNull();
        verify(numberSeriesRepository).save(entity);
    }
}
