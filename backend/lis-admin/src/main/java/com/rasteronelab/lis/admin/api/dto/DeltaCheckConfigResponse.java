package com.rasteronelab.lis.admin.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for DeltaCheckConfig entity.
 * Includes parameterName for display purposes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeltaCheckConfigResponse {

    private UUID id;
    private UUID branchId;
    private UUID parameterId;
    private String parameterName;
    private BigDecimal percentageThreshold;
    private BigDecimal absoluteThreshold;
    private Integer timeWindowHours;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
