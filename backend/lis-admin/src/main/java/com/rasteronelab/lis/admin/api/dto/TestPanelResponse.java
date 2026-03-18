package com.rasteronelab.lis.admin.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for TestPanel entity.
 * Includes departmentName and constituent tests for display.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestPanelResponse {

    private UUID id;
    private UUID branchId;
    private UUID departmentId;
    private String departmentName;
    private String name;
    private String code;
    private String shortName;
    private String description;
    private BigDecimal panelPrice;
    private Integer displayOrder;
    private Boolean isActive;
    private List<PanelTestItem> tests;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PanelTestItem {
        private UUID testId;
        private String testName;
        private String testCode;
        private Integer displayOrder;
    }
}
