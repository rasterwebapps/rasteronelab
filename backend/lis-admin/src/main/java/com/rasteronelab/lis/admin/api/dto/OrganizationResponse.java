package com.rasteronelab.lis.admin.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Organization entity.
 * Includes all entity fields plus audit fields for display.
 */
public class OrganizationResponse {

    private UUID id;
    private String name;
    private String code;
    private String displayName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String phone;
    private String email;
    private String website;
    private String logoUrl;
    private String gstNumber;
    private String panNumber;
    private String licenseNumber;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public OrganizationResponse() {
    }

    public OrganizationResponse(UUID id, String name, String code, String displayName, String addressLine1, String addressLine2, String city, String state, String country, String pincode, String phone, String email, String website, String logoUrl, String gstNumber, String panNumber, String licenseNumber, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.displayName = displayName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.logoUrl = logoUrl;
        this.gstNumber = gstNumber;
        this.panNumber = panNumber;
        this.licenseNumber = licenseNumber;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public UUID getId() {
        return this.id;
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

    public String getWebsite() {
        return this.website;
    }

    public String getLogoUrl() {
        return this.logoUrl;
    }

    public String getGstNumber() {
        return this.gstNumber;
    }

    public String getPanNumber() {
        return this.panNumber;
    }

    public String getLicenseNumber() {
        return this.licenseNumber;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
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
        OrganizationResponse that = (OrganizationResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.name, that.name) &&
               java.util.Objects.equals(this.code, that.code) &&
               java.util.Objects.equals(this.displayName, that.displayName) &&
               java.util.Objects.equals(this.addressLine1, that.addressLine1) &&
               java.util.Objects.equals(this.addressLine2, that.addressLine2) &&
               java.util.Objects.equals(this.city, that.city) &&
               java.util.Objects.equals(this.state, that.state) &&
               java.util.Objects.equals(this.country, that.country) &&
               java.util.Objects.equals(this.pincode, that.pincode) &&
               java.util.Objects.equals(this.phone, that.phone) &&
               java.util.Objects.equals(this.email, that.email) &&
               java.util.Objects.equals(this.website, that.website) &&
               java.util.Objects.equals(this.logoUrl, that.logoUrl) &&
               java.util.Objects.equals(this.gstNumber, that.gstNumber) &&
               java.util.Objects.equals(this.panNumber, that.panNumber) &&
               java.util.Objects.equals(this.licenseNumber, that.licenseNumber) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.name, this.code, this.displayName, this.addressLine1, this.addressLine2, this.city, this.state, this.country, this.pincode, this.phone, this.email, this.website, this.logoUrl, this.gstNumber, this.panNumber, this.licenseNumber, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "OrganizationResponse{id=" + id +
               ", name=" + name +
               ", code=" + code +
               ", displayName=" + displayName +
               ", addressLine1=" + addressLine1 +
               ", addressLine2=" + addressLine2 +
               ", city=" + city +
               ", state=" + state +
               ", country=" + country +
               ", pincode=" + pincode +
               ", phone=" + phone +
               ", email=" + email +
               ", website=" + website +
               ", logoUrl=" + logoUrl +
               ", gstNumber=" + gstNumber +
               ", panNumber=" + panNumber +
               ", licenseNumber=" + licenseNumber +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static OrganizationResponseBuilder builder() {
        return new OrganizationResponseBuilder();
    }

    public static class OrganizationResponseBuilder {
        private UUID id;
        private String name;
        private String code;
        private String displayName;
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String state;
        private String country;
        private String pincode;
        private String phone;
        private String email;
        private String website;
        private String logoUrl;
        private String gstNumber;
        private String panNumber;
        private String licenseNumber;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        OrganizationResponseBuilder() {
        }

        public OrganizationResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public OrganizationResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public OrganizationResponseBuilder code(String code) {
            this.code = code;
            return this;
        }

        public OrganizationResponseBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public OrganizationResponseBuilder addressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
            return this;
        }

        public OrganizationResponseBuilder addressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
            return this;
        }

        public OrganizationResponseBuilder city(String city) {
            this.city = city;
            return this;
        }

        public OrganizationResponseBuilder state(String state) {
            this.state = state;
            return this;
        }

        public OrganizationResponseBuilder country(String country) {
            this.country = country;
            return this;
        }

        public OrganizationResponseBuilder pincode(String pincode) {
            this.pincode = pincode;
            return this;
        }

        public OrganizationResponseBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public OrganizationResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public OrganizationResponseBuilder website(String website) {
            this.website = website;
            return this;
        }

        public OrganizationResponseBuilder logoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }

        public OrganizationResponseBuilder gstNumber(String gstNumber) {
            this.gstNumber = gstNumber;
            return this;
        }

        public OrganizationResponseBuilder panNumber(String panNumber) {
            this.panNumber = panNumber;
            return this;
        }

        public OrganizationResponseBuilder licenseNumber(String licenseNumber) {
            this.licenseNumber = licenseNumber;
            return this;
        }

        public OrganizationResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public OrganizationResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public OrganizationResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public OrganizationResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public OrganizationResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public OrganizationResponse build() {
            return new OrganizationResponse(this.id, this.name, this.code, this.displayName, this.addressLine1, this.addressLine2, this.city, this.state, this.country, this.pincode, this.phone, this.email, this.website, this.logoUrl, this.gstNumber, this.panNumber, this.licenseNumber, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
