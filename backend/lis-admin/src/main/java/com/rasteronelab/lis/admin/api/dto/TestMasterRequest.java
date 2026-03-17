package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for creating or updating a Test Master.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestMasterRequest {

    @NotBlank(message = "Test name is required")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;

    @NotBlank(message = "Test code is required")
    @Size(max = 30, message = "Code must not exceed 30 characters")
    private String code;

    @NotNull(message = "Department ID is required")
    private UUID departmentId;

    @Size(max = 100, message = "Short name must not exceed 100 characters")
    private String shortName;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Size(max = 100, message = "Sample type must not exceed 100 characters")
    private String sampleType;

    @Size(max = 100, message = "Tube type must not exceed 100 characters")
    private String tubeType;

    @Size(max = 100, message = "Report section must not exceed 100 characters")
    private String reportSection;

    @Size(max = 100, message = "Method must not exceed 100 characters")
    private String method;

    private Integer tatRoutineHours;

    private Integer tatStatHours;

    private BigDecimal basePrice;

    private Boolean isOutsourced;

    @Size(max = 200, message = "Outsource lab name must not exceed 200 characters")
    private String outsourceLabName;

    private Integer displayOrder;

    private Boolean isActive;
}
