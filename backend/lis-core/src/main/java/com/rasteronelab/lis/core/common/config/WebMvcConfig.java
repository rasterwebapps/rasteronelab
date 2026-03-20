package com.rasteronelab.lis.core.common.config;

import com.rasteronelab.lis.core.infrastructure.BranchInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Registers the BranchInterceptor for all API endpoints,
 * excluding infrastructure paths (actuator, health, swagger, api-docs).
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final BranchInterceptor branchInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(branchInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/actuator/**",
                        "/health/**",
                        "/api-docs/**",
                        "/swagger-ui/**"
                );
    }

    public WebMvcConfig(BranchInterceptor branchInterceptor) {
        this.branchInterceptor = branchInterceptor;
    }

}
