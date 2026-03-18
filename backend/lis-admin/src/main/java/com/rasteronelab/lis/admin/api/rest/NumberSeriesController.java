package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.NumberSeriesRequest;
import com.rasteronelab.lis.admin.api.dto.NumberSeriesResponse;
import com.rasteronelab.lis.admin.application.service.NumberSeriesService;
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
 * REST controller for Number Series CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/config/number-series")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class NumberSeriesController {

    private final NumberSeriesService numberSeriesService;

    @PostMapping
    public ResponseEntity<ApiResponse<NumberSeriesResponse>> create(
            @Valid @RequestBody NumberSeriesRequest request) {
        NumberSeriesResponse response = numberSeriesService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Number series created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<NumberSeriesResponse>>> getAll(Pageable pageable) {
        Page<NumberSeriesResponse> page = numberSeriesService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NumberSeriesResponse>> getById(@PathVariable UUID id) {
        NumberSeriesResponse response = numberSeriesService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NumberSeriesResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody NumberSeriesRequest request) {
        NumberSeriesResponse response = numberSeriesService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Number series updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        numberSeriesService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Number series deleted successfully"));
    }

    public NumberSeriesController(NumberSeriesService numberSeriesService) {
        this.numberSeriesService = numberSeriesService;
    }

}
