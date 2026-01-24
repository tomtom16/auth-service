package com.wiensquare.user.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "user.service.app")
public class UserServiceAppProperties {

    private CorsConfig corsConfig;

    @Getter
    @Setter
    public static class CorsConfig {
        private List<String> allowedOriginPatterns;
        private List<String> allowedMethods;
        private List<String> allowedHeaders;
        private boolean allowCredentials;
        private long maxAge;
    }

}
