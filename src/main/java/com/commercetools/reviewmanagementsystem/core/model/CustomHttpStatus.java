package com.commercetools.reviewmanagementsystem.core.model;

public enum CustomHttpStatus {
    SUCCESS(0),
    FAILED(1);

    private final int status;

    CustomHttpStatus(int status) {
        this.status = status;
    }
}
