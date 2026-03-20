package com.rasteronelab.lis.admin.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for TestMaster entity.
 * Includes all entity fields plus departmentName for display.
 */
public class TestMasterResponse {

    private UUID id;
    private UUID branchId;
    private UUID departmentId;
    private String departmentName;
    private String name;
    private String code;
    private String shortName;
    private String description;
    private String sampleType;
    private String tubeType;
    private String reportSection;
    private String method;
    private Integer tatRoutineHours;
    private Integer tatStatHours;
    private BigDecimal basePrice;
    private Boolean isOutsourced;
    private String outsourceLabName;
    private Integer displayOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public TestMasterResponse() {
    }

    public TestMasterResponse(UUID id, UUID branchId, UUID departmentId, String departmentName, String name, String code, String shortName, String description, String sampleType, String tubeType, String reportSection, String method, Integer tatRoutineHours, Integer tatStatHours, BigDecimal basePrice, Boolean isOutsourced, String outsourceLabName, Integer displayOrder, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.name = name;
        this.code = code;
        this.shortName = shortName;
        this.description = description;
        this.sampleType = sampleType;
        this.tubeType = tubeType;
        this.reportSection = reportSection;
        this.method = method;
        this.tatRoutineHours = tatRoutineHours;
        this.tatStatHours = tatStatHours;
        this.basePrice = basePrice;
        this.isOutsourced = isOutsourced;
        this.outsourceLabName = outsourceLabName;
        this.displayOrder = displayOrder;
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

    public UUID getDepartmentId() {
        return this.departmentId;
    }

    public String getDepartmentName() {
        return this.departmentName;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getShortName() {
        return this.shortName;
    }

    public String getDescription() {
        return this.description;
    }

    public String getSampleType() {
        return this.sampleType;
    }

    public String getTubeType() {
        return this.tubeType;
    }

    public String getReportSection() {
        return this.reportSection;
    }

    public String getMethod() {
        return this.method;
    }

    public Integer getTatRoutineHours() {
        return this.tatRoutineHours;
    }

    public Integer getTatStatHours() {
        return this.tatStatHours;
    }

    public BigDecimal getBasePrice() {
        return this.basePrice;
    }

    public Boolean getIsOutsourced() {
        return this.isOutsourced;
    }

    public String getOutsourceLabName() {
        return this.outsourceLabName;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
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

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public void setTubeType(String tubeType) {
        this.tubeType = tubeType;
    }

    public void setReportSection(String reportSection) {
        this.reportSection = reportSection;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setTatRoutineHours(Integer tatRoutineHours) {
        this.tatRoutineHours = tatRoutineHours;
    }

    public void setTatStatHours(Integer tatStatHours) {
        this.tatStatHours = tatStatHours;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public void setIsOutsourced(Boolean isOutsourced) {
        this.isOutsourced = isOutsourced;
    }

    public void setOutsourceLabName(String outsourceLabName) {
        this.outsourceLabName = outsourceLabName;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
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
        TestMasterResponse that = (TestMasterResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.departmentId, that.departmentId) &&
               java.util.Objects.equals(this.departmentName, that.departmentName) &&
               java.util.Objects.equals(this.name, that.name) &&
               java.util.Objects.equals(this.code, that.code) &&
               java.util.Objects.equals(this.shortName, that.shortName) &&
               java.util.Objects.equals(this.description, that.description) &&
               java.util.Objects.equals(this.sampleType, that.sampleType) &&
               java.util.Objects.equals(this.tubeType, that.tubeType) &&
               java.util.Objects.equals(this.reportSection, that.reportSection) &&
               java.util.Objects.equals(this.method, that.method) &&
               java.util.Objects.equals(this.tatRoutineHours, that.tatRoutineHours) &&
               java.util.Objects.equals(this.tatStatHours, that.tatStatHours) &&
               java.util.Objects.equals(this.basePrice, that.basePrice) &&
               java.util.Objects.equals(this.isOutsourced, that.isOutsourced) &&
               java.util.Objects.equals(this.outsourceLabName, that.outsourceLabName) &&
               java.util.Objects.equals(this.displayOrder, that.displayOrder) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.departmentId, this.departmentName, this.name, this.code, this.shortName, this.description, this.sampleType, this.tubeType, this.reportSection, this.method, this.tatRoutineHours, this.tatStatHours, this.basePrice, this.isOutsourced, this.outsourceLabName, this.displayOrder, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "TestMasterResponse{id=" + id +
               ", branchId=" + branchId +
               ", departmentId=" + departmentId +
               ", departmentName=" + departmentName +
               ", name=" + name +
               ", code=" + code +
               ", shortName=" + shortName +
               ", description=" + description +
               ", sampleType=" + sampleType +
               ", tubeType=" + tubeType +
               ", reportSection=" + reportSection +
               ", method=" + method +
               ", tatRoutineHours=" + tatRoutineHours +
               ", tatStatHours=" + tatStatHours +
               ", basePrice=" + basePrice +
               ", isOutsourced=" + isOutsourced +
               ", outsourceLabName=" + outsourceLabName +
               ", displayOrder=" + displayOrder +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static TestMasterResponseBuilder builder() {
        return new TestMasterResponseBuilder();
    }

    public static class TestMasterResponseBuilder {
        private UUID id;
        private UUID branchId;
        private UUID departmentId;
        private String departmentName;
        private String name;
        private String code;
        private String shortName;
        private String description;
        private String sampleType;
        private String tubeType;
        private String reportSection;
        private String method;
        private Integer tatRoutineHours;
        private Integer tatStatHours;
        private BigDecimal basePrice;
        private Boolean isOutsourced;
        private String outsourceLabName;
        private Integer displayOrder;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        TestMasterResponseBuilder() {
        }

        public TestMasterResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public TestMasterResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public TestMasterResponseBuilder departmentId(UUID departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public TestMasterResponseBuilder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public TestMasterResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TestMasterResponseBuilder code(String code) {
            this.code = code;
            return this;
        }

        public TestMasterResponseBuilder shortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public TestMasterResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TestMasterResponseBuilder sampleType(String sampleType) {
            this.sampleType = sampleType;
            return this;
        }

        public TestMasterResponseBuilder tubeType(String tubeType) {
            this.tubeType = tubeType;
            return this;
        }

        public TestMasterResponseBuilder reportSection(String reportSection) {
            this.reportSection = reportSection;
            return this;
        }

        public TestMasterResponseBuilder method(String method) {
            this.method = method;
            return this;
        }

        public TestMasterResponseBuilder tatRoutineHours(Integer tatRoutineHours) {
            this.tatRoutineHours = tatRoutineHours;
            return this;
        }

        public TestMasterResponseBuilder tatStatHours(Integer tatStatHours) {
            this.tatStatHours = tatStatHours;
            return this;
        }

        public TestMasterResponseBuilder basePrice(BigDecimal basePrice) {
            this.basePrice = basePrice;
            return this;
        }

        public TestMasterResponseBuilder isOutsourced(Boolean isOutsourced) {
            this.isOutsourced = isOutsourced;
            return this;
        }

        public TestMasterResponseBuilder outsourceLabName(String outsourceLabName) {
            this.outsourceLabName = outsourceLabName;
            return this;
        }

        public TestMasterResponseBuilder displayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
            return this;
        }

        public TestMasterResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public TestMasterResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public TestMasterResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public TestMasterResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public TestMasterResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public TestMasterResponse build() {
            return new TestMasterResponse(this.id, this.branchId, this.departmentId, this.departmentName, this.name, this.code, this.shortName, this.description, this.sampleType, this.tubeType, this.reportSection, this.method, this.tatRoutineHours, this.tatStatHours, this.basePrice, this.isOutsourced, this.outsourceLabName, this.displayOrder, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
