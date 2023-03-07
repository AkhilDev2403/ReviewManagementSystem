package com.commercetools.reviewmanagementsystem;

import com.commercetools.reviewmanagementsystem.config.CORSFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ReviewManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewManagementSystemApplication.class, args);
	}
	@Bean
	public FilterRegistrationBean crosFilterRegistration(){
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CORSFilter());
		registrationBean.setName("CORS Filter");
		registrationBean.addUrlPatterns("/*");
		registrationBean.setOrder(1);
		return registrationBean;
	}

}
