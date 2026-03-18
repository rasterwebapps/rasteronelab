package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.ParameterRequest;
import com.rasteronelab.lis.admin.api.dto.ParameterResponse;
import com.rasteronelab.lis.admin.application.service.ParameterService;
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
 * REST controller for Parameter CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/parameters")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
@RequiredArgsConstructor
public class ParameterController {

    private final ParameterService parameterService;

    @PostMapping
    public ResponseEntity<ApiResponse<ParameterResponse>> create(
            @Valid @RequestBody ParameterRequest request) {
        ParameterResponse response = parameterService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Parameter created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<ParameterResponse>>> getAll(Pageable pageable) {
        Page<ParameterResponse> page = parameterService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<PagedResponse<ParameterResponse>>> getActive(Pageable pageable) {
        Page<ParameterResponse> page = parameterService.getActive(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ParameterResponse>> getById(@PathVariable UUID id) {
        ParameterResponse response = parameterService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ParameterResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody ParameterRequest request) {
        ParameterResponse response = parameterService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Parameter updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        parameterService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Parameter deleted successfully"));
    }
}
