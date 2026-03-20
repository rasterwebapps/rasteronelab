package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.WorkingHoursRequest;
import com.rasteronelab.lis.admin.api.dto.WorkingHoursResponse;
import com.rasteronelab.lis.admin.api.mapper.WorkingHoursMapper;
import com.rasteronelab.lis.admin.domain.model.WorkingHours;
import com.rasteronelab.lis.admin.domain.repository.WorkingHoursRepository;
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

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("WorkingHoursService")
@ExtendWith(MockitoExtension.class)
class WorkingHoursServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private WorkingHoursRepository workingHoursRepository;

    @Mock
    private WorkingHoursMapper workingHoursMapper;

    @InjectMocks
    private WorkingHoursService workingHoursService;

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
    @DisplayName("create should save and return working hours")
    void create_shouldSaveAndReturnWorkingHours() {
        WorkingHoursRequest request = new WorkingHoursRequest();
        request.setDayOfWeek(1);
        request.setOpenTime(LocalTime.of(8, 0));
        request.setCloseTime(LocalTime.of(17, 0));
        WorkingHours workingHours = new WorkingHours();
        WorkingHours saved = new WorkingHours();
        WorkingHoursResponse response = new WorkingHoursResponse();

        when(workingHoursRepository.existsByBranchIdAndDayOfWeekAndIsDeletedFalse(BRANCH_ID, 1)).thenReturn(false);
        when(workingHoursMapper.toEntity(request)).thenReturn(workingHours);
        when(workingHoursRepository.save(workingHours)).thenReturn(saved);
        when(workingHoursMapper.toResponse(saved)).thenReturn(response);

        WorkingHoursResponse result = workingHoursService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(workingHours.getBranchId()).isEqualTo(BRANCH_ID);
        verify(workingHoursRepository).save(workingHours);
    }

    @Test
    @DisplayName("create with duplicate day of week should throw DuplicateResourceException")
    void create_withDuplicateDayOfWeek_shouldThrowDuplicateResourceException() {
        WorkingHoursRequest request = new WorkingHoursRequest();
        request.setDayOfWeek(1);

        when(workingHoursRepository.existsByBranchIdAndDayOfWeekAndIsDeletedFalse(BRANCH_ID, 1)).thenReturn(true);

        assertThatThrownBy(() -> workingHoursService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("dayOfWeek");
    }

    @Test
    @DisplayName("create with open time after close time should throw IllegalArgumentException")
    void create_withOpenTimeAfterCloseTime_shouldThrowIllegalArgumentException() {
        WorkingHoursRequest request = new WorkingHoursRequest();
        request.setDayOfWeek(1);
        request.setOpenTime(LocalTime.of(17, 0));
        request.setCloseTime(LocalTime.of(8, 0));

        when(workingHoursRepository.existsByBranchIdAndDayOfWeekAndIsDeletedFalse(BRANCH_ID, 1)).thenReturn(false);

        assertThatThrownBy(() -> workingHoursService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Open time must be before close time");
    }

    @Test
    @DisplayName("create with equal open and close time should throw IllegalArgumentException")
    void create_withEqualOpenAndCloseTime_shouldThrowIllegalArgumentException() {
        WorkingHoursRequest request = new WorkingHoursRequest();
        request.setDayOfWeek(1);
        request.setOpenTime(LocalTime.of(9, 0));
        request.setCloseTime(LocalTime.of(9, 0));

        when(workingHoursRepository.existsByBranchIdAndDayOfWeekAndIsDeletedFalse(BRANCH_ID, 1)).thenReturn(false);

        assertThatThrownBy(() -> workingHoursService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Open time must be before close time");
    }

    @Test
    @DisplayName("create with null times should skip time validation")
    void create_withNullTimes_shouldSkipTimeValidation() {
        WorkingHoursRequest request = new WorkingHoursRequest();
        request.setDayOfWeek(1);
        request.setOpenTime(null);
        request.setCloseTime(null);
        WorkingHours workingHours = new WorkingHours();
        WorkingHours saved = new WorkingHours();
        WorkingHoursResponse response = new WorkingHoursResponse();

        when(workingHoursRepository.existsByBranchIdAndDayOfWeekAndIsDeletedFalse(BRANCH_ID, 1)).thenReturn(false);
        when(workingHoursMapper.toEntity(request)).thenReturn(workingHours);
        when(workingHoursRepository.save(workingHours)).thenReturn(saved);
        when(workingHoursMapper.toResponse(saved)).thenReturn(response);

        WorkingHoursResponse result = workingHoursService.create(request);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById should return working hours")
    void getById_shouldReturnWorkingHours() {
        UUID workingHoursId = UUID.randomUUID();
        WorkingHours workingHours = new WorkingHours();
        WorkingHoursResponse response = new WorkingHoursResponse();

        when(workingHoursRepository.findByIdAndBranchIdAndIsDeletedFalse(workingHoursId, BRANCH_ID)).thenReturn(Optional.of(workingHours));
        when(workingHoursMapper.toResponse(workingHours)).thenReturn(response);

        WorkingHoursResponse result = workingHoursService.getById(workingHoursId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID workingHoursId = UUID.randomUUID();

        when(workingHoursRepository.findByIdAndBranchIdAndIsDeletedFalse(workingHoursId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workingHoursService.getById(workingHoursId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("WorkingHours");
    }

    @Test
    @DisplayName("getAll should return paged results")
    void getAll_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        WorkingHours workingHours = new WorkingHours();
        WorkingHoursResponse response = new WorkingHoursResponse();
        Page<WorkingHours> workingHoursPage = new PageImpl<>(List.of(workingHours));

        when(workingHoursRepository.findAllByBranchIdAndIsDeletedFalse(BRANCH_ID, pageable)).thenReturn(workingHoursPage);
        when(workingHoursMapper.toResponse(workingHours)).thenReturn(response);

        Page<WorkingHoursResponse> result = workingHoursService.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("update should update and return working hours")
    void update_shouldUpdateAndReturnWorkingHours() {
        UUID workingHoursId = UUID.randomUUID();
        WorkingHoursRequest request = new WorkingHoursRequest();
        request.setDayOfWeek(2);
        request.setOpenTime(LocalTime.of(8, 0));
        request.setCloseTime(LocalTime.of(17, 0));
        WorkingHours workingHours = new WorkingHours();
        workingHours.setDayOfWeek(1);
        WorkingHours saved = new WorkingHours();
        WorkingHoursResponse response = new WorkingHoursResponse();

        when(workingHoursRepository.findByIdAndBranchIdAndIsDeletedFalse(workingHoursId, BRANCH_ID)).thenReturn(Optional.of(workingHours));
        when(workingHoursRepository.existsByBranchIdAndDayOfWeekAndIsDeletedFalse(BRANCH_ID, 2)).thenReturn(false);
        when(workingHoursRepository.save(workingHours)).thenReturn(saved);
        when(workingHoursMapper.toResponse(saved)).thenReturn(response);

        WorkingHoursResponse result = workingHoursService.update(workingHoursId, request);

        assertThat(result).isEqualTo(response);
        verify(workingHoursMapper).updateEntity(request, workingHours);
    }

    @Test
    @DisplayName("update with same day should not check duplicate")
    void update_withSameDay_shouldNotCheckDuplicate() {
        UUID workingHoursId = UUID.randomUUID();
        WorkingHoursRequest request = new WorkingHoursRequest();
        request.setDayOfWeek(1);
        request.setOpenTime(LocalTime.of(9, 0));
        request.setCloseTime(LocalTime.of(18, 0));
        WorkingHours workingHours = new WorkingHours();
        workingHours.setDayOfWeek(1);
        WorkingHours saved = new WorkingHours();
        WorkingHoursResponse response = new WorkingHoursResponse();

        when(workingHoursRepository.findByIdAndBranchIdAndIsDeletedFalse(workingHoursId, BRANCH_ID)).thenReturn(Optional.of(workingHours));
        when(workingHoursRepository.save(workingHours)).thenReturn(saved);
        when(workingHoursMapper.toResponse(saved)).thenReturn(response);

        WorkingHoursResponse result = workingHoursService.update(workingHoursId, request);

        assertThat(result).isEqualTo(response);
        verify(workingHoursMapper).updateEntity(request, workingHours);
    }

    @Test
    @DisplayName("update with duplicate day should throw DuplicateResourceException")
    void update_withDuplicateDay_shouldThrowDuplicateResourceException() {
        UUID workingHoursId = UUID.randomUUID();
        WorkingHoursRequest request = new WorkingHoursRequest();
        request.setDayOfWeek(2);
        request.setOpenTime(LocalTime.of(8, 0));
        request.setCloseTime(LocalTime.of(17, 0));
        WorkingHours workingHours = new WorkingHours();
        workingHours.setDayOfWeek(1);

        when(workingHoursRepository.findByIdAndBranchIdAndIsDeletedFalse(workingHoursId, BRANCH_ID)).thenReturn(Optional.of(workingHours));
        when(workingHoursRepository.existsByBranchIdAndDayOfWeekAndIsDeletedFalse(BRANCH_ID, 2)).thenReturn(true);

        assertThatThrownBy(() -> workingHoursService.update(workingHoursId, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("dayOfWeek");
    }

    @Test
    @DisplayName("update not found should throw NotFoundException")
    void update_notFound_shouldThrowNotFoundException() {
        UUID workingHoursId = UUID.randomUUID();
        WorkingHoursRequest request = new WorkingHoursRequest();

        when(workingHoursRepository.findByIdAndBranchIdAndIsDeletedFalse(workingHoursId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workingHoursService.update(workingHoursId, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("WorkingHours");
    }

    @Test
    @DisplayName("delete should soft delete working hours")
    void delete_shouldSoftDelete() {
        UUID workingHoursId = UUID.randomUUID();
        WorkingHours workingHours = new WorkingHours();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(workingHoursRepository.findByIdAndBranchIdAndIsDeletedFalse(workingHoursId, BRANCH_ID)).thenReturn(Optional.of(workingHours));

        workingHoursService.delete(workingHoursId);

        assertThat(workingHours.getIsDeleted()).isTrue();
        assertThat(workingHours.getDeletedAt()).isNotNull();
        verify(workingHoursRepository).save(workingHours);
    }

    @Test
    @DisplayName("delete not found should throw NotFoundException")
    void delete_notFound_shouldThrowNotFoundException() {
        UUID workingHoursId = UUID.randomUUID();

        when(workingHoursRepository.findByIdAndBranchIdAndIsDeletedFalse(workingHoursId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workingHoursService.delete(workingHoursId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("WorkingHours");
    }
}
