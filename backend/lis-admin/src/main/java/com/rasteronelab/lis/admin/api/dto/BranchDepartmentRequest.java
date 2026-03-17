package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Request DTO for assigning a Department to a Branch.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchDepartmentRequest {

    @NotNull(message = "Branch ID is required")
    private UUID branchId;

    @NotNull(message = "Department ID is required")
    private UUID departmentId;

    private Boolean isActive;
}
