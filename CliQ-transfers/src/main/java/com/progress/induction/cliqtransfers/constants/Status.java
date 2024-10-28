package com.progress.induction.cliqtransfers.constants;

public enum Status {
    PENDING("PENDING"),
    FAILED("FAILED"),
    COMPLETED("COMPLETED");

    public final String status;

    Status(String status) {
        this.status = status;
    }
}
