package com.rasteronelab.lis.result.api.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public class ResultAuthorizationRequest {

    @NotEmpty(message = "At least one result ID is required")
    private List<UUID> resultIds;

    private String comments;

    public List<UUID> getResultIds() {
        return resultIds;
    }

    public void setResultIds(List<UUID> resultIds) {
        this.resultIds = resultIds;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
