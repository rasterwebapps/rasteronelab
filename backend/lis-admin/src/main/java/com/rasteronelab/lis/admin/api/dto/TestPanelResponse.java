package com.rasteronelab.lis.admin.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for TestPanel entity.
 * Includes departmentName and constituent tests for display.
 */
public class TestPanelResponse {

    private UUID id;
    private UUID branchId;
    private UUID departmentId;
    private String departmentName;
    private String name;
    private String code;
    private String shortName;
    private String description;
    private BigDecimal panelPrice;
    private Integer displayOrder;
    private Boolean isActive;
    private List<PanelTestItem> tests;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public static class PanelTestItem {
        private UUID testId;
        private String testName;
        private String testCode;
        private Integer displayOrder;

        public PanelTestItem() {
        }

        public PanelTestItem(UUID testId, String testName, String testCode, Integer displayOrder) {
            this.testId = testId;
            this.testName = testName;
            this.testCode = testCode;
            this.displayOrder = displayOrder;
        }

        public UUID getTestId() {
            return this.testId;
        }

        public String getTestName() {
            return this.testName;
        }

        public String getTestCode() {
            return this.testCode;
        }

        public Integer getDisplayOrder() {
            return this.displayOrder;
        }

        public void setTestId(UUID testId) {
            this.testId = testId;
        }

        public void setTestName(String testName) {
            this.testName = testName;
        }

        public void setTestCode(String testCode) {
            this.testCode = testCode;
        }

        public void setDisplayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PanelTestItem that = (PanelTestItem) o;
            return java.util.Objects.equals(this.testId, that.testId) &&
                   java.util.Objects.equals(this.testName, that.testName) &&
                   java.util.Objects.equals(this.testCode, that.testCode) &&
                   java.util.Objects.equals(this.displayOrder, that.displayOrder);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(this.testId, this.testName, this.testCode, this.displayOrder);
        }

        @Override
        public String toString() {
            return "PanelTestItem{testId=" + testId +
                   ", testName=" + testName +
                   ", testCode=" + testCode +
                   ", displayOrder=" + displayOrder +
                   "}";
        }

        public static PanelTestItemBuilder builder() {
            return new PanelTestItemBuilder();
        }

        public static class PanelTestItemBuilder {
            private UUID testId;
            private String testName;
            private String testCode;
            private Integer displayOrder;

            PanelTestItemBuilder() {
            }

            public PanelTestItemBuilder testId(UUID testId) {
                this.testId = testId;
                return this;
            }

            public PanelTestItemBuilder testName(String testName) {
                this.testName = testName;
                return this;
            }

            public PanelTestItemBuilder testCode(String testCode) {
                this.testCode = testCode;
                return this;
            }

            public PanelTestItemBuilder displayOrder(Integer displayOrder) {
                this.displayOrder = displayOrder;
                return this;
            }

            public PanelTestItem build() {
                return new PanelTestItem(this.testId, this.testName, this.testCode, this.displayOrder);
            }
        }
    }

    public TestPanelResponse() {
    }

    public TestPanelResponse(UUID id, UUID branchId, UUID departmentId, String departmentName, String name, String code, String shortName, String description, BigDecimal panelPrice, Integer displayOrder, Boolean isActive, List<PanelTestItem> tests, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.name = name;
        this.code = code;
        this.shortName = shortName;
        this.description = description;
        this.panelPrice = panelPrice;
        this.displayOrder = displayOrder;
        this.isActive = isActive;
        this.tests = tests;
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

    public BigDecimal getPanelPrice() {
        return this.panelPrice;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public List<PanelTestItem> getTests() {
        return this.tests;
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

    public void setPanelPrice(BigDecimal panelPrice) {
        this.panelPrice = panelPrice;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setTests(List<PanelTestItem> tests) {
        this.tests = tests;
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
        TestPanelResponse that = (TestPanelResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.departmentId, that.departmentId) &&
               java.util.Objects.equals(this.departmentName, that.departmentName) &&
               java.util.Objects.equals(this.name, that.name) &&
               java.util.Objects.equals(this.code, that.code) &&
               java.util.Objects.equals(this.shortName, that.shortName) &&
               java.util.Objects.equals(this.description, that.description) &&
               java.util.Objects.equals(this.panelPrice, that.panelPrice) &&
               java.util.Objects.equals(this.displayOrder, that.displayOrder) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.tests, that.tests) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.departmentId, this.departmentName, this.name, this.code, this.shortName, this.description, this.panelPrice, this.displayOrder, this.isActive, this.tests, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "TestPanelResponse{id=" + id +
               ", branchId=" + branchId +
               ", departmentId=" + departmentId +
               ", departmentName=" + departmentName +
               ", name=" + name +
               ", code=" + code +
               ", shortName=" + shortName +
               ", description=" + description +
               ", panelPrice=" + panelPrice +
               ", displayOrder=" + displayOrder +
               ", isActive=" + isActive +
               ", tests=" + tests +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static TestPanelResponseBuilder builder() {
        return new TestPanelResponseBuilder();
    }

    public static class TestPanelResponseBuilder {
        private UUID id;
        private UUID branchId;
        private UUID departmentId;
        private String departmentName;
        private String name;
        private String code;
        private String shortName;
        private String description;
        private BigDecimal panelPrice;
        private Integer displayOrder;
        private Boolean isActive;
        private List<PanelTestItem> tests;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        TestPanelResponseBuilder() {
        }

        public TestPanelResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public TestPanelResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public TestPanelResponseBuilder departmentId(UUID departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public TestPanelResponseBuilder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public TestPanelResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TestPanelResponseBuilder code(String code) {
            this.code = code;
            return this;
        }

        public TestPanelResponseBuilder shortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public TestPanelResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TestPanelResponseBuilder panelPrice(BigDecimal panelPrice) {
            this.panelPrice = panelPrice;
            return this;
        }

        public TestPanelResponseBuilder displayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
            return this;
        }

        public TestPanelResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public TestPanelResponseBuilder tests(List<PanelTestItem> tests) {
            this.tests = tests;
            return this;
        }

        public TestPanelResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public TestPanelResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public TestPanelResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public TestPanelResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public TestPanelResponse build() {
            return new TestPanelResponse(this.id, this.branchId, this.departmentId, this.departmentName, this.name, this.code, this.shortName, this.description, this.panelPrice, this.displayOrder, this.isActive, this.tests, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
