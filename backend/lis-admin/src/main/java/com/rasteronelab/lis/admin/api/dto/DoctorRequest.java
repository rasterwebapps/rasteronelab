package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Request DTO for creating or updating a Doctor.
 */
public class DoctorRequest {

    @NotBlank(message = "Doctor name is required")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;

    @Size(max = 30, message = "Code must not exceed 30 characters")
    private String code;

    @Size(max = 100, message = "Specialization must not exceed 100 characters")
    private String specialization;

    @Size(max = 200, message = "Qualification must not exceed 200 characters")
    private String qualification;

    @Size(max = 50, message = "Registration number must not exceed 50 characters")
    private String registrationNumber;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Email(message = "Email must be a valid email address")
    private String email;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    private BigDecimal referralCommissionPercent;

    private Boolean isActive;

    public DoctorRequest() {
    }

    public DoctorRequest(String name, String code, String specialization, String qualification, String registrationNumber, String phone, String email, String address, BigDecimal referralCommissionPercent, Boolean isActive) {
        this.name = name;
        this.code = code;
        this.specialization = specialization;
        this.qualification = qualification;
        this.registrationNumber = registrationNumber;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.referralCommissionPercent = referralCommissionPercent;
        this.isActive = isActive;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getSpecialization() {
        return this.specialization;
    }

    public String getQualification() {
        return this.qualification;
    }

    public String getRegistrationNumber() {
        return this.registrationNumber;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getEmail() {
        return this.email;
    }

    public String getAddress() {
        return this.address;
    }

    public BigDecimal getReferralCommissionPercent() {
        return this.referralCommissionPercent;
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

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setReferralCommissionPercent(BigDecimal referralCommissionPercent) {
        this.referralCommissionPercent = referralCommissionPercent;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorRequest that = (DoctorRequest) o;
        return java.util.Objects.equals(this.name, that.name) &&
               java.util.Objects.equals(this.code, that.code) &&
               java.util.Objects.equals(this.specialization, that.specialization) &&
               java.util.Objects.equals(this.qualification, that.qualification) &&
               java.util.Objects.equals(this.registrationNumber, that.registrationNumber) &&
               java.util.Objects.equals(this.phone, that.phone) &&
               java.util.Objects.equals(this.email, that.email) &&
               java.util.Objects.equals(this.address, that.address) &&
               java.util.Objects.equals(this.referralCommissionPercent, that.referralCommissionPercent) &&
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.name, this.code, this.specialization, this.qualification, this.registrationNumber, this.phone, this.email, this.address, this.referralCommissionPercent, this.isActive);
    }

    @Override
    public String toString() {
        return "DoctorRequest{name=" + name +
               ", code=" + code +
               ", specialization=" + specialization +
               ", qualification=" + qualification +
               ", registrationNumber=" + registrationNumber +
               ", phone=" + phone +
               ", email=" + email +
               ", address=" + address +
               ", referralCommissionPercent=" + referralCommissionPercent +
               ", isActive=" + isActive +
               "}";
    }

    public static DoctorRequestBuilder builder() {
        return new DoctorRequestBuilder();
    }

    public static class DoctorRequestBuilder {
        private String name;
        private String code;
        private String specialization;
        private String qualification;
        private String registrationNumber;
        private String phone;
        private String email;
        private String address;
        private BigDecimal referralCommissionPercent;
        private Boolean isActive;

        DoctorRequestBuilder() {
        }

        public DoctorRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DoctorRequestBuilder code(String code) {
            this.code = code;
            return this;
        }

        public DoctorRequestBuilder specialization(String specialization) {
            this.specialization = specialization;
            return this;
        }

        public DoctorRequestBuilder qualification(String qualification) {
            this.qualification = qualification;
            return this;
        }

        public DoctorRequestBuilder registrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }

        public DoctorRequestBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public DoctorRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        public DoctorRequestBuilder address(String address) {
            this.address = address;
            return this;
        }

        public DoctorRequestBuilder referralCommissionPercent(BigDecimal referralCommissionPercent) {
            this.referralCommissionPercent = referralCommissionPercent;
            return this;
        }

        public DoctorRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public DoctorRequest build() {
            return new DoctorRequest(this.name, this.code, this.specialization, this.qualification, this.registrationNumber, this.phone, this.email, this.address, this.referralCommissionPercent, this.isActive);
        }
    }

}
