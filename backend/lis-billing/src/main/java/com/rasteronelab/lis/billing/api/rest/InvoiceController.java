package com.rasteronelab.lis.billing.api.rest;

import com.rasteronelab.lis.billing.api.dto.DiscountApplicationRequest;
import com.rasteronelab.lis.billing.api.dto.InvoiceRequest;
import com.rasteronelab.lis.billing.api.dto.InvoiceResponse;
import com.rasteronelab.lis.billing.api.dto.OutstandingInvoiceResponse;
import com.rasteronelab.lis.billing.application.service.InvoiceService;
import com.rasteronelab.lis.billing.domain.model.InvoiceStatus;
import com.rasteronelab.lis.core.api.ApiResponse;
import com.rasteronelab.lis.core.api.PagedResponse;
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

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'BILLING_STAFF')")
@Tag(name = "Invoice", description = "Invoice generation, discount application, and management")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    @Operation(summary = "Generate an invoice", description = "Creates an invoice for a test order with pricing calculation")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Invoice generated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<ApiResponse<InvoiceResponse>> generate(@Valid @RequestBody InvoiceRequest request) {
        InvoiceResponse response = invoiceService.generateInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Invoice generated successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get invoice by ID", description = "Retrieves invoice details with line items")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getById(@Parameter(description = "Invoice UUID") @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getById(id)));
    }

    @GetMapping
    @Operation(summary = "List all invoices", description = "Returns paginated list of invoices in the current branch")
    public ResponseEntity<ApiResponse<PagedResponse<InvoiceResponse>>> getAll(Pageable pageable) {
        Page<InvoiceResponse> page = invoiceService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get invoices by patient", description = "Returns invoices for a specific patient")
    public ResponseEntity<ApiResponse<PagedResponse<InvoiceResponse>>> getByPatient(
            @PathVariable UUID patientId, Pageable pageable) {
        Page<InvoiceResponse> page = invoiceService.getByPatient(patientId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get invoices by status", description = "Returns invoices filtered by status")
    public ResponseEntity<ApiResponse<PagedResponse<InvoiceResponse>>> getByStatus(
            @PathVariable InvoiceStatus status, Pageable pageable) {
        Page<InvoiceResponse> page = invoiceService.getByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get invoice by order", description = "Retrieves the invoice associated with a test order")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getByOrder(@Parameter(description = "Order UUID") @PathVariable UUID orderId) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getByOrder(orderId)));
    }

    @PutMapping("/{id}/discount")
    @Operation(summary = "Apply discount to invoice", description = "Applies a discount to an existing invoice")
    public ResponseEntity<ApiResponse<InvoiceResponse>> applyDiscount(
            @PathVariable UUID id,
            @RequestParam String discountType,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String reason) {
        InvoiceResponse response = invoiceService.applyDiscount(id, discountType, amount, reason);
        return ResponseEntity.ok(ApiResponse.success("Discount applied successfully", response));
    }

    @PostMapping("/{id}/apply-discount")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'BILLING_STAFF')")
    @Operation(summary = "Apply discount scheme to invoice",
               description = "Applies a discount scheme with PERCENTAGE or FLAT calculation to an invoice")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Discount scheme applied successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Invoice not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Business rule violation")
    })
    public ResponseEntity<ApiResponse<InvoiceResponse>> applyDiscountScheme(
            @Parameter(description = "Invoice UUID") @PathVariable UUID id,
            @Valid @RequestBody DiscountApplicationRequest request) {
        request.setInvoiceId(id);
        InvoiceResponse response = invoiceService.applyDiscountScheme(request);
        return ResponseEntity.ok(ApiResponse.success("Discount scheme applied successfully", response));
    }

    @GetMapping("/outstanding/patient/{patientId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'BILLING_STAFF')")
    @Operation(summary = "Get outstanding invoices by patient",
               description = "Returns total outstanding balance and invoice summaries for a patient")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Outstanding invoices retrieved successfully")
    })
    public ResponseEntity<ApiResponse<OutstandingInvoiceResponse>> getOutstandingByPatient(
            @Parameter(description = "Patient UUID") @PathVariable UUID patientId) {
        OutstandingInvoiceResponse response = invoiceService.getOutstandingByPatient(patientId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an invoice", description = "Soft-deletes an invoice")
    public ResponseEntity<ApiResponse<Void>> delete(@Parameter(description = "Invoice UUID") @PathVariable UUID id) {
        invoiceService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Invoice deleted successfully"));
    }
}
