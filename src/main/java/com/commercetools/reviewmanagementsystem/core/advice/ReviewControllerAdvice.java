package com.commercetools.reviewmanagementsystem.core.advice;

import com.commercetools.reviewmanagementsystem.core.exception.ApiException;
import com.commercetools.reviewmanagementsystem.core.exception.CustomException;
import com.commercetools.reviewmanagementsystem.core.exception.ReviewException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ReviewControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleCustomException(CustomException customException) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ReviewException reviewException = new ReviewException(customException.getMessage(), httpStatus);
        return new ResponseEntity<>(reviewException, httpStatus);
    }

    @ExceptionHandler(value = {java.lang.Exception.class})
    public ResponseEntity<Object> handleOtherExceptions(ReviewException ex, WebRequest request) {
        ApiException exp = new ApiException(new Date(), ex.getMessage());
        return new ResponseEntity<>(exp, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
