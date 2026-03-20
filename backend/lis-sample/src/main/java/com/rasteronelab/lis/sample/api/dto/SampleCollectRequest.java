package com.rasteronelab.lis.sample.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Request to collect one or more tubes for a given order.
 */
public class SampleCollectRequest {

    @NotNull
    private UUID orderId;

    @NotNull
    private UUID patientId;

    @NotEmpty
    @Valid
    private List<SampleTubeRequest> tubes;

    private Boolean homeCollection = false;

    private String notes;

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public UUID getPatientId() { return patientId; }
    public void setPatientId(UUID patientId) { this.patientId = patientId; }

    public List<SampleTubeRequest> getTubes() { return tubes; }
    public void setTubes(List<SampleTubeRequest> tubes) { this.tubes = tubes; }

    public Boolean getHomeCollection() { return homeCollection; }
    public void setHomeCollection(Boolean homeCollection) { this.homeCollection = homeCollection; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
