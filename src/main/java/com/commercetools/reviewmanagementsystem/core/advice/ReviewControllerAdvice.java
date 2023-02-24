package com.commercetools.reviewmanagementsystem.core.advice;

import com.commercetools.reviewmanagementsystem.controller.ReviewController;
import com.commercetools.reviewmanagementsystem.core.exception.CustomException;
import com.commercetools.reviewmanagementsystem.core.exception.ReviewException;
import com.commercetools.reviewmanagementsystem.core.exception.UserNotFoundException;
import com.commercetools.reviewmanagementsystem.core.model.ApiResponse;
import com.commercetools.reviewmanagementsystem.core.model.Constants;
import com.commercetools.reviewmanagementsystem.core.model.CustomHttpStatus;
import com.commercetools.reviewmanagementsystem.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ReviewControllerAdvice extends ResponseEntityExceptionHandler {

    @Autowired
    ReviewController reviewController;

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ApiResponse> handleCustomException(CustomException customException) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        Map data = new HashMap();
        data.put(Constants.MESSAGE, customException.getMessage());
        ApiResponse apiResponse = new ApiResponse(data, Constants.FAILED, CustomHttpStatus.FAILED.ordinal());
        return new ResponseEntity<>(apiResponse, httpStatus);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<ApiResponse> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        Map data = new HashMap();
        data.put(Constants.MESSAGE, userNotFoundException.getMessage());
        ApiResponse apiResponse = new ApiResponse(data, Constants.FAILED, CustomHttpStatus.FAILED.ordinal());
        return new ResponseEntity<>(apiResponse, httpStatus);
    }
}
