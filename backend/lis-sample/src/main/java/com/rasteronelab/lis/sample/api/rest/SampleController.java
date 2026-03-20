package com.rasteronelab.lis.sample.api.rest;

import com.rasteronelab.lis.core.api.ApiResponse;
import com.rasteronelab.lis.core.api.PagedResponse;
import com.rasteronelab.lis.sample.api.dto.SampleAliquotRequest;
import com.rasteronelab.lis.sample.api.dto.SampleCollectRequest;
import com.rasteronelab.lis.sample.api.dto.SampleReceiveRequest;
import com.rasteronelab.lis.sample.api.dto.SampleRejectRequest;
import com.rasteronelab.lis.sample.api.dto.SampleResponse;
import com.rasteronelab.lis.sample.api.dto.SampleStorageRequest;
import com.rasteronelab.lis.sample.api.dto.SampleTrackingResponse;
import com.rasteronelab.lis.sample.api.dto.SampleTransferRequest;
import com.rasteronelab.lis.sample.api.dto.SampleTransferResponse;
import com.rasteronelab.lis.sample.application.service.SampleService;
import com.rasteronelab.lis.sample.domain.model.SampleStatus;
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
 * REST controller for sample lifecycle management (LIS-055 to LIS-061).
 * All endpoints require branch context (X-Branch-Id header) via BranchInterceptor.
 */
@RestController
@RequestMapping("/api/v1/samples")
@Tag(name = "Sample Management", description = "Sample collection, receiving, aliquoting, storage, and transfer")
public class SampleController {

    private final SampleService sampleService;

    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    // ── Collection (LIS-055) ─────────────────────────────────────────────────

