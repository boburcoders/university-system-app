package com.company.student.app.dto.twoFA;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerifyTwoFactorRequest {
    @NotBlank(message = "temp.token.required")
    private String tempToken;

    @NotBlank(message = "code.required")
    private String code;

}
