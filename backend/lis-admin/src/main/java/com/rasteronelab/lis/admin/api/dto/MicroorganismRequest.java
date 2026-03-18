package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating or updating a Microorganism.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MicroorganismRequest {

    @NotBlank(message = "Microorganism name is required")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;

    @NotBlank(message = "Microorganism code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @Size(max = 20, message = "Gram type must not exceed 20 characters")
    private String gramType;

    @Size(max = 50, message = "Organism type must not exceed 50 characters")
    private String organismType;

    @Size(max = 500, message = "Clinical significance must not exceed 500 characters")
    private String clinicalSignificance;

    @Size(max = 500, message = "Colony morphology must not exceed 500 characters")
    private String colonyMorphology;

    private Boolean isActive;
}
