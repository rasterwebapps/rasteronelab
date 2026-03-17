package com.rasteronelab.lis.admin.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for BranchDepartment mapping entity.
 * Includes branch and department names for display.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchDepartmentResponse {

    private UUID id;
    private UUID branchId;
    private String branchName;
    private UUID departmentId;
    private String departmentName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
