package com.company.student.app.service.impl;

import com.company.student.app.config.security.jwt.JwtService;
import com.company.student.app.dto.HttpApiResponse;
import com.company.student.app.dto.TokeRequestDto;
import com.company.student.app.dto.TokenResponseDto;
import com.company.student.app.dto.UniversityShortResponse;
import com.company.student.app.model.AuthUser;
import com.company.student.app.model.University;
import com.company.student.app.model.UniversityUserRole;
import com.company.student.app.repository.AuthUserRepository;
import com.company.student.app.repository.UniversityRepository;
import com.company.student.app.repository.UniversityUserRoleRepository;
import com.company.student.app.service.AuthService;
import com.company.student.app.service.mapper.UniversityMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthUserRepository authUserRepository;
    private final UniversityRepository universityRepository;
    private final UniversityUserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UniversityMapper universityMapper;

    @Override
    public HttpApiResponse<TokenResponseDto> login(TokeRequestDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        Long organisationId = dto.getOrganisationId();

        universityRepository.findByIdAndDeletedAtIsNull(organisationId)
                .orElseThrow(() -> new EntityNotFoundException("university.not.found"));

        AuthUser authUser = authUserRepository.findByUserName(username, organisationId)
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));

        UniversityUserRole userRole = userRoleRepository.findUserWithRole(username, organisationId)
                .orElseThrow(() -> new EntityNotFoundException("user.role.not.found"));

        if (!passwordEncoder.matches(password, (authUser.getPassword()))) {
            throw new IllegalArgumentException("username.or.password.incorrect");
        }

        Map<String, Object> claims = Map.of(
                "userId", authUser.getId(),
                "universityId", organisationId,
                "role", userRole.getRole().name()
        );

        TokenResponseDto responseDto = new TokenResponseDto(
                jwtService.generateAccessToken(authUser.getUsername(), claims),
                jwtService.generateRefreshToken(authUser.getUsername(), claims)
        );

        return HttpApiResponse.<TokenResponseDto>builder()
                .success(true)
                .status(200)
                .message("user.login.successfully")
                .data(responseDto)
                .build();
    }

    @Override
    public HttpApiResponse<List<UniversityShortResponse>> getUniversitiesShortInfo() {
        List<University> universityList = universityRepository.findAllUniversity();
        return HttpApiResponse.<List<UniversityShortResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(universityList.stream().map(universityMapper::mapToUniversityShortResponse).toList())
                .build();
    }
}
