package com.commercetools.reviewmanagementsystem.core.model;

import lombok.Getter;
import java.util.Map;

@Getter
public class ApiResponse {

    private final Map responseData;
    private final String message;
    private final Integer status;

    public ApiResponse(Map responseData, String message, Integer status) {
        this.responseData = responseData;
        this.message = message;
        this.status = status;
    }
}
