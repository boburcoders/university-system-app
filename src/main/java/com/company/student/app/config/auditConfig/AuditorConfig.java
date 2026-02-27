package com.company.student.app.config.auditConfig;

import com.company.student.app.config.security.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class AuditorConfig {
    private final UserSession userSession;

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> {
            try {
                return Optional.ofNullable(userSession.userId());
            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }
}
