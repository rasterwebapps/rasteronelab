package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.AntibioticOrganismMappingRequest;
import com.rasteronelab.lis.admin.api.dto.AntibioticOrganismMappingResponse;
import com.rasteronelab.lis.admin.api.mapper.AntibioticOrganismMappingMapper;
import com.rasteronelab.lis.admin.domain.model.Antibiotic;
import com.rasteronelab.lis.admin.domain.model.AntibioticOrganismMapping;
import com.rasteronelab.lis.admin.domain.model.Microorganism;
import com.rasteronelab.lis.admin.domain.repository.AntibioticOrganismMappingRepository;
import com.rasteronelab.lis.admin.domain.repository.AntibioticRepository;
import com.rasteronelab.lis.admin.domain.repository.MicroorganismRepository;
import com.rasteronelab.lis.core.common.exception.DuplicateResourceException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for AntibioticOrganismMapping CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Validates antibiotic and microorganism existence.
 * Mapping must be unique per branch (antibiotic + microorganism).
 */
@Service
@Transactional
public class AntibioticOrganismMappingService {

    private final AntibioticOrganismMappingRepository mappingRepository;
    private final AntibioticRepository antibioticRepository;
    private final MicroorganismRepository microorganismRepository;
    private final AntibioticOrganismMappingMapper mappingMapper;

    public AntibioticOrganismMappingResponse create(AntibioticOrganismMappingRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Antibiotic antibiotic = antibioticRepository.findByIdAndBranchIdAndIsDeletedFalse(request.getAntibioticId(), branchId)
                .orElseThrow(() -> new NotFoundException("Antibiotic", request.getAntibioticId()));

        Microorganism microorganism = microorganismRepository.findByIdAndBranchIdAndIsDeletedFalse(request.getMicroorganismId(), branchId)
                .orElseThrow(() -> new NotFoundException("Microorganism", request.getMicroorganismId()));

        if (mappingRepository.existsByBranchIdAndAntibioticIdAndMicroorganismIdAndIsDeletedFalse(
                branchId, request.getAntibioticId(), request.getMicroorganismId())) {
            throw new DuplicateResourceException("AntibioticOrganismMapping", "antibiotic-microorganism",
                    request.getAntibioticId() + "-" + request.getMicroorganismId());
        }

        AntibioticOrganismMapping mapping = mappingMapper.toEntity(request);
        mapping.setBranchId(branchId);
        mapping.setAntibiotic(antibiotic);
        mapping.setMicroorganism(microorganism);
        AntibioticOrganismMapping saved = mappingRepository.save(mapping);
        return mappingMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public AntibioticOrganismMappingResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        AntibioticOrganismMapping mapping = mappingRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("AntibioticOrganismMapping", id));
        return mappingMapper.toResponse(mapping);
    }

    @Transactional(readOnly = true)
    public Page<AntibioticOrganismMappingResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return mappingRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(mappingMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AntibioticOrganismMappingResponse> getByAntibiotic(UUID antibioticId, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        if (!antibioticRepository.existsById(antibioticId)) {
            throw new NotFoundException("Antibiotic", antibioticId);
        }
        return mappingRepository.findAllByBranchIdAndAntibioticIdAndIsDeletedFalse(branchId, antibioticId, pageable)
                .map(mappingMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AntibioticOrganismMappingResponse> getByMicroorganism(UUID microorganismId, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        if (!microorganismRepository.existsById(microorganismId)) {
            throw new NotFoundException("Microorganism", microorganismId);
        }
        return mappingRepository.findAllByBranchIdAndMicroorganismIdAndIsDeletedFalse(branchId, microorganismId, pageable)
                .map(mappingMapper::toResponse);
    }

    public AntibioticOrganismMappingResponse update(UUID id, AntibioticOrganismMappingRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        AntibioticOrganismMapping mapping = mappingRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("AntibioticOrganismMapping", id));

        Antibiotic antibiotic = antibioticRepository.findByIdAndBranchIdAndIsDeletedFalse(request.getAntibioticId(), branchId)
                .orElseThrow(() -> new NotFoundException("Antibiotic", request.getAntibioticId()));

        Microorganism microorganism = microorganismRepository.findByIdAndBranchIdAndIsDeletedFalse(request.getMicroorganismId(), branchId)
                .orElseThrow(() -> new NotFoundException("Microorganism", request.getMicroorganismId()));

        // Validate unique mapping if antibiotic or microorganism changed
        boolean antibioticChanged = !request.getAntibioticId().equals(mapping.getAntibioticId());
        boolean microorganismChanged = !request.getMicroorganismId().equals(mapping.getMicroorganismId());
        if (antibioticChanged || microorganismChanged) {
            if (mappingRepository.existsByBranchIdAndAntibioticIdAndMicroorganismIdAndIsDeletedFalse(
                    branchId, request.getAntibioticId(), request.getMicroorganismId())) {
                throw new DuplicateResourceException("AntibioticOrganismMapping", "antibiotic-microorganism",
                        request.getAntibioticId() + "-" + request.getMicroorganismId());
            }
        }

        mappingMapper.updateEntity(request, mapping);
        mapping.setAntibiotic(antibiotic);
        mapping.setMicroorganism(microorganism);
        AntibioticOrganismMapping saved = mappingRepository.save(mapping);
        return mappingMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        AntibioticOrganismMapping mapping = mappingRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("AntibioticOrganismMapping", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        mapping.softDelete(currentUser);
        mappingRepository.save(mapping);
    }

    public AntibioticOrganismMappingService(AntibioticOrganismMappingRepository mappingRepository, AntibioticRepository antibioticRepository, MicroorganismRepository microorganismRepository, AntibioticOrganismMappingMapper mappingMapper) {
        this.mappingRepository = mappingRepository;
        this.antibioticRepository = antibioticRepository;
        this.microorganismRepository = microorganismRepository;
        this.mappingMapper = mappingMapper;
    }

}
