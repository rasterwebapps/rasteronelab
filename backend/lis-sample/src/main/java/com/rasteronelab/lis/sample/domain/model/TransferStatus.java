package com.rasteronelab.lis.sample.domain.model;

/**
 * Status of an inter-branch sample transfer.
 */
public enum TransferStatus {
    INITIATED,
    IN_TRANSIT,
    RECEIVED_AT_DEST,
    CANCELLED
}
