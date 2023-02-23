package com.commercetools.reviewmanagementsystem.core.advice;

import com.commercetools.reviewmanagementsystem.core.exception.CustomException;
import com.commercetools.reviewmanagementsystem.core.exception.ReviewException;
import com.commercetools.reviewmanagementsystem.core.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ReviewControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<Object> handleCustomException(CustomException customException) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ReviewException reviewException = new ReviewException(customException.getMessage(), httpStatus);
        return new ResponseEntity<>(reviewException, httpStatus);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ReviewException exception = new ReviewException(userNotFoundException.getMessage(), httpStatus);
        return new ResponseEntity<>(exception, httpStatus);
    }
}
