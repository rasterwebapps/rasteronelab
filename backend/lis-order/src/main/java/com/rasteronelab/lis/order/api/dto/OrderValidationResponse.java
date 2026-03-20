package com.rasteronelab.lis.order.api.dto;

import java.util.List;
import java.util.Objects;

/**
 * Response DTO for order validation results.
 * Contains validation status, any errors found, and sample grouping information.
 */
public class OrderValidationResponse {

    private boolean valid;
    private List<String> errors;
    private List<SampleGroupResponse> sampleGroups;

    public OrderValidationResponse() {
    }

    public OrderValidationResponse(boolean valid, List<String> errors, List<SampleGroupResponse> sampleGroups) {
        this.valid = valid;
        this.errors = errors;
        this.sampleGroups = sampleGroups;
    }

    public boolean isValid() {
        return this.valid;
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public List<SampleGroupResponse> getSampleGroups() {
        return this.sampleGroups;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void setSampleGroups(List<SampleGroupResponse> sampleGroups) {
        this.sampleGroups = sampleGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderValidationResponse that = (OrderValidationResponse) o;
        return this.valid == that.valid &&
               Objects.equals(this.errors, that.errors) &&
               Objects.equals(this.sampleGroups, that.sampleGroups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.valid, this.errors, this.sampleGroups);
    }

    @Override
    public String toString() {
        return "OrderValidationResponse{valid=" + valid +
               ", errors=" + errors +
               ", sampleGroups=" + sampleGroups +
               "}";
    }

    public static OrderValidationResponseBuilder builder() {
        return new OrderValidationResponseBuilder();
    }

    public static class OrderValidationResponseBuilder {
        private boolean valid;
        private List<String> errors;
        private List<SampleGroupResponse> sampleGroups;

        OrderValidationResponseBuilder() {
        }

        public OrderValidationResponseBuilder valid(boolean valid) {
            this.valid = valid;
            return this;
        }

        public OrderValidationResponseBuilder errors(List<String> errors) {
            this.errors = errors;
            return this;
        }

        public OrderValidationResponseBuilder sampleGroups(List<SampleGroupResponse> sampleGroups) {
            this.sampleGroups = sampleGroups;
            return this;
        }

        public OrderValidationResponse build() {
            return new OrderValidationResponse(this.valid, this.errors, this.sampleGroups);
        }
    }

}
