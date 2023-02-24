package com.commercetools.reviewmanagementsystem.core.exception;

public enum ErrorMessages {
    INVALID_RATING("ENTER A VALID RATING"),
    INVALID_CUSTOMER("INVALID CUSTOMER ID. PLEASE LOG IN"),
    ALREADY_REVIEWED("YOU'VE ALREADY REVIEWED THIS PRODUCT"),
    INVALID_PRODUCT("THERE'S NO SUCH PRODUCT EXIST"),
    UNAUTHORIZED_CUSTOMER("UNAUTHORIZED CUSTOMER"),
    REVIEW_NOT_FOUND("REVIEW NOT FOUND TO GET DELETED"),
    NO_REVIEW_FOUND("There is no review given for the specified product"),
    INVALID_SECRET("INVALID SECRET"),
    FAILED("OPERATION FAILED");

    ErrorMessages(String errorMessages) {
        this.errorMessages = errorMessages;
    }

    private String errorMessages;

    public String getErrorMessages() {
        return errorMessages;
    }


}
