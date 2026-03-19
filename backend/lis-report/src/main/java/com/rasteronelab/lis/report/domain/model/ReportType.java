package com.rasteronelab.lis.report.domain.model;

/**
 * Types of lab reports.
 * INDIVIDUAL — single-test report, CONSOLIDATED — multi-test grouped report,
 * INTERIM — partial results, AMENDED — corrected version of a prior report.
 */
public enum ReportType {
    INDIVIDUAL,
    CONSOLIDATED,
    INTERIM,
    AMENDED
}
