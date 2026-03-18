package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.AutoValidationRuleRequest;
import com.rasteronelab.lis.admin.api.dto.AutoValidationRuleResponse;
import com.rasteronelab.lis.admin.application.service.AutoValidationRuleService;
import com.rasteronelab.lis.core.api.ApiResponse;
import com.rasteronelab.lis.core.api.PagedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.UUID;

/**
 * REST controller for Auto Validation Rule CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/config/auto-validation")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
@RequiredArgsConstructor
public class AutoValidationRuleController {

    private final AutoValidationRuleService autoValidationRuleService;

    @PostMapping
    public ResponseEntity<ApiResponse<AutoValidationRuleResponse>> create(
            @Valid @RequestBody AutoValidationRuleRequest request) {
        AutoValidationRuleResponse response = autoValidationRuleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Auto validation rule created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<AutoValidationRuleResponse>>> getAll(Pageable pageable) {
        Page<AutoValidationRuleResponse> page = autoValidationRuleService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<ApiResponse<List<AutoValidationRuleResponse>>> getByTest(
            @PathVariable UUID testId) {
        List<AutoValidationRuleResponse> rules = autoValidationRuleService.getByTestId(testId);
        return ResponseEntity.ok(ApiResponse.success(rules));
    }

    @GetMapping("/parameter/{parameterId}")
    public ResponseEntity<ApiResponse<List<AutoValidationRuleResponse>>> getByParameter(
            @PathVariable UUID parameterId) {
        List<AutoValidationRuleResponse> rules = autoValidationRuleService.getByParameterId(parameterId);
        return ResponseEntity.ok(ApiResponse.success(rules));
    }

    @GetMapping("/rule-type/{ruleType}")
    public ResponseEntity<ApiResponse<List<AutoValidationRuleResponse>>> getByRuleType(
            @PathVariable String ruleType) {
        List<AutoValidationRuleResponse> rules = autoValidationRuleService.getByRuleType(ruleType);
        return ResponseEntity.ok(ApiResponse.success(rules));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AutoValidationRuleResponse>> getById(@PathVariable UUID id) {
        AutoValidationRuleResponse response = autoValidationRuleService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AutoValidationRuleResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody AutoValidationRuleRequest request) {
        AutoValidationRuleResponse response = autoValidationRuleService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Auto validation rule updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        autoValidationRuleService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Auto validation rule deleted successfully"));
    }
}
