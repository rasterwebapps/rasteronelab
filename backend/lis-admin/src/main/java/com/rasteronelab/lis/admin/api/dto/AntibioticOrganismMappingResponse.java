package com.rasteronelab.lis.admin.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for AntibioticOrganismMapping entity.
 * Includes antibioticName and microorganismName for display.
 */
public class AntibioticOrganismMappingResponse {

    private UUID id;
    private UUID branchId;
    private UUID antibioticId;
    private String antibioticName;
    private UUID microorganismId;
    private String microorganismName;
    private String susceptibility;
    private Boolean isDefaultPanel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public AntibioticOrganismMappingResponse() {
    }

    public AntibioticOrganismMappingResponse(UUID id, UUID branchId, UUID antibioticId, String antibioticName, UUID microorganismId, String microorganismName, String susceptibility, Boolean isDefaultPanel, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.antibioticId = antibioticId;
        this.antibioticName = antibioticName;
        this.microorganismId = microorganismId;
        this.microorganismName = microorganismName;
        this.susceptibility = susceptibility;
        this.isDefaultPanel = isDefaultPanel;
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

    public UUID getAntibioticId() {
        return this.antibioticId;
    }

    public String getAntibioticName() {
        return this.antibioticName;
    }

    public UUID getMicroorganismId() {
        return this.microorganismId;
    }

    public String getMicroorganismName() {
        return this.microorganismName;
    }

    public String getSusceptibility() {
        return this.susceptibility;
    }

    public Boolean getIsDefaultPanel() {
        return this.isDefaultPanel;
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

    public void setAntibioticId(UUID antibioticId) {
        this.antibioticId = antibioticId;
    }

    public void setAntibioticName(String antibioticName) {
        this.antibioticName = antibioticName;
    }

    public void setMicroorganismId(UUID microorganismId) {
        this.microorganismId = microorganismId;
    }

    public void setMicroorganismName(String microorganismName) {
        this.microorganismName = microorganismName;
    }

    public void setSusceptibility(String susceptibility) {
        this.susceptibility = susceptibility;
    }

    public void setIsDefaultPanel(Boolean isDefaultPanel) {
        this.isDefaultPanel = isDefaultPanel;
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
        AntibioticOrganismMappingResponse that = (AntibioticOrganismMappingResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.antibioticId, that.antibioticId) &&
               java.util.Objects.equals(this.antibioticName, that.antibioticName) &&
               java.util.Objects.equals(this.microorganismId, that.microorganismId) &&
               java.util.Objects.equals(this.microorganismName, that.microorganismName) &&
               java.util.Objects.equals(this.susceptibility, that.susceptibility) &&
               java.util.Objects.equals(this.isDefaultPanel, that.isDefaultPanel) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.antibioticId, this.antibioticName, this.microorganismId, this.microorganismName, this.susceptibility, this.isDefaultPanel, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "AntibioticOrganismMappingResponse{id=" + id +
               ", branchId=" + branchId +
               ", antibioticId=" + antibioticId +
               ", antibioticName=" + antibioticName +
               ", microorganismId=" + microorganismId +
               ", microorganismName=" + microorganismName +
               ", susceptibility=" + susceptibility +
               ", isDefaultPanel=" + isDefaultPanel +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static AntibioticOrganismMappingResponseBuilder builder() {
        return new AntibioticOrganismMappingResponseBuilder();
    }

    public static class AntibioticOrganismMappingResponseBuilder {
        private UUID id;
        private UUID branchId;
        private UUID antibioticId;
        private String antibioticName;
        private UUID microorganismId;
        private String microorganismName;
        private String susceptibility;
        private Boolean isDefaultPanel;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        AntibioticOrganismMappingResponseBuilder() {
        }

        public AntibioticOrganismMappingResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public AntibioticOrganismMappingResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public AntibioticOrganismMappingResponseBuilder antibioticId(UUID antibioticId) {
            this.antibioticId = antibioticId;
            return this;
        }

        public AntibioticOrganismMappingResponseBuilder antibioticName(String antibioticName) {
            this.antibioticName = antibioticName;
            return this;
        }

        public AntibioticOrganismMappingResponseBuilder microorganismId(UUID microorganismId) {
            this.microorganismId = microorganismId;
            return this;
        }

        public AntibioticOrganismMappingResponseBuilder microorganismName(String microorganismName) {
            this.microorganismName = microorganismName;
            return this;
        }

        public AntibioticOrganismMappingResponseBuilder susceptibility(String susceptibility) {
            this.susceptibility = susceptibility;
            return this;
        }

        public AntibioticOrganismMappingResponseBuilder isDefaultPanel(Boolean isDefaultPanel) {
            this.isDefaultPanel = isDefaultPanel;
            return this;
        }

        public AntibioticOrganismMappingResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AntibioticOrganismMappingResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public AntibioticOrganismMappingResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public AntibioticOrganismMappingResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public AntibioticOrganismMappingResponse build() {
            return new AntibioticOrganismMappingResponse(this.id, this.branchId, this.antibioticId, this.antibioticName, this.microorganismId, this.microorganismName, this.susceptibility, this.isDefaultPanel, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
