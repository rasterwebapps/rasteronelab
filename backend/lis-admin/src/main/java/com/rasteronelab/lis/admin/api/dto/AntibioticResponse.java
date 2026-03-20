package com.rasteronelab.lis.admin.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Antibiotic entity.
 */
public class AntibioticResponse {

    private UUID id;
    private UUID branchId;
    private String name;
    private String code;
    private String antibioticGroup;
    private BigDecimal clsiBreakpointS;
    private BigDecimal clsiBreakpointR;
    private String clsiMethod;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public AntibioticResponse() {
    }

    public AntibioticResponse(UUID id, UUID branchId, String name, String code, String antibioticGroup, BigDecimal clsiBreakpointS, BigDecimal clsiBreakpointR, String clsiMethod, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.name = name;
        this.code = code;
        this.antibioticGroup = antibioticGroup;
        this.clsiBreakpointS = clsiBreakpointS;
        this.clsiBreakpointR = clsiBreakpointR;
        this.clsiMethod = clsiMethod;
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

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getAntibioticGroup() {
        return this.antibioticGroup;
    }

    public BigDecimal getClsiBreakpointS() {
        return this.clsiBreakpointS;
    }

    public BigDecimal getClsiBreakpointR() {
        return this.clsiBreakpointR;
    }

    public String getClsiMethod() {
        return this.clsiMethod;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setAntibioticGroup(String antibioticGroup) {
        this.antibioticGroup = antibioticGroup;
    }

    public void setClsiBreakpointS(BigDecimal clsiBreakpointS) {
        this.clsiBreakpointS = clsiBreakpointS;
    }

    public void setClsiBreakpointR(BigDecimal clsiBreakpointR) {
        this.clsiBreakpointR = clsiBreakpointR;
    }

    public void setClsiMethod(String clsiMethod) {
        this.clsiMethod = clsiMethod;
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
        AntibioticResponse that = (AntibioticResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.name, that.name) &&
               java.util.Objects.equals(this.code, that.code) &&
               java.util.Objects.equals(this.antibioticGroup, that.antibioticGroup) &&
               java.util.Objects.equals(this.clsiBreakpointS, that.clsiBreakpointS) &&
               java.util.Objects.equals(this.clsiBreakpointR, that.clsiBreakpointR) &&
               java.util.Objects.equals(this.clsiMethod, that.clsiMethod) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.name, this.code, this.antibioticGroup, this.clsiBreakpointS, this.clsiBreakpointR, this.clsiMethod, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "AntibioticResponse{id=" + id +
               ", branchId=" + branchId +
               ", name=" + name +
               ", code=" + code +
               ", antibioticGroup=" + antibioticGroup +
               ", clsiBreakpointS=" + clsiBreakpointS +
               ", clsiBreakpointR=" + clsiBreakpointR +
               ", clsiMethod=" + clsiMethod +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static AntibioticResponseBuilder builder() {
        return new AntibioticResponseBuilder();
    }

    public static class AntibioticResponseBuilder {
        private UUID id;
        private UUID branchId;
        private String name;
        private String code;
        private String antibioticGroup;
        private BigDecimal clsiBreakpointS;
        private BigDecimal clsiBreakpointR;
        private String clsiMethod;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        AntibioticResponseBuilder() {
        }

        public AntibioticResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public AntibioticResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public AntibioticResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public AntibioticResponseBuilder code(String code) {
            this.code = code;
            return this;
        }

        public AntibioticResponseBuilder antibioticGroup(String antibioticGroup) {
            this.antibioticGroup = antibioticGroup;
            return this;
        }

        public AntibioticResponseBuilder clsiBreakpointS(BigDecimal clsiBreakpointS) {
            this.clsiBreakpointS = clsiBreakpointS;
            return this;
        }

        public AntibioticResponseBuilder clsiBreakpointR(BigDecimal clsiBreakpointR) {
            this.clsiBreakpointR = clsiBreakpointR;
            return this;
        }

        public AntibioticResponseBuilder clsiMethod(String clsiMethod) {
            this.clsiMethod = clsiMethod;
            return this;
        }

        public AntibioticResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public AntibioticResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AntibioticResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public AntibioticResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public AntibioticResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public AntibioticResponse build() {
            return new AntibioticResponse(this.id, this.branchId, this.name, this.code, this.antibioticGroup, this.clsiBreakpointS, this.clsiBreakpointR, this.clsiMethod, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
