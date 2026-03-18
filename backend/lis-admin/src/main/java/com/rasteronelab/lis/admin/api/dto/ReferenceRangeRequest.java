package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for creating or updating a Reference Range.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceRangeRequest {

    @NotNull(message = "Parameter ID is required")
    private UUID parameterId;

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
}
