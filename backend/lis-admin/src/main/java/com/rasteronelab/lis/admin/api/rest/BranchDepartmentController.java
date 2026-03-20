package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.BranchDepartmentRequest;
import com.rasteronelab.lis.admin.api.dto.BranchDepartmentResponse;
import com.rasteronelab.lis.admin.application.service.BranchDepartmentService;
import com.rasteronelab.lis.core.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for BranchDepartment mapping operations.
 * Manages which departments are active per branch.
 */
@RestController
@RequestMapping("/api/v1/branch-departments")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN')")
public class BranchDepartmentController {

    private final BranchDepartmentService branchDepartmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<BranchDepartmentResponse>> assign(
            @Valid @RequestBody BranchDepartmentRequest request) {
        BranchDepartmentResponse response = branchDepartmentService.assign(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Department assigned to branch successfully", response));
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<ApiResponse<List<BranchDepartmentResponse>>> getByBranch(
            @PathVariable UUID branchId) {
        List<BranchDepartmentResponse> responses = branchDepartmentService.getByBranch(branchId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchDepartmentResponse>> getById(@PathVariable UUID id) {
        BranchDepartmentResponse response = branchDepartmentService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> unassign(@PathVariable UUID id) {
        branchDepartmentService.unassign(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Department unassigned from branch successfully"));
    }

    public BranchDepartmentController(BranchDepartmentService branchDepartmentService) {
        this.branchDepartmentService = branchDepartmentService;
    }

}
