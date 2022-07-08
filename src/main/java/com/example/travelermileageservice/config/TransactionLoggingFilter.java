package com.example.travelermileageservice.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class TransactionLoggingFilter implements Filter {

    /**
     * 요청마다 고유한 transactionId 발급
     */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        MDC.put("transactionId", String.valueOf(UUID.randomUUID()));
        chain.doFilter(request, response);
    }
}
