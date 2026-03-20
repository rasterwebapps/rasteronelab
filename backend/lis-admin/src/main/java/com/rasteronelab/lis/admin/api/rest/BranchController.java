package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.BranchRequest;
import com.rasteronelab.lis.admin.api.dto.BranchResponse;
import com.rasteronelab.lis.admin.application.service.BranchService;
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
 * REST controller for Branch CRUD operations.
 * Restricted to SUPER_ADMIN and ORG_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/branches")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN')")
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<ApiResponse<BranchResponse>> create(
            @Valid @RequestBody BranchRequest request) {
        BranchResponse response = branchService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Branch created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<BranchResponse>>> getAll(Pageable pageable) {
        Page<BranchResponse> page = branchService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<ApiResponse<PagedResponse<BranchResponse>>> getByOrganization(
            @PathVariable UUID orgId, Pageable pageable) {
        Page<BranchResponse> page = branchService.getByOrganization(orgId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchResponse>> getById(@PathVariable UUID id) {
        BranchResponse response = branchService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody BranchRequest request) {
        BranchResponse response = branchService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Branch updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        branchService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Branch deleted successfully"));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<BranchResponse>> activate(@PathVariable UUID id) {
        BranchResponse response = branchService.activate(id);
        return ResponseEntity.ok(ApiResponse.success("Branch activated successfully", response));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<BranchResponse>> deactivate(@PathVariable UUID id) {
        BranchResponse response = branchService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.success("Branch deactivated successfully", response));
    }

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

}
