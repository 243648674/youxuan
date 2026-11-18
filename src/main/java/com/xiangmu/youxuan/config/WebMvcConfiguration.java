package com.xiangmu.youxuan.config;

import com.xiangmu.youxuan.interceptor.JwtTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private JwtTokenInterceptor jwtTokenInterceptor;
    public WebMvcConfiguration(JwtTokenInterceptor jwtTokenInterceptor){
        this.jwtTokenInterceptor=jwtTokenInterceptor;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/employee/employeeLogin","/user/userLogin","/user/insert");
    }
}
