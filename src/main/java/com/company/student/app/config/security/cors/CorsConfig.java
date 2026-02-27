package com.company.student.app.config.security.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Ruxsat etilgan originlar
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:5173/",
                "http://127.0.0.1:5500/test.html",
                "http://127.0.0.1:5500/"
        ));

        // Ruxsat etilgan metodlar
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE"
        ));

        // 🔥 MUHIM QATORLAR
        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type"
        ));

        configuration.setExposedHeaders(List.of(
                "Authorization"
        ));

        // Credentials ruxsat
        configuration.setAllowCredentials(true);

        // Preflight request cache time
        configuration.setMaxAge(3600L);

        // Exposed headers (agar kerak bo'lsa)
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
