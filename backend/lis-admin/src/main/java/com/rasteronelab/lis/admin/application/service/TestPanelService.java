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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service for TestPanel CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Panel code must be unique within a branch.
 */
@Service
@Transactional
public class TestPanelService {

    private final TestPanelRepository testPanelRepository;
    private final PanelTestRepository panelTestRepository;
    private final TestMasterRepository testMasterRepository;
    private final DepartmentRepository departmentRepository;
    private final TestPanelMapper testPanelMapper;

    public TestPanelResponse create(TestPanelRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Department department = departmentRepository.findByIdAndIsDeletedFalse(request.getDepartmentId())
                .orElseThrow(() -> new NotFoundException("Department", request.getDepartmentId()));

        if (testPanelRepository.existsByCodeAndBranchIdAndIsDeletedFalse(request.getCode(), branchId)) {
            throw new DuplicateResourceException("Panel", "code", request.getCode());
        }

        TestPanel panel = testPanelMapper.toEntity(request);
        panel.setBranchId(branchId);
        panel.setDepartment(department);
        TestPanel saved = testPanelRepository.save(panel);

        if (request.getTestIds() != null && !request.getTestIds().isEmpty()) {
            addTestsToPanel(saved, request.getTestIds(), branchId);
        }

        return buildResponse(saved);
    }

    @Transactional(readOnly = true)
    public TestPanelResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        TestPanel panel = testPanelRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Panel", id));
        return buildResponse(panel);
    }

    @Transactional(readOnly = true)
    public Page<TestPanelResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return testPanelRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(this::buildResponse);
    }

    @Transactional(readOnly = true)
    public Page<TestPanelResponse> getActive(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return testPanelRepository.findAllByBranchIdAndIsActiveAndIsDeletedFalse(branchId, true, pageable)
                .map(this::buildResponse);
    }

    public TestPanelResponse update(UUID id, TestPanelRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        TestPanel panel = testPanelRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Panel", id));

        Department department = departmentRepository.findByIdAndIsDeletedFalse(request.getDepartmentId())
                .orElseThrow(() -> new NotFoundException("Department", request.getDepartmentId()));

        if (!request.getCode().equals(panel.getCode())) {
            if (testPanelRepository.existsByCodeAndBranchIdAndIsDeletedFalse(request.getCode(), branchId)) {
                throw new DuplicateResourceException("Panel", "code", request.getCode());
            }
        }

        testPanelMapper.updateEntity(request, panel);
        panel.setDepartment(department);
        TestPanel saved = testPanelRepository.save(panel);
        return buildResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        TestPanel panel = testPanelRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Panel", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        // Soft-delete associated panel-test mappings
        List<PanelTest> panelTests = panelTestRepository.findAllByPanelIdAndIsDeletedFalse(id);
        for (PanelTest pt : panelTests) {
            pt.softDelete(currentUser);
            panelTestRepository.save(pt);
        }

        panel.softDelete(currentUser);
        testPanelRepository.save(panel);
    }

    public TestPanelResponse addTest(UUID panelId, UUID testId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        TestPanel panel = testPanelRepository.findByIdAndBranchIdAndIsDeletedFalse(panelId, branchId)
                .orElseThrow(() -> new NotFoundException("Panel", panelId));

        TestMaster test = testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(testId, branchId)
                .orElseThrow(() -> new NotFoundException("Test", testId));

        if (panelTestRepository.existsByPanelIdAndTestIdAndIsDeletedFalse(panelId, testId)) {
            throw new DuplicateResourceException("PanelTest", "testId", testId.toString());
        }

        PanelTest panelTest = new PanelTest();
        panelTest.setTestPanel(panel);
        panelTest.setTestMaster(test);
        panelTestRepository.save(panelTest);

        return buildResponse(panel);
    }

    public TestPanelResponse removeTest(UUID panelId, UUID testId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        TestPanel panel = testPanelRepository.findByIdAndBranchIdAndIsDeletedFalse(panelId, branchId)
                .orElseThrow(() -> new NotFoundException("Panel", panelId));

        List<PanelTest> panelTests = panelTestRepository.findAllByPanelIdAndIsDeletedFalse(panelId);
        PanelTest target = panelTests.stream()
                .filter(pt -> testId.equals(pt.getTestId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("PanelTest", testId));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        target.softDelete(currentUser);
        panelTestRepository.save(target);

        return buildResponse(panel);
    }

    @Transactional(readOnly = true)
    public List<TestPanelResponse.PanelTestItem> getExpandedTests(UUID panelId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (!testPanelRepository.existsByIdAndBranchIdAndIsDeletedFalse(panelId, branchId)) {
            throw new NotFoundException("Panel", panelId);
        }

        return getPanelTestItems(panelId);
    }

    private void addTestsToPanel(TestPanel panel, List<UUID> testIds, UUID branchId) {
        int order = 1;
        for (UUID testId : testIds) {
            TestMaster test = testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(testId, branchId)
                    .orElseThrow(() -> new NotFoundException("Test", testId));

            PanelTest panelTest = new PanelTest();
            panelTest.setTestPanel(panel);
            panelTest.setTestMaster(test);
            panelTest.setDisplayOrder(order++);
            panelTestRepository.save(panelTest);
        }
    }

    private TestPanelResponse buildResponse(TestPanel panel) {
        TestPanelResponse response = testPanelMapper.toResponse(panel);
        response.setTests(getPanelTestItems(panel.getId()));
        return response;
    }

    private List<TestPanelResponse.PanelTestItem> getPanelTestItems(UUID panelId) {
        List<PanelTest> panelTests = panelTestRepository.findAllByPanelIdAndIsDeletedFalse(panelId);
        List<TestPanelResponse.PanelTestItem> items = new ArrayList<>();
        for (PanelTest pt : panelTests) {
            TestMaster test = pt.getTestMaster();
            items.add(TestPanelResponse.PanelTestItem.builder()
                    .testId(pt.getTestId())
                    .testName(test.getName())
                    .testCode(test.getCode())
                    .displayOrder(pt.getDisplayOrder())
                    .build());
        }
        return items;
    }

    public TestPanelService(TestPanelRepository testPanelRepository, PanelTestRepository panelTestRepository, TestMasterRepository testMasterRepository, DepartmentRepository departmentRepository, TestPanelMapper testPanelMapper) {
        this.testPanelRepository = testPanelRepository;
        this.panelTestRepository = panelTestRepository;
        this.testMasterRepository = testMasterRepository;
        this.departmentRepository = departmentRepository;
        this.testPanelMapper = testPanelMapper;
    }

}
