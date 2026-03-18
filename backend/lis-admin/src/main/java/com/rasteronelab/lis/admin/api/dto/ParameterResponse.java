package com.rasteronelab.lis.admin.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Parameter entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParameterResponse {

    private UUID id;
    private UUID branchId;
    private String name;
    private String code;
    private String printName;
    private String unit;
    private String dataType;
    private Integer decimalPlaces;
    private Integer displayOrder;
    private String method;
    private String loincCode;
    private String formula;
    private Boolean isCalculated;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
