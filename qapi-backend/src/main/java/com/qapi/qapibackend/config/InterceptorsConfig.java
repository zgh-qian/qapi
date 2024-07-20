package com.qapi.qapibackend.config;

import com.qapi.qapibackend.interceptors.GlobalInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 */
@Configuration
public class InterceptorsConfig implements WebMvcConfigurer {

    /**
     * @param registry 拦截器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加需要拦截的路径
        String[] includePathPatterns = new String[]{
                "/**"
        };
        // 排除不需要拦截的路径
        String[] excludePathPatterns = new String[]{
                "/api/doc.html",
                "/api/swagger-ui.html",
                "/api/swagger-resources/**",
                "/api/webjars/**",
        };
        // 添加全局拦截器
        registry.addInterceptor(new GlobalInterceptor())
                .addPathPatterns(includePathPatterns) // 添加需要拦截的路径
                .excludePathPatterns(excludePathPatterns);// 排除不需要拦截的路径
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
