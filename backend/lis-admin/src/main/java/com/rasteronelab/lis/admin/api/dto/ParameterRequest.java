package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating or updating a Parameter.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParameterRequest {

    @NotBlank(message = "Parameter name is required")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;

    @NotBlank(message = "Parameter code is required")
    @Size(max = 30, message = "Code must not exceed 30 characters")
    private String code;

    @Size(max = 200, message = "Print name must not exceed 200 characters")
    private String printName;

    @Size(max = 30, message = "Unit must not exceed 30 characters")
    private String unit;

    @NotBlank(message = "Data type is required")
    private String dataType;

    private Integer decimalPlaces;

    private Integer displayOrder;

    @Size(max = 100, message = "Method must not exceed 100 characters")
    private String method;

    @Size(max = 20, message = "LOINC code must not exceed 20 characters")
    private String loincCode;

    @Size(max = 500, message = "Formula must not exceed 500 characters")
    private String formula;

    private Boolean isCalculated;

    private Boolean isActive;
}
