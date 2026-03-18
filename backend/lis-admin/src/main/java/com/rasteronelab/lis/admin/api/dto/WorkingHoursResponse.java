package com.rasteronelab.lis.admin.api.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Response DTO for WorkingHours entity.
 */
public class WorkingHoursResponse {

    private UUID id;
    private UUID branchId;
    private Integer dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Boolean isWorkingDay;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public WorkingHoursResponse() {
    }

    public WorkingHoursResponse(UUID id, UUID branchId, Integer dayOfWeek, LocalTime openTime, LocalTime closeTime, Boolean isWorkingDay, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.isWorkingDay = isWorkingDay;
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

    public Integer getDayOfWeek() {
        return this.dayOfWeek;
    }

    public LocalTime getOpenTime() {
        return this.openTime;
    }

    public LocalTime getCloseTime() {
        return this.closeTime;
    }

    public Boolean getIsWorkingDay() {
        return this.isWorkingDay;
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

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }

    public void setIsWorkingDay(Boolean isWorkingDay) {
        this.isWorkingDay = isWorkingDay;
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
        WorkingHoursResponse that = (WorkingHoursResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.dayOfWeek, that.dayOfWeek) &&
               java.util.Objects.equals(this.openTime, that.openTime) &&
               java.util.Objects.equals(this.closeTime, that.closeTime) &&
               java.util.Objects.equals(this.isWorkingDay, that.isWorkingDay) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.dayOfWeek, this.openTime, this.closeTime, this.isWorkingDay, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "WorkingHoursResponse{id=" + id +
               ", branchId=" + branchId +
               ", dayOfWeek=" + dayOfWeek +
               ", openTime=" + openTime +
               ", closeTime=" + closeTime +
               ", isWorkingDay=" + isWorkingDay +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static WorkingHoursResponseBuilder builder() {
        return new WorkingHoursResponseBuilder();
    }

    public static class WorkingHoursResponseBuilder {
        private UUID id;
        private UUID branchId;
        private Integer dayOfWeek;
        private LocalTime openTime;
        private LocalTime closeTime;
        private Boolean isWorkingDay;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        WorkingHoursResponseBuilder() {
        }

        public WorkingHoursResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public WorkingHoursResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public WorkingHoursResponseBuilder dayOfWeek(Integer dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
            return this;
        }

        public WorkingHoursResponseBuilder openTime(LocalTime openTime) {
            this.openTime = openTime;
            return this;
        }

        public WorkingHoursResponseBuilder closeTime(LocalTime closeTime) {
            this.closeTime = closeTime;
            return this;
        }

        public WorkingHoursResponseBuilder isWorkingDay(Boolean isWorkingDay) {
            this.isWorkingDay = isWorkingDay;
            return this;
        }

        public WorkingHoursResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public WorkingHoursResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public WorkingHoursResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public WorkingHoursResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public WorkingHoursResponse build() {
            return new WorkingHoursResponse(this.id, this.branchId, this.dayOfWeek, this.openTime, this.closeTime, this.isWorkingDay, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
