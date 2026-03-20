package com.rasteronelab.lis.result.api.rest;

import com.rasteronelab.lis.core.api.ApiResponse;
import com.rasteronelab.lis.core.api.PagedResponse;
import com.rasteronelab.lis.result.api.dto.ResultAmendRequest;
import com.rasteronelab.lis.result.api.dto.ResultAuthorizationRequest;
import com.rasteronelab.lis.result.api.dto.ResultEntryRequest;
import com.rasteronelab.lis.result.api.dto.TestResultCreateRequest;
import com.rasteronelab.lis.result.api.dto.TestResultResponse;
import com.rasteronelab.lis.result.application.service.ResultAuthorizationService;
import com.rasteronelab.lis.result.application.service.ResultEntryService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for test result entry, validation, and authorization.
 */
@RestController
@RequestMapping("/api/v1/results")
@Tag(name = "Result", description = "Test result entry, validation, and authorization")
public class ResultController {

    private final ResultEntryService resultEntryService;
    private final ResultAuthorizationService resultAuthorizationService;

    public ResultController(ResultEntryService resultEntryService,
                            ResultAuthorizationService resultAuthorizationService) {
        this.resultEntryService = resultEntryService;
        this.resultAuthorizationService = resultAuthorizationService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'LAB_TECHNICIAN', 'PATHOLOGIST')")
    @Operation(summary = "Create a test result", description = "Creates a new test result record in PENDING status")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Test result created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<ApiResponse<TestResultResponse>> create(
            @Valid @RequestBody TestResultCreateRequest request) {
        TestResultResponse response = resultEntryService.createTestResult(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Test result created successfully", response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'LAB_TECHNICIAN', 'PATHOLOGIST')")
    @Operation(summary = "Get test result by ID", description = "Retrieves a test result with its values by UUID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Test result found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Test result not found")
    })
    public ResponseEntity<ApiResponse<TestResultResponse>> getById(
            @Parameter(description = "Test result UUID") @PathVariable UUID id) {
        TestResultResponse response = resultEntryService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'LAB_TECHNICIAN', 'PATHOLOGIST')")
    @Operation(summary = "List all test results", description = "Returns paginated list of test results in the current branch")
    public ResponseEntity<ApiResponse<PagedResponse<TestResultResponse>>> getAll(Pageable pageable) {
        Page<TestResultResponse> page = resultEntryService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'LAB_TECHNICIAN', 'PATHOLOGIST')")
    @Operation(summary = "Get results by order", description = "Retrieves all test results for a specific order")
    public ResponseEntity<ApiResponse<PagedResponse<TestResultResponse>>> getByOrder(
            @Parameter(description = "Order UUID") @PathVariable UUID orderId,
            Pageable pageable) {
        Page<TestResultResponse> page = resultEntryService.getByOrder(orderId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'LAB_TECHNICIAN', 'PATHOLOGIST')")
    @Operation(summary = "Get results by patient", description = "Retrieves all test results for a specific patient")
    public ResponseEntity<ApiResponse<PagedResponse<TestResultResponse>>> getByPatient(
            @Parameter(description = "Patient UUID") @PathVariable UUID patientId,
            Pageable pageable) {
        Page<TestResultResponse> page = resultEntryService.getByPatient(patientId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/worklist/{departmentId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'LAB_TECHNICIAN', 'PATHOLOGIST')")
    @Operation(summary = "Get worklist by department", description = "Retrieves pending test results for a department")
    public ResponseEntity<ApiResponse<PagedResponse<TestResultResponse>>> getWorklist(
            @Parameter(description = "Department UUID") @PathVariable UUID departmentId,
            Pageable pageable) {
        Page<TestResultResponse> page = resultEntryService.getWorklist(departmentId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @PutMapping("/enter")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'LAB_TECHNICIAN', 'PATHOLOGIST')")
    @Operation(summary = "Enter result values", description = "Enters result values for a test result and transitions to ENTERED status")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Results entered successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Test result not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Invalid status transition")
    })
    public ResponseEntity<ApiResponse<TestResultResponse>> enterResults(
            @Valid @RequestBody ResultEntryRequest request) {
        TestResultResponse response = resultEntryService.enterResults(request);
        return ResponseEntity.ok(ApiResponse.success("Results entered successfully", response));
    }

    @PutMapping("/{id}/validate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'LAB_TECHNICIAN', 'PATHOLOGIST')")
    @Operation(summary = "Validate test result", description = "Validates a test result (ENTERED → VALIDATED)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Result validated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Test result not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Invalid status transition")
    })
    public ResponseEntity<ApiResponse<TestResultResponse>> validateResult(
            @Parameter(description = "Test result UUID") @PathVariable UUID id) {
        TestResultResponse response = resultEntryService.validateResult(id);
        return ResponseEntity.ok(ApiResponse.success("Result validated successfully", response));
    }

    @PutMapping("/{id}/authorize")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PATHOLOGIST')")
    @Operation(summary = "Authorize test result", description = "Authorizes a validated test result (VALIDATED → AUTHORIZED)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Result authorized successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Test result not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Invalid status transition")
    })
    public ResponseEntity<ApiResponse<TestResultResponse>> authorizeResult(
            @Parameter(description = "Test result UUID") @PathVariable UUID id) {
        TestResultResponse response = resultAuthorizationService.authorizeResult(id);
        return ResponseEntity.ok(ApiResponse.success("Result authorized successfully", response));
    }

    @PutMapping("/batch-authorize")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PATHOLOGIST')")
    @Operation(summary = "Batch authorize results", description = "Authorizes multiple validated test results at once")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Results authorized successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "One or more results could not be authorized")
    })
    public ResponseEntity<ApiResponse<List<TestResultResponse>>> batchAuthorize(
            @Valid @RequestBody ResultAuthorizationRequest request) {
        List<TestResultResponse> responses = resultAuthorizationService.batchAuthorize(request.getResultIds());
        return ResponseEntity.ok(ApiResponse.success("Results authorized successfully", responses));
    }

    @PutMapping("/{id}/amend")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PATHOLOGIST')")
    @Operation(summary = "Amend test result", description = "Amends an authorized or released test result")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Result amended successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Test result not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Invalid status transition")
    })
    public ResponseEntity<ApiResponse<TestResultResponse>> amendResult(
            @Parameter(description = "Test result UUID") @PathVariable UUID id,
            @Valid @RequestBody ResultAmendRequest request) {
        request.setTestResultId(id);
        TestResultResponse response = resultAuthorizationService.amendResult(request);
        return ResponseEntity.ok(ApiResponse.success("Result amended successfully", response));
    }
}
