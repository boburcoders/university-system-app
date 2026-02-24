package com.company.student.app.config.security;

import com.company.student.app.model.AuthUser;
import com.company.student.app.model.UniversityUserRole;
import com.company.student.app.model.enums.UniversityRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {
    @Getter
    private final Long userId;
    @Getter
    private final Long organisationId;
    private final UniversityRole role;
    private final String username;
    private final String password;

    public UserPrincipal(AuthUser user, UniversityUserRole organRole) {
        this.userId = user.getId();
        this.organisationId = user.getOrganizationId();
        this.role = organRole.getRole();
        this.username = user.getUsername();
        this.password = user.getPassword();

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    @Override
    public  String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean canCreateUserWithRole(UniversityRole target) {
        return switch (this.role) {
            case SUPER_ADMIN -> true;
            case UNIVERSITY_ADMIN -> target == UniversityRole.STUDENT
                    || target == UniversityRole.TEACHER;
            default -> false;
        };
    }
}
