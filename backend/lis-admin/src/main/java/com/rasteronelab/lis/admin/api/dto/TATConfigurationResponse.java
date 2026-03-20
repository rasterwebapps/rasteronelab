package com.rasteronelab.lis.admin.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for TATConfiguration entity.
 * Includes testName and departmentName for display.
 */
public class TATConfigurationResponse {

    private UUID id;
    private UUID branchId;
    private UUID testId;
    private String testName;
    private UUID departmentId;
    private String departmentName;
    private Integer routineHours;
    private Integer statHours;
    private Integer criticalHours;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public TATConfigurationResponse() {
    }

    public TATConfigurationResponse(UUID id, UUID branchId, UUID testId, String testName, UUID departmentId, String departmentName, Integer routineHours, Integer statHours, Integer criticalHours, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.testId = testId;
        this.testName = testName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.routineHours = routineHours;
        this.statHours = statHours;
        this.criticalHours = criticalHours;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getBranchId() {
        return this.branchId;
    }

    public UUID getTestId() {
        return this.testId;
    }

    public String getTestName() {
        return this.testName;
    }

    public UUID getDepartmentId() {
        return this.departmentId;
    }

    public String getDepartmentName() {
        return this.departmentName;
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

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TATConfigurationResponse that = (TATConfigurationResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.testId, that.testId) &&
               java.util.Objects.equals(this.testName, that.testName) &&
               java.util.Objects.equals(this.departmentId, that.departmentId) &&
               java.util.Objects.equals(this.departmentName, that.departmentName) &&
               java.util.Objects.equals(this.routineHours, that.routineHours) &&
               java.util.Objects.equals(this.statHours, that.statHours) &&
               java.util.Objects.equals(this.criticalHours, that.criticalHours) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.testId, this.testName, this.departmentId, this.departmentName, this.routineHours, this.statHours, this.criticalHours, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "TATConfigurationResponse{id=" + id +
               ", branchId=" + branchId +
               ", testId=" + testId +
               ", testName=" + testName +
               ", departmentId=" + departmentId +
               ", departmentName=" + departmentName +
               ", routineHours=" + routineHours +
               ", statHours=" + statHours +
               ", criticalHours=" + criticalHours +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static TATConfigurationResponseBuilder builder() {
        return new TATConfigurationResponseBuilder();
    }

    public static class TATConfigurationResponseBuilder {
        private UUID id;
        private UUID branchId;
        private UUID testId;
        private String testName;
        private UUID departmentId;
        private String departmentName;
        private Integer routineHours;
        private Integer statHours;
        private Integer criticalHours;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        TATConfigurationResponseBuilder() {
        }

        public TATConfigurationResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public TATConfigurationResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public TATConfigurationResponseBuilder testId(UUID testId) {
            this.testId = testId;
            return this;
        }

        public TATConfigurationResponseBuilder testName(String testName) {
            this.testName = testName;
            return this;
        }

        public TATConfigurationResponseBuilder departmentId(UUID departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public TATConfigurationResponseBuilder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public TATConfigurationResponseBuilder routineHours(Integer routineHours) {
            this.routineHours = routineHours;
            return this;
        }

        public TATConfigurationResponseBuilder statHours(Integer statHours) {
            this.statHours = statHours;
            return this;
        }

        public TATConfigurationResponseBuilder criticalHours(Integer criticalHours) {
            this.criticalHours = criticalHours;
            return this;
        }

        public TATConfigurationResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public TATConfigurationResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public TATConfigurationResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public TATConfigurationResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public TATConfigurationResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public TATConfigurationResponse build() {
            return new TATConfigurationResponse(this.id, this.branchId, this.testId, this.testName, this.departmentId, this.departmentName, this.routineHours, this.statHours, this.criticalHours, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
