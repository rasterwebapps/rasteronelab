package com.rasteronelab.lis.billing.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Response DTO for outstanding invoice summary per patient.
 */
public class OutstandingInvoiceResponse {

    private UUID patientId;
    private BigDecimal totalOutstanding;
    private int invoiceCount;
    private List<InvoiceSummary> invoices = new ArrayList<>();

    public OutstandingInvoiceResponse() {
    }

    public OutstandingInvoiceResponse(UUID patientId, BigDecimal totalOutstanding,
                                      int invoiceCount, List<InvoiceSummary> invoices) {
        this.patientId = patientId;
        this.totalOutstanding = totalOutstanding;
        this.invoiceCount = invoiceCount;
        this.invoices = invoices;
    }

    public UUID getPatientId() {
        return this.patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public BigDecimal getTotalOutstanding() {
        return this.totalOutstanding;
    }

    public void setTotalOutstanding(BigDecimal totalOutstanding) {
        this.totalOutstanding = totalOutstanding;
    }

    public int getInvoiceCount() {
        return this.invoiceCount;
    }

    public void setInvoiceCount(int invoiceCount) {
        this.invoiceCount = invoiceCount;
    }

    public List<InvoiceSummary> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(List<InvoiceSummary> invoices) {
        this.invoices = invoices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutstandingInvoiceResponse that = (OutstandingInvoiceResponse) o;
        return this.invoiceCount == that.invoiceCount &&
               Objects.equals(this.patientId, that.patientId) &&
               Objects.equals(this.totalOutstanding, that.totalOutstanding) &&
               Objects.equals(this.invoices, that.invoices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.patientId, this.totalOutstanding, this.invoiceCount, this.invoices);
    }

    @Override
    public String toString() {
        return "OutstandingInvoiceResponse{patientId=" + patientId +
               ", totalOutstanding=" + totalOutstanding +
               ", invoiceCount=" + invoiceCount +
               "}";
    }

    public static OutstandingInvoiceResponseBuilder builder() {
        return new OutstandingInvoiceResponseBuilder();
    }

    public static class OutstandingInvoiceResponseBuilder {
        private UUID patientId;
        private BigDecimal totalOutstanding;
        private int invoiceCount;
        private List<InvoiceSummary> invoices = new ArrayList<>();

        OutstandingInvoiceResponseBuilder() {
        }

        public OutstandingInvoiceResponseBuilder patientId(UUID patientId) {
            this.patientId = patientId;
            return this;
        }

        public OutstandingInvoiceResponseBuilder totalOutstanding(BigDecimal totalOutstanding) {
            this.totalOutstanding = totalOutstanding;
            return this;
        }

        public OutstandingInvoiceResponseBuilder invoiceCount(int invoiceCount) {
            this.invoiceCount = invoiceCount;
            return this;
        }

        public OutstandingInvoiceResponseBuilder invoices(List<InvoiceSummary> invoices) {
            this.invoices = invoices;
            return this;
        }

        public OutstandingInvoiceResponse build() {
            return new OutstandingInvoiceResponse(this.patientId, this.totalOutstanding,
                    this.invoiceCount, this.invoices);
        }
    }

    /**
     * Summary of an individual outstanding invoice.
     */
    public static class InvoiceSummary {

        private UUID id;
        private String invoiceNumber;
        private BigDecimal totalAmount;
        private BigDecimal balanceAmount;
        private LocalDate dueDate;

        public InvoiceSummary() {
        }

        public InvoiceSummary(UUID id, String invoiceNumber, BigDecimal totalAmount,
                              BigDecimal balanceAmount, LocalDate dueDate) {
            this.id = id;
            this.invoiceNumber = invoiceNumber;
            this.totalAmount = totalAmount;
            this.balanceAmount = balanceAmount;
            this.dueDate = dueDate;
        }

        public UUID getId() {
            return this.id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getInvoiceNumber() {
            return this.invoiceNumber;
        }

        public void setInvoiceNumber(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
        }

        public BigDecimal getTotalAmount() {
            return this.totalAmount;
        }

        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }

        public BigDecimal getBalanceAmount() {
            return this.balanceAmount;
        }

        public void setBalanceAmount(BigDecimal balanceAmount) {
            this.balanceAmount = balanceAmount;
        }

        public LocalDate getDueDate() {
            return this.dueDate;
        }

        public void setDueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InvoiceSummary that = (InvoiceSummary) o;
            return Objects.equals(this.id, that.id) &&
                   Objects.equals(this.invoiceNumber, that.invoiceNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.id, this.invoiceNumber);
        }

        @Override
        public String toString() {
            return "InvoiceSummary{id=" + id +
                   ", invoiceNumber=" + invoiceNumber +
                   ", balanceAmount=" + balanceAmount +
                   "}";
        }

        public static InvoiceSummaryBuilder builder() {
            return new InvoiceSummaryBuilder();
        }

        public static class InvoiceSummaryBuilder {
            private UUID id;
            private String invoiceNumber;
            private BigDecimal totalAmount;
            private BigDecimal balanceAmount;
            private LocalDate dueDate;

            InvoiceSummaryBuilder() {
            }

            public InvoiceSummaryBuilder id(UUID id) {
                this.id = id;
                return this;
            }

            public InvoiceSummaryBuilder invoiceNumber(String invoiceNumber) {
                this.invoiceNumber = invoiceNumber;
                return this;
            }

            public InvoiceSummaryBuilder totalAmount(BigDecimal totalAmount) {
                this.totalAmount = totalAmount;
                return this;
            }

            public InvoiceSummaryBuilder balanceAmount(BigDecimal balanceAmount) {
                this.balanceAmount = balanceAmount;
                return this;
            }

            public InvoiceSummaryBuilder dueDate(LocalDate dueDate) {
                this.dueDate = dueDate;
                return this;
            }

            public InvoiceSummary build() {
                return new InvoiceSummary(this.id, this.invoiceNumber, this.totalAmount,
                        this.balanceAmount, this.dueDate);
            }
        }
    }
}
