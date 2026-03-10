package com.company.student.app.dto.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponseDto {
    private Boolean requiresTwoFactor;
    private String accessToken;
    private String refreshToken;
    private String tempToken;
}
