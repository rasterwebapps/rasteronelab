package com.rasteronelab.lis.order.api.rest;

import com.rasteronelab.lis.core.api.ApiResponse;
import com.rasteronelab.lis.core.api.PagedResponse;
import com.rasteronelab.lis.order.api.dto.TestOrderRequest;
import com.rasteronelab.lis.order.api.dto.TestOrderResponse;
import com.rasteronelab.lis.order.application.service.TestOrderService;
import com.rasteronelab.lis.order.domain.model.OrderStatus;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 * REST controller for TestOrder CRUD and state management operations.
 * Restricted to SUPER_ADMIN, ORG_ADMIN, ADMIN, RECEPTIONIST, LAB_TECHNICIAN roles.
 */
@RestController
@RequestMapping("/api/v1/orders")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'RECEPTIONIST', 'LAB_TECHNICIAN')")
@Tag(name = "Test Order", description = "Test order creation, state management, and lifecycle operations")
public class TestOrderController {

    private final TestOrderService testOrderService;

    @PostMapping
    @Operation(summary = "Create a new test order", description = "Creates a draft test order with line items")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Order created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<ApiResponse<TestOrderResponse>> create(
            @Valid @RequestBody TestOrderRequest request) {
        TestOrderResponse response = testOrderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieves a test order with its line items")
    public ResponseEntity<ApiResponse<TestOrderResponse>> getById(
            @Parameter(description = "Order UUID") @PathVariable UUID id) {
        TestOrderResponse response = testOrderService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "List all orders", description = "Returns paginated list of orders in the current branch")
    public ResponseEntity<ApiResponse<PagedResponse<TestOrderResponse>>> getAll(Pageable pageable) {
        Page<TestOrderResponse> page = testOrderService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get orders by patient", description = "Returns orders for a specific patient")
    public ResponseEntity<ApiResponse<PagedResponse<TestOrderResponse>>> getByPatient(
            @Parameter(description = "Patient UUID") @PathVariable UUID patientId, Pageable pageable) {
        Page<TestOrderResponse> page = testOrderService.getByPatient(patientId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status", description = "Returns orders filtered by status")
    public ResponseEntity<ApiResponse<PagedResponse<TestOrderResponse>>> getByStatus(
            @Parameter(description = "Order status") @PathVariable OrderStatus status, Pageable pageable) {
        Page<TestOrderResponse> page = testOrderService.getByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @PutMapping("/{id}/place")
    @Operation(summary = "Place an order", description = "Transitions order from DRAFT to PLACED status")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order placed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Invalid state transition")
    })
    public ResponseEntity<ApiResponse<TestOrderResponse>> placeOrder(
            @Parameter(description = "Order UUID") @PathVariable UUID id) {
        TestOrderResponse response = testOrderService.placeOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order placed successfully", response));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel an order", description = "Cancels a DRAFT or PLACED order with a reason")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Order cannot be cancelled in current state")
    })
    public ResponseEntity<ApiResponse<TestOrderResponse>> cancelOrder(
            @Parameter(description = "Order UUID") @PathVariable UUID id,
            @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        TestOrderResponse response = testOrderService.cancelOrder(id, reason);
        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order", description = "Soft-deletes a test order")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "Order UUID") @PathVariable UUID id) {
        testOrderService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Order deleted successfully"));
    }

    public TestOrderController(TestOrderService testOrderService) {
        this.testOrderService = testOrderService;
    }

}
