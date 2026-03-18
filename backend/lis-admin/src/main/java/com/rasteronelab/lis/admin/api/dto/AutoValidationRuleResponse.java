package com.rasteronelab.lis.admin.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for AutoValidationRule entity.
 * Includes parameterName and testName for display purposes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoValidationRuleResponse {

    private UUID id;
    private UUID branchId;
    private UUID parameterId;
    private String parameterName;
    private UUID testId;
    private String testName;
    private String ruleName;
    private String ruleType;
    private String conditionExpression;
    private String actionOnPass;
    private String actionOnFail;
    private Integer priority;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
