package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.AppUserRequest;
import com.rasteronelab.lis.admin.api.dto.AppUserResponse;
import com.rasteronelab.lis.admin.application.service.AppUserService;
import com.rasteronelab.lis.core.api.ApiResponse;
import com.rasteronelab.lis.core.api.PagedResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 * REST controller for User Management CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/users")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AppUserController {

    private final AppUserService appUserService;

    @PostMapping
    public ResponseEntity<ApiResponse<AppUserResponse>> create(
            @Valid @RequestBody AppUserRequest request) {
        AppUserResponse response = appUserService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<AppUserResponse>>> getAll(Pageable pageable) {
        Page<AppUserResponse> page = appUserService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<AppUserResponse>>> search(
            @RequestParam String query, Pageable pageable) {
        Page<AppUserResponse> page = appUserService.search(query, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/active/{isActive}")
    public ResponseEntity<ApiResponse<PagedResponse<AppUserResponse>>> getByActiveStatus(
            @PathVariable Boolean isActive, Pageable pageable) {
        Page<AppUserResponse> page = appUserService.getByActiveStatus(isActive, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppUserResponse>> getById(@PathVariable UUID id) {
        AppUserResponse response = appUserService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AppUserResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody AppUserRequest request) {
        AppUserResponse response = appUserService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        appUserService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("User deleted successfully"));
    }

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

}
