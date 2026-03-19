package com.rasteronelab.lis.billing.api.dto;

import com.rasteronelab.lis.billing.domain.model.CreditAccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Request DTO for creating or updating a CreditAccount.
 */
public class CreditAccountRequest {

    @NotBlank(message = "Account name is required")
    private String accountName;

    @NotNull(message = "Account type is required")
    private CreditAccountType accountType;

    private BigDecimal creditLimit;

    private String contactPerson;

    private String contactPhone;

    private String contactEmail;

    public CreditAccountRequest() {
    }

    public CreditAccountRequest(String accountName, CreditAccountType accountType,
                                BigDecimal creditLimit, String contactPerson,
                                String contactPhone, String contactEmail) {
        this.accountName = accountName;
        this.accountType = accountType;
        this.creditLimit = creditLimit;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public CreditAccountType getAccountType() {
        return this.accountType;
    }

    public void setAccountType(CreditAccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getCreditLimit() {
        return this.creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getContactPerson() {
        return this.contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return this.contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditAccountRequest that = (CreditAccountRequest) o;
        return java.util.Objects.equals(this.accountName, that.accountName) &&
               java.util.Objects.equals(this.accountType, that.accountType);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.accountName, this.accountType);
    }

    @Override
    public String toString() {
        return "CreditAccountRequest{accountName=" + accountName +
               ", accountType=" + accountType +
               "}";
    }

    public static CreditAccountRequestBuilder builder() {
        return new CreditAccountRequestBuilder();
    }

    public static class CreditAccountRequestBuilder {
        private String accountName;
        private CreditAccountType accountType;
        private BigDecimal creditLimit;
        private String contactPerson;
        private String contactPhone;
        private String contactEmail;

        CreditAccountRequestBuilder() {
        }

        public CreditAccountRequestBuilder accountName(String accountName) {
            this.accountName = accountName;
            return this;
        }

        public CreditAccountRequestBuilder accountType(CreditAccountType accountType) {
            this.accountType = accountType;
            return this;
        }

        public CreditAccountRequestBuilder creditLimit(BigDecimal creditLimit) {
            this.creditLimit = creditLimit;
            return this;
        }

        public CreditAccountRequestBuilder contactPerson(String contactPerson) {
            this.contactPerson = contactPerson;
            return this;
        }

        public CreditAccountRequestBuilder contactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
            return this;
        }

        public CreditAccountRequestBuilder contactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
            return this;
        }

        public CreditAccountRequest build() {
            return new CreditAccountRequest(this.accountName, this.accountType,
                    this.creditLimit, this.contactPerson, this.contactPhone, this.contactEmail);
        }
    }
}
