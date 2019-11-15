package com.tensquare.friend.config;

import com.tensquare.friend.interceptor.JwtIntetceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Autowired
    private JwtIntetceptor jwtIntetceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtIntetceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/**/login/**");
    }
}
