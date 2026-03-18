package com.rasteronelab.lis.admin.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for AntibioticOrganismMapping entity.
 * Includes antibioticName and microorganismName for display.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AntibioticOrganismMappingResponse {

    private UUID id;
    private UUID branchId;
    private UUID antibioticId;
    private String antibioticName;
    private UUID microorganismId;
    private String microorganismName;
    private String susceptibility;
    private Boolean isDefaultPanel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
