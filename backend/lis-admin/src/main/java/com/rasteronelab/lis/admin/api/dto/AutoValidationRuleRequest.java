package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Request DTO for creating or updating an Auto Validation Rule.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoValidationRuleRequest {

    private UUID parameterId;

    private UUID testId;

    @NotBlank(message = "Rule name is required")
    @Size(max = 100, message = "Rule name must not exceed 100 characters")
    private String ruleName;

    @NotBlank(message = "Rule type is required")
    @Size(max = 30, message = "Rule type must not exceed 30 characters")
    private String ruleType;

    @Size(max = 500, message = "Condition expression must not exceed 500 characters")
    private String conditionExpression;

    @Size(max = 30, message = "Action on pass must not exceed 30 characters")
    private String actionOnPass;

    @Size(max = 30, message = "Action on fail must not exceed 30 characters")
    private String actionOnFail;

    private Integer priority;

    private Boolean isActive;
}
