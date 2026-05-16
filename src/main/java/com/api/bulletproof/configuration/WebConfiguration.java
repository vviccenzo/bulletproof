package com.api.bulletproof.configuration;

import com.api.bulletproof.interceptor.TransactionUserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final TransactionUserInterceptor transactionUserInterceptor;

    WebConfiguration(TransactionUserInterceptor transactionUserInterceptor) {
        this.transactionUserInterceptor = transactionUserInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(transactionUserInterceptor).addPathPatterns("**");
    }
}
