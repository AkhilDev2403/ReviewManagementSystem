package com.commercetools.reviewmanagementsystem.core.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class GeneralInterceptor implements HandlerInterceptor {

    @Value("${secret_auth_key}")
    private String secretAuthKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request.getHeader("Secret-Key") == null) {
            log.info("No secret-key.");
            response.addHeader("secretKey", "No key available");
            return false;
        } else if (request.getHeader("Secret-Key").equals(secretAuthKey)) {
            response.addHeader("Interceptor", "Key - Success");
            log.info("Validation Success.");
            return true;
        } else {
            log.info("Validation Error. Invalid Key");
            response.addHeader("Interceptor", "Invalid Key");
            return false;
        }
    }
}
