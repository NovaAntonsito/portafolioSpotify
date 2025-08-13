package com.portafolio.spotifyweb.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class CorsFilter implements Filter {

    @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:8080}")
    private String[] allowedOrigins;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin = request.getHeader("Origin");
        
        // Log para debugging
        log.debug("CORS Filter - Origin: {}, Method: {}, URI: {}", 
                 origin, request.getMethod(), request.getRequestURI());

        // Verificar si el origen está permitido
        if (origin != null && isOriginAllowed(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", 
                             "Origin, X-Requested-With, Content-Type, Accept, Authorization");
            response.setHeader("Access-Control-Max-Age", "3600");
            
            log.debug("CORS headers added for allowed origin: {}", origin);
        } else if (origin != null) {
            log.warn("CORS request blocked from unauthorized origin: {}", origin);
        }

        // Handle preflight requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(req, res);
    }

    private boolean isOriginAllowed(String origin) {
        List<String> allowedList = Arrays.asList(allowedOrigins);
        
        // Verificación exacta
        if (allowedList.contains(origin)) {
            return true;
        }
        
        // Verificación de patrones (para localhost:*)
        for (String allowed : allowedList) {
            if (allowed.contains("*")) {
                String pattern = allowed.replace("*", ".*");
                if (origin.matches(pattern)) {
                    return true;
                }
            }
        }
        
        return false;
    }
}