package com.rasteronelab.lis.patient.api.dto;

import com.rasteronelab.lis.patient.domain.model.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Request DTO for creating or updating a Patient.
 */
public class PatientRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    private LocalDate dateOfBirth;

    private Integer ageYears;

    private Integer ageMonths;

    private Integer ageDays;

    private Gender gender;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Size(max = 10, message = "Blood group must not exceed 10 characters")
    private String bloodGroup;

    @Size(max = 255, message = "Address line 1 must not exceed 255 characters")
    private String addressLine1;

    @Size(max = 255, message = "Address line 2 must not exceed 255 characters")
    private String addressLine2;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;

    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    @Size(max = 100, message = "Emergency contact name must not exceed 100 characters")
    private String emergencyContactName;

    @Size(max = 20, message = "Emergency contact phone must not exceed 20 characters")
    private String emergencyContactPhone;

    @Size(max = 100, message = "Nationality must not exceed 100 characters")
    private String nationality;

    @Size(max = 50, message = "ID type must not exceed 50 characters")
    private String idType;

    @Size(max = 50, message = "ID number must not exceed 50 characters")
    private String idNumber;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    public PatientRequest() {
    }

    public PatientRequest(String firstName, String lastName, LocalDate dateOfBirth,
                          Integer ageYears, Integer ageMonths, Integer ageDays, Gender gender,
                          String phone, String email, String bloodGroup, String addressLine1,
                          String addressLine2, String city, String state, String postalCode,
                          String country, String emergencyContactName, String emergencyContactPhone,
                          String nationality, String idType, String idNumber, String notes) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientRequest that = (PatientRequest) o;
        return java.util.Objects.equals(this.firstName, that.firstName) &&
               java.util.Objects.equals(this.lastName, that.lastName) &&
               java.util.Objects.equals(this.dateOfBirth, that.dateOfBirth) &&
               java.util.Objects.equals(this.ageYears, that.ageYears) &&
               java.util.Objects.equals(this.ageMonths, that.ageMonths) &&
               java.util.Objects.equals(this.ageDays, that.ageDays) &&
               java.util.Objects.equals(this.gender, that.gender) &&
               java.util.Objects.equals(this.phone, that.phone) &&
               java.util.Objects.equals(this.email, that.email) &&
               java.util.Objects.equals(this.bloodGroup, that.bloodGroup) &&
               java.util.Objects.equals(this.addressLine1, that.addressLine1) &&
               java.util.Objects.equals(this.addressLine2, that.addressLine2) &&
               java.util.Objects.equals(this.city, that.city) &&
               java.util.Objects.equals(this.state, that.state) &&
               java.util.Objects.equals(this.postalCode, that.postalCode) &&
               java.util.Objects.equals(this.country, that.country) &&
               java.util.Objects.equals(this.emergencyContactName, that.emergencyContactName) &&
               java.util.Objects.equals(this.emergencyContactPhone, that.emergencyContactPhone) &&
               java.util.Objects.equals(this.nationality, that.nationality) &&
               java.util.Objects.equals(this.idType, that.idType) &&
               java.util.Objects.equals(this.idNumber, that.idNumber) &&
               java.util.Objects.equals(this.notes, that.notes);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.firstName, this.lastName, this.dateOfBirth,
                this.ageYears, this.ageMonths, this.ageDays, this.gender, this.phone,
                this.email, this.bloodGroup, this.addressLine1, this.addressLine2,
                this.city, this.state, this.postalCode, this.country,
                this.emergencyContactName, this.emergencyContactPhone,
                this.nationality, this.idType, this.idNumber, this.notes);
    }

    @Override
    public String toString() {
        return "PatientRequest{firstName=" + firstName +
               ", lastName=" + lastName +
               ", phone=" + phone +
               ", gender=" + gender +
               "}";
    }

    public static PatientRequestBuilder builder() {
        return new PatientRequestBuilder();
    }

    public static class PatientRequestBuilder {
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

        PatientRequestBuilder() {
        }

        public PatientRequestBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public PatientRequestBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public PatientRequestBuilder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public PatientRequestBuilder ageYears(Integer ageYears) {
            this.ageYears = ageYears;
            return this;
        }

        public PatientRequestBuilder ageMonths(Integer ageMonths) {
            this.ageMonths = ageMonths;
            return this;
        }

        public PatientRequestBuilder ageDays(Integer ageDays) {
            this.ageDays = ageDays;
            return this;
        }

        public PatientRequestBuilder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public PatientRequestBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public PatientRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        public PatientRequestBuilder bloodGroup(String bloodGroup) {
            this.bloodGroup = bloodGroup;
            return this;
        }

        public PatientRequestBuilder addressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
            return this;
        }

        public PatientRequestBuilder addressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
            return this;
        }

        public PatientRequestBuilder city(String city) {
            this.city = city;
            return this;
        }

        public PatientRequestBuilder state(String state) {
            this.state = state;
            return this;
        }

        public PatientRequestBuilder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public PatientRequestBuilder country(String country) {
            this.country = country;
            return this;
        }

        public PatientRequestBuilder emergencyContactName(String emergencyContactName) {
            this.emergencyContactName = emergencyContactName;
            return this;
        }

        public PatientRequestBuilder emergencyContactPhone(String emergencyContactPhone) {
            this.emergencyContactPhone = emergencyContactPhone;
            return this;
        }

        public PatientRequestBuilder nationality(String nationality) {
            this.nationality = nationality;
            return this;
        }

        public PatientRequestBuilder idType(String idType) {
            this.idType = idType;
            return this;
        }

        public PatientRequestBuilder idNumber(String idNumber) {
            this.idNumber = idNumber;
            return this;
        }

        public PatientRequestBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public PatientRequest build() {
            return new PatientRequest(this.firstName, this.lastName, this.dateOfBirth,
                    this.ageYears, this.ageMonths, this.ageDays, this.gender, this.phone,
                    this.email, this.bloodGroup, this.addressLine1, this.addressLine2,
                    this.city, this.state, this.postalCode, this.country,
                    this.emergencyContactName, this.emergencyContactPhone,
                    this.nationality, this.idType, this.idNumber, this.notes);
        }
    }

}
