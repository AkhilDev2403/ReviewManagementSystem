package com.commercetools.reviewmanagementsystem.core.interceptor;

import com.commercetools.reviewmanagementsystem.core.exception.CustomException;
import com.commercetools.reviewmanagementsystem.core.exception.ErrorMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Slf4j
@Component
public class GeneralInterceptor implements HandlerInterceptor {

    @Value("${token}")
    private String token;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println("Header Name - " + headerName + ", Value - " + request.getHeader(headerName));
        }

        if (request.getHeader("token") == null) {
            log.info("No secret-key.");
            response.addHeader("secretKey", "No key available");
            throw new CustomException(ErrorMessages.SECRET_NOT_EXISTING.getErrorMessages());
        } else if (request.getHeader("token").equals(token)) {
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
