package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.AntibioticOrganismMappingRequest;
import com.rasteronelab.lis.admin.api.dto.AntibioticOrganismMappingResponse;
import com.rasteronelab.lis.admin.application.service.AntibioticOrganismMappingService;
import com.rasteronelab.lis.core.api.ApiResponse;
import com.rasteronelab.lis.core.api.PagedResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller for Antibiotic-Organism Mapping CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/antibiotic-organism-mappings")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AntibioticOrganismMappingController {

    private final AntibioticOrganismMappingService mappingService;

    @PostMapping
    public ResponseEntity<ApiResponse<AntibioticOrganismMappingResponse>> create(
            @Valid @RequestBody AntibioticOrganismMappingRequest request) {
        AntibioticOrganismMappingResponse response = mappingService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Antibiotic-Organism mapping created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<AntibioticOrganismMappingResponse>>> getAll(Pageable pageable) {
        Page<AntibioticOrganismMappingResponse> page = mappingService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/antibiotic/{antibioticId}")
    public ResponseEntity<ApiResponse<PagedResponse<AntibioticOrganismMappingResponse>>> getByAntibiotic(
            @PathVariable UUID antibioticId, Pageable pageable) {
        Page<AntibioticOrganismMappingResponse> page = mappingService.getByAntibiotic(antibioticId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/microorganism/{microorganismId}")
    public ResponseEntity<ApiResponse<PagedResponse<AntibioticOrganismMappingResponse>>> getByMicroorganism(
            @PathVariable UUID microorganismId, Pageable pageable) {
        Page<AntibioticOrganismMappingResponse> page = mappingService.getByMicroorganism(microorganismId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AntibioticOrganismMappingResponse>> getById(@PathVariable UUID id) {
        AntibioticOrganismMappingResponse response = mappingService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AntibioticOrganismMappingResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody AntibioticOrganismMappingRequest request) {
        AntibioticOrganismMappingResponse response = mappingService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Antibiotic-Organism mapping updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        mappingService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Antibiotic-Organism mapping deleted successfully"));
    }

    public AntibioticOrganismMappingController(AntibioticOrganismMappingService mappingService) {
        this.mappingService = mappingService;
    }

}
