package com.company.student.app.config;

import com.company.student.app.model.AuthUser;
import com.company.student.app.model.SystemAdminProfile;
import com.company.student.app.model.University;
import com.company.student.app.model.UniversityUserRole;
import com.company.student.app.model.enums.UniversityRole;
import com.company.student.app.repository.AuthUserRepository;
import com.company.student.app.repository.SuperAdminRepository;
import com.company.student.app.repository.UniversityRepository;
import com.company.student.app.repository.UniversityUserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {
    private final UniversityRepository universityRepository;
    private final AuthUserRepository authUserRepository;
    private final UniversityUserRoleRepository roleRepository;
    private final SuperAdminRepository superAdminRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Transactional
    public ApplicationRunner initData() {
        return args -> {

            if (universityRepository.count() > 0) return;

            University university = University.builder()
                    .name("Super University")
                    .code("SUPER")
                    .description("System default university")
                    .build();

            universityRepository.save(university);

            AuthUser authUser = AuthUser.builder()
                    .username("superadmin")
                    .password(passwordEncoder.encode("123456")) // 🔐 hash
                    .organizationId(university.getId())
                    .build();
            authUserRepository.save(authUser);

            SystemAdminProfile profile = SystemAdminProfile.builder()
                    .email("bobur@gmail.com")
                    .phoneNumber("+99893555555")
                    .user(authUser)
                    .organizationId(university.getId())
                    .build();

            superAdminRepository.save(profile);


            UniversityUserRole userRole = UniversityUserRole.builder()
                    .user(authUser)
                    .university(university)
                    .role(UniversityRole.SUPER_ADMIN)
                    .organizationId(university.getId())
                    .build();

            roleRepository.save(userRole);

            log.info("🔥 DEFAULT SUPER ADMIN CREATED");
            log.info("username: superadmin");
            log.info("password: 123456");
        };
    }

}
