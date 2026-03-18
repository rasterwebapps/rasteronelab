package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.TestMasterRequest;
import com.rasteronelab.lis.admin.api.dto.TestMasterResponse;
import com.rasteronelab.lis.admin.application.service.TestMasterService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller for Test Master CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/tests")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
@RequiredArgsConstructor
public class TestMasterController {

    private final TestMasterService testMasterService;

    @PostMapping
    public ResponseEntity<ApiResponse<TestMasterResponse>> create(
            @Valid @RequestBody TestMasterRequest request) {
        TestMasterResponse response = testMasterService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Test created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<TestMasterResponse>>> getAll(Pageable pageable) {
        Page<TestMasterResponse> page = testMasterService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<TestMasterResponse>>> search(
            @RequestParam String query, Pageable pageable) {
        Page<TestMasterResponse> page = testMasterService.search(query, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<PagedResponse<TestMasterResponse>>> getByDepartment(
            @PathVariable UUID departmentId, Pageable pageable) {
        Page<TestMasterResponse> page = testMasterService.getByDepartment(departmentId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TestMasterResponse>> getById(@PathVariable UUID id) {
        TestMasterResponse response = testMasterService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TestMasterResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody TestMasterRequest request) {
        TestMasterResponse response = testMasterService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Test updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        testMasterService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Test deleted successfully"));
    }
}
