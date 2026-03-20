package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * Doctor entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Represents a referring doctor within a branch.
 */
@Entity
@Table(name = "doctor")
public class Doctor extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "qualification")
    private String qualification;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "referral_commission_percent")
    private BigDecimal referralCommissionPercent;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

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

}
