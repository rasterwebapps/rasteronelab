package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.TestPanelRequest;
import com.rasteronelab.lis.admin.api.dto.TestPanelResponse;
import com.rasteronelab.lis.admin.application.service.TestPanelService;
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
 * REST controller for Test Panel CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/panels")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
@RequiredArgsConstructor
public class TestPanelController {

    private final TestPanelService testPanelService;

    @PostMapping
    public ResponseEntity<ApiResponse<TestPanelResponse>> create(
            @Valid @RequestBody TestPanelRequest request) {
        TestPanelResponse response = testPanelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Panel created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<TestPanelResponse>>> getAll(Pageable pageable) {
        Page<TestPanelResponse> page = testPanelService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<PagedResponse<TestPanelResponse>>> getActive(Pageable pageable) {
        Page<TestPanelResponse> page = testPanelService.getActive(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TestPanelResponse>> getById(@PathVariable UUID id) {
        TestPanelResponse response = testPanelService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}/tests")
    public ResponseEntity<ApiResponse<List<TestPanelResponse.PanelTestItem>>> getExpandedTests(
            @PathVariable UUID id) {
        List<TestPanelResponse.PanelTestItem> tests = testPanelService.getExpandedTests(id);
        return ResponseEntity.ok(ApiResponse.success(tests));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TestPanelResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody TestPanelRequest request) {
        TestPanelResponse response = testPanelService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Panel updated successfully", response));
    }

    @PostMapping("/{panelId}/tests/{testId}")
    public ResponseEntity<ApiResponse<TestPanelResponse>> addTest(
            @PathVariable UUID panelId, @PathVariable UUID testId) {
        TestPanelResponse response = testPanelService.addTest(panelId, testId);
        return ResponseEntity.ok(ApiResponse.success("Test added to panel successfully", response));
    }

    @DeleteMapping("/{panelId}/tests/{testId}")
    public ResponseEntity<ApiResponse<TestPanelResponse>> removeTest(
            @PathVariable UUID panelId, @PathVariable UUID testId) {
        TestPanelResponse response = testPanelService.removeTest(panelId, testId);
        return ResponseEntity.ok(ApiResponse.success("Test removed from panel successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        testPanelService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Panel deleted successfully"));
    }
}
