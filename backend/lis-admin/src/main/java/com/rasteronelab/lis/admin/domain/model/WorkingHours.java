package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalTime;

/**
 * Working Hours entity (L3 branch-level).
 * Defines operational hours for each day of the week per branch.
 */
@Entity
@Table(name = "working_hours")
public class WorkingHours extends BaseEntity {

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;

    @Column(name = "is_working_day", nullable = false)
    private Boolean isWorkingDay = true;

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

}
