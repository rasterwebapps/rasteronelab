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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for PriceCatalog CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Exactly one of testId or panelId must be provided per entry.
 */
@Service
@Transactional
public class PriceCatalogService {

    private final PriceCatalogRepository priceCatalogRepository;
    private final TestMasterRepository testMasterRepository;
    private final TestPanelRepository testPanelRepository;
    private final PriceCatalogMapper priceCatalogMapper;

    public PriceCatalogResponse create(PriceCatalogRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        validateTestOrPanel(request);

        PriceCatalog priceCatalog = priceCatalogMapper.toEntity(request);
        priceCatalog.setBranchId(branchId);

        setTestOrPanel(priceCatalog, request, branchId);

        PriceCatalog saved = priceCatalogRepository.save(priceCatalog);
        return priceCatalogMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PriceCatalogResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        PriceCatalog priceCatalog = priceCatalogRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("PriceCatalog", id));
        return priceCatalogMapper.toResponse(priceCatalog);
    }

    @Transactional(readOnly = true)
    public Page<PriceCatalogResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return priceCatalogRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(priceCatalogMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<PriceCatalogResponse> getByRateListType(String rateListType, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return priceCatalogRepository.findAllByBranchIdAndRateListTypeAndIsDeletedFalse(branchId, rateListType, pageable)
                .map(priceCatalogMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<PriceCatalogResponse> getByTestId(UUID testId, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        if (!testMasterRepository.existsByIdAndBranchIdAndIsDeletedFalse(testId, branchId)) {
            throw new NotFoundException("Test", testId);
        }
        return priceCatalogRepository.findAllByBranchIdAndTestIdAndIsDeletedFalse(branchId, testId, pageable)
                .map(priceCatalogMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<PriceCatalogResponse> getByPanelId(UUID panelId, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        if (!testPanelRepository.existsByIdAndBranchIdAndIsDeletedFalse(panelId, branchId)) {
            throw new NotFoundException("Panel", panelId);
        }
        return priceCatalogRepository.findAllByBranchIdAndPanelIdAndIsDeletedFalse(branchId, panelId, pageable)
                .map(priceCatalogMapper::toResponse);
    }

    public PriceCatalogResponse update(UUID id, PriceCatalogRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        validateTestOrPanel(request);

        PriceCatalog priceCatalog = priceCatalogRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("PriceCatalog", id));

        priceCatalogMapper.updateEntity(request, priceCatalog);
        setTestOrPanel(priceCatalog, request, branchId);

        PriceCatalog saved = priceCatalogRepository.save(priceCatalog);
        return priceCatalogMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        PriceCatalog priceCatalog = priceCatalogRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("PriceCatalog", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        priceCatalog.softDelete(currentUser);
        priceCatalogRepository.save(priceCatalog);
    }

    private void validateTestOrPanel(PriceCatalogRequest request) {
        boolean hasTest = request.getTestId() != null;
        boolean hasPanel = request.getPanelId() != null;

        if (!hasTest && !hasPanel) {
            throw new BusinessRuleException("Either testId or panelId must be provided");
        }
        if (hasTest && hasPanel) {
            throw new BusinessRuleException("Only one of testId or panelId must be provided, not both");
        }
    }

    private void setTestOrPanel(PriceCatalog priceCatalog, PriceCatalogRequest request, UUID branchId) {
        if (request.getTestId() != null) {
            TestMaster test = testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(request.getTestId(), branchId)
                    .orElseThrow(() -> new NotFoundException("Test", request.getTestId()));
            priceCatalog.setTest(test);
            priceCatalog.setPanel(null);
        } else {
            TestPanel panel = testPanelRepository.findByIdAndBranchIdAndIsDeletedFalse(request.getPanelId(), branchId)
                    .orElseThrow(() -> new NotFoundException("Panel", request.getPanelId()));
            priceCatalog.setPanel(panel);
            priceCatalog.setTest(null);
        }
    }

    public PriceCatalogService(PriceCatalogRepository priceCatalogRepository, TestMasterRepository testMasterRepository, TestPanelRepository testPanelRepository, PriceCatalogMapper priceCatalogMapper) {
        this.priceCatalogRepository = priceCatalogRepository;
        this.testMasterRepository = testMasterRepository;
        this.testPanelRepository = testPanelRepository;
        this.priceCatalogMapper = priceCatalogMapper;
    }

}
