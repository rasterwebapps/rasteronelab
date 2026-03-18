package com.rasteronelab.lis.admin.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for TestMaster entity.
 * Includes all entity fields plus departmentName for display.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestMasterResponse {

    private UUID id;
    private UUID branchId;
    private UUID departmentId;
    private String departmentName;
    private String name;
    private String code;
    private String shortName;
    private String description;
    private String sampleType;
    private String tubeType;
    private String reportSection;
    private String method;
    private Integer tatRoutineHours;
    private Integer tatStatHours;
    private BigDecimal basePrice;
    private Boolean isOutsourced;
    private String outsourceLabName;
    private Integer displayOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
