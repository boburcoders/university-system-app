package com.company.student.app.dto.twoFA;

import lombok.Builder;

@Builder
public record TwoFactorSetupResponse(
        String secret,
        String otpAuthUrl
) {
}
