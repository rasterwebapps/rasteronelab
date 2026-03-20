package com.rasteronelab.lis.admin.domain.model;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Panel-Test mapping entity.
 * Links TestPanel to TestMaster with ordering.
 * Does NOT extend BaseEntity — it is a mapping table with its own audit fields.
 */
@Entity
@Table(name = "panel_test")
@EntityListeners(AuditingEntityListener.class)
public class PanelTest {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id = UuidCreator.getTimeOrderedEpoch();

    @Column(name = "panel_id", nullable = false, insertable = false, updatable = false)
    private UUID panelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "panel_id", nullable = false)
    private TestPanel testPanel;

    @Column(name = "test_id", nullable = false, insertable = false, updatable = false)
    private UUID testId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    private TestMaster testMaster;

    @Column(name = "display_order")
    private Integer displayOrder;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void softDelete(String deletedBy) {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.updatedBy = deletedBy;
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getPanelId() {
        return this.panelId;
    }

    public TestPanel getTestPanel() {
        return this.testPanel;
    }

    public UUID getTestId() {
        return this.testId;
    }

    public TestMaster getTestMaster() {
        return this.testMaster;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public LocalDateTime getDeletedAt() {
        return this.deletedAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setPanelId(UUID panelId) {
        this.panelId = panelId;
    }

    public void setTestPanel(TestPanel testPanel) {
        this.testPanel = testPanel;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public void setTestMaster(TestMaster testMaster) {
        this.testMaster = testMaster;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

}
