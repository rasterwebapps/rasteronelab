package com.rasteronelab.lis.admin.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Antibiotic entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AntibioticResponse {

    private UUID id;
    private UUID branchId;
    private String name;
    private String code;
    private String antibioticGroup;
    private BigDecimal clsiBreakpointS;
    private BigDecimal clsiBreakpointR;
    private String clsiMethod;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
