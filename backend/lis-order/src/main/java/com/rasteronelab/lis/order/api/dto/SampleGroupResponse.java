package com.rasteronelab.lis.order.api.dto;

import java.util.List;
import java.util.Objects;

/**
 * Response DTO that groups tests by sample type and tube type for efficient sample collection.
 * Each group represents a single tube/container to be collected from the patient.
 */
public class SampleGroupResponse {

    private String sampleType;
    private String tubeType;
    private List<String> testCodes;
    private List<String> testNames;
    private String priority;

    public SampleGroupResponse() {
    }

    public SampleGroupResponse(String sampleType, String tubeType, List<String> testCodes,
                               List<String> testNames, String priority) {
        this.sampleType = sampleType;
        this.tubeType = tubeType;
        this.testCodes = testCodes;
        this.testNames = testNames;
        this.priority = priority;
    }

    public String getSampleType() {
        return this.sampleType;
    }

    public String getTubeType() {
        return this.tubeType;
    }

    public List<String> getTestCodes() {
        return this.testCodes;
    }

    public List<String> getTestNames() {
        return this.testNames;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public void setTubeType(String tubeType) {
        this.tubeType = tubeType;
    }

    public void setTestCodes(List<String> testCodes) {
        this.testCodes = testCodes;
    }

    public void setTestNames(List<String> testNames) {
        this.testNames = testNames;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleGroupResponse that = (SampleGroupResponse) o;
        return Objects.equals(this.sampleType, that.sampleType) &&
               Objects.equals(this.tubeType, that.tubeType) &&
               Objects.equals(this.testCodes, that.testCodes) &&
               Objects.equals(this.testNames, that.testNames) &&
               Objects.equals(this.priority, that.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.sampleType, this.tubeType, this.testCodes, this.testNames, this.priority);
    }

    @Override
    public String toString() {
        return "SampleGroupResponse{sampleType=" + sampleType +
               ", tubeType=" + tubeType +
               ", testCodes=" + testCodes +
               ", testNames=" + testNames +
               ", priority=" + priority +
               "}";
    }

    public static SampleGroupResponseBuilder builder() {
        return new SampleGroupResponseBuilder();
    }

    public static class SampleGroupResponseBuilder {
        private String sampleType;
        private String tubeType;
        private List<String> testCodes;
        private List<String> testNames;
        private String priority;

        SampleGroupResponseBuilder() {
        }

        public SampleGroupResponseBuilder sampleType(String sampleType) {
            this.sampleType = sampleType;
            return this;
        }

        public SampleGroupResponseBuilder tubeType(String tubeType) {
            this.tubeType = tubeType;
            return this;
        }

        public SampleGroupResponseBuilder testCodes(List<String> testCodes) {
            this.testCodes = testCodes;
            return this;
        }

        public SampleGroupResponseBuilder testNames(List<String> testNames) {
            this.testNames = testNames;
            return this;
        }

        public SampleGroupResponseBuilder priority(String priority) {
            this.priority = priority;
            return this;
        }

        public SampleGroupResponse build() {
            return new SampleGroupResponse(this.sampleType, this.tubeType, this.testCodes,
                    this.testNames, this.priority);
        }
    }

}
