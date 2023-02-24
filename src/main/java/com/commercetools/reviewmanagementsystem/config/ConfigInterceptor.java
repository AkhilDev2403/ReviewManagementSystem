package com.commercetools.reviewmanagementsystem.config;

import com.commercetools.reviewmanagementsystem.core.interceptor.GeneralInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Component
@RequiredArgsConstructor
public class ConfigInterceptor extends WebMvcConfigurationSupport {
    private final GeneralInterceptor interceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}
