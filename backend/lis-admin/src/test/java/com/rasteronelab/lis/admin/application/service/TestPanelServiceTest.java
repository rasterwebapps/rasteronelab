package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.TestPanelRequest;
import com.rasteronelab.lis.admin.api.dto.TestPanelResponse;
import com.rasteronelab.lis.admin.api.mapper.TestPanelMapper;
import com.rasteronelab.lis.admin.domain.model.Department;
import com.rasteronelab.lis.admin.domain.model.PanelTest;
import com.rasteronelab.lis.admin.domain.model.TestMaster;
import com.rasteronelab.lis.admin.domain.model.TestPanel;
import com.rasteronelab.lis.admin.domain.repository.DepartmentRepository;
import com.rasteronelab.lis.admin.domain.repository.PanelTestRepository;
import com.rasteronelab.lis.admin.domain.repository.TestMasterRepository;
import com.rasteronelab.lis.admin.domain.repository.TestPanelRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("TestPanelService")
@ExtendWith(MockitoExtension.class)
class TestPanelServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private TestPanelRepository testPanelRepository;

    @Mock
    private PanelTestRepository panelTestRepository;

    @Mock
    private TestMasterRepository testMasterRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private TestPanelMapper testPanelMapper;

    @InjectMocks
    private TestPanelService testPanelService;

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
    @DisplayName("create should save and return panel")
    void create_shouldSaveAndReturnPanel() {
        UUID departmentId = UUID.randomUUID();
        TestPanelRequest request = new TestPanelRequest();
        request.setCode("PNL-001");
        request.setDepartmentId(departmentId);
        request.setTestIds(null);
        Department department = new Department();
        TestPanel panel = new TestPanel();
        UUID panelId = UUID.randomUUID();
        panel.setId(panelId);
        TestPanel saved = new TestPanel();
        saved.setId(panelId);
        TestPanelResponse response = new TestPanelResponse();

        when(departmentRepository.findByIdAndIsDeletedFalse(departmentId)).thenReturn(Optional.of(department));
        when(testPanelRepository.existsByCodeAndBranchIdAndIsDeletedFalse("PNL-001", BRANCH_ID)).thenReturn(false);
        when(testPanelMapper.toEntity(request)).thenReturn(panel);
        when(testPanelRepository.save(panel)).thenReturn(saved);
        when(testPanelMapper.toResponse(saved)).thenReturn(response);
        when(panelTestRepository.findAllByPanelIdAndIsDeletedFalse(panelId)).thenReturn(Collections.emptyList());

        TestPanelResponse result = testPanelService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(panel.getBranchId()).isEqualTo(BRANCH_ID);
        verify(testPanelRepository).save(panel);
    }

    @Test
    @DisplayName("create with test ids should add tests to panel")
    void create_withTestIds_shouldAddTestsToPanel() {
        UUID departmentId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        TestPanelRequest request = new TestPanelRequest();
        request.setCode("PNL-001");
        request.setDepartmentId(departmentId);
        request.setTestIds(List.of(testId));
        Department department = new Department();
        TestPanel panel = new TestPanel();
        UUID panelId = UUID.randomUUID();
        panel.setId(panelId);
        TestPanel saved = new TestPanel();
        saved.setId(panelId);
        TestPanelResponse response = new TestPanelResponse();
        TestMaster testMaster = new TestMaster();

        when(departmentRepository.findByIdAndIsDeletedFalse(departmentId)).thenReturn(Optional.of(department));
        when(testPanelRepository.existsByCodeAndBranchIdAndIsDeletedFalse("PNL-001", BRANCH_ID)).thenReturn(false);
        when(testPanelMapper.toEntity(request)).thenReturn(panel);
        when(testPanelRepository.save(panel)).thenReturn(saved);
        when(testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(testId, BRANCH_ID)).thenReturn(Optional.of(testMaster));
        when(panelTestRepository.save(any(PanelTest.class))).thenAnswer(i -> i.getArgument(0));
        when(testPanelMapper.toResponse(saved)).thenReturn(response);
        when(panelTestRepository.findAllByPanelIdAndIsDeletedFalse(panelId)).thenReturn(Collections.emptyList());

        TestPanelResponse result = testPanelService.create(request);

        assertThat(result).isEqualTo(response);
        verify(panelTestRepository).save(any(PanelTest.class));
    }

    @Test
    @DisplayName("create with duplicate code should throw DuplicateResourceException")
    void create_withDuplicateCode_shouldThrowDuplicateResourceException() {
        UUID departmentId = UUID.randomUUID();
        TestPanelRequest request = new TestPanelRequest();
        request.setCode("PNL-001");
        request.setDepartmentId(departmentId);
        Department department = new Department();

        when(departmentRepository.findByIdAndIsDeletedFalse(departmentId)).thenReturn(Optional.of(department));
        when(testPanelRepository.existsByCodeAndBranchIdAndIsDeletedFalse("PNL-001", BRANCH_ID)).thenReturn(true);

        assertThatThrownBy(() -> testPanelService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("code");
    }

    @Test
    @DisplayName("create with department not found should throw NotFoundException")
    void create_withDepartmentNotFound_shouldThrowNotFoundException() {
        UUID departmentId = UUID.randomUUID();
        TestPanelRequest request = new TestPanelRequest();
        request.setDepartmentId(departmentId);

        when(departmentRepository.findByIdAndIsDeletedFalse(departmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> testPanelService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Department");
    }

    @Test
    @DisplayName("getById should return panel")
    void getById_shouldReturnPanel() {
        UUID panelId = UUID.randomUUID();
        TestPanel panel = new TestPanel();
        panel.setId(panelId);
        TestPanelResponse response = new TestPanelResponse();

        when(testPanelRepository.findByIdAndBranchIdAndIsDeletedFalse(panelId, BRANCH_ID)).thenReturn(Optional.of(panel));
        when(testPanelMapper.toResponse(panel)).thenReturn(response);
        when(panelTestRepository.findAllByPanelIdAndIsDeletedFalse(panelId)).thenReturn(Collections.emptyList());

        TestPanelResponse result = testPanelService.getById(panelId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID panelId = UUID.randomUUID();

        when(testPanelRepository.findByIdAndBranchIdAndIsDeletedFalse(panelId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> testPanelService.getById(panelId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Panel");
    }

    @Test
    @DisplayName("getAll should return paged results")
    void getAll_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        TestPanel panel = new TestPanel();
        UUID panelId = UUID.randomUUID();
        panel.setId(panelId);
        TestPanelResponse response = new TestPanelResponse();
        Page<TestPanel> panelPage = new PageImpl<>(List.of(panel));

        when(testPanelRepository.findAllByBranchIdAndIsDeletedFalse(BRANCH_ID, pageable)).thenReturn(panelPage);
        when(testPanelMapper.toResponse(panel)).thenReturn(response);
        when(panelTestRepository.findAllByPanelIdAndIsDeletedFalse(panelId)).thenReturn(Collections.emptyList());

        Page<TestPanelResponse> result = testPanelService.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("getActive should return active paged results")
    void getActive_shouldReturnActivePagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        TestPanel panel = new TestPanel();
        UUID panelId = UUID.randomUUID();
        panel.setId(panelId);
        TestPanelResponse response = new TestPanelResponse();
        Page<TestPanel> panelPage = new PageImpl<>(List.of(panel));

        when(testPanelRepository.findAllByBranchIdAndIsActiveAndIsDeletedFalse(BRANCH_ID, true, pageable)).thenReturn(panelPage);
        when(testPanelMapper.toResponse(panel)).thenReturn(response);
        when(panelTestRepository.findAllByPanelIdAndIsDeletedFalse(panelId)).thenReturn(Collections.emptyList());

        Page<TestPanelResponse> result = testPanelService.getActive(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("update should update and return panel")
    void update_shouldUpdateAndReturnPanel() {
        UUID panelId = UUID.randomUUID();
        UUID departmentId = UUID.randomUUID();
        TestPanelRequest request = new TestPanelRequest();
        request.setCode("PNL-002");
        request.setDepartmentId(departmentId);
        TestPanel panel = new TestPanel();
        panel.setId(panelId);
        panel.setCode("PNL-001");
        Department department = new Department();
        TestPanel saved = new TestPanel();
        saved.setId(panelId);
        TestPanelResponse response = new TestPanelResponse();

        when(testPanelRepository.findByIdAndBranchIdAndIsDeletedFalse(panelId, BRANCH_ID)).thenReturn(Optional.of(panel));
        when(departmentRepository.findByIdAndIsDeletedFalse(departmentId)).thenReturn(Optional.of(department));
        when(testPanelRepository.existsByCodeAndBranchIdAndIsDeletedFalse("PNL-002", BRANCH_ID)).thenReturn(false);
        when(testPanelRepository.save(panel)).thenReturn(saved);
        when(testPanelMapper.toResponse(saved)).thenReturn(response);
        when(panelTestRepository.findAllByPanelIdAndIsDeletedFalse(panelId)).thenReturn(Collections.emptyList());

        TestPanelResponse result = testPanelService.update(panelId, request);

        assertThat(result).isEqualTo(response);
        verify(testPanelMapper).updateEntity(request, panel);
    }

    @Test
    @DisplayName("update with duplicate new code should throw DuplicateResourceException")
    void update_withDuplicateNewCode_shouldThrowDuplicateResourceException() {
        UUID panelId = UUID.randomUUID();
        UUID departmentId = UUID.randomUUID();
        TestPanelRequest request = new TestPanelRequest();
        request.setCode("PNL-002");
        request.setDepartmentId(departmentId);
        TestPanel panel = new TestPanel();
        panel.setCode("PNL-001");
        Department department = new Department();

        when(testPanelRepository.findByIdAndBranchIdAndIsDeletedFalse(panelId, BRANCH_ID)).thenReturn(Optional.of(panel));
        when(departmentRepository.findByIdAndIsDeletedFalse(departmentId)).thenReturn(Optional.of(department));
        when(testPanelRepository.existsByCodeAndBranchIdAndIsDeletedFalse("PNL-002", BRANCH_ID)).thenReturn(true);

        assertThatThrownBy(() -> testPanelService.update(panelId, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("code");
    }

    @Test
    @DisplayName("delete should soft delete panel and panel tests")
    void delete_shouldSoftDeletePanelAndPanelTests() {
        UUID panelId = UUID.randomUUID();
        TestPanel panel = new TestPanel();
        PanelTest panelTest = new PanelTest();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(testPanelRepository.findByIdAndBranchIdAndIsDeletedFalse(panelId, BRANCH_ID)).thenReturn(Optional.of(panel));
        when(panelTestRepository.findAllByPanelIdAndIsDeletedFalse(panelId)).thenReturn(List.of(panelTest));

        testPanelService.delete(panelId);

        assertThat(panel.getIsDeleted()).isTrue();
        assertThat(panel.getDeletedAt()).isNotNull();
        assertThat(panelTest.getIsDeleted()).isTrue();
        verify(testPanelRepository).save(panel);
        verify(panelTestRepository).save(panelTest);
    }

    @Test
    @DisplayName("delete not found should throw NotFoundException")
    void delete_notFound_shouldThrowNotFoundException() {
        UUID panelId = UUID.randomUUID();

        when(testPanelRepository.findByIdAndBranchIdAndIsDeletedFalse(panelId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> testPanelService.delete(panelId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Panel");
    }

    @Test
    @DisplayName("addTest should add test to panel")
    void addTest_shouldAddTestToPanel() {
        UUID panelId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        TestPanel panel = new TestPanel();
        panel.setId(panelId);
        TestMaster testMaster = new TestMaster();
        TestPanelResponse response = new TestPanelResponse();

        when(testPanelRepository.findByIdAndBranchIdAndIsDeletedFalse(panelId, BRANCH_ID)).thenReturn(Optional.of(panel));
        when(testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(testId, BRANCH_ID)).thenReturn(Optional.of(testMaster));
        when(panelTestRepository.existsByPanelIdAndTestIdAndIsDeletedFalse(panelId, testId)).thenReturn(false);
        when(panelTestRepository.save(any(PanelTest.class))).thenAnswer(i -> i.getArgument(0));
        when(testPanelMapper.toResponse(panel)).thenReturn(response);
        when(panelTestRepository.findAllByPanelIdAndIsDeletedFalse(panelId)).thenReturn(Collections.emptyList());

        TestPanelResponse result = testPanelService.addTest(panelId, testId);

        assertThat(result).isEqualTo(response);
        verify(panelTestRepository).save(any(PanelTest.class));
    }

    @Test
    @DisplayName("addTest with duplicate should throw DuplicateResourceException")
    void addTest_withDuplicate_shouldThrowDuplicateResourceException() {
        UUID panelId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        TestPanel panel = new TestPanel();
        TestMaster testMaster = new TestMaster();

        when(testPanelRepository.findByIdAndBranchIdAndIsDeletedFalse(panelId, BRANCH_ID)).thenReturn(Optional.of(panel));
        when(testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(testId, BRANCH_ID)).thenReturn(Optional.of(testMaster));
        when(panelTestRepository.existsByPanelIdAndTestIdAndIsDeletedFalse(panelId, testId)).thenReturn(true);

        assertThatThrownBy(() -> testPanelService.addTest(panelId, testId))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("testId");
    }

    @Test
    @DisplayName("removeTest should soft delete panel test")
    void removeTest_shouldSoftDeletePanelTest() {
        UUID panelId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        TestPanel panel = new TestPanel();
        panel.setId(panelId);
        PanelTest panelTest = new PanelTest();
        panelTest.setTestId(testId);
        TestPanelResponse response = new TestPanelResponse();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(testPanelRepository.findByIdAndBranchIdAndIsDeletedFalse(panelId, BRANCH_ID)).thenReturn(Optional.of(panel));
        // First call for removeTest logic, second call for buildResponse
        when(panelTestRepository.findAllByPanelIdAndIsDeletedFalse(panelId))
                .thenReturn(List.of(panelTest))
                .thenReturn(Collections.emptyList());
        when(panelTestRepository.save(panelTest)).thenReturn(panelTest);
        when(testPanelMapper.toResponse(panel)).thenReturn(response);

        TestPanelResponse result = testPanelService.removeTest(panelId, testId);

        assertThat(result).isEqualTo(response);
        assertThat(panelTest.getIsDeleted()).isTrue();
        verify(panelTestRepository).save(panelTest);
    }

    @Test
    @DisplayName("removeTest with test not in panel should throw NotFoundException")
    void removeTest_testNotInPanel_shouldThrowNotFoundException() {
        UUID panelId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        TestPanel panel = new TestPanel();

        when(testPanelRepository.findByIdAndBranchIdAndIsDeletedFalse(panelId, BRANCH_ID)).thenReturn(Optional.of(panel));
        when(panelTestRepository.findAllByPanelIdAndIsDeletedFalse(panelId)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> testPanelService.removeTest(panelId, testId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("PanelTest");
    }

    @Test
    @DisplayName("getExpandedTests should return panel test items")
    void getExpandedTests_shouldReturnPanelTestItems() {
        UUID panelId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        TestMaster testMaster = new TestMaster();
        testMaster.setName("CBC");
        testMaster.setCode("CBC-001");
        PanelTest panelTest = new PanelTest();
        panelTest.setTestId(testId);
        panelTest.setTestMaster(testMaster);
        panelTest.setDisplayOrder(1);

        when(testPanelRepository.existsByIdAndBranchIdAndIsDeletedFalse(panelId, BRANCH_ID)).thenReturn(true);
        when(panelTestRepository.findAllByPanelIdAndIsDeletedFalse(panelId)).thenReturn(List.of(panelTest));

        List<TestPanelResponse.PanelTestItem> result = testPanelService.getExpandedTests(panelId);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTestName()).isEqualTo("CBC");
        assertThat(result.getFirst().getTestCode()).isEqualTo("CBC-001");
    }

    @Test
    @DisplayName("getExpandedTests with panel not found should throw NotFoundException")
    void getExpandedTests_panelNotFound_shouldThrowNotFoundException() {
        UUID panelId = UUID.randomUUID();

        when(testPanelRepository.existsByIdAndBranchIdAndIsDeletedFalse(panelId, BRANCH_ID)).thenReturn(false);

        assertThatThrownBy(() -> testPanelService.getExpandedTests(panelId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Panel");
    }
}
