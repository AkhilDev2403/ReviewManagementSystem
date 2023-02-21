package com.commercetools.reviewmanagementsystem.core.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ApiException {
    private Date timeStamp;
    private String message;
}
