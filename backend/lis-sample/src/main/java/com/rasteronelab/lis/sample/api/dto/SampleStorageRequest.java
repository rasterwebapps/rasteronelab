package com.rasteronelab.lis.sample.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * Request to record storage location for an accepted sample.
 */
public class SampleStorageRequest {

    @NotBlank
    private String storageRack;

    @NotBlank
    private String storageShelf;

    @NotBlank
    private String storagePosition;

    private LocalDateTime disposalDate;

    public String getStorageRack() { return storageRack; }
    public void setStorageRack(String storageRack) { this.storageRack = storageRack; }

    public String getStorageShelf() { return storageShelf; }
    public void setStorageShelf(String storageShelf) { this.storageShelf = storageShelf; }

    public String getStoragePosition() { return storagePosition; }
    public void setStoragePosition(String storagePosition) { this.storagePosition = storagePosition; }

    public LocalDateTime getDisposalDate() { return disposalDate; }
    public void setDisposalDate(LocalDateTime disposalDate) { this.disposalDate = disposalDate; }
}
