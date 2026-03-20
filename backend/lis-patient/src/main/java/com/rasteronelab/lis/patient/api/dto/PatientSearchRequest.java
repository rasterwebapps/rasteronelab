package com.rasteronelab.lis.patient.api.dto;

import com.rasteronelab.lis.patient.domain.model.Gender;

import java.time.LocalDate;

/**
 * Request DTO for patient search with multiple filter criteria.
 */
public class PatientSearchRequest {

    private String searchTerm;
    private Gender gender;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public PatientSearchRequest() {
    }

    public PatientSearchRequest(String searchTerm, Gender gender, LocalDate dateFrom, LocalDate dateTo) {
        this.searchTerm = searchTerm;
        this.gender = gender;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public String getSearchTerm() {
        return this.searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateFrom() {
        return this.dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return this.dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientSearchRequest that = (PatientSearchRequest) o;
        return java.util.Objects.equals(this.searchTerm, that.searchTerm) &&
               java.util.Objects.equals(this.gender, that.gender) &&
               java.util.Objects.equals(this.dateFrom, that.dateFrom) &&
               java.util.Objects.equals(this.dateTo, that.dateTo);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.searchTerm, this.gender, this.dateFrom, this.dateTo);
    }

    @Override
    public String toString() {
        return "PatientSearchRequest{searchTerm=" + searchTerm +
               ", gender=" + gender +
               ", dateFrom=" + dateFrom +
               ", dateTo=" + dateTo +
               "}";
    }
}
