package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Request DTO for creating or updating an Antibiotic-Organism Mapping.
 */
public class AntibioticOrganismMappingRequest {

    @NotNull(message = "Antibiotic ID is required")
    private UUID antibioticId;

    @NotNull(message = "Microorganism ID is required")
    private UUID microorganismId;

    @Size(max = 20, message = "Susceptibility must not exceed 20 characters")
    private String susceptibility;

    private Boolean isDefaultPanel;

    public AntibioticOrganismMappingRequest() {
    }

    public AntibioticOrganismMappingRequest(UUID antibioticId, UUID microorganismId, String susceptibility, Boolean isDefaultPanel) {
        this.antibioticId = antibioticId;
        this.microorganismId = microorganismId;
        this.susceptibility = susceptibility;
        this.isDefaultPanel = isDefaultPanel;
    }

    public UUID getAntibioticId() {
        return this.antibioticId;
    }

    public UUID getMicroorganismId() {
        return this.microorganismId;
    }

    public String getSusceptibility() {
        return this.susceptibility;
    }

    public Boolean getIsDefaultPanel() {
        return this.isDefaultPanel;
    }

    public void setAntibioticId(UUID antibioticId) {
        this.antibioticId = antibioticId;
    }

    public void setMicroorganismId(UUID microorganismId) {
        this.microorganismId = microorganismId;
    }

    public void setSusceptibility(String susceptibility) {
        this.susceptibility = susceptibility;
    }

    public void setIsDefaultPanel(Boolean isDefaultPanel) {
        this.isDefaultPanel = isDefaultPanel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AntibioticOrganismMappingRequest that = (AntibioticOrganismMappingRequest) o;
        return java.util.Objects.equals(this.antibioticId, that.antibioticId) &&
               java.util.Objects.equals(this.microorganismId, that.microorganismId) &&
               java.util.Objects.equals(this.susceptibility, that.susceptibility) &&
               java.util.Objects.equals(this.isDefaultPanel, that.isDefaultPanel);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.antibioticId, this.microorganismId, this.susceptibility, this.isDefaultPanel);
    }

    @Override
    public String toString() {
        return "AntibioticOrganismMappingRequest{antibioticId=" + antibioticId +
               ", microorganismId=" + microorganismId +
               ", susceptibility=" + susceptibility +
               ", isDefaultPanel=" + isDefaultPanel +
               "}";
    }

    public static AntibioticOrganismMappingRequestBuilder builder() {
        return new AntibioticOrganismMappingRequestBuilder();
    }

    public static class AntibioticOrganismMappingRequestBuilder {
        private UUID antibioticId;
        private UUID microorganismId;
        private String susceptibility;
        private Boolean isDefaultPanel;

        AntibioticOrganismMappingRequestBuilder() {
        }

        public AntibioticOrganismMappingRequestBuilder antibioticId(UUID antibioticId) {
            this.antibioticId = antibioticId;
            return this;
        }

        public AntibioticOrganismMappingRequestBuilder microorganismId(UUID microorganismId) {
            this.microorganismId = microorganismId;
            return this;
        }

        public AntibioticOrganismMappingRequestBuilder susceptibility(String susceptibility) {
            this.susceptibility = susceptibility;
            return this;
        }

        public AntibioticOrganismMappingRequestBuilder isDefaultPanel(Boolean isDefaultPanel) {
            this.isDefaultPanel = isDefaultPanel;
            return this;
        }

        public AntibioticOrganismMappingRequest build() {
            return new AntibioticOrganismMappingRequest(this.antibioticId, this.microorganismId, this.susceptibility, this.isDefaultPanel);
        }
    }

}
