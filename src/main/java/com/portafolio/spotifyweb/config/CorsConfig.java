package com.portafolio.spotifyweb.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@Slf4j
public class CorsConfig implements WebMvcConfigurer {
    
    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;
    
    @Value("${cors.allowed-methods:GET}")
    private String[] allowedMethods;
    
    @Value("${cors.allowed-headers}")
    private String allowedHeaders;
    
    @Value("${cors.allow-credentials}")
    private boolean allowCredentials;
    
    @Value("${cors.max-age}")
    private long maxAge;
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/v1/api/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods(allowedMethods)
                .allowedHeaders(allowedHeaders)
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);
        
        log.info("CORS configured:");
        log.info("  - Allowed Origins: {}", Arrays.toString(allowedOrigins));
        log.info("  - Allowed Methods: {}", Arrays.toString(allowedMethods));
        log.info("  - Allow Credentials: {}", allowCredentials);
        log.info("  - Max Age: {} seconds", maxAge);
        
        // Log warning si hay @CrossOrigin en controladores
        log.warn("IMPORTANT: Make sure no @CrossOrigin annotations override this global config!");
    }
}