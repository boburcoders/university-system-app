package com.company.student.app.service;


import com.company.student.app.config.security.jwt.JwtService;
import com.company.student.app.dto.twoFA.TwoFactorConfirmRequest;
import com.company.student.app.dto.twoFA.TwoFactorSetupResponse;
import com.company.student.app.model.AuthUser;
import com.company.student.app.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TwoFactorService {

    private final AuthUserRepository authUserRepository;
    private final TotpService totpService;
    private final SecretEncryptionService encryptionService;

    @Transactional
    public TwoFactorSetupResponse setup(AuthUser user) {
        String secret = totpService.generateSecret();
        user.setTwoFactorTempSecret(encryptionService.encrypt(secret));
        authUserRepository.save(user);

        String otpAuthUrl = totpService.buildOtpAuthUrl(
                "Student Management App",
                user.getUsername(),
                secret
        );

        return TwoFactorSetupResponse.builder()
                .secret(secret)
                .otpAuthUrl(otpAuthUrl)
                .build();
    }

    @Transactional
    public void confirm(AuthUser user, TwoFactorConfirmRequest request) {
        if (user.getTwoFactorTempSecret() == null) {
            throw new IllegalArgumentException("2FA setup not started");
        }

        String tempSecret = encryptionService.decrypt(user.getTwoFactorTempSecret());
        boolean valid = totpService.verifyCode(tempSecret, request.code());

        if (!valid) {
            throw new IllegalArgumentException("Invalid 2FA code");
        }

        user.setTwoFactorSecret(user.getTwoFactorTempSecret());
        user.setTwoFactorTempSecret(null);
        user.setTwoFactorEnabled(true);
        user.setTwoFactorConfirmedAt(LocalDateTime.now());
        user.setTwoFactorFailedAttempts(0);
        user.setTwoFactorLockedUntil(null);

        authUserRepository.save(user);
    }

    @Transactional
    public void disable(AuthUser user, String code) {
        if (!Boolean.TRUE.equals(user.getTwoFactorEnabled())) {
            throw new IllegalArgumentException("2FA already disabled");
        }

        String secret = encryptionService.decrypt(user.getTwoFactorSecret());
        boolean valid = totpService.verifyCode(secret, code);

        if (!valid) {
            throw new IllegalArgumentException("Invalid 2FA code");
        }

        user.setTwoFactorEnabled(false);
        user.setTwoFactorSecret(null);
        user.setTwoFactorTempSecret(null);
        user.setTwoFactorConfirmedAt(null);
        user.setTwoFactorFailedAttempts(0);
        user.setTwoFactorLockedUntil(null);

        authUserRepository.save(user);
    }

}
