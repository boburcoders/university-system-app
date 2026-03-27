package com.company.student.app.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomAuthenticationDetails {
    private String ip;
    private String userAgent;
    private String deviceKey;
}
