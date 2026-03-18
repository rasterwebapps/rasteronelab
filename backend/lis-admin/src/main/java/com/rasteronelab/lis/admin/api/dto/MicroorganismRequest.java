package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a Microorganism.
 */
public class MicroorganismRequest {

    @NotBlank(message = "Microorganism name is required")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;

    @NotBlank(message = "Microorganism code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @Size(max = 20, message = "Gram type must not exceed 20 characters")
    private String gramType;

    @Size(max = 50, message = "Organism type must not exceed 50 characters")
    private String organismType;

    @Size(max = 500, message = "Clinical significance must not exceed 500 characters")
    private String clinicalSignificance;

    @Size(max = 500, message = "Colony morphology must not exceed 500 characters")
    private String colonyMorphology;

    private Boolean isActive;

    public MicroorganismRequest() {
    }

    public MicroorganismRequest(String name, String code, String gramType, String organismType, String clinicalSignificance, String colonyMorphology, Boolean isActive) {
        this.name = name;
        this.code = code;
        this.gramType = gramType;
        this.organismType = organismType;
        this.clinicalSignificance = clinicalSignificance;
        this.colonyMorphology = colonyMorphology;
        this.isActive = isActive;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getGramType() {
        return this.gramType;
    }

    public String getOrganismType() {
        return this.organismType;
    }

    public String getClinicalSignificance() {
        return this.clinicalSignificance;
    }

    public String getColonyMorphology() {
        return this.colonyMorphology;
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

    public void setGramType(String gramType) {
        this.gramType = gramType;
    }

    public void setOrganismType(String organismType) {
        this.organismType = organismType;
    }

    public void setClinicalSignificance(String clinicalSignificance) {
        this.clinicalSignificance = clinicalSignificance;
    }

    public void setColonyMorphology(String colonyMorphology) {
        this.colonyMorphology = colonyMorphology;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MicroorganismRequest that = (MicroorganismRequest) o;
        return java.util.Objects.equals(this.name, that.name) &&
               java.util.Objects.equals(this.code, that.code) &&
               java.util.Objects.equals(this.gramType, that.gramType) &&
               java.util.Objects.equals(this.organismType, that.organismType) &&
               java.util.Objects.equals(this.clinicalSignificance, that.clinicalSignificance) &&
               java.util.Objects.equals(this.colonyMorphology, that.colonyMorphology) &&
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.name, this.code, this.gramType, this.organismType, this.clinicalSignificance, this.colonyMorphology, this.isActive);
    }

    @Override
    public String toString() {
        return "MicroorganismRequest{name=" + name +
               ", code=" + code +
               ", gramType=" + gramType +
               ", organismType=" + organismType +
               ", clinicalSignificance=" + clinicalSignificance +
               ", colonyMorphology=" + colonyMorphology +
               ", isActive=" + isActive +
               "}";
    }

    public static MicroorganismRequestBuilder builder() {
        return new MicroorganismRequestBuilder();
    }

    public static class MicroorganismRequestBuilder {
        private String name;
        private String code;
        private String gramType;
        private String organismType;
        private String clinicalSignificance;
        private String colonyMorphology;
        private Boolean isActive;

        MicroorganismRequestBuilder() {
        }

        public MicroorganismRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MicroorganismRequestBuilder code(String code) {
            this.code = code;
            return this;
        }

        public MicroorganismRequestBuilder gramType(String gramType) {
            this.gramType = gramType;
            return this;
        }

        public MicroorganismRequestBuilder organismType(String organismType) {
            this.organismType = organismType;
            return this;
        }

        public MicroorganismRequestBuilder clinicalSignificance(String clinicalSignificance) {
            this.clinicalSignificance = clinicalSignificance;
            return this;
        }

        public MicroorganismRequestBuilder colonyMorphology(String colonyMorphology) {
            this.colonyMorphology = colonyMorphology;
            return this;
        }

        public MicroorganismRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public MicroorganismRequest build() {
            return new MicroorganismRequest(this.name, this.code, this.gramType, this.organismType, this.clinicalSignificance, this.colonyMorphology, this.isActive);
        }
    }

}
