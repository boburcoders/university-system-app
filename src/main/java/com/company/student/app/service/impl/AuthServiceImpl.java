package com.company.student.app.service.impl;

import com.company.student.app.config.security.jwt.JwtService;
import com.company.student.app.dto.auth.TokeRequestDto;
import com.company.student.app.dto.auth.TokenResponseDto;
import com.company.student.app.dto.response.HttpApiResponse;
import com.company.student.app.dto.twoFA.VerifyTwoFactorRequest;
import com.company.student.app.dto.university.UniversityShortResponse;
import com.company.student.app.model.AuthUser;
import com.company.student.app.model.University;
import com.company.student.app.model.UniversityUserRole;
import com.company.student.app.repository.AuthUserRepository;
import com.company.student.app.repository.UniversityRepository;
import com.company.student.app.repository.UniversityUserRoleRepository;
import com.company.student.app.service.AuthService;
import com.company.student.app.service.SecretEncryptionService;
import com.company.student.app.service.TotpService;
import com.company.student.app.service.mapper.UniversityMapper;
import com.company.student.app.utils.Translator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final Translator translator;
    private final TotpService totpService;
    private final SecretEncryptionService encryptionService;


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

        if (!passwordEncoder.matches(password, authUser.getPassword())) {
            throw new IllegalArgumentException("username.or.password.incorrect");
        }

        // 2FA yoqilgan bo'lsa full token bermaymiz
        if (Boolean.TRUE.equals(authUser.getTwoFactorEnabled())) {
            Map<String, Object> tempClaims = Map.of(
                    "userId", authUser.getId(),
                    "universityId", organisationId,
                    "role", userRole.getRole().name(),
                    "type", "MFA_PENDING"
            );

            String tempToken = jwtService.generateTempToken(authUser.getUsername(), tempClaims);

            TokenResponseDto responseDto = TokenResponseDto.builder()
                    .requiresTwoFactor(true)
                    .tempToken(tempToken)
                    .build();

            return HttpApiResponse.<TokenResponseDto>builder()
                    .success(true)
                    .status(200)
                    .message("2fa.required")
                    .data(responseDto)
                    .build();
        }

        Map<String, Object> claims = Map.of(
                "userId", authUser.getId(),
                "universityId", organisationId,
                "role", userRole.getRole().name(),
                "type", "ACCESS"
        );

        TokenResponseDto responseDto = TokenResponseDto.builder()
                .requiresTwoFactor(false)
                .accessToken(jwtService.generateAccessToken(authUser.getUsername(), claims))
                .refreshToken(jwtService.generateRefreshToken(authUser.getUsername(), claims))
                .build();

        return HttpApiResponse.<TokenResponseDto>builder()
                .success(true)
                .status(200)
                .message(translator.toLocale("user.login.successfully"))
                .data(responseDto)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<TokenResponseDto> verifyTwoFactor(VerifyTwoFactorRequest dto) {
        String tempToken = dto.getTempToken();
        String code = dto.getCode();

        if (!jwtService.isTempToken(tempToken)) {
            throw new IllegalArgumentException("invalid.temp.token");
        }

        String username = jwtService.extractUsername(tempToken);
        Long userId = jwtService.extractUserId(tempToken);
        Long universityId = jwtService.extractUniversityId(tempToken);
        String role = jwtService.extractRole(tempToken);

        AuthUser authUser = authUserRepository.findByIdAndDeletedAtIsNull(userId, universityId)
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));

        if (!Boolean.TRUE.equals(authUser.getTwoFactorEnabled())) {
            throw new IllegalArgumentException("2fa.not.enabled");
        }

        if (authUser.getTwoFactorLockedUntil() != null &&
                authUser.getTwoFactorLockedUntil().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("2fa.temporarily.locked");
        }

        String secret = encryptionService.decrypt(authUser.getTwoFactorSecret());
        boolean valid = totpService.verifyCode(secret, code);

        if (!valid) {
            int attempts = authUser.getTwoFactorFailedAttempts() + 1;
            authUser.setTwoFactorFailedAttempts(attempts);

            if (attempts >= 5) {
                authUser.setTwoFactorLockedUntil(LocalDateTime.now().plusMinutes(5));
                authUser.setTwoFactorFailedAttempts(0);
            }

            authUserRepository.save(authUser);
            throw new IllegalArgumentException("invalid.2fa.code");
        }

        authUser.setTwoFactorFailedAttempts(0);
        authUser.setTwoFactorLockedUntil(null);
        authUserRepository.save(authUser);

        Map<String, Object> claims = Map.of(
                "userId", authUser.getId(),
                "universityId", universityId,
                "role", role,
                "type", "ACCESS"
        );

        TokenResponseDto responseDto = TokenResponseDto.builder()
                .requiresTwoFactor(false)
                .accessToken(jwtService.generateAccessToken(username, claims))
                .refreshToken(jwtService.generateRefreshToken(username, claims))
                .tempToken(null)
                .build();

        return HttpApiResponse.<TokenResponseDto>builder()
                .success(true)
                .status(200)
                .message("2fa.verified.successfully")
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
