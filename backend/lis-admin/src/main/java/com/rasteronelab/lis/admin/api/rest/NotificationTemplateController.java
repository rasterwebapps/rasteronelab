package com.rasteronelab.lis.admin.api.rest;

import com.rasteronelab.lis.admin.api.dto.NotificationTemplateRequest;
import com.rasteronelab.lis.admin.api.dto.NotificationTemplateResponse;
import com.rasteronelab.lis.admin.application.service.NotificationTemplateService;
import com.rasteronelab.lis.core.api.ApiResponse;
import com.rasteronelab.lis.core.api.PagedResponse;
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

import java.util.UUID;

/**
 * REST controller for NotificationTemplate CRUD operations.
 * Restricted to ADMIN and SUPER_ADMIN roles.
 */
@RestController
@RequestMapping("/api/v1/notification-templates")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class NotificationTemplateController {

    private final NotificationTemplateService notificationTemplateService;

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> create(
            @Valid @RequestBody NotificationTemplateRequest request) {
        NotificationTemplateResponse response = notificationTemplateService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Notification template created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<NotificationTemplateResponse>>> getAll(Pageable pageable) {
        Page<NotificationTemplateResponse> page = notificationTemplateService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<NotificationTemplateResponse>>> search(
            @RequestParam String query, Pageable pageable) {
        Page<NotificationTemplateResponse> page = notificationTemplateService.search(query, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> getById(@PathVariable UUID id) {
        NotificationTemplateResponse response = notificationTemplateService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody NotificationTemplateRequest request) {
        NotificationTemplateResponse response = notificationTemplateService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Notification template updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        notificationTemplateService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Notification template deleted successfully"));
    }

    public NotificationTemplateController(NotificationTemplateService notificationTemplateService) {
        this.notificationTemplateService = notificationTemplateService;
    }

}
