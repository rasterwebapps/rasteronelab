package com.rasteronelab.lis.admin.api.dto;

import com.rasteronelab.lis.admin.domain.model.BranchType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a Branch.
 */
public class BranchRequest {

    @NotNull(message = "Organization ID is required")
    private java.util.UUID orgId;

    @NotBlank(message = "Branch name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @NotBlank(message = "Branch code is required")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String code;

    @Size(max = 255, message = "Display name must not exceed 255 characters")
    private String displayName;

    private BranchType branchType;

    @Size(max = 255, message = "Address line 1 must not exceed 255 characters")
    private String addressLine1;

    @Size(max = 255, message = "Address line 2 must not exceed 255 characters")
    private String addressLine2;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    @Size(max = 20, message = "Pincode must not exceed 20 characters")
    private String pincode;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Email(message = "Email must be a valid email address")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @Size(max = 100, message = "NABL number must not exceed 100 characters")
    private String nablNumber;

    @Size(max = 100, message = "License number must not exceed 100 characters")
    private String licenseNumber;

    private String headerText;

    private String footerText;

    @Size(max = 500, message = "Logo URL must not exceed 500 characters")
    private String logoUrl;

    @Size(max = 500, message = "Report header URL must not exceed 500 characters")
    private String reportHeaderUrl;

    private Boolean isActive;

    public BranchRequest() {
    }

    public BranchRequest(java.util.UUID orgId, String name, String code, String displayName, BranchType branchType, String addressLine1, String addressLine2, String city, String state, String country, String pincode, String phone, String email, String nablNumber, String licenseNumber, String headerText, String footerText, String logoUrl, String reportHeaderUrl, Boolean isActive) {
        this.orgId = orgId;
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
    }

    public java.util.UUID getOrgId() {
        return this.orgId;
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

    public void setOrgId(java.util.UUID orgId) {
        this.orgId = orgId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BranchRequest that = (BranchRequest) o;
        return java.util.Objects.equals(this.orgId, that.orgId) &&
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
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.orgId, this.name, this.code, this.displayName, this.branchType, this.addressLine1, this.addressLine2, this.city, this.state, this.country, this.pincode, this.phone, this.email, this.nablNumber, this.licenseNumber, this.headerText, this.footerText, this.logoUrl, this.reportHeaderUrl, this.isActive);
    }

    @Override
    public String toString() {
        return "BranchRequest{orgId=" + orgId +
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
               "}";
    }

    public static BranchRequestBuilder builder() {
        return new BranchRequestBuilder();
    }

    public static class BranchRequestBuilder {
        private java.util.UUID orgId;
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

        BranchRequestBuilder() {
        }

        public BranchRequestBuilder orgId(java.util.UUID orgId) {
            this.orgId = orgId;
            return this;
        }

        public BranchRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public BranchRequestBuilder code(String code) {
            this.code = code;
            return this;
        }

        public BranchRequestBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public BranchRequestBuilder branchType(BranchType branchType) {
            this.branchType = branchType;
            return this;
        }

        public BranchRequestBuilder addressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
            return this;
        }

        public BranchRequestBuilder addressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
            return this;
        }

        public BranchRequestBuilder city(String city) {
            this.city = city;
            return this;
        }

        public BranchRequestBuilder state(String state) {
            this.state = state;
            return this;
        }

        public BranchRequestBuilder country(String country) {
            this.country = country;
            return this;
        }

        public BranchRequestBuilder pincode(String pincode) {
            this.pincode = pincode;
            return this;
        }

        public BranchRequestBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public BranchRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        public BranchRequestBuilder nablNumber(String nablNumber) {
            this.nablNumber = nablNumber;
            return this;
        }

        public BranchRequestBuilder licenseNumber(String licenseNumber) {
            this.licenseNumber = licenseNumber;
            return this;
        }

        public BranchRequestBuilder headerText(String headerText) {
            this.headerText = headerText;
            return this;
        }

        public BranchRequestBuilder footerText(String footerText) {
            this.footerText = footerText;
            return this;
        }

        public BranchRequestBuilder logoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }

        public BranchRequestBuilder reportHeaderUrl(String reportHeaderUrl) {
            this.reportHeaderUrl = reportHeaderUrl;
            return this;
        }

        public BranchRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public BranchRequest build() {
            return new BranchRequest(this.orgId, this.name, this.code, this.displayName, this.branchType, this.addressLine1, this.addressLine2, this.city, this.state, this.country, this.pincode, this.phone, this.email, this.nablNumber, this.licenseNumber, this.headerText, this.footerText, this.logoUrl, this.reportHeaderUrl, this.isActive);
        }
    }

}
