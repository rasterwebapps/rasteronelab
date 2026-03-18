package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO for creating or updating a TAT Configuration.
 */
public class TATConfigurationRequest {

    private UUID testId;

    private UUID departmentId;

    @NotNull(message = "Routine hours is required")
    private Integer routineHours;

    private Integer statHours;

    private Integer criticalHours;

    private Boolean isActive;

    public TATConfigurationRequest() {
    }

    public TATConfigurationRequest(UUID testId, UUID departmentId, Integer routineHours, Integer statHours, Integer criticalHours, Boolean isActive) {
        this.testId = testId;
        this.departmentId = departmentId;
        this.routineHours = routineHours;
        this.statHours = statHours;
        this.criticalHours = criticalHours;
        this.isActive = isActive;
    }

    public UUID getTestId() {
        return this.testId;
    }

    public UUID getDepartmentId() {
        return this.departmentId;
    }

    public Integer getRoutineHours() {
        return this.routineHours;
    }

    public Integer getStatHours() {
        return this.statHours;
    }

    public Integer getCriticalHours() {
        return this.criticalHours;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public void setRoutineHours(Integer routineHours) {
        this.routineHours = routineHours;
    }

    public void setStatHours(Integer statHours) {
        this.statHours = statHours;
    }

    public void setCriticalHours(Integer criticalHours) {
        this.criticalHours = criticalHours;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TATConfigurationRequest that = (TATConfigurationRequest) o;
        return java.util.Objects.equals(this.testId, that.testId) &&
               java.util.Objects.equals(this.departmentId, that.departmentId) &&
               java.util.Objects.equals(this.routineHours, that.routineHours) &&
               java.util.Objects.equals(this.statHours, that.statHours) &&
               java.util.Objects.equals(this.criticalHours, that.criticalHours) &&
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.testId, this.departmentId, this.routineHours, this.statHours, this.criticalHours, this.isActive);
    }

    @Override
    public String toString() {
        return "TATConfigurationRequest{testId=" + testId +
               ", departmentId=" + departmentId +
               ", routineHours=" + routineHours +
               ", statHours=" + statHours +
               ", criticalHours=" + criticalHours +
               ", isActive=" + isActive +
               "}";
    }

    public static TATConfigurationRequestBuilder builder() {
        return new TATConfigurationRequestBuilder();
    }

    public static class TATConfigurationRequestBuilder {
        private UUID testId;
        private UUID departmentId;
        private Integer routineHours;
        private Integer statHours;
        private Integer criticalHours;
        private Boolean isActive;

        TATConfigurationRequestBuilder() {
        }

        public TATConfigurationRequestBuilder testId(UUID testId) {
            this.testId = testId;
            return this;
        }

        public TATConfigurationRequestBuilder departmentId(UUID departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public TATConfigurationRequestBuilder routineHours(Integer routineHours) {
            this.routineHours = routineHours;
            return this;
        }

        public TATConfigurationRequestBuilder statHours(Integer statHours) {
            this.statHours = statHours;
            return this;
        }

        public TATConfigurationRequestBuilder criticalHours(Integer criticalHours) {
            this.criticalHours = criticalHours;
            return this;
        }

        public TATConfigurationRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public TATConfigurationRequest build() {
            return new TATConfigurationRequest(this.testId, this.departmentId, this.routineHours, this.statHours, this.criticalHours, this.isActive);
        }
    }

}
