package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for creating or updating a Critical Value Config.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriticalValueConfigRequest {

    @NotNull(message = "Parameter ID is required")
    private UUID parameterId;

    private BigDecimal lowThreshold;

    private BigDecimal highThreshold;

    private Boolean notificationRequired;

    private Boolean autoFlag;

    private Boolean isActive;
}
