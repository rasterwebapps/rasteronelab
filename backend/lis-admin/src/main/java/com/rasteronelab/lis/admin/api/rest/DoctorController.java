package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.DoctorRequest;
import com.rasteronelab.lis.admin.api.dto.DoctorResponse;
import com.rasteronelab.lis.admin.application.service.DoctorService;
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
 * REST controller for Doctor CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/doctors")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<ApiResponse<DoctorResponse>> create(
            @Valid @RequestBody DoctorRequest request) {
        DoctorResponse response = doctorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Doctor created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<DoctorResponse>>> getAll(Pageable pageable) {
        Page<DoctorResponse> page = doctorService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<DoctorResponse>>> search(
            @RequestParam String query, Pageable pageable) {
        Page<DoctorResponse> page = doctorService.search(query, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> getById(@PathVariable UUID id) {
        DoctorResponse response = doctorService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody DoctorRequest request) {
        DoctorResponse response = doctorService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Doctor updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        doctorService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Doctor deleted successfully"));
    }

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

}
