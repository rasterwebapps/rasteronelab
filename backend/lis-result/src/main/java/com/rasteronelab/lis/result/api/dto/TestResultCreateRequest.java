package com.rasteronelab.lis.result.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class TestResultCreateRequest {

    @NotNull(message = "Order ID is required")
    private UUID orderId;

    @NotNull(message = "Order line item ID is required")
    private UUID orderLineItemId;

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotNull(message = "Test ID is required")
    private UUID testId;

    private String testCode;

    private String testName;

    private UUID departmentId;

    private String departmentName;

    private UUID sampleId;

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getOrderLineItemId() {
        return orderLineItemId;
    }

    public void setOrderLineItemId(UUID orderLineItemId) {
        this.orderLineItemId = orderLineItemId;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public UUID getTestId() {
        return testId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public UUID getSampleId() {
        return sampleId;
    }

    public void setSampleId(UUID sampleId) {
        this.sampleId = sampleId;
    }
}
