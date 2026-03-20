package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DiscountScheme entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Represents a discount scheme applicable to walk-in, corporate, or insurance customers.
 */
@Entity
@Table(name = "discount_scheme")
public class DiscountScheme extends BaseEntity {

    @Column(name = "scheme_code", nullable = false)
    private String schemeCode;

    @Column(name = "scheme_name", nullable = false)
    private String schemeName;

    @Column(name = "applicable_to", nullable = false)
    private String applicableTo = "WALK_IN";

    @Column(name = "discount_type", nullable = false)
    private String discountType = "PERCENTAGE";

    @Column(name = "discount_value", nullable = false)
    private BigDecimal discountValue;

    @Column(name = "min_transaction_amount")
    private BigDecimal minTransactionAmount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public String getSchemeCode() {
        return this.schemeCode;
    }

    public String getSchemeName() {
        return this.schemeName;
    }

    public String getApplicableTo() {
        return this.applicableTo;
    }

    public String getDiscountType() {
        return this.discountType;
    }

    public BigDecimal getDiscountValue() {
        return this.discountValue;
    }

    public BigDecimal getMinTransactionAmount() {
        return this.minTransactionAmount;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public void setApplicableTo(String applicableTo) {
        this.applicableTo = applicableTo;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public void setMinTransactionAmount(BigDecimal minTransactionAmount) {
        this.minTransactionAmount = minTransactionAmount;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
