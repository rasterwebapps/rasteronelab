package com.rasteronelab.lis.core.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class BarcodeGeneratorUtilTest {

    private static final String TODAY = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    @Test
    void generateUhid_shouldFormatWithPrefixAndPaddedSequence() {
        String uhid = BarcodeGeneratorUtil.generateUhid("BR01", 1);
        assertEquals("BR01-000001", uhid);
    }

    @Test
    void generateUhid_shouldHandleLargeSequence() {
        String uhid = BarcodeGeneratorUtil.generateUhid("HQ", 999999);
        assertEquals("HQ-999999", uhid);
    }

    @Test
    void generateOrderNumber_shouldFollowFormat() {
        String order = BarcodeGeneratorUtil.generateOrderNumber(42);
        assertEquals("ORD-" + TODAY + "-000042", order);
    }

    @Test
    void generateInvoiceNumber_shouldFollowFormat() {
        String invoice = BarcodeGeneratorUtil.generateInvoiceNumber(7);
        assertEquals("INV-" + TODAY + "-000007", invoice);
    }

    @Test
    void generateReceiptNumber_shouldFollowFormat() {
        String receipt = BarcodeGeneratorUtil.generateReceiptNumber(100);
        assertEquals("RCP-" + TODAY + "-000100", receipt);
    }

    @Test
    void generateSampleBarcode_shouldFollowFormat() {
        String sample = BarcodeGeneratorUtil.generateSampleBarcode(55);
        assertEquals("SMP-" + TODAY + "-000055", sample);
    }

    @Test
    void generateVisitNumber_shouldFollowFormat() {
        String visit = BarcodeGeneratorUtil.generateVisitNumber(3);
        assertEquals("VST-" + TODAY + "-000003", visit);
    }

    @Test
    void generateRefundNumber_shouldFollowFormat() {
        String refund = BarcodeGeneratorUtil.generateRefundNumber(12);
        assertEquals("RFD-" + TODAY + "-000012", refund);
    }

    @Test
    void allDateBasedFormats_shouldMatchExpectedPattern() {
        String order = BarcodeGeneratorUtil.generateOrderNumber(1);
        assertTrue(order.matches("ORD-\\d{8}-\\d{6}"), "Order format mismatch: " + order);

        String invoice = BarcodeGeneratorUtil.generateInvoiceNumber(1);
        assertTrue(invoice.matches("INV-\\d{8}-\\d{6}"), "Invoice format mismatch: " + invoice);

        String receipt = BarcodeGeneratorUtil.generateReceiptNumber(1);
        assertTrue(receipt.matches("RCP-\\d{8}-\\d{6}"), "Receipt format mismatch: " + receipt);

        String sample = BarcodeGeneratorUtil.generateSampleBarcode(1);
        assertTrue(sample.matches("SMP-\\d{8}-\\d{6}"), "Sample format mismatch: " + sample);

        String visit = BarcodeGeneratorUtil.generateVisitNumber(1);
        assertTrue(visit.matches("VST-\\d{8}-\\d{6}"), "Visit format mismatch: " + visit);

        String refund = BarcodeGeneratorUtil.generateRefundNumber(1);
        assertTrue(refund.matches("RFD-\\d{8}-\\d{6}"), "Refund format mismatch: " + refund);
    }
}
