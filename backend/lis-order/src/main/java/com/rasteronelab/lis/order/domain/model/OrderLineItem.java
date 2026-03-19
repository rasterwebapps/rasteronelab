package com.rasteronelab.lis.order.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * OrderLineItem entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Represents an individual test within a TestOrder.
 */
@Entity
@Table(name = "order_line_item")
public class OrderLineItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private TestOrder order;

    @Column(name = "test_id", nullable = false)
    private UUID testId;

    @Column(name = "test_code", nullable = false)
    private String testCode;

    @Column(name = "test_name", nullable = false)
    private String testName;

    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "sample_type")
    private String sampleType;

    @Column(name = "tube_type")
    private String tubeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderLineItemStatus status = OrderLineItemStatus.PENDING;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name = "discount_amount", nullable = false)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "net_price", nullable = false)
    private BigDecimal netPrice = BigDecimal.ZERO;

    @Column(name = "is_urgent", nullable = false)
    private Boolean isUrgent = false;

    @Column(name = "is_outsourced", nullable = false)
    private Boolean isOutsourced = false;

    @Column(name = "outsource_lab")
    private String outsourceLab;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public TestOrder getOrder() {
        return this.order;
    }

    public UUID getTestId() {
        return this.testId;
    }

    public String getTestCode() {
        return this.testCode;
    }

    public String getTestName() {
        return this.testName;
    }

    public UUID getDepartmentId() {
        return this.departmentId;
    }

    public String getSampleType() {
        return this.sampleType;
    }

    public String getTubeType() {
        return this.tubeType;
    }

    public OrderLineItemStatus getStatus() {
        return this.status;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public BigDecimal getNetPrice() {
        return this.netPrice;
    }

    public Boolean getIsUrgent() {
        return this.isUrgent;
    }

    public Boolean getIsOutsourced() {
        return this.isOutsourced;
    }

    public String getOutsourceLab() {
        return this.outsourceLab;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setOrder(TestOrder order) {
        this.order = order;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public void setTubeType(String tubeType) {
        this.tubeType = tubeType;
    }

    public void setStatus(OrderLineItemStatus status) {
        this.status = status;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    public void setIsUrgent(Boolean isUrgent) {
        this.isUrgent = isUrgent;
    }

    public void setIsOutsourced(Boolean isOutsourced) {
        this.isOutsourced = isOutsourced;
    }

    public void setOutsourceLab(String outsourceLab) {
        this.outsourceLab = outsourceLab;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
