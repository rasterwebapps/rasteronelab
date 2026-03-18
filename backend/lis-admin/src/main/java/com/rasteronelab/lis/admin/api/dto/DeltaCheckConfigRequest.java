package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for creating or updating a Delta Check Config.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeltaCheckConfigRequest {

    @NotNull(message = "Parameter ID is required")
    private UUID parameterId;

    @NotNull(message = "Percentage threshold is required")
    private BigDecimal percentageThreshold;

    private BigDecimal absoluteThreshold;

    private Integer timeWindowHours;

    private Boolean isActive;
}
