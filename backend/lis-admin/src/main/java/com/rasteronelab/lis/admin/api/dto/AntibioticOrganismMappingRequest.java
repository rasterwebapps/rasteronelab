package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Request DTO for creating or updating an Antibiotic-Organism Mapping.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AntibioticOrganismMappingRequest {

    @NotNull(message = "Antibiotic ID is required")
    private UUID antibioticId;

    @NotNull(message = "Microorganism ID is required")
    private UUID microorganismId;

    @Size(max = 20, message = "Susceptibility must not exceed 20 characters")
    private String susceptibility;

    private Boolean isDefaultPanel;
}
