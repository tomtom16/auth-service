package com.wiensquare.user.security;

import com.wiensquare.user.configuration.UserServiceAppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final UserServiceAppProperties appProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(appProperties.getCorsConfig().getAllowedOriginPatterns().toArray(new String[0]))
                .allowedMethods(appProperties.getCorsConfig().getAllowedMethods().toArray(new String[0]))
                .allowedHeaders(appProperties.getCorsConfig().getAllowedHeaders().toArray(new String[0]))
                .allowCredentials(appProperties.getCorsConfig().isAllowCredentials())
                .maxAge(appProperties.getCorsConfig().getMaxAge());
    }
}