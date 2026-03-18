package com.rasteronelab.lis.admin.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Microorganism entity.
 */
public class MicroorganismResponse {

    private UUID id;
    private UUID branchId;
    private String name;
    private String code;
    private String gramType;
    private String organismType;
    private String clinicalSignificance;
    private String colonyMorphology;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public MicroorganismResponse() {
    }

    public MicroorganismResponse(UUID id, UUID branchId, String name, String code, String gramType, String organismType, String clinicalSignificance, String colonyMorphology, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.name = name;
        this.code = code;
        this.gramType = gramType;
        this.organismType = organismType;
        this.clinicalSignificance = clinicalSignificance;
        this.colonyMorphology = colonyMorphology;
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
        MicroorganismResponse that = (MicroorganismResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.name, that.name) &&
               java.util.Objects.equals(this.code, that.code) &&
               java.util.Objects.equals(this.gramType, that.gramType) &&
               java.util.Objects.equals(this.organismType, that.organismType) &&
               java.util.Objects.equals(this.clinicalSignificance, that.clinicalSignificance) &&
               java.util.Objects.equals(this.colonyMorphology, that.colonyMorphology) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.name, this.code, this.gramType, this.organismType, this.clinicalSignificance, this.colonyMorphology, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "MicroorganismResponse{id=" + id +
               ", branchId=" + branchId +
               ", name=" + name +
               ", code=" + code +
               ", gramType=" + gramType +
               ", organismType=" + organismType +
               ", clinicalSignificance=" + clinicalSignificance +
               ", colonyMorphology=" + colonyMorphology +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static MicroorganismResponseBuilder builder() {
        return new MicroorganismResponseBuilder();
    }

    public static class MicroorganismResponseBuilder {
        private UUID id;
        private UUID branchId;
        private String name;
        private String code;
        private String gramType;
        private String organismType;
        private String clinicalSignificance;
        private String colonyMorphology;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        MicroorganismResponseBuilder() {
        }

        public MicroorganismResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public MicroorganismResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public MicroorganismResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MicroorganismResponseBuilder code(String code) {
            this.code = code;
            return this;
        }

        public MicroorganismResponseBuilder gramType(String gramType) {
            this.gramType = gramType;
            return this;
        }

        public MicroorganismResponseBuilder organismType(String organismType) {
            this.organismType = organismType;
            return this;
        }

        public MicroorganismResponseBuilder clinicalSignificance(String clinicalSignificance) {
            this.clinicalSignificance = clinicalSignificance;
            return this;
        }

        public MicroorganismResponseBuilder colonyMorphology(String colonyMorphology) {
            this.colonyMorphology = colonyMorphology;
            return this;
        }

        public MicroorganismResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public MicroorganismResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public MicroorganismResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public MicroorganismResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public MicroorganismResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public MicroorganismResponse build() {
            return new MicroorganismResponse(this.id, this.branchId, this.name, this.code, this.gramType, this.organismType, this.clinicalSignificance, this.colonyMorphology, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
