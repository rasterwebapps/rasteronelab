package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.OrganizationRequest;
import com.rasteronelab.lis.admin.api.dto.OrganizationResponse;
import com.rasteronelab.lis.admin.application.service.OrganizationService;
import com.rasteronelab.lis.core.api.ApiResponse;
import com.rasteronelab.lis.core.api.PagedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
 * REST controller for Organization CRUD operations.
 * Restricted to SUPER_ADMIN role.
 */
@RestController
@RequestMapping("/api/v1/organizations")
@PreAuthorize("hasRole('SUPER_ADMIN')")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrganizationResponse>> create(
            @Valid @RequestBody OrganizationRequest request) {
        OrganizationResponse response = organizationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Organization created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<OrganizationResponse>>> getAll(Pageable pageable) {
        Page<OrganizationResponse> page = organizationService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> getById(@PathVariable UUID id) {
        OrganizationResponse response = organizationService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody OrganizationRequest request) {
        OrganizationResponse response = organizationService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Organization updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        organizationService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Organization deleted successfully"));
    }
}
