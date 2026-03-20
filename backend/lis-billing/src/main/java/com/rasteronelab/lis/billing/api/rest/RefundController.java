package com.rasteronelab.lis.billing.api.rest;

import com.rasteronelab.lis.billing.api.dto.RefundRequest;
import com.rasteronelab.lis.billing.api.dto.RefundResponse;
import com.rasteronelab.lis.billing.application.service.RefundService;
import com.rasteronelab.lis.core.api.ApiResponse;
import com.rasteronelab.lis.core.api.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequestMapping("/api/v1/refunds")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'BILLING_STAFF')")
@Tag(name = "Refund", description = "Refund request, approval, and processing workflow")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @PostMapping
    @Operation(summary = "Request a refund", description = "Initiates a refund request for an invoice")
    public ResponseEntity<ApiResponse<RefundResponse>> request(@Valid @RequestBody RefundRequest request) {
        RefundResponse response = refundService.requestRefund(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Refund requested successfully", response));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve a refund", description = "Approves a pending refund request")
    public ResponseEntity<ApiResponse<RefundResponse>> approve(
            @PathVariable UUID id, @RequestParam String approvedBy) {
        return ResponseEntity.ok(ApiResponse.success("Refund approved", refundService.approveRefund(id, approvedBy)));
    }

    @PutMapping("/{id}/process")
    @Operation(summary = "Process a refund", description = "Processes an approved refund")
    public ResponseEntity<ApiResponse<RefundResponse>> process(@Parameter(description = "Refund UUID") @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Refund processed", refundService.processRefund(id)));
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject a refund", description = "Rejects a pending refund request with a reason")
    public ResponseEntity<ApiResponse<RefundResponse>> reject(
            @PathVariable UUID id, @RequestParam String reason) {
        return ResponseEntity.ok(ApiResponse.success("Refund rejected", refundService.rejectRefund(id, reason)));
    }

    @GetMapping
    @Operation(summary = "List all refunds", description = "Returns paginated list of refunds")
    public ResponseEntity<ApiResponse<PagedResponse<RefundResponse>>> getAll(Pageable pageable) {
        Page<RefundResponse> page = refundService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/invoice/{invoiceId}")
    @Operation(summary = "Get refunds by invoice", description = "Returns all refunds for a specific invoice")
    public ResponseEntity<ApiResponse<List<RefundResponse>>> getByInvoice(@Parameter(description = "Invoice UUID") @PathVariable UUID invoiceId) {
        return ResponseEntity.ok(ApiResponse.success(refundService.getByInvoice(invoiceId)));
    }
}
