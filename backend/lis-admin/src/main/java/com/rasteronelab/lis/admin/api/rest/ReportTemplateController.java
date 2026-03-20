package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.ReportTemplateRequest;
import com.rasteronelab.lis.admin.api.dto.ReportTemplateResponse;
import com.rasteronelab.lis.admin.application.service.ReportTemplateService;
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
 * REST controller for ReportTemplate CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/report-templates")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class ReportTemplateController {

    private final ReportTemplateService reportTemplateService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReportTemplateResponse>> create(
            @Valid @RequestBody ReportTemplateRequest request) {
        ReportTemplateResponse response = reportTemplateService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Report template created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<ReportTemplateResponse>>> getAll(Pageable pageable) {
        Page<ReportTemplateResponse> page = reportTemplateService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<ReportTemplateResponse>>> search(
            @RequestParam String query, Pageable pageable) {
        Page<ReportTemplateResponse> page = reportTemplateService.search(query, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReportTemplateResponse>> getById(@PathVariable UUID id) {
        ReportTemplateResponse response = reportTemplateService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReportTemplateResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody ReportTemplateRequest request) {
        ReportTemplateResponse response = reportTemplateService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Report template updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        reportTemplateService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Report template deleted successfully"));
    }

    public ReportTemplateController(ReportTemplateService reportTemplateService) {
        this.reportTemplateService = reportTemplateService;
    }

}
