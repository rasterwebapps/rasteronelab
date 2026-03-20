package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.TATConfigurationRequest;
import com.rasteronelab.lis.admin.api.dto.TATConfigurationResponse;
import com.rasteronelab.lis.admin.application.service.TATConfigurationService;
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
 * REST controller for TAT Configuration CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/config/tat")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class TATConfigurationController {

    private final TATConfigurationService tatConfigurationService;

    @PostMapping
    public ResponseEntity<ApiResponse<TATConfigurationResponse>> create(
            @Valid @RequestBody TATConfigurationRequest request) {
        TATConfigurationResponse response = tatConfigurationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("TAT configuration created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<TATConfigurationResponse>>> getAll(Pageable pageable) {
        Page<TATConfigurationResponse> page = tatConfigurationService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<ApiResponse<PagedResponse<TATConfigurationResponse>>> getByTest(
            @PathVariable UUID testId, Pageable pageable) {
        Page<TATConfigurationResponse> page = tatConfigurationService.getByTest(testId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<PagedResponse<TATConfigurationResponse>>> getByDepartment(
            @PathVariable UUID departmentId, Pageable pageable) {
        Page<TATConfigurationResponse> page = tatConfigurationService.getByDepartment(departmentId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TATConfigurationResponse>> getById(@PathVariable UUID id) {
        TATConfigurationResponse response = tatConfigurationService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TATConfigurationResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody TATConfigurationRequest request) {
        TATConfigurationResponse response = tatConfigurationService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("TAT configuration updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        tatConfigurationService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("TAT configuration deleted successfully"));
    }

    public TATConfigurationController(TATConfigurationService tatConfigurationService) {
        this.tatConfigurationService = tatConfigurationService;
    }

}
