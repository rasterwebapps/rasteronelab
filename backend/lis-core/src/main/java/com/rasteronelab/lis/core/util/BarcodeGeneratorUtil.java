package com.rasteronelab.lis.core.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility for generating formatted identifiers used across modules.
 *
 * Formats:
 * - UHID: {prefix}-{6-digit-sequence} e.g., BR01-000001
 * - Order: ORD-{YYYYMMDD}-{6-digit-sequence} e.g., ORD-20260319-000001
 * - Invoice: INV-{YYYYMMDD}-{6-digit-sequence} e.g., INV-20260319-000001
 * - Receipt: RCP-{YYYYMMDD}-{6-digit-sequence} e.g., RCP-20260319-000001
 * - Sample: SMP-{YYYYMMDD}-{6-digit-sequence} e.g., SMP-20260319-000001
 * - Visit: VST-{YYYYMMDD}-{6-digit-sequence} e.g., VST-20260319-000001
 * - Refund: RFD-{YYYYMMDD}-{6-digit-sequence} e.g., RFD-20260319-000001
 */
public final class BarcodeGeneratorUtil {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private BarcodeGeneratorUtil() {}

    public static String generateUhid(String branchPrefix, long sequence) {
        return String.format("%s-%06d", branchPrefix, sequence);
    }

    public static String generateOrderNumber(long sequence) {
        return String.format("ORD-%s-%06d", LocalDate.now().format(DATE_FMT), sequence);
    }

    public static String generateInvoiceNumber(long sequence) {
        return String.format("INV-%s-%06d", LocalDate.now().format(DATE_FMT), sequence);
    }

    public static String generateReceiptNumber(long sequence) {
        return String.format("RCP-%s-%06d", LocalDate.now().format(DATE_FMT), sequence);
    }

    public static String generateSampleBarcode(long sequence) {
        return String.format("SMP-%s-%06d", LocalDate.now().format(DATE_FMT), sequence);
    }

    public static String generateVisitNumber(long sequence) {
        return String.format("VST-%s-%06d", LocalDate.now().format(DATE_FMT), sequence);
    }

    public static String generateRefundNumber(long sequence) {
        return String.format("RFD-%s-%06d", LocalDate.now().format(DATE_FMT), sequence);
    }
}
