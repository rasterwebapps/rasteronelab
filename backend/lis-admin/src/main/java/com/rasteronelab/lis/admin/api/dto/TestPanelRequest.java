package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Request DTO for creating or updating a Test Panel.
 */
public class TestPanelRequest {

    @NotBlank(message = "Panel name is required")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;

    @NotBlank(message = "Panel code is required")
    @Size(max = 30, message = "Code must not exceed 30 characters")
    private String code;

    @NotNull(message = "Department ID is required")
    private UUID departmentId;

    @Size(max = 50, message = "Short name must not exceed 50 characters")
    private String shortName;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private BigDecimal panelPrice;

    private Integer displayOrder;

    private Boolean isActive;

    private List<UUID> testIds;

    public TestPanelRequest() {
    }

    public TestPanelRequest(String name, String code, UUID departmentId, String shortName, String description, BigDecimal panelPrice, Integer displayOrder, Boolean isActive, List<UUID> testIds) {
        this.name = name;
        this.code = code;
        this.departmentId = departmentId;
        this.shortName = shortName;
        this.description = description;
        this.panelPrice = panelPrice;
        this.displayOrder = displayOrder;
        this.isActive = isActive;
        this.testIds = testIds;
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

    public BigDecimal getPanelPrice() {
        return this.panelPrice;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public List<UUID> getTestIds() {
        return this.testIds;
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

    public void setPanelPrice(BigDecimal panelPrice) {
        this.panelPrice = panelPrice;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setTestIds(List<UUID> testIds) {
        this.testIds = testIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestPanelRequest that = (TestPanelRequest) o;
        return java.util.Objects.equals(this.name, that.name) &&
               java.util.Objects.equals(this.code, that.code) &&
               java.util.Objects.equals(this.departmentId, that.departmentId) &&
               java.util.Objects.equals(this.shortName, that.shortName) &&
               java.util.Objects.equals(this.description, that.description) &&
               java.util.Objects.equals(this.panelPrice, that.panelPrice) &&
               java.util.Objects.equals(this.displayOrder, that.displayOrder) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.testIds, that.testIds);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.name, this.code, this.departmentId, this.shortName, this.description, this.panelPrice, this.displayOrder, this.isActive, this.testIds);
    }

    @Override
    public String toString() {
        return "TestPanelRequest{name=" + name +
               ", code=" + code +
               ", departmentId=" + departmentId +
               ", shortName=" + shortName +
               ", description=" + description +
               ", panelPrice=" + panelPrice +
               ", displayOrder=" + displayOrder +
               ", isActive=" + isActive +
               ", testIds=" + testIds +
               "}";
    }

    public static TestPanelRequestBuilder builder() {
        return new TestPanelRequestBuilder();
    }

    public static class TestPanelRequestBuilder {
        private String name;
        private String code;
        private UUID departmentId;
        private String shortName;
        private String description;
        private BigDecimal panelPrice;
        private Integer displayOrder;
        private Boolean isActive;
        private List<UUID> testIds;

        TestPanelRequestBuilder() {
        }

        public TestPanelRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TestPanelRequestBuilder code(String code) {
            this.code = code;
            return this;
        }

        public TestPanelRequestBuilder departmentId(UUID departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public TestPanelRequestBuilder shortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public TestPanelRequestBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TestPanelRequestBuilder panelPrice(BigDecimal panelPrice) {
            this.panelPrice = panelPrice;
            return this;
        }

        public TestPanelRequestBuilder displayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
            return this;
        }

        public TestPanelRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public TestPanelRequestBuilder testIds(List<UUID> testIds) {
            this.testIds = testIds;
            return this;
        }

        public TestPanelRequest build() {
            return new TestPanelRequest(this.name, this.code, this.departmentId, this.shortName, this.description, this.panelPrice, this.displayOrder, this.isActive, this.testIds);
        }
    }

}
