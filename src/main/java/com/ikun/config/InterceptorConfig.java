package com.ikun.config;

import com.ikun.config.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    static final String[] SWAGGER_PATH = new String[]{
            "/swagger-resources/**",
            "/webjars/**",
            "/v3/**",
            "/swagger-ui.html/**"
    };

    static final String[] WHITE_LIST = new String[]{
            "/user/register",
            "/user/login",
    };


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器，排除swagger
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(WHITE_LIST)
                .excludePathPatterns(SWAGGER_PATH);
    }


}
