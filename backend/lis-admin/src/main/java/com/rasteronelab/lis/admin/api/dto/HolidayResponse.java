package com.rasteronelab.lis.admin.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Holiday entity.
 */
public class HolidayResponse {

    private UUID id;
    private UUID branchId;
    private LocalDate holidayDate;
    private String name;
    private String description;
    private Boolean isHalfDay;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public HolidayResponse() {
    }

    public HolidayResponse(UUID id, UUID branchId, LocalDate holidayDate, String name, String description, Boolean isHalfDay, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.holidayDate = holidayDate;
        this.name = name;
        this.description = description;
        this.isHalfDay = isHalfDay;
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
        HolidayResponse that = (HolidayResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.holidayDate, that.holidayDate) &&
               java.util.Objects.equals(this.name, that.name) &&
               java.util.Objects.equals(this.description, that.description) &&
               java.util.Objects.equals(this.isHalfDay, that.isHalfDay) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.holidayDate, this.name, this.description, this.isHalfDay, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "HolidayResponse{id=" + id +
               ", branchId=" + branchId +
               ", holidayDate=" + holidayDate +
               ", name=" + name +
               ", description=" + description +
               ", isHalfDay=" + isHalfDay +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static HolidayResponseBuilder builder() {
        return new HolidayResponseBuilder();
    }

    public static class HolidayResponseBuilder {
        private UUID id;
        private UUID branchId;
        private LocalDate holidayDate;
        private String name;
        private String description;
        private Boolean isHalfDay;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        HolidayResponseBuilder() {
        }

        public HolidayResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public HolidayResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public HolidayResponseBuilder holidayDate(LocalDate holidayDate) {
            this.holidayDate = holidayDate;
            return this;
        }

        public HolidayResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public HolidayResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public HolidayResponseBuilder isHalfDay(Boolean isHalfDay) {
            this.isHalfDay = isHalfDay;
            return this;
        }

        public HolidayResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public HolidayResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public HolidayResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public HolidayResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public HolidayResponse build() {
            return new HolidayResponse(this.id, this.branchId, this.holidayDate, this.name, this.description, this.isHalfDay, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
