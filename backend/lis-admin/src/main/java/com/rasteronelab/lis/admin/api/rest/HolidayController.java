package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.HolidayRequest;
import com.rasteronelab.lis.admin.api.dto.HolidayResponse;
import com.rasteronelab.lis.admin.application.service.HolidayService;
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
 * REST controller for Holiday CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/config/holidays")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class HolidayController {

    private final HolidayService holidayService;

    @PostMapping
    public ResponseEntity<ApiResponse<HolidayResponse>> create(
            @Valid @RequestBody HolidayRequest request) {
        HolidayResponse response = holidayService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Holiday created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<HolidayResponse>>> getAll(Pageable pageable) {
        Page<HolidayResponse> page = holidayService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HolidayResponse>> getById(@PathVariable UUID id) {
        HolidayResponse response = holidayService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HolidayResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody HolidayRequest request) {
        HolidayResponse response = holidayService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Holiday updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        holidayService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Holiday deleted successfully"));
    }

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

}
