package com.rasteronelab.lis.report.domain.model;

/**
 * Lifecycle states for a lab report.
 * Transitions:
 *   DRAFT → GENERATED → SIGNED → DELIVERED
 *                               └──► AMENDED (creates new version)
 */
public enum ReportStatus {
    DRAFT,
    GENERATED,
    SIGNED,
    DELIVERED,
    AMENDED
}
