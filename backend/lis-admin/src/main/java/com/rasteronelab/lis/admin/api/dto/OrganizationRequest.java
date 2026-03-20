package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating an Organization.
 */
public class OrganizationRequest {

    @NotBlank(message = "Organization name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @NotBlank(message = "Organization code is required")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String code;

    @Size(max = 255, message = "Display name must not exceed 255 characters")
    private String displayName;

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

    @Size(max = 255, message = "Website must not exceed 255 characters")
    private String website;

    @Size(max = 500, message = "Logo URL must not exceed 500 characters")
    private String logoUrl;

    @Size(max = 50, message = "GST number must not exceed 50 characters")
    private String gstNumber;

    @Size(max = 50, message = "PAN number must not exceed 50 characters")
    private String panNumber;

    @Size(max = 100, message = "License number must not exceed 100 characters")
    private String licenseNumber;

    private Boolean isActive;

    public OrganizationRequest() {
    }

    public OrganizationRequest(String name, String code, String displayName, String addressLine1, String addressLine2, String city, String state, String country, String pincode, String phone, String email, String website, String logoUrl, String gstNumber, String panNumber, String licenseNumber, Boolean isActive) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationRequest that = (OrganizationRequest) o;
        return java.util.Objects.equals(this.name, that.name) &&
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
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.name, this.code, this.displayName, this.addressLine1, this.addressLine2, this.city, this.state, this.country, this.pincode, this.phone, this.email, this.website, this.logoUrl, this.gstNumber, this.panNumber, this.licenseNumber, this.isActive);
    }

    @Override
    public String toString() {
        return "OrganizationRequest{name=" + name +
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
               "}";
    }

    public static OrganizationRequestBuilder builder() {
        return new OrganizationRequestBuilder();
    }

    public static class OrganizationRequestBuilder {
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

        OrganizationRequestBuilder() {
        }

        public OrganizationRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public OrganizationRequestBuilder code(String code) {
            this.code = code;
            return this;
        }

        public OrganizationRequestBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public OrganizationRequestBuilder addressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
            return this;
        }

        public OrganizationRequestBuilder addressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
            return this;
        }

        public OrganizationRequestBuilder city(String city) {
            this.city = city;
            return this;
        }

        public OrganizationRequestBuilder state(String state) {
            this.state = state;
            return this;
        }

        public OrganizationRequestBuilder country(String country) {
            this.country = country;
            return this;
        }

        public OrganizationRequestBuilder pincode(String pincode) {
            this.pincode = pincode;
            return this;
        }

        public OrganizationRequestBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public OrganizationRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        public OrganizationRequestBuilder website(String website) {
            this.website = website;
            return this;
        }

        public OrganizationRequestBuilder logoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }

        public OrganizationRequestBuilder gstNumber(String gstNumber) {
            this.gstNumber = gstNumber;
            return this;
        }

        public OrganizationRequestBuilder panNumber(String panNumber) {
            this.panNumber = panNumber;
            return this;
        }

        public OrganizationRequestBuilder licenseNumber(String licenseNumber) {
            this.licenseNumber = licenseNumber;
            return this;
        }

        public OrganizationRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public OrganizationRequest build() {
            return new OrganizationRequest(this.name, this.code, this.displayName, this.addressLine1, this.addressLine2, this.city, this.state, this.country, this.pincode, this.phone, this.email, this.website, this.logoUrl, this.gstNumber, this.panNumber, this.licenseNumber, this.isActive);
        }
    }

}
