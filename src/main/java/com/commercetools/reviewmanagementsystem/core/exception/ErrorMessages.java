package com.commercetools.reviewmanagementsystem.core.exception;

public enum ErrorMessages {

    WISH_PRODUCT_EXISTS("PRODUCT ALREADY IN WISH"),
    INVALID_RATING("ENTER A VALID RATING"),
    INVALID_CUSTOMER("INVALID CUSTOMER. PLEASE LOG IN"),
    ALREADY_REVIEWED("YOU'VE ALREADY REVIEWED THIS PRODUCT"),
    INVALID_PRODUCT("THERE'S NO SUCH PRODUCT"),
    UNAUTHORIZED_CUSTOMER("UNAUTHORIZED CUSTOMER"),
    INVALID_SECRET("INVALID SECRET"),
    FAILED("OPERATION FAILED"),
    NO_REVIEWS("No Reviews to display"),
    WISH_PID_FOUND("PROVIDE PRODUCT ID");

    ErrorMessages(String errorMessages) {
        this.errorMessages = errorMessages;
    }

    private String errorMessages;

    public String getErrorMessages() {
        return errorMessages;
    }


}
