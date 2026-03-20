package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for creating or updating a Test Master.
 */
public class TestMasterRequest {

    @NotBlank(message = "Test name is required")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;

    @NotBlank(message = "Test code is required")
    @Size(max = 30, message = "Code must not exceed 30 characters")
    private String code;

    @NotNull(message = "Department ID is required")
    private UUID departmentId;

    @Size(max = 50, message = "Short name must not exceed 50 characters")
    private String shortName;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Size(max = 50, message = "Sample type must not exceed 50 characters")
    private String sampleType;

    @Size(max = 50, message = "Tube type must not exceed 50 characters")
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

    public TestMasterRequest() {
    }

    public TestMasterRequest(String name, String code, UUID departmentId, String shortName, String description, String sampleType, String tubeType, String reportSection, String method, Integer tatRoutineHours, Integer tatStatHours, BigDecimal basePrice, Boolean isOutsourced, String outsourceLabName, Integer displayOrder, Boolean isActive) {
        this.name = name;
        this.code = code;
        this.departmentId = departmentId;
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
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public UUID getDepartmentId() {
        return this.departmentId;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestMasterRequest that = (TestMasterRequest) o;
        return java.util.Objects.equals(this.name, that.name) &&
               java.util.Objects.equals(this.code, that.code) &&
               java.util.Objects.equals(this.departmentId, that.departmentId) &&
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
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.name, this.code, this.departmentId, this.shortName, this.description, this.sampleType, this.tubeType, this.reportSection, this.method, this.tatRoutineHours, this.tatStatHours, this.basePrice, this.isOutsourced, this.outsourceLabName, this.displayOrder, this.isActive);
    }

    @Override
    public String toString() {
        return "TestMasterRequest{name=" + name +
               ", code=" + code +
               ", departmentId=" + departmentId +
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
               "}";
    }

    public static TestMasterRequestBuilder builder() {
        return new TestMasterRequestBuilder();
    }

    public static class TestMasterRequestBuilder {
        private String name;
        private String code;
        private UUID departmentId;
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

        TestMasterRequestBuilder() {
        }

        public TestMasterRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TestMasterRequestBuilder code(String code) {
            this.code = code;
            return this;
        }

        public TestMasterRequestBuilder departmentId(UUID departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public TestMasterRequestBuilder shortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public TestMasterRequestBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TestMasterRequestBuilder sampleType(String sampleType) {
            this.sampleType = sampleType;
            return this;
        }

        public TestMasterRequestBuilder tubeType(String tubeType) {
            this.tubeType = tubeType;
            return this;
        }

        public TestMasterRequestBuilder reportSection(String reportSection) {
            this.reportSection = reportSection;
            return this;
        }

        public TestMasterRequestBuilder method(String method) {
            this.method = method;
            return this;
        }

        public TestMasterRequestBuilder tatRoutineHours(Integer tatRoutineHours) {
            this.tatRoutineHours = tatRoutineHours;
            return this;
        }

        public TestMasterRequestBuilder tatStatHours(Integer tatStatHours) {
            this.tatStatHours = tatStatHours;
            return this;
        }

        public TestMasterRequestBuilder basePrice(BigDecimal basePrice) {
            this.basePrice = basePrice;
            return this;
        }

        public TestMasterRequestBuilder isOutsourced(Boolean isOutsourced) {
            this.isOutsourced = isOutsourced;
            return this;
        }

        public TestMasterRequestBuilder outsourceLabName(String outsourceLabName) {
            this.outsourceLabName = outsourceLabName;
            return this;
        }

        public TestMasterRequestBuilder displayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
            return this;
        }

        public TestMasterRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public TestMasterRequest build() {
            return new TestMasterRequest(this.name, this.code, this.departmentId, this.shortName, this.description, this.sampleType, this.tubeType, this.reportSection, this.method, this.tatRoutineHours, this.tatStatHours, this.basePrice, this.isOutsourced, this.outsourceLabName, this.displayOrder, this.isActive);
        }
    }

}
