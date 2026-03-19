package com.rasteronelab.lis.sample.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request to reject a sample with a mandatory reason.
 */
public class SampleRejectRequest {

    @NotNull
    private UUID rejectedBy;

    @NotBlank
    private String rejectionReason;

    private String comments;

    private Boolean recollectionRequired = true;

    public UUID getRejectedBy() { return rejectedBy; }
    public void setRejectedBy(UUID rejectedBy) { this.rejectedBy = rejectedBy; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public Boolean getRecollectionRequired() { return recollectionRequired; }
    public void setRecollectionRequired(Boolean recollectionRequired) { this.recollectionRequired = recollectionRequired; }
}
