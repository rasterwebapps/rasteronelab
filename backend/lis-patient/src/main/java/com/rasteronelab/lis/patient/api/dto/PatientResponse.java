package com.rasteronelab.lis.patient.api.dto;

import com.rasteronelab.lis.patient.domain.model.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Patient entity.
 * Includes all patient fields plus audit metadata.
 */
public class PatientResponse {

    private UUID id;
    private String uhid;
    private UUID branchId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Integer ageYears;
    private Integer ageMonths;
    private Integer ageDays;
    private Gender gender;
    private String phone;
    private String email;
    private String bloodGroup;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String nationality;
    private String idType;
    private String idNumber;
    private String notes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public PatientResponse() {
    }

    public PatientResponse(UUID id, String uhid, UUID branchId, String firstName, String lastName,
                           LocalDate dateOfBirth, Integer ageYears, Integer ageMonths, Integer ageDays,
                           Gender gender, String phone, String email, String bloodGroup,
                           String addressLine1, String addressLine2, String city, String state,
                           String postalCode, String country, String emergencyContactName,
                           String emergencyContactPhone, String nationality, String idType,
                           String idNumber, String notes, Boolean isActive,
                           LocalDateTime createdAt, LocalDateTime updatedAt,
                           String createdBy, String updatedBy) {
        this.id = id;
        this.uhid = uhid;
        this.branchId = branchId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.ageYears = ageYears;
        this.ageMonths = ageMonths;
        this.ageDays = ageDays;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.bloodGroup = bloodGroup;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.nationality = nationality;
        this.idType = idType;
        this.idNumber = idNumber;
        this.notes = notes;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUhid() {
        return this.uhid;
    }

    public void setUhid(String uhid) {
        this.uhid = uhid;
    }

    public UUID getBranchId() {
        return this.branchId;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getAgeYears() {
        return this.ageYears;
    }

    public void setAgeYears(Integer ageYears) {
        this.ageYears = ageYears;
    }

    public Integer getAgeMonths() {
        return this.ageMonths;
    }

    public void setAgeMonths(Integer ageMonths) {
        this.ageMonths = ageMonths;
    }

    public Integer getAgeDays() {
        return this.ageDays;
    }

    public void setAgeDays(Integer ageDays) {
        this.ageDays = ageDays;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBloodGroup() {
        return this.bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return this.addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmergencyContactName() {
        return this.emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return this.emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getNationality() {
        return this.nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getIdType() {
        return this.idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientResponse that = (PatientResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.uhid, that.uhid) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.firstName, that.firstName) &&
               java.util.Objects.equals(this.lastName, that.lastName) &&
               java.util.Objects.equals(this.phone, that.phone);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.uhid, this.branchId, this.firstName, this.lastName, this.phone);
    }

    @Override
    public String toString() {
        return "PatientResponse{id=" + id +
               ", uhid=" + uhid +
               ", branchId=" + branchId +
               ", firstName=" + firstName +
               ", lastName=" + lastName +
               ", phone=" + phone +
               "}";
    }

    public static PatientResponseBuilder builder() {
        return new PatientResponseBuilder();
    }

    public static class PatientResponseBuilder {
        private UUID id;
        private String uhid;
        private UUID branchId;
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;
        private Integer ageYears;
        private Integer ageMonths;
        private Integer ageDays;
        private Gender gender;
        private String phone;
        private String email;
        private String bloodGroup;
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String state;
        private String postalCode;
        private String country;
        private String emergencyContactName;
        private String emergencyContactPhone;
        private String nationality;
        private String idType;
        private String idNumber;
        private String notes;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        PatientResponseBuilder() {
        }

        public PatientResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public PatientResponseBuilder uhid(String uhid) {
            this.uhid = uhid;
            return this;
        }

        public PatientResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public PatientResponseBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public PatientResponseBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public PatientResponseBuilder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public PatientResponseBuilder ageYears(Integer ageYears) {
            this.ageYears = ageYears;
            return this;
        }

        public PatientResponseBuilder ageMonths(Integer ageMonths) {
            this.ageMonths = ageMonths;
            return this;
        }

        public PatientResponseBuilder ageDays(Integer ageDays) {
            this.ageDays = ageDays;
            return this;
        }

        public PatientResponseBuilder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public PatientResponseBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public PatientResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public PatientResponseBuilder bloodGroup(String bloodGroup) {
            this.bloodGroup = bloodGroup;
            return this;
        }

        public PatientResponseBuilder addressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
            return this;
        }

        public PatientResponseBuilder addressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
            return this;
        }

        public PatientResponseBuilder city(String city) {
            this.city = city;
            return this;
        }

        public PatientResponseBuilder state(String state) {
            this.state = state;
            return this;
        }

        public PatientResponseBuilder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public PatientResponseBuilder country(String country) {
            this.country = country;
            return this;
        }

        public PatientResponseBuilder emergencyContactName(String emergencyContactName) {
            this.emergencyContactName = emergencyContactName;
            return this;
        }

        public PatientResponseBuilder emergencyContactPhone(String emergencyContactPhone) {
            this.emergencyContactPhone = emergencyContactPhone;
            return this;
        }

        public PatientResponseBuilder nationality(String nationality) {
            this.nationality = nationality;
            return this;
        }

        public PatientResponseBuilder idType(String idType) {
            this.idType = idType;
            return this;
        }

        public PatientResponseBuilder idNumber(String idNumber) {
            this.idNumber = idNumber;
            return this;
        }

        public PatientResponseBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public PatientResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public PatientResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PatientResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public PatientResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public PatientResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public PatientResponse build() {
            return new PatientResponse(this.id, this.uhid, this.branchId, this.firstName,
                    this.lastName, this.dateOfBirth, this.ageYears, this.ageMonths, this.ageDays,
                    this.gender, this.phone, this.email, this.bloodGroup, this.addressLine1,
                    this.addressLine2, this.city, this.state, this.postalCode, this.country,
                    this.emergencyContactName, this.emergencyContactPhone, this.nationality,
                    this.idType, this.idNumber, this.notes, this.isActive, this.createdAt,
                    this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
