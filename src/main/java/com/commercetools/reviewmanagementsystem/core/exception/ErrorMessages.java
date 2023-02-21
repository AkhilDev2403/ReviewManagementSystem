package com.commercetools.reviewmanagementsystem.core.exception;

public enum ErrorMessages {

    WISH_PRODUCT_EXISTS("PRODUCT ALREADY IN WISH"),
    INVALID_RATING("ENTER A VALID RATING"),
    INVALID_CUSTOMER("INVALID CUSTOMER. PLEASE LOG IN"),
    INVALID_PRODUCT("THERE'S NO SUCH PRODUCT"),
    WISH_PID_FOUND("PROVIDE PRODUCT ID");

    ErrorMessages(String errorMessages) {
        this.errorMessages = errorMessages;
    }

    private String errorMessages;

    public String getErrorMessages() {
        return errorMessages;
    }


}
