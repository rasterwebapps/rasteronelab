package com.rasteronelab.lis.billing.api.dto;

import com.rasteronelab.lis.billing.domain.model.CreditAccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for CreditAccount entity.
 */
public class CreditAccountResponse {

    private UUID id;
    private UUID branchId;
    private String accountName;
    private CreditAccountType accountType;
    private BigDecimal creditLimit;
    private BigDecimal currentBalance;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public CreditAccountResponse() {
    }

    public CreditAccountResponse(UUID id, UUID branchId, String accountName,
                                 CreditAccountType accountType, BigDecimal creditLimit,
                                 BigDecimal currentBalance, String contactPerson,
                                 String contactPhone, String contactEmail, Boolean isActive,
                                 LocalDateTime createdAt, LocalDateTime updatedAt,
                                 String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.accountName = accountName;
        this.accountType = accountType;
        this.creditLimit = creditLimit;
        this.currentBalance = currentBalance;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
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

    public UUID getBranchId() {
        return this.branchId;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
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

    public BigDecimal getCurrentBalance() {
        return this.currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
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
        CreditAccountResponse that = (CreditAccountResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.accountName, that.accountName) &&
               java.util.Objects.equals(this.branchId, that.branchId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.accountName, this.branchId);
    }

    @Override
    public String toString() {
        return "CreditAccountResponse{id=" + id +
               ", accountName=" + accountName +
               ", accountType=" + accountType +
               "}";
    }

    public static CreditAccountResponseBuilder builder() {
        return new CreditAccountResponseBuilder();
    }

    public static class CreditAccountResponseBuilder {
        private UUID id;
        private UUID branchId;
        private String accountName;
        private CreditAccountType accountType;
        private BigDecimal creditLimit;
        private BigDecimal currentBalance;
        private String contactPerson;
        private String contactPhone;
        private String contactEmail;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        CreditAccountResponseBuilder() {
        }

        public CreditAccountResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public CreditAccountResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public CreditAccountResponseBuilder accountName(String accountName) {
            this.accountName = accountName;
            return this;
        }

        public CreditAccountResponseBuilder accountType(CreditAccountType accountType) {
            this.accountType = accountType;
            return this;
        }

        public CreditAccountResponseBuilder creditLimit(BigDecimal creditLimit) {
            this.creditLimit = creditLimit;
            return this;
        }

        public CreditAccountResponseBuilder currentBalance(BigDecimal currentBalance) {
            this.currentBalance = currentBalance;
            return this;
        }

        public CreditAccountResponseBuilder contactPerson(String contactPerson) {
            this.contactPerson = contactPerson;
            return this;
        }

        public CreditAccountResponseBuilder contactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
            return this;
        }

        public CreditAccountResponseBuilder contactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
            return this;
        }

        public CreditAccountResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public CreditAccountResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CreditAccountResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public CreditAccountResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public CreditAccountResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public CreditAccountResponse build() {
            return new CreditAccountResponse(this.id, this.branchId, this.accountName,
                    this.accountType, this.creditLimit, this.currentBalance, this.contactPerson,
                    this.contactPhone, this.contactEmail, this.isActive,
                    this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }
}
