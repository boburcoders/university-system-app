package com.company.student.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentCreateRequest {
    @NotBlank(message = "email.must.not.be.null")
    private String email;
    @NotBlank(message = "username.must.not.be.null")
    private String username;
    @NotBlank(message = "password.must.not.be.null")
    private String password;
    @NotBlank(message = "password.must.not.be.null")
    private String phoneNumber;
}
