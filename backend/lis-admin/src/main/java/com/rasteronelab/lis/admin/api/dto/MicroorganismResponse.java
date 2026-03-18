package com.rasteronelab.lis.admin.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Microorganism entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MicroorganismResponse {

    private UUID id;
    private UUID branchId;
    private String name;
    private String code;
    private String gramType;
    private String organismType;
    private String clinicalSignificance;
    private String colonyMorphology;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
