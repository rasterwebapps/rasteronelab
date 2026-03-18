package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for creating or updating an Antibiotic.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AntibioticRequest {

    @NotBlank(message = "Antibiotic name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Antibiotic code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @Size(max = 50, message = "Antibiotic group must not exceed 50 characters")
    private String antibioticGroup;

    private BigDecimal clsiBreakpointS;

    private BigDecimal clsiBreakpointR;

    @Size(max = 50, message = "CLSI method must not exceed 50 characters")
    private String clsiMethod;

    private Boolean isActive;
}
