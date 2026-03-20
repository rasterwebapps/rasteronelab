package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Number Series entity (L3 branch-level).
 * Defines auto-numbering sequences for entities like UHID, ORDER, SAMPLE, INVOICE, RECEIPT.
 */
@Entity
@Table(name = "number_series")
public class NumberSeries extends BaseEntity {

    @Column(name = "entity_type", nullable = false, length = 30)
    private String entityType;

    @Column(name = "prefix", nullable = false, length = 20)
    private String prefix;

    @Column(name = "suffix", length = 20)
    private String suffix;

    @Column(name = "current_number", nullable = false)
    private Long currentNumber = 0L;

    @Column(name = "padding_length", nullable = false)
    private Integer paddingLength = 6;

    @Column(name = "format_pattern", length = 100)
    private String formatPattern;

    @Column(name = "reset_frequency", length = 20)
    private String resetFrequency = "YEARLY";

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public String getEntityType() {
        return this.entityType;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public Long getCurrentNumber() {
        return this.currentNumber;
    }

    public Integer getPaddingLength() {
        return this.paddingLength;
    }

    public String getFormatPattern() {
        return this.formatPattern;
    }

    public String getResetFrequency() {
        return this.resetFrequency;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setCurrentNumber(Long currentNumber) {
        this.currentNumber = currentNumber;
    }

    public void setPaddingLength(Integer paddingLength) {
        this.paddingLength = paddingLength;
    }

    public void setFormatPattern(String formatPattern) {
        this.formatPattern = formatPattern;
    }

    public void setResetFrequency(String resetFrequency) {
        this.resetFrequency = resetFrequency;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
