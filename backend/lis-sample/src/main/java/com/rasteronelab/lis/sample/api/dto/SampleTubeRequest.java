package com.rasteronelab.lis.sample.api.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for a single tube entry in a sample collection request.
 */
public class SampleTubeRequest {

    @NotNull
    private String tubeType;

    private String sampleType;

    private String collectionSite;

    @NotNull
    private UUID collectedBy;

    @NotNull
    private LocalDateTime collectedAt;

    private BigDecimal quantity;

    private String unit;

    public String getTubeType() { return tubeType; }
    public void setTubeType(String tubeType) { this.tubeType = tubeType; }

    public String getSampleType() { return sampleType; }
    public void setSampleType(String sampleType) { this.sampleType = sampleType; }

    public String getCollectionSite() { return collectionSite; }
    public void setCollectionSite(String collectionSite) { this.collectionSite = collectionSite; }

    public UUID getCollectedBy() { return collectedBy; }
    public void setCollectedBy(UUID collectedBy) { this.collectedBy = collectedBy; }

    public LocalDateTime getCollectedAt() { return collectedAt; }
    public void setCollectedAt(LocalDateTime collectedAt) { this.collectedAt = collectedAt; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
