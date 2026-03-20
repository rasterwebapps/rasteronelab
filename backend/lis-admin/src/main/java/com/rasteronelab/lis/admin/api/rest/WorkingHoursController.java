package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.WorkingHoursRequest;
import com.rasteronelab.lis.admin.api.dto.WorkingHoursResponse;
import com.rasteronelab.lis.admin.application.service.WorkingHoursService;
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
 * REST controller for Working Hours CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/config/working-hours")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class WorkingHoursController {

    private final WorkingHoursService workingHoursService;

    @PostMapping
    public ResponseEntity<ApiResponse<WorkingHoursResponse>> create(
            @Valid @RequestBody WorkingHoursRequest request) {
        WorkingHoursResponse response = workingHoursService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Working hours created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<WorkingHoursResponse>>> getAll(Pageable pageable) {
        Page<WorkingHoursResponse> page = workingHoursService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkingHoursResponse>> getById(@PathVariable UUID id) {
        WorkingHoursResponse response = workingHoursService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkingHoursResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody WorkingHoursRequest request) {
        WorkingHoursResponse response = workingHoursService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Working hours updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        workingHoursService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Working hours deleted successfully"));
    }

    public WorkingHoursController(WorkingHoursService workingHoursService) {
        this.workingHoursService = workingHoursService;
    }

}
