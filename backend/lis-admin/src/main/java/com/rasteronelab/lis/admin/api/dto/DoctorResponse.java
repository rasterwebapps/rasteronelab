package com.rasteronelab.lis.admin.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Doctor entity.
 * Includes all entity fields plus audit metadata.
 */
public class DoctorResponse {

    private UUID id;
    private UUID branchId;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public DoctorResponse() {
    }

    public DoctorResponse(UUID id, UUID branchId, String name, String code, String specialization, String qualification, String registrationNumber, String phone, String email, String address, BigDecimal referralCommissionPercent, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
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
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getBranchId() {
        return this.branchId;
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

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
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
        DoctorResponse that = (DoctorResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.name, that.name) &&
               java.util.Objects.equals(this.code, that.code) &&
               java.util.Objects.equals(this.specialization, that.specialization) &&
               java.util.Objects.equals(this.qualification, that.qualification) &&
               java.util.Objects.equals(this.registrationNumber, that.registrationNumber) &&
               java.util.Objects.equals(this.phone, that.phone) &&
               java.util.Objects.equals(this.email, that.email) &&
               java.util.Objects.equals(this.address, that.address) &&
               java.util.Objects.equals(this.referralCommissionPercent, that.referralCommissionPercent) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.name, this.code, this.specialization, this.qualification, this.registrationNumber, this.phone, this.email, this.address, this.referralCommissionPercent, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "DoctorResponse{id=" + id +
               ", branchId=" + branchId +
               ", name=" + name +
               ", code=" + code +
               ", specialization=" + specialization +
               ", qualification=" + qualification +
               ", registrationNumber=" + registrationNumber +
               ", phone=" + phone +
               ", email=" + email +
               ", address=" + address +
               ", referralCommissionPercent=" + referralCommissionPercent +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static DoctorResponseBuilder builder() {
        return new DoctorResponseBuilder();
    }

    public static class DoctorResponseBuilder {
        private UUID id;
        private UUID branchId;
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
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        DoctorResponseBuilder() {
        }

        public DoctorResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public DoctorResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public DoctorResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DoctorResponseBuilder code(String code) {
            this.code = code;
            return this;
        }

        public DoctorResponseBuilder specialization(String specialization) {
            this.specialization = specialization;
            return this;
        }

        public DoctorResponseBuilder qualification(String qualification) {
            this.qualification = qualification;
            return this;
        }

        public DoctorResponseBuilder registrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }

        public DoctorResponseBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public DoctorResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public DoctorResponseBuilder address(String address) {
            this.address = address;
            return this;
        }

        public DoctorResponseBuilder referralCommissionPercent(BigDecimal referralCommissionPercent) {
            this.referralCommissionPercent = referralCommissionPercent;
            return this;
        }

        public DoctorResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public DoctorResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DoctorResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DoctorResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public DoctorResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public DoctorResponse build() {
            return new DoctorResponse(this.id, this.branchId, this.name, this.code, this.specialization, this.qualification, this.registrationNumber, this.phone, this.email, this.address, this.referralCommissionPercent, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
