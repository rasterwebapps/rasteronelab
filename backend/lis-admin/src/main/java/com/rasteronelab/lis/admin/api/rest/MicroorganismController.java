package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.MicroorganismRequest;
import com.rasteronelab.lis.admin.api.dto.MicroorganismResponse;
import com.rasteronelab.lis.admin.application.service.MicroorganismService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller for Microorganism CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/microorganisms")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
@RequiredArgsConstructor
public class MicroorganismController {

    private final MicroorganismService microorganismService;

    @PostMapping
    public ResponseEntity<ApiResponse<MicroorganismResponse>> create(
            @Valid @RequestBody MicroorganismRequest request) {
        MicroorganismResponse response = microorganismService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Microorganism created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<MicroorganismResponse>>> getAll(Pageable pageable) {
        Page<MicroorganismResponse> page = microorganismService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<MicroorganismResponse>>> search(
            @RequestParam String query, Pageable pageable) {
        Page<MicroorganismResponse> page = microorganismService.search(query, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/gram-type/{gramType}")
    public ResponseEntity<ApiResponse<PagedResponse<MicroorganismResponse>>> getByGramType(
            @PathVariable String gramType, Pageable pageable) {
        Page<MicroorganismResponse> page = microorganismService.getByGramType(gramType, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MicroorganismResponse>> getById(@PathVariable UUID id) {
        MicroorganismResponse response = microorganismService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MicroorganismResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody MicroorganismRequest request) {
        MicroorganismResponse response = microorganismService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Microorganism updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        microorganismService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Microorganism deleted successfully"));
    }
}
