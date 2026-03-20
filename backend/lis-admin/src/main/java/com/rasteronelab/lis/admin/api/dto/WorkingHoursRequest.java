package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

/**
 * Request DTO for creating or updating Working Hours.
 */
public class WorkingHoursRequest {

    @NotNull(message = "Day of week is required")
    @Min(value = 1, message = "Day of week must be between 1 (Monday) and 7 (Sunday)")
    @Max(value = 7, message = "Day of week must be between 1 (Monday) and 7 (Sunday)")
    private Integer dayOfWeek;

    @NotNull(message = "Open time is required")
    private LocalTime openTime;

    @NotNull(message = "Close time is required")
    private LocalTime closeTime;

    private Boolean isWorkingDay;

    public WorkingHoursRequest() {
    }

    public WorkingHoursRequest(Integer dayOfWeek, LocalTime openTime, LocalTime closeTime, Boolean isWorkingDay) {
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.isWorkingDay = isWorkingDay;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkingHoursRequest that = (WorkingHoursRequest) o;
        return java.util.Objects.equals(this.dayOfWeek, that.dayOfWeek) &&
               java.util.Objects.equals(this.openTime, that.openTime) &&
               java.util.Objects.equals(this.closeTime, that.closeTime) &&
               java.util.Objects.equals(this.isWorkingDay, that.isWorkingDay);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.dayOfWeek, this.openTime, this.closeTime, this.isWorkingDay);
    }

    @Override
    public String toString() {
        return "WorkingHoursRequest{dayOfWeek=" + dayOfWeek +
               ", openTime=" + openTime +
               ", closeTime=" + closeTime +
               ", isWorkingDay=" + isWorkingDay +
               "}";
    }

    public static WorkingHoursRequestBuilder builder() {
        return new WorkingHoursRequestBuilder();
    }

    public static class WorkingHoursRequestBuilder {
        private Integer dayOfWeek;
        private LocalTime openTime;
        private LocalTime closeTime;
        private Boolean isWorkingDay;

        WorkingHoursRequestBuilder() {
        }

        public WorkingHoursRequestBuilder dayOfWeek(Integer dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
            return this;
        }

        public WorkingHoursRequestBuilder openTime(LocalTime openTime) {
            this.openTime = openTime;
            return this;
        }

        public WorkingHoursRequestBuilder closeTime(LocalTime closeTime) {
            this.closeTime = closeTime;
            return this;
        }

        public WorkingHoursRequestBuilder isWorkingDay(Boolean isWorkingDay) {
            this.isWorkingDay = isWorkingDay;
            return this;
        }

        public WorkingHoursRequest build() {
            return new WorkingHoursRequest(this.dayOfWeek, this.openTime, this.closeTime, this.isWorkingDay);
        }
    }

}
