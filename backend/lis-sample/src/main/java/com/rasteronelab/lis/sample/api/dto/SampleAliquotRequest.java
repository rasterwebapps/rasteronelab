package com.rasteronelab.lis.sample.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * Request to aliquot a parent sample into multiple child samples.
 */
public class SampleAliquotRequest {

    @NotEmpty
    @Valid
    private List<AliquotRequest> aliquots;

    public List<AliquotRequest> getAliquots() { return aliquots; }
    public void setAliquots(List<AliquotRequest> aliquots) { this.aliquots = aliquots; }
}
