package com.example.travelermileageservice.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebFilterConfig {

    @Bean
    public FilterRegistrationBean<TransactionLoggingFilter> loggingFilter() {
        final FilterRegistrationBean<TransactionLoggingFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new TransactionLoggingFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
