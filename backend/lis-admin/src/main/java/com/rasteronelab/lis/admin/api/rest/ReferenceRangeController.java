package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.ReferenceRangeRequest;
import com.rasteronelab.lis.admin.api.dto.ReferenceRangeResponse;
import com.rasteronelab.lis.admin.application.service.ReferenceRangeService;
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
 * REST controller for Reference Range CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/reference-ranges")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class ReferenceRangeController {

    private final ReferenceRangeService referenceRangeService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReferenceRangeResponse>> create(
            @Valid @RequestBody ReferenceRangeRequest request) {
        ReferenceRangeResponse response = referenceRangeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Reference range created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<ReferenceRangeResponse>>> getAll(Pageable pageable) {
        Page<ReferenceRangeResponse> page = referenceRangeService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/parameter/{parameterId}")
    public ResponseEntity<ApiResponse<List<ReferenceRangeResponse>>> getByParameter(
            @PathVariable UUID parameterId) {
        List<ReferenceRangeResponse> ranges = referenceRangeService.getByParameterId(parameterId);
        return ResponseEntity.ok(ApiResponse.success(ranges));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReferenceRangeResponse>> getById(@PathVariable UUID id) {
        ReferenceRangeResponse response = referenceRangeService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReferenceRangeResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody ReferenceRangeRequest request) {
        ReferenceRangeResponse response = referenceRangeService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Reference range updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        referenceRangeService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Reference range deleted successfully"));
    }

    public ReferenceRangeController(ReferenceRangeService referenceRangeService) {
        this.referenceRangeService = referenceRangeService;
    }

}
