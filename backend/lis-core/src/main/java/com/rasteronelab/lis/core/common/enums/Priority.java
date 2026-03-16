package com.rasteronelab.lis.core.common.enums;

/**
 * Order/sample processing priority levels.
 */
public enum Priority {
    /** Standard processing — default TAT applies */
    ROUTINE,
    /** Faster processing — reduced TAT */
    URGENT,
    /** Immediate processing — STAT TAT */
    STAT,
    /** Critical patient — highest priority */
    CRITICAL
}
