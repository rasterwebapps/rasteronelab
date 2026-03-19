package com.rasteronelab.lis.patient.api.rest;

import com.rasteronelab.lis.core.api.ApiResponse;
import com.rasteronelab.lis.core.api.PagedResponse;
import com.rasteronelab.lis.patient.api.dto.PatientRequest;
import com.rasteronelab.lis.patient.api.dto.PatientResponse;
import com.rasteronelab.lis.patient.application.service.PatientMergeService;
import com.rasteronelab.lis.patient.application.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * REST controller for Patient CRUD operations.
 * Restricted to SUPER_ADMIN, ORG_ADMIN, ADMIN, RECEPTIONIST, and LAB_TECHNICIAN roles.
 */
@RestController
@RequestMapping("/api/v1/patients")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN', 'RECEPTIONIST', 'LAB_TECHNICIAN')")
@Tag(name = "Patient", description = "Patient registration and demographics management")
public class PatientController {

    private final PatientService patientService;
    private final PatientMergeService patientMergeService;

    @PostMapping
    @Operation(summary = "Register a new patient", description = "Creates a new patient with auto-generated UHID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Patient created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Duplicate patient detected")
    })
    public ResponseEntity<ApiResponse<PatientResponse>> create(
            @Valid @RequestBody PatientRequest request) {
        PatientResponse response = patientService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Patient created successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID", description = "Retrieves patient details by UUID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Patient found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<ApiResponse<PatientResponse>> getById(
            @Parameter(description = "Patient UUID") @PathVariable UUID id) {
        PatientResponse response = patientService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "List all patients", description = "Returns paginated list of patients in the current branch")
    public ResponseEntity<ApiResponse<PagedResponse<PatientResponse>>> getAll(Pageable pageable) {
        Page<PatientResponse> page = patientService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @GetMapping("/search")
    @Operation(summary = "Search patients", description = "Searches patients by UHID, name, or phone number")
    public ResponseEntity<ApiResponse<PagedResponse<PatientResponse>>> search(
            @Parameter(description = "Search term (UHID, name, or phone)") @RequestParam("q") String searchTerm,
            Pageable pageable) {
        Page<PatientResponse> page = patientService.search(searchTerm, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(page)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update patient", description = "Updates an existing patient's details")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Patient updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<ApiResponse<PatientResponse>> update(
            @Parameter(description = "Patient UUID") @PathVariable UUID id,
            @Valid @RequestBody PatientRequest request) {
        PatientResponse response = patientService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Patient updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient", description = "Soft-deletes a patient record")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Patient deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "Patient UUID") @PathVariable UUID id) {
        patientService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Patient deleted successfully"));
    }

    @GetMapping("/duplicates")
    @Operation(summary = "Find duplicate patients", description = "Detects potential duplicate patients by name, phone, and date of birth")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> findDuplicates(
            @Parameter(description = "First name") @RequestParam String firstName,
            @Parameter(description = "Last name") @RequestParam String lastName,
            @Parameter(description = "Phone number") @RequestParam String phone,
            @Parameter(description = "Date of birth (ISO format)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob) {
        List<PatientResponse> duplicates = patientService.findDuplicates(firstName, lastName, phone, dob);
        return ResponseEntity.ok(ApiResponse.success(duplicates));
    }

    @PostMapping("/merge")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ORG_ADMIN', 'ADMIN')")
    @Operation(summary = "Merge duplicate patients", description = "Merges a duplicate patient into the primary record")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Patients merged successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Cannot merge patients")
    })
    public ResponseEntity<ApiResponse<String>> mergePatients(@RequestBody Map<String, UUID> body) {
        UUID primaryId = body.get("primaryPatientId");
        UUID mergedId = body.get("mergedPatientId");
        patientMergeService.mergePatients(primaryId, mergedId);
        return ResponseEntity.ok(ApiResponse.successMessage("Patients merged successfully"));
    }

    public PatientController(PatientService patientService, PatientMergeService patientMergeService) {
        this.patientService = patientService;
        this.patientMergeService = patientMergeService;
    }

}
