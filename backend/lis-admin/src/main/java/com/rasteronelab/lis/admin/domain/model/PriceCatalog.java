package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Price catalog entity (L3 branch-level).
 * Maps test or panel to pricing by rate list type.
 */
@Entity
@Table(name = "price_catalog")
public class PriceCatalog extends BaseEntity {

    @Column(name = "test_id", insertable = false, updatable = false)
    private UUID testId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    private TestMaster test;

    @Column(name = "panel_id", insertable = false, updatable = false)
    private UUID panelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "panel_id")
    private TestPanel panel;

    @Column(name = "rate_list_type", nullable = false)
    private String rateListType;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public UUID getTestId() {
        return this.testId;
    }

    public TestMaster getTest() {
        return this.test;
    }

    public UUID getPanelId() {
        return this.panelId;
    }

    public TestPanel getPanel() {
        return this.panel;
    }

    public String getRateListType() {
        return this.rateListType;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public LocalDate getEffectiveFrom() {
        return this.effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return this.effectiveTo;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public void setTest(TestMaster test) {
        this.test = test;
    }

    public void setPanelId(UUID panelId) {
        this.panelId = panelId;
    }

    public void setPanel(TestPanel panel) {
        this.panel = panel;
    }

    public void setRateListType(String rateListType) {
        this.rateListType = rateListType;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
