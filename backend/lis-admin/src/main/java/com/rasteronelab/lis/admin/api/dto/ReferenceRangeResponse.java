package com.rasteronelab.lis.admin.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for ReferenceRange entity.
 * Includes parameterName for display purposes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceRangeResponse {

    private UUID id;
    private UUID branchId;
    private UUID parameterId;
    private String parameterName;
    private String gender;
    private BigDecimal ageMin;
    private BigDecimal ageMax;
    private String ageUnit;
    private BigDecimal normalMin;
    private BigDecimal normalMax;
    private BigDecimal criticalLow;
    private BigDecimal criticalHigh;
    private String normalText;
    private String unit;
    private Boolean isPregnancy;
    private String displayText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
