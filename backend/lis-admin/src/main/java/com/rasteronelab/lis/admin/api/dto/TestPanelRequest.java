package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Request DTO for creating or updating a Test Panel.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestPanelRequest {

    @NotBlank(message = "Panel name is required")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;

    @NotBlank(message = "Panel code is required")
    @Size(max = 30, message = "Code must not exceed 30 characters")
    private String code;

    @NotNull(message = "Department ID is required")
    private UUID departmentId;

    @Size(max = 50, message = "Short name must not exceed 50 characters")
    private String shortName;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private BigDecimal panelPrice;

    private Integer displayOrder;

    private Boolean isActive;

    private List<UUID> testIds;
}
