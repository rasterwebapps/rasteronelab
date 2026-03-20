package com.rasteronelab.lis.admin.api.dto;

import com.rasteronelab.lis.admin.domain.model.BranchType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Branch entity.
 * Includes all entity fields plus organizationName for display.
 */
public class BranchResponse {

    private UUID id;
    private UUID orgId;
    private String organizationName;
    private String name;
    private String code;
    private String displayName;
    private BranchType branchType;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String phone;
    private String email;
    private String nablNumber;
    private String licenseNumber;
    private String headerText;
    private String footerText;
    private String logoUrl;
    private String reportHeaderUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public BranchResponse() {
    }

    public BranchResponse(UUID id, UUID orgId, String organizationName, String name, String code, String displayName, BranchType branchType, String addressLine1, String addressLine2, String city, String state, String country, String pincode, String phone, String email, String nablNumber, String licenseNumber, String headerText, String footerText, String logoUrl, String reportHeaderUrl, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.orgId = orgId;
        this.organizationName = organizationName;
        this.name = name;
        this.code = code;
        this.displayName = displayName;
        this.branchType = branchType;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
        this.phone = phone;
        this.email = email;
        this.nablNumber = nablNumber;
        this.licenseNumber = licenseNumber;
        this.headerText = headerText;
        this.footerText = footerText;
        this.logoUrl = logoUrl;
        this.reportHeaderUrl = reportHeaderUrl;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getOrgId() {
        return this.orgId;
    }

    public String getOrganizationName() {
        return this.organizationName;
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

    public void setId(UUID id) {
        this.id = id;
    }

    public void setOrgId(UUID orgId) {
        this.orgId = orgId;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BranchResponse that = (BranchResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.orgId, that.orgId) &&
               java.util.Objects.equals(this.organizationName, that.organizationName) &&
               java.util.Objects.equals(this.name, that.name) &&
               java.util.Objects.equals(this.code, that.code) &&
               java.util.Objects.equals(this.displayName, that.displayName) &&
               java.util.Objects.equals(this.branchType, that.branchType) &&
               java.util.Objects.equals(this.addressLine1, that.addressLine1) &&
               java.util.Objects.equals(this.addressLine2, that.addressLine2) &&
               java.util.Objects.equals(this.city, that.city) &&
               java.util.Objects.equals(this.state, that.state) &&
               java.util.Objects.equals(this.country, that.country) &&
               java.util.Objects.equals(this.pincode, that.pincode) &&
               java.util.Objects.equals(this.phone, that.phone) &&
               java.util.Objects.equals(this.email, that.email) &&
               java.util.Objects.equals(this.nablNumber, that.nablNumber) &&
               java.util.Objects.equals(this.licenseNumber, that.licenseNumber) &&
               java.util.Objects.equals(this.headerText, that.headerText) &&
               java.util.Objects.equals(this.footerText, that.footerText) &&
               java.util.Objects.equals(this.logoUrl, that.logoUrl) &&
               java.util.Objects.equals(this.reportHeaderUrl, that.reportHeaderUrl) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.orgId, this.organizationName, this.name, this.code, this.displayName, this.branchType, this.addressLine1, this.addressLine2, this.city, this.state, this.country, this.pincode, this.phone, this.email, this.nablNumber, this.licenseNumber, this.headerText, this.footerText, this.logoUrl, this.reportHeaderUrl, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "BranchResponse{id=" + id +
               ", orgId=" + orgId +
               ", organizationName=" + organizationName +
               ", name=" + name +
               ", code=" + code +
               ", displayName=" + displayName +
               ", branchType=" + branchType +
               ", addressLine1=" + addressLine1 +
               ", addressLine2=" + addressLine2 +
               ", city=" + city +
               ", state=" + state +
               ", country=" + country +
               ", pincode=" + pincode +
               ", phone=" + phone +
               ", email=" + email +
               ", nablNumber=" + nablNumber +
               ", licenseNumber=" + licenseNumber +
               ", headerText=" + headerText +
               ", footerText=" + footerText +
               ", logoUrl=" + logoUrl +
               ", reportHeaderUrl=" + reportHeaderUrl +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static BranchResponseBuilder builder() {
        return new BranchResponseBuilder();
    }

    public static class BranchResponseBuilder {
        private UUID id;
        private UUID orgId;
        private String organizationName;
        private String name;
        private String code;
        private String displayName;
        private BranchType branchType;
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String state;
        private String country;
        private String pincode;
        private String phone;
        private String email;
        private String nablNumber;
        private String licenseNumber;
        private String headerText;
        private String footerText;
        private String logoUrl;
        private String reportHeaderUrl;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        BranchResponseBuilder() {
        }

        public BranchResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public BranchResponseBuilder orgId(UUID orgId) {
            this.orgId = orgId;
            return this;
        }

        public BranchResponseBuilder organizationName(String organizationName) {
            this.organizationName = organizationName;
            return this;
        }

        public BranchResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public BranchResponseBuilder code(String code) {
            this.code = code;
            return this;
        }

        public BranchResponseBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public BranchResponseBuilder branchType(BranchType branchType) {
            this.branchType = branchType;
            return this;
        }

        public BranchResponseBuilder addressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
            return this;
        }

        public BranchResponseBuilder addressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
            return this;
        }

        public BranchResponseBuilder city(String city) {
            this.city = city;
            return this;
        }

        public BranchResponseBuilder state(String state) {
            this.state = state;
            return this;
        }

        public BranchResponseBuilder country(String country) {
            this.country = country;
            return this;
        }

        public BranchResponseBuilder pincode(String pincode) {
            this.pincode = pincode;
            return this;
        }

        public BranchResponseBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public BranchResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public BranchResponseBuilder nablNumber(String nablNumber) {
            this.nablNumber = nablNumber;
            return this;
        }

        public BranchResponseBuilder licenseNumber(String licenseNumber) {
            this.licenseNumber = licenseNumber;
            return this;
        }

        public BranchResponseBuilder headerText(String headerText) {
            this.headerText = headerText;
            return this;
        }

        public BranchResponseBuilder footerText(String footerText) {
            this.footerText = footerText;
            return this;
        }

        public BranchResponseBuilder logoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }

        public BranchResponseBuilder reportHeaderUrl(String reportHeaderUrl) {
            this.reportHeaderUrl = reportHeaderUrl;
            return this;
        }

        public BranchResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public BranchResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public BranchResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public BranchResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public BranchResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public BranchResponse build() {
            return new BranchResponse(this.id, this.orgId, this.organizationName, this.name, this.code, this.displayName, this.branchType, this.addressLine1, this.addressLine2, this.city, this.state, this.country, this.pincode, this.phone, this.email, this.nablNumber, this.licenseNumber, this.headerText, this.footerText, this.logoUrl, this.reportHeaderUrl, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
