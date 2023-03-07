package com.commercetools.reviewmanagementsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/reviews/**")
                .allowedOrigins("https://reviewmanagementsystem-production.up.railway.app/")
                .allowedMethods("PUT", "DELETE", "POST", "GET")
                .allowedHeaders("token")
                .exposedHeaders("token")
                .allowCredentials(true).maxAge(3600);
    }
}
