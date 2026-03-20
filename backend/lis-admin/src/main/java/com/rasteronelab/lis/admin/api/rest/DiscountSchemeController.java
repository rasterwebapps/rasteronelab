package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.DiscountSchemeRequest;
import com.rasteronelab.lis.admin.api.dto.DiscountSchemeResponse;
import com.rasteronelab.lis.admin.application.service.DiscountSchemeService;
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
 * REST controller for DiscountScheme CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/discount-schemes")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class DiscountSchemeController {

    private final DiscountSchemeService discountSchemeService;

    @PostMapping
    public ResponseEntity<ApiResponse<DiscountSchemeResponse>> create(
            @Valid @RequestBody DiscountSchemeRequest request) {
        DiscountSchemeResponse response = discountSchemeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Discount scheme created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<DiscountSchemeResponse>>> getAll(Pageable pageable) {
        Page<DiscountSchemeResponse> page = discountSchemeService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<DiscountSchemeResponse>>> search(
            @RequestParam String query, Pageable pageable) {
        Page<DiscountSchemeResponse> page = discountSchemeService.search(query, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DiscountSchemeResponse>> getById(@PathVariable UUID id) {
        DiscountSchemeResponse response = discountSchemeService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DiscountSchemeResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody DiscountSchemeRequest request) {
        DiscountSchemeResponse response = discountSchemeService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Discount scheme updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        discountSchemeService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Discount scheme deleted successfully"));
    }

    public DiscountSchemeController(DiscountSchemeService discountSchemeService) {
        this.discountSchemeService = discountSchemeService;
    }

}
