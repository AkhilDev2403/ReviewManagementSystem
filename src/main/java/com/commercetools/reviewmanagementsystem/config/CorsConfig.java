package com.commercetools.reviewmanagementsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CorsConfig {

    @Value("${Secret_Auth_Key}")
    private String Secret_Auth_Key;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/reviews/**")
                        .allowedOrigins("http://localhost:8080")
                        .allowedMethods("GET", "POST", "DELETE", "PUT")
                        .allowedHeaders(Secret_Auth_Key)
                        .exposedHeaders(Secret_Auth_Key);
            }
        };
    }
}

