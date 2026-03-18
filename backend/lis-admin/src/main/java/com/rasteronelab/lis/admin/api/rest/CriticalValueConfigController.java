package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.CriticalValueConfigRequest;
import com.rasteronelab.lis.admin.api.dto.CriticalValueConfigResponse;
import com.rasteronelab.lis.admin.application.service.CriticalValueConfigService;
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

import java.util.List;
import java.util.UUID;

/**
 * REST controller for Critical Value Config CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/config/critical-values")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class CriticalValueConfigController {

    private final CriticalValueConfigService criticalValueConfigService;

    @PostMapping
    public ResponseEntity<ApiResponse<CriticalValueConfigResponse>> create(
            @Valid @RequestBody CriticalValueConfigRequest request) {
        CriticalValueConfigResponse response = criticalValueConfigService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Critical value config created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<CriticalValueConfigResponse>>> getAll(Pageable pageable) {
        Page<CriticalValueConfigResponse> page = criticalValueConfigService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/parameter/{parameterId}")
    public ResponseEntity<ApiResponse<List<CriticalValueConfigResponse>>> getByParameter(
            @PathVariable UUID parameterId) {
        List<CriticalValueConfigResponse> configs = criticalValueConfigService.getByParameterId(parameterId);
        return ResponseEntity.ok(ApiResponse.success(configs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CriticalValueConfigResponse>> getById(@PathVariable UUID id) {
        CriticalValueConfigResponse response = criticalValueConfigService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CriticalValueConfigResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody CriticalValueConfigRequest request) {
        CriticalValueConfigResponse response = criticalValueConfigService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Critical value config updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        criticalValueConfigService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Critical value config deleted successfully"));
    }

    public CriticalValueConfigController(CriticalValueConfigService criticalValueConfigService) {
        this.criticalValueConfigService = criticalValueConfigService;
    }

}
