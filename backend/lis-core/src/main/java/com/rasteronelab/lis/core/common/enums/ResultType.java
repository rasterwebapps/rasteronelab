package com.rasteronelab.lis.core.common.enums;

/**
 * Result entry types for different laboratory departments.
 */
public enum ResultType {
    /** Single numeric value with unit and reference range (Biochemistry, Hematology CBC) */
    NUMERIC,
    /** Integer value (count-based tests) */
    INTEGER,
    /** Calculated from formula (LDL, eGFR, A/G ratio) */
    CALCULATED,
    /** Positive/Negative, Reactive/Non-Reactive, Detected/Not Detected */
    QUALITATIVE,
    /** Nil/Trace/1+/2+/3+ (urine routine, stool) */
    SEMI_QUANTITATIVE,
    /** Free text narrative (histopathology, microbiology) */
    NARRATIVE,
    /** Narrative with embedded images (histopathology) */
    NARRATIVE_WITH_IMAGE,
    /** Culture with organism identification and antibiotic sensitivity (microbiology) */
    CULTURE_SENSITIVITY,
    /** Ratio/titer value (serology: 1:20, 1:40) */
    RATIO,
    /** Percentage value summing to 100% (differential count) */
    PERCENTAGE,
    /** CT value from PCR (molecular biology) */
    CT_VALUE,
    /** Matrix/grid result (antibiogram) */
    MATRIX,
    /** Karyotype result (genetics) */
    KARYOTYPE,
    /** Blood group (immunohematology) */
    BLOOD_GROUP,
    /** Scatter plot / histogram image (hematology) */
    SCATTER_PLOT
}
