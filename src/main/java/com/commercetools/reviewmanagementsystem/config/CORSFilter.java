package com.commercetools.reviewmanagementsystem.config;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CORSFilter extends GenericFilterBean implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chaine)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "*");
        httpResponse.setHeader("Access-Control-Allow-Headers", "token");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "false");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        System.out.println("****************** CORS Configuration Completed *******************");
        chaine.doFilter(request, response);
    }

}