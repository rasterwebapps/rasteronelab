package com.rasteronelab.lis.admin.domain.model;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * Branch entity (L2 org-level).
 * Does NOT extend BaseEntity because Branch IS the tenant entity itself —
 * its id becomes the branch_id used across all other entities.
 * Manages its own audit fields and UUID v7 primary key.
 */
@Entity
@Table(name = "branch")
@EntityListeners(AuditingEntityListener.class)
public class Branch {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id = UuidCreator.getTimeOrderedEpoch();

    // Read-only mapping of the FK column — value is managed by the organization relationship
    @Column(name = "org_id", nullable = false, insertable = false, updatable = false)
    private UUID orgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "display_name")
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "branch_type", nullable = false)
    private BranchType branchType = BranchType.LAB;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "nabl_number")
    private String nablNumber;

    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "header_text")
    private String headerText;

    @Column(name = "footer_text")
    private String footerText;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "report_header_url")
    private String reportHeaderUrl;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

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

    public UUID getOrgId() {
        return this.orgId;
    }

    public Organization getOrganization() {
        return this.organization;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public BranchType getBranchType() {
        return this.branchType;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public String getAddressLine2() {
        return this.addressLine2;
    }

    public String getCity() {
        return this.city;
    }

    public String getState() {
        return this.state;
    }

    public String getCountry() {
        return this.country;
    }

    public String getPincode() {
        return this.pincode;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getEmail() {
        return this.email;
    }

    public String getNablNumber() {
        return this.nablNumber;
    }

    public String getLicenseNumber() {
        return this.licenseNumber;
    }

    public String getHeaderText() {
        return this.headerText;
    }

    public String getFooterText() {
        return this.footerText;
    }

    public String getLogoUrl() {
        return this.logoUrl;
    }

    public String getReportHeaderUrl() {
        return this.reportHeaderUrl;
    }

    public Boolean getIsActive() {
        return this.isActive;
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

    public void setOrgId(UUID orgId) {
        this.orgId = orgId;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setBranchType(BranchType branchType) {
        this.branchType = branchType;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNablNumber(String nablNumber) {
        this.nablNumber = nablNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void setReportHeaderUrl(String reportHeaderUrl) {
        this.reportHeaderUrl = reportHeaderUrl;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
