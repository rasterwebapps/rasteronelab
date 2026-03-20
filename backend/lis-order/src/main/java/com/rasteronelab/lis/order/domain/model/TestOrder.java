package com.rasteronelab.lis.order.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * TestOrder entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Represents a lab test order placed for a patient.
 */
@Entity
@Table(name = "test_order")
public class TestOrder extends BaseEntity {

    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "visit_id")
    private UUID visitId;

    @Column(name = "referring_doctor_id")
    private UUID referringDoctorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority = Priority.ROUTINE;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.DRAFT;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "clinical_history")
    private String clinicalHistory;

    @Column(name = "special_instructions")
    private String specialInstructions;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "estimated_completion_time")
    private LocalDateTime estimatedCompletionTime;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> lineItems = new ArrayList<>();

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public UUID getPatientId() {
        return this.patientId;
    }

    public UUID getVisitId() {
        return this.visitId;
    }

    public UUID getReferringDoctorId() {
        return this.referringDoctorId;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public LocalDateTime getOrderDate() {
        return this.orderDate;
    }

    public String getClinicalHistory() {
        return this.clinicalHistory;
    }

    public String getSpecialInstructions() {
        return this.specialInstructions;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public LocalDateTime getEstimatedCompletionTime() {
        return this.estimatedCompletionTime;
    }

    public LocalDateTime getCompletedAt() {
        return this.completedAt;
    }

    public LocalDateTime getCancelledAt() {
        return this.cancelledAt;
    }

    public String getCancelReason() {
        return this.cancelReason;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public List<OrderLineItem> getLineItems() {
        return this.lineItems;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public void setVisitId(UUID visitId) {
        this.visitId = visitId;
    }

    public void setReferringDoctorId(UUID referringDoctorId) {
        this.referringDoctorId = referringDoctorId;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public void setClinicalHistory(String clinicalHistory) {
        this.clinicalHistory = clinicalHistory;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setEstimatedCompletionTime(LocalDateTime estimatedCompletionTime) {
        this.estimatedCompletionTime = estimatedCompletionTime;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setLineItems(List<OrderLineItem> lineItems) {
        this.lineItems = lineItems;
    }

}
