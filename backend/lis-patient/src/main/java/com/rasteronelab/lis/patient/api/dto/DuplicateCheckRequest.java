package com.rasteronelab.lis.patient.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

/**
 * Request DTO for duplicate patient detection with weighted scoring.
 * Used by the scored duplicate detection endpoint.
 */
public class DuplicateCheckRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String phone;
    private String email;
    private String gender;
    private LocalDate dateOfBirth;

    public DuplicateCheckRequest() {
    }

    public DuplicateCheckRequest(String firstName, String lastName, String phone,
                                 String email, String gender, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
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

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "DuplicateCheckRequest{firstName=" + firstName +
               ", lastName=" + lastName +
               ", phone=" + phone +
               ", gender=" + gender +
               ", dateOfBirth=" + dateOfBirth +
               "}";
    }
}
