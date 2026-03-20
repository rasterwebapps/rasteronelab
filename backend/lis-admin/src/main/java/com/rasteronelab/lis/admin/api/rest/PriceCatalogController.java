package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.PriceCatalogRequest;
import com.rasteronelab.lis.admin.api.dto.PriceCatalogResponse;
import com.rasteronelab.lis.admin.application.service.PriceCatalogService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller for Price Catalog CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/price-catalog")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class PriceCatalogController {

    private final PriceCatalogService priceCatalogService;

    @PostMapping
    public ResponseEntity<ApiResponse<PriceCatalogResponse>> create(
            @Valid @RequestBody PriceCatalogRequest request) {
        PriceCatalogResponse response = priceCatalogService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Price catalog entry created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<PriceCatalogResponse>>> getAll(Pageable pageable) {
        Page<PriceCatalogResponse> page = priceCatalogService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PriceCatalogResponse>> getById(@PathVariable UUID id) {
        PriceCatalogResponse response = priceCatalogService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/rate-list-type/{rateListType}")
    public ResponseEntity<ApiResponse<PagedResponse<PriceCatalogResponse>>> getByRateListType(
            @PathVariable String rateListType, Pageable pageable) {
        Page<PriceCatalogResponse> page = priceCatalogService.getByRateListType(rateListType, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<ApiResponse<PagedResponse<PriceCatalogResponse>>> getByTestId(
            @PathVariable UUID testId, Pageable pageable) {
        Page<PriceCatalogResponse> page = priceCatalogService.getByTestId(testId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/panel/{panelId}")
    public ResponseEntity<ApiResponse<PagedResponse<PriceCatalogResponse>>> getByPanelId(
            @PathVariable UUID panelId, Pageable pageable) {
        Page<PriceCatalogResponse> page = priceCatalogService.getByPanelId(panelId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PriceCatalogResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody PriceCatalogRequest request) {
        PriceCatalogResponse response = priceCatalogService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Price catalog entry updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        priceCatalogService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Price catalog entry deleted successfully"));
    }

    public PriceCatalogController(PriceCatalogService priceCatalogService) {
        this.priceCatalogService = priceCatalogService;
    }

}
