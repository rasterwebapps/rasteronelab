package com.rasteronelab.lis.sample.domain.model;

/**
 * Tube types used in sample collection.
 * Maps to physical tube colours used in phlebotomy.
 */
public enum TubeType {
    /** Red / plain serum — LFT, RFT, Lipids, Thyroid */
    RED,
    /** Purple / EDTA — CBC, HbA1c, Blood Group */
    EDTA,
    /** Blue / Sodium Citrate — PT/INR, APTT, D-Dimer */
    CITRATE,
    /** Grey / Fluoride-Oxalate — Fasting/PP Glucose */
    FLUORIDE,
    /** Green / Li-Heparin — ABG, Ammonia */
    HEPARIN,
    /** Yellow / SST — General Chemistry */
    SST,
    /** Other / miscellaneous */
    OTHER
}
