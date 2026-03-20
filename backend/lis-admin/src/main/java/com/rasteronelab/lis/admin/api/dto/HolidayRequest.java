package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Request DTO for creating or updating a Holiday.
 */
public class HolidayRequest {

    @NotNull(message = "Holiday date is required")
    private LocalDate holidayDate;

    @NotBlank(message = "Holiday name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private Boolean isHalfDay;

    public HolidayRequest() {
    }

    public HolidayRequest(LocalDate holidayDate, String name, String description, Boolean isHalfDay) {
        this.holidayDate = holidayDate;
        this.name = name;
        this.description = description;
        this.isHalfDay = isHalfDay;
    }

    public LocalDate getHolidayDate() {
        return this.holidayDate;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Boolean getIsHalfDay() {
        return this.isHalfDay;
    }

    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsHalfDay(Boolean isHalfDay) {
        this.isHalfDay = isHalfDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HolidayRequest that = (HolidayRequest) o;
        return java.util.Objects.equals(this.holidayDate, that.holidayDate) &&
               java.util.Objects.equals(this.name, that.name) &&
               java.util.Objects.equals(this.description, that.description) &&
               java.util.Objects.equals(this.isHalfDay, that.isHalfDay);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.holidayDate, this.name, this.description, this.isHalfDay);
    }

    @Override
    public String toString() {
        return "HolidayRequest{holidayDate=" + holidayDate +
               ", name=" + name +
               ", description=" + description +
               ", isHalfDay=" + isHalfDay +
               "}";
    }

    public static HolidayRequestBuilder builder() {
        return new HolidayRequestBuilder();
    }

    public static class HolidayRequestBuilder {
        private LocalDate holidayDate;
        private String name;
        private String description;
        private Boolean isHalfDay;

        HolidayRequestBuilder() {
        }

        public HolidayRequestBuilder holidayDate(LocalDate holidayDate) {
            this.holidayDate = holidayDate;
            return this;
        }

        public HolidayRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public HolidayRequestBuilder description(String description) {
            this.description = description;
            return this;
        }

        public HolidayRequestBuilder isHalfDay(Boolean isHalfDay) {
            this.isHalfDay = isHalfDay;
            return this;
        }

        public HolidayRequest build() {
            return new HolidayRequest(this.holidayDate, this.name, this.description, this.isHalfDay);
        }
    }

}
