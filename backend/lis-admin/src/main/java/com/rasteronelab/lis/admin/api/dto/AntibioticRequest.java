package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Request DTO for creating or updating an Antibiotic.
 */
public class AntibioticRequest {

    @NotBlank(message = "Antibiotic name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Antibiotic code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @Size(max = 50, message = "Antibiotic group must not exceed 50 characters")
    private String antibioticGroup;

    private BigDecimal clsiBreakpointS;

    private BigDecimal clsiBreakpointR;

    @Size(max = 50, message = "CLSI method must not exceed 50 characters")
    private String clsiMethod;

    private Boolean isActive;

    public AntibioticRequest() {
    }

    public AntibioticRequest(String name, String code, String antibioticGroup, BigDecimal clsiBreakpointS, BigDecimal clsiBreakpointR, String clsiMethod, Boolean isActive) {
        this.name = name;
        this.code = code;
        this.antibioticGroup = antibioticGroup;
        this.clsiBreakpointS = clsiBreakpointS;
        this.clsiBreakpointR = clsiBreakpointR;
        this.clsiMethod = clsiMethod;
        this.isActive = isActive;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AntibioticRequest that = (AntibioticRequest) o;
        return java.util.Objects.equals(this.name, that.name) &&
               java.util.Objects.equals(this.code, that.code) &&
               java.util.Objects.equals(this.antibioticGroup, that.antibioticGroup) &&
               java.util.Objects.equals(this.clsiBreakpointS, that.clsiBreakpointS) &&
               java.util.Objects.equals(this.clsiBreakpointR, that.clsiBreakpointR) &&
               java.util.Objects.equals(this.clsiMethod, that.clsiMethod) &&
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.name, this.code, this.antibioticGroup, this.clsiBreakpointS, this.clsiBreakpointR, this.clsiMethod, this.isActive);
    }

    @Override
    public String toString() {
        return "AntibioticRequest{name=" + name +
               ", code=" + code +
               ", antibioticGroup=" + antibioticGroup +
               ", clsiBreakpointS=" + clsiBreakpointS +
               ", clsiBreakpointR=" + clsiBreakpointR +
               ", clsiMethod=" + clsiMethod +
               ", isActive=" + isActive +
               "}";
    }

    public static AntibioticRequestBuilder builder() {
        return new AntibioticRequestBuilder();
    }

    public static class AntibioticRequestBuilder {
        private String name;
        private String code;
        private String antibioticGroup;
        private BigDecimal clsiBreakpointS;
        private BigDecimal clsiBreakpointR;
        private String clsiMethod;
        private Boolean isActive;

        AntibioticRequestBuilder() {
        }

        public AntibioticRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public AntibioticRequestBuilder code(String code) {
            this.code = code;
            return this;
        }

        public AntibioticRequestBuilder antibioticGroup(String antibioticGroup) {
            this.antibioticGroup = antibioticGroup;
            return this;
        }

        public AntibioticRequestBuilder clsiBreakpointS(BigDecimal clsiBreakpointS) {
            this.clsiBreakpointS = clsiBreakpointS;
            return this;
        }

        public AntibioticRequestBuilder clsiBreakpointR(BigDecimal clsiBreakpointR) {
            this.clsiBreakpointR = clsiBreakpointR;
            return this;
        }

        public AntibioticRequestBuilder clsiMethod(String clsiMethod) {
            this.clsiMethod = clsiMethod;
            return this;
        }

        public AntibioticRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public AntibioticRequest build() {
            return new AntibioticRequest(this.name, this.code, this.antibioticGroup, this.clsiBreakpointS, this.clsiBreakpointR, this.clsiMethod, this.isActive);
        }
    }

}
