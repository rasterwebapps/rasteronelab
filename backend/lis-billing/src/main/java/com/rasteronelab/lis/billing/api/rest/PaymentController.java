package com.rasteronelab.lis.billing.api.rest;

import com.rasteronelab.lis.billing.api.dto.PaymentRequest;
import com.rasteronelab.lis.billing.api.dto.PaymentResponse;
import com.rasteronelab.lis.billing.application.service.PaymentService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'BILLING_STAFF')")
@Tag(name = "Payment", description = "Payment recording and tracking")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @Operation(summary = "Record a payment", description = "Records a payment against an invoice, supports split/partial payments")
    public ResponseEntity<ApiResponse<PaymentResponse>> record(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.recordPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Payment recorded successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID", description = "Retrieves payment details by UUID")
    public ResponseEntity<ApiResponse<PaymentResponse>> getById(
            @Parameter(description = "Payment UUID") @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.getById(id)));
    }

    @GetMapping
    @Operation(summary = "List all payments", description = "Returns paginated list of payments in the current branch")
    public ResponseEntity<ApiResponse<PagedResponse<PaymentResponse>>> getAll(Pageable pageable) {
        Page<PaymentResponse> page = paymentService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/invoice/{invoiceId}")
    @Operation(summary = "Get payments by invoice", description = "Returns all payments for a specific invoice")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getByInvoice(
            @Parameter(description = "Invoice UUID") @PathVariable UUID invoiceId) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.getByInvoice(invoiceId)));
    }
}
