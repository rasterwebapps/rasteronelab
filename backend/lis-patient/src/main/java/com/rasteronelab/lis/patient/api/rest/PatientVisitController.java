package com.rasteronelab.lis.patient.api.rest;

import com.rasteronelab.lis.core.api.ApiResponse;
import com.rasteronelab.lis.core.api.PagedResponse;
import com.rasteronelab.lis.patient.api.dto.PatientVisitRequest;
import com.rasteronelab.lis.patient.api.dto.PatientVisitResponse;
import com.rasteronelab.lis.patient.application.service.PatientVisitService;
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

/**
 * REST controller for PatientVisit CRUD operations.
 * Nested under patients: /api/v1/patients/{patientId}/visits
 * Restricted to SUPER_ADMIN, ORG_ADMIN, ADMIN, RECEPTIONIST, and LAB_TECHNICIAN roles.
 */
@RestController
@RequestMapping("/api/v1/patients/{patientId}/visits")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'RECEPTIONIST', 'LAB_TECHNICIAN')")
@Tag(name = "Patient Visit", description = "Patient visit tracking and management")
public class PatientVisitController {

    private final PatientVisitService patientVisitService;

    @PostMapping
    @Operation(summary = "Create a patient visit", description = "Records a new visit for the specified patient")
    public ResponseEntity<ApiResponse<PatientVisitResponse>> create(
            @PathVariable UUID patientId,
            @Valid @RequestBody PatientVisitRequest request) {
        PatientVisitResponse response = patientVisitService.create(patientId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Patient visit created successfully", response));
    }

    @GetMapping("/{visitId}")
    @Operation(summary = "Get visit by ID", description = "Retrieves a specific patient visit")
    public ResponseEntity<ApiResponse<PatientVisitResponse>> getById(
            @PathVariable UUID patientId,
            @PathVariable UUID visitId) {
        PatientVisitResponse response = patientVisitService.getById(patientId, visitId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "List patient visits", description = "Returns paginated visit history for a patient")
    public ResponseEntity<ApiResponse<PagedResponse<PatientVisitResponse>>> getByPatientId(
            @PathVariable UUID patientId, Pageable pageable) {
        Page<PatientVisitResponse> page = patientVisitService.getByPatientId(patientId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @PutMapping("/{visitId}")
    @Operation(summary = "Update a patient visit", description = "Updates an existing patient visit")
    public ResponseEntity<ApiResponse<PatientVisitResponse>> update(
            @PathVariable UUID patientId,
            @PathVariable UUID visitId,
            @Valid @RequestBody PatientVisitRequest request) {
        PatientVisitResponse response = patientVisitService.update(patientId, visitId, request);
        return ResponseEntity.ok(ApiResponse.success("Patient visit updated successfully", response));
    }

    @DeleteMapping("/{visitId}")
    @Operation(summary = "Delete a patient visit", description = "Soft-deletes a patient visit record")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID patientId,
            @PathVariable UUID visitId) {
        patientVisitService.delete(patientId, visitId);
        return ResponseEntity.ok(ApiResponse.successMessage("Patient visit deleted successfully"));
    }

    public PatientVisitController(PatientVisitService patientVisitService) {
        this.patientVisitService = patientVisitService;
    }

}