    @PostMapping("/collect")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'PHLEBOTOMIST', 'LAB_TECHNICIAN')")
    @Operation(summary = "Collect samples for an order",
               description = "Records collection of one or more tubes. Generates barcodes and updates order status.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Samples collected successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Business rule violation")
    })
    public ResponseEntity<ApiResponse<List<SampleResponse>>> collectSamples(
            @Valid @RequestBody SampleCollectRequest request) {
        List<SampleResponse> responses = sampleService.collectSamples(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Samples collected successfully", responses));
    }

    // ── Receiving (LIS-057) ───────────────────────────────────────────────────

    @PutMapping("/{id}/receive")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_TECHNICIAN', 'LAB_SUPERVISOR')")
    @Operation(summary = "Receive a sample at lab",
               description = "Marks a collected sample as received and starts the TAT clock.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sample received"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Sample not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Invalid state transition")
    })
    public ResponseEntity<ApiResponse<SampleResponse>> receiveSample(
            @Parameter(description = "Sample UUID") @PathVariable UUID id,
            @Valid @RequestBody SampleReceiveRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Sample received", sampleService.receiveSample(id, request)));
    }

    @PutMapping("/{id}/accept")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_TECHNICIAN', 'LAB_SUPERVISOR')")
    @Operation(summary = "Accept a received sample",
               description = "Transitions a received sample to ACCEPTED status for processing.")
    public ResponseEntity<ApiResponse<SampleResponse>> acceptSample(
            @Parameter(description = "Sample UUID") @PathVariable UUID id,
            @Parameter(description = "Accepting user UUID") @RequestParam UUID acceptedBy) {
        return ResponseEntity.ok(ApiResponse.success("Sample accepted", sampleService.acceptSample(id, acceptedBy)));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_TECHNICIAN', 'LAB_SUPERVISOR')")
    @Operation(summary = "Reject a sample",
               description = "Rejects a sample with a mandatory reason. Triggers recollection notification if required.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sample rejected"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Missing rejection reason")
    })
    public ResponseEntity<ApiResponse<SampleResponse>> rejectSample(
            @Parameter(description = "Sample UUID") @PathVariable UUID id,
            @Valid @RequestBody SampleRejectRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Sample rejected", sampleService.rejectSample(id, request)));
    }

    // ── Aliquoting (LIS-058) ─────────────────────────────────────────────────

    @PostMapping("/{id}/aliquot")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_TECHNICIAN', 'LAB_SUPERVISOR')")
    @Operation(summary = "Aliquot a sample",
               description = "Splits an accepted sample into child aliquots for different departments.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Sample aliquoted"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Sample not in ACCEPTED status")
    })
    public ResponseEntity<ApiResponse<List<SampleResponse>>> aliquotSample(
            @Parameter(description = "Parent sample UUID") @PathVariable UUID id,
            @Valid @RequestBody SampleAliquotRequest request) {
        List<SampleResponse> aliquots = sampleService.aliquotSample(id, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Sample aliquoted successfully", aliquots));
    }

    // ── Storage (LIS-059) ─────────────────────────────────────────────────────

    @PutMapping("/{id}/storage")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_TECHNICIAN', 'LAB_SUPERVISOR')")
    @Operation(summary = "Store a sample",
               description = "Records the physical storage location (rack/shelf/position) of a processed sample.")
    public ResponseEntity<ApiResponse<SampleResponse>> storeSample(
            @Parameter(description = "Sample UUID") @PathVariable UUID id,
            @Valid @RequestBody SampleStorageRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Sample stored", sampleService.storeSample(id, request)));
    }

    @PutMapping("/{id}/dispose")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_SUPERVISOR')")
    @Operation(summary = "Dispose a sample", description = "Marks a stored sample as disposed.")
    public ResponseEntity<ApiResponse<SampleResponse>> disposeSample(
            @Parameter(description = "Sample UUID") @PathVariable UUID id,
            @Parameter(description = "Disposing user UUID") @RequestParam UUID disposedBy) {
        return ResponseEntity.ok(ApiResponse.success("Sample disposed", sampleService.disposeSample(id, disposedBy)));
    }

    @GetMapping("/{id}/tracking")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_TECHNICIAN', 'LAB_SUPERVISOR', 'RECEPTIONIST')")
    @Operation(summary = "Get sample tracking timeline",
               description = "Returns the full chronological audit trail of status changes for a sample.")
    public ResponseEntity<ApiResponse<List<SampleTrackingResponse>>> getTracking(
            @Parameter(description = "Sample UUID") @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(sampleService.getTracking(id)));
    }

    // ── Transfer (LIS-060) ────────────────────────────────────────────────────

    @PostMapping("/{id}/transfer")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'BRANCH_ADMIN', 'ADMIN')")
    @Operation(summary = "Transfer sample to another branch",
               description = "Initiates an inter-branch sample transfer. BRANCH_ADMIN or higher required.")
    public ResponseEntity<ApiResponse<SampleTransferResponse>> transferSample(
            @Parameter(description = "Sample UUID") @PathVariable UUID id,
            @Valid @RequestBody SampleTransferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Transfer initiated", sampleService.transferSample(id, request)));
    }

    @PutMapping("/transfers/{transferId}/confirm")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'BRANCH_ADMIN', 'ADMIN', 'LAB_TECHNICIAN')")
    @Operation(summary = "Confirm transfer receipt",
               description = "Confirms receipt of a transferred sample at the destination branch.")
    public ResponseEntity<ApiResponse<SampleTransferResponse>> confirmTransfer(
            @Parameter(description = "Transfer UUID") @PathVariable UUID transferId,
            @Parameter(description = "Receiving user UUID") @RequestParam UUID receivedBy) {
        return ResponseEntity.ok(ApiResponse.success("Transfer confirmed",
                sampleService.confirmTransferReceived(transferId, receivedBy)));
    }

    // ── Queries (LIS-061) ────────────────────────────────────────────────────

    @GetMapping("/pending-receipt")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_TECHNICIAN', 'LAB_SUPERVISOR')")
    @Operation(summary = "List samples pending receipt",
               description = "Returns all COLLECTED samples not yet received at lab (pending receipt worklist).")
    public ResponseEntity<ApiResponse<PagedResponse<SampleResponse>>> getPendingReceipt(Pageable pageable) {
        Page<SampleResponse> page = sampleService.getPendingReceipt(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    // ── CRUD ──────────────────────────────────────────────────────────────────

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_TECHNICIAN', 'LAB_SUPERVISOR', 'RECEPTIONIST')")
    @Operation(summary = "List all samples", description = "Returns paginated list of samples in the current branch.")
    public ResponseEntity<ApiResponse<PagedResponse<SampleResponse>>> getAll(Pageable pageable) {
        Page<SampleResponse> page = sampleService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_TECHNICIAN', 'LAB_SUPERVISOR', 'RECEPTIONIST')")
    @Operation(summary = "Get sample by ID", description = "Returns full details of a single sample.")
    public ResponseEntity<ApiResponse<SampleResponse>> getById(
            @Parameter(description = "Sample UUID") @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(sampleService.getById(id)));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_TECHNICIAN', 'LAB_SUPERVISOR', 'RECEPTIONIST')")
    @Operation(summary = "Get samples by order", description = "Returns all samples (tubes) for a given order.")
    public ResponseEntity<ApiResponse<List<SampleResponse>>> getByOrder(
            @Parameter(description = "Order UUID") @PathVariable UUID orderId) {
        return ResponseEntity.ok(ApiResponse.success(sampleService.getByOrder(orderId)));
    }

    @GetMapping("/barcode/{barcode}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_TECHNICIAN', 'LAB_SUPERVISOR', 'RECEPTIONIST', 'PHLEBOTOMIST')")
    @Operation(summary = "Get sample by barcode",
               description = "Retrieves a sample by scanning its barcode (barcode wedge scanner support).")
    public ResponseEntity<ApiResponse<SampleResponse>> getByBarcode(
            @Parameter(description = "Sample barcode string") @PathVariable String barcode) {
        return ResponseEntity.ok(ApiResponse.success(sampleService.getByBarcode(barcode)));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'LAB_TECHNICIAN', 'LAB_SUPERVISOR')")
    @Operation(summary = "Get samples by status", description = "Returns samples filtered by lifecycle status.")
    public ResponseEntity<ApiResponse<PagedResponse<SampleResponse>>> getByStatus(
            @Parameter(description = "Sample status") @PathVariable SampleStatus status,
            Pageable pageable) {
        Page<SampleResponse> page = sampleService.getByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }
}
