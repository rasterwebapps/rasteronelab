package com.rasteronelab.lis.report.api.rest;

import com.rasteronelab.lis.core.api.ApiResponse;
import com.rasteronelab.lis.core.api.PagedResponse;
import com.rasteronelab.lis.report.api.dto.ReportDeliverRequest;
import com.rasteronelab.lis.report.api.dto.ReportGenerateRequest;
import com.rasteronelab.lis.report.api.dto.ReportResponse;
import com.rasteronelab.lis.report.api.dto.ReportSignRequest;
import com.rasteronelab.lis.report.application.service.ReportService;
import com.rasteronelab.lis.report.domain.model.ReportStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.util.List;
import java.util.UUID;

/**
 * REST controller for lab report management.
 * All endpoints require branch context (X-Branch-Id header) via BranchInterceptor.
 */
@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "Report Management", description = "Lab report generation, signing, delivery, and amendment")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // ── Generate ──────────────────────────────────────────────────────────────

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_SUPERVISOR', 'LAB_TECHNICIAN')")
    @Operation(summary = "Generate a lab report",
               description = "Creates a new lab report for a test order. Generates report number and sets status to GENERATED.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Report generated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Business rule violation")
    })
    public ResponseEntity<ApiResponse<ReportResponse>> generateReport(
            @Valid @RequestBody ReportGenerateRequest request) {
        ReportResponse response = reportService.generateReport(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Report generated successfully", response));
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_SUPERVISOR', 'LAB_TECHNICIAN', 'DOCTOR', 'RECEPTIONIST')")
    @Operation(summary = "Get report by ID", description = "Returns full details of a single lab report.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Report found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Report not found")
    })
    public ResponseEntity<ApiResponse<ReportResponse>> getById(
            @Parameter(description = "Report UUID") @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_SUPERVISOR', 'LAB_TECHNICIAN', 'DOCTOR', 'RECEPTIONIST')")
    @Operation(summary = "List all reports", description = "Returns paginated list of reports in the current branch.")
    public ResponseEntity<ApiResponse<PagedResponse<ReportResponse>>> getAll(Pageable pageable) {
        Page<ReportResponse> page = reportService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_SUPERVISOR', 'LAB_TECHNICIAN', 'DOCTOR', 'RECEPTIONIST')")
    @Operation(summary = "Get reports by order", description = "Returns all reports for a given order.")
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getByOrder(
            @Parameter(description = "Order UUID") @PathVariable UUID orderId) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getByOrderId(orderId)));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_SUPERVISOR', 'LAB_TECHNICIAN', 'DOCTOR', 'RECEPTIONIST')")
    @Operation(summary = "Get reports by patient", description = "Returns paginated reports for a patient.")
    public ResponseEntity<ApiResponse<PagedResponse<ReportResponse>>> getByPatient(
            @Parameter(description = "Patient UUID") @PathVariable UUID patientId,
            Pageable pageable) {
        Page<ReportResponse> page = reportService.getByPatientId(patientId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_SUPERVISOR', 'LAB_TECHNICIAN')")
    @Operation(summary = "Get reports by status", description = "Returns reports filtered by lifecycle status.")
    public ResponseEntity<ApiResponse<PagedResponse<ReportResponse>>> getByStatus(
            @Parameter(description = "Report status") @PathVariable ReportStatus status,
            Pageable pageable) {
        Page<ReportResponse> page = reportService.getByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    // ── Sign ──────────────────────────────────────────────────────────────────

    @PutMapping("/{id}/sign")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_SUPERVISOR', 'DOCTOR')")
    @Operation(summary = "Sign a report",
               description = "Transitions a GENERATED report to SIGNED status. Only authorized personnel can sign.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Report signed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Report not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Invalid state transition")
    })
    public ResponseEntity<ApiResponse<ReportResponse>> signReport(
            @Parameter(description = "Report UUID") @PathVariable UUID id,
            @Valid @RequestBody ReportSignRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Report signed", reportService.signReport(id, request)));
    }

    // ── Deliver ───────────────────────────────────────────────────────────────

    @PutMapping("/{id}/deliver")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_SUPERVISOR', 'RECEPTIONIST')")
    @Operation(summary = "Deliver a report",
               description = "Transitions a SIGNED report to DELIVERED status via the specified channel.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Report delivered"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Report not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Invalid state transition")
    })
    public ResponseEntity<ApiResponse<ReportResponse>> deliverReport(
            @Parameter(description = "Report UUID") @PathVariable UUID id,
            @Valid @RequestBody ReportDeliverRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Report delivered", reportService.deliverReport(id, request)));
    }

    // ── Amend ─────────────────────────────────────────────────────────────────

    @PostMapping("/{id}/amend")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_SUPERVISOR', 'DOCTOR')")
    @Operation(summary = "Amend a report",
               description = "Creates a new amended version of a SIGNED or DELIVERED report. Original is marked as AMENDED.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Amended report created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Report not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Invalid state transition")
    })
    public ResponseEntity<ApiResponse<ReportResponse>> amendReport(
            @Parameter(description = "Report UUID") @PathVariable UUID id,
            @Parameter(description = "Reason for amendment") @RequestParam String reason) {
        ReportResponse response = reportService.amendReport(id, reason);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Report amended successfully", response));
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN')")
    @Operation(summary = "Delete a report", description = "Soft-deletes a lab report.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Report deleted"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Report not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteReport(
            @Parameter(description = "Report UUID") @PathVariable UUID id) {
        reportService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Report deleted successfully"));
    }
}
