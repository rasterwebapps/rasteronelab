package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.DeltaCheckConfigRequest;
import com.rasteronelab.lis.admin.api.dto.DeltaCheckConfigResponse;
import com.rasteronelab.lis.admin.application.service.DeltaCheckConfigService;
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
 * REST controller for Delta Check Config CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/config/delta-checks")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class DeltaCheckConfigController {

    private final DeltaCheckConfigService deltaCheckConfigService;

    @PostMapping
    public ResponseEntity<ApiResponse<DeltaCheckConfigResponse>> create(
            @Valid @RequestBody DeltaCheckConfigRequest request) {
        DeltaCheckConfigResponse response = deltaCheckConfigService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Delta check config created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<DeltaCheckConfigResponse>>> getAll(Pageable pageable) {
        Page<DeltaCheckConfigResponse> page = deltaCheckConfigService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/parameter/{parameterId}")
    public ResponseEntity<ApiResponse<List<DeltaCheckConfigResponse>>> getByParameter(
            @PathVariable UUID parameterId) {
        List<DeltaCheckConfigResponse> configs = deltaCheckConfigService.getByParameterId(parameterId);
        return ResponseEntity.ok(ApiResponse.success(configs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DeltaCheckConfigResponse>> getById(@PathVariable UUID id) {
        DeltaCheckConfigResponse response = deltaCheckConfigService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DeltaCheckConfigResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody DeltaCheckConfigRequest request) {
        DeltaCheckConfigResponse response = deltaCheckConfigService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Delta check config updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        deltaCheckConfigService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Delta check config deleted successfully"));
    }

    public DeltaCheckConfigController(DeltaCheckConfigService deltaCheckConfigService) {
        this.deltaCheckConfigService = deltaCheckConfigService;
    }

}
