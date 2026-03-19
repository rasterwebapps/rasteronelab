package com.rasteronelab.lis.billing.api.rest;

import com.rasteronelab.lis.billing.api.dto.CreditAccountRequest;
import com.rasteronelab.lis.billing.api.dto.CreditAccountResponse;
import com.rasteronelab.lis.billing.application.service.CreditAccountService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/credit-accounts")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'BILLING_STAFF')")
@Tag(name = "Credit Account", description = "Corporate and insurance credit account management")
public class CreditAccountController {

    private final CreditAccountService creditAccountService;

    public CreditAccountController(CreditAccountService creditAccountService) {
        this.creditAccountService = creditAccountService;
    }

    @PostMapping
    @Operation(summary = "Create a credit account", description = "Creates a new corporate or insurance credit account")
    public ResponseEntity<ApiResponse<CreditAccountResponse>> create(@Valid @RequestBody CreditAccountRequest request) {
        CreditAccountResponse response = creditAccountService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Credit account created successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get credit account by ID", description = "Retrieves credit account details")
    public ResponseEntity<ApiResponse<CreditAccountResponse>> getById(@Parameter(description = "Credit account UUID") @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(creditAccountService.getById(id)));
    }

    @GetMapping
    @Operation(summary = "List all credit accounts", description = "Returns paginated list of credit accounts")
    public ResponseEntity<ApiResponse<PagedResponse<CreditAccountResponse>>> getAll(Pageable pageable) {
        Page<CreditAccountResponse> page = creditAccountService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a credit account", description = "Updates an existing credit account")
    public ResponseEntity<ApiResponse<CreditAccountResponse>> update(
            @PathVariable UUID id, @Valid @RequestBody CreditAccountRequest request) {
        CreditAccountResponse response = creditAccountService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Credit account updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a credit account", description = "Soft-deletes a credit account")
    public ResponseEntity<ApiResponse<Void>> delete(@Parameter(description = "Credit account UUID") @PathVariable UUID id) {
        creditAccountService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Credit account deleted successfully"));
    }
}
