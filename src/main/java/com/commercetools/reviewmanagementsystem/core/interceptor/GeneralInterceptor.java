package com.commercetools.reviewmanagementsystem.core.interceptor;

import com.commercetools.reviewmanagementsystem.core.exception.CustomException;
import com.commercetools.reviewmanagementsystem.core.exception.ErrorMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class GeneralInterceptor implements HandlerInterceptor {

    @Value("${Secret_Auth_Key}")
    private String secretAuthKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request.getHeader("Secret-Key") == null) {
            log.info("No secret-key.");
            response.addHeader("secretKey", "No key available");
            throw new CustomException(ErrorMessages.SECRET_NOT_EXISTING.getErrorMessages());
        } else if (request.getHeader("Secret-Key").equals(secretAuthKey)) {
            response.addHeader("Interceptor", "Key - Success");
            log.info("Validation Success.");
            return true;
        } else {
            log.info("Validation Error. Invalid Key");
            response.addHeader("Interceptor", "Invalid Key");
            throw new CustomException(ErrorMessages.INVALID_SECRET.getErrorMessages());
        }
    }
}
