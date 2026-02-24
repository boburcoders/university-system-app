package com.company.student.app.config.security;

import com.company.student.app.model.TeacherProfile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserSession {
    private UserPrincipal getPrincipal() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal user)) {
            throw new RuntimeException("No authenticated user found in session");
        }

        return user;
    }

    public Long userId() {
        return getPrincipal().getUserId();
    }

    public Long universityId() {
        return getPrincipal().getOrganisationId();
    }

    public String username() {
        return getPrincipal().getUsername();
    }

    public String role() {
        return getPrincipal()
                .getAuthorities()
                .stream()
                .findFirst()
                .map(a -> a.getAuthority())
                .orElse(null);
    }


    public UserPrincipal principal() {
        return getPrincipal();
    }

    public boolean isSuperAdmin() {
        return getPrincipal().getAuthorities()
                .stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_SUPER_ADMIN"));
    }

    public boolean isUniversityAdmin() {
        return getPrincipal().getAuthorities()
                .stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_UNIVERSITY_ADMIN"));
    }
}
