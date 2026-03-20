package com.rasteronelab.lis.qc.domain.model;

public enum QCLevel {
    LEVEL_1("Low"),
    LEVEL_2("Normal"),
    LEVEL_3("High");

    private final String description;

    QCLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
