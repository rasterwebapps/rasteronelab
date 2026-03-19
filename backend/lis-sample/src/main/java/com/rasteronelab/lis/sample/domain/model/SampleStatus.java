package com.rasteronelab.lis.sample.domain.model;

/**
 * Lifecycle states for a Sample.
 * Transitions:
 *   COLLECTED → RECEIVED → ACCEPTED → PROCESSING → COMPLETED → STORED → DISPOSED
 *                      └──► ALIQUOTED → PROCESSING ...
 *                      └──► REJECTED  (end / recollection)
 */
public enum SampleStatus {
    COLLECTED,
    RECEIVED,
    ACCEPTED,
    REJECTED,
    ALIQUOTED,
    PROCESSING,
    COMPLETED,
    STORED,
    DISPOSED
}
