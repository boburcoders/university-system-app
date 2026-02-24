package com.company.student.app.config.security;

import com.company.student.app.model.AuthUser;
import com.company.student.app.model.University;
import com.company.student.app.model.UniversityUserRole;
import com.company.student.app.repository.AuthUserRepository;
import com.company.student.app.repository.UniversityRepository;
import com.company.student.app.repository.UniversityUserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final AuthUserRepository authUserRepository;
    private final UniversityRepository universityRepository;
    private final UniversityUserRoleRepository universityUserRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Long universityId = TenantContext.getTenantId();
        if (universityId == null) {
            throw new UsernameNotFoundException("Tenant not resolved");
        }
        return loadUserByUsernameAndUniversity(username, universityId);
    }


    public UserDetails loadUserByUsernameAndUniversity(String username, Long universityId) {
        universityRepository.findByIdAndDeletedAtIsNull(universityId)
                .orElseThrow(() -> new EntityNotFoundException("university.not.found"));

        authUserRepository.findByAuthUserNameAndOrganisationId(username, universityId)
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));

        UniversityUserRole userRole = universityUserRoleRepository.findUserWithRole(username, universityId)
                .orElseThrow(() -> new EntityNotFoundException("univer.role.not.found"));

        return new UserPrincipal(userRole.getUser(), userRole);
    }
}
