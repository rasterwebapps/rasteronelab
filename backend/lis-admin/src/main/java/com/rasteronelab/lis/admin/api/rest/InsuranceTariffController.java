package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.InsuranceTariffRequest;
import com.rasteronelab.lis.admin.api.dto.InsuranceTariffResponse;
import com.rasteronelab.lis.admin.application.service.InsuranceTariffService;
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
 * REST controller for InsuranceTariff CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/insurance-tariffs")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class InsuranceTariffController {

    private final InsuranceTariffService insuranceTariffService;

    @PostMapping
    public ResponseEntity<ApiResponse<InsuranceTariffResponse>> create(
            @Valid @RequestBody InsuranceTariffRequest request) {
        InsuranceTariffResponse response = insuranceTariffService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Insurance tariff created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<InsuranceTariffResponse>>> getAll(Pageable pageable) {
        Page<InsuranceTariffResponse> page = insuranceTariffService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<InsuranceTariffResponse>>> search(
            @RequestParam String query, Pageable pageable) {
        Page<InsuranceTariffResponse> page = insuranceTariffService.search(query, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InsuranceTariffResponse>> getById(@PathVariable UUID id) {
        InsuranceTariffResponse response = insuranceTariffService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InsuranceTariffResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody InsuranceTariffRequest request) {
        InsuranceTariffResponse response = insuranceTariffService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Insurance tariff updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        insuranceTariffService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Insurance tariff deleted successfully"));
    }

    public InsuranceTariffController(InsuranceTariffService insuranceTariffService) {
        this.insuranceTariffService = insuranceTariffService;
    }

}
