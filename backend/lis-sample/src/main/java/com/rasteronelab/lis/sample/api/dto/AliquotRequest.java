package com.rasteronelab.lis.sample.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Describes a single aliquot child sample.
 */
public class AliquotRequest {

    @NotNull
    private UUID departmentId;

    private BigDecimal volume;

    private String unit;

    public UUID getDepartmentId() { return departmentId; }
    public void setDepartmentId(UUID departmentId) { this.departmentId = departmentId; }

    public BigDecimal getVolume() { return volume; }
    public void setVolume(BigDecimal volume) { this.volume = volume; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
