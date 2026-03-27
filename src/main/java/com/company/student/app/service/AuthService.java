package com.company.student.app.service;

import com.company.student.app.dto.auth.TokeRequestDto;
import com.company.student.app.dto.auth.TokenResponseDto;
import com.company.student.app.dto.response.HttpApiResponse;
import com.company.student.app.dto.twoFA.VerifyTwoFactorRequest;
import com.company.student.app.dto.university.UniversityShortResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthService {
    HttpApiResponse<TokenResponseDto> login(@Valid TokeRequestDto dto, HttpServletRequest request);

    HttpApiResponse<List<UniversityShortResponse>> getUniversitiesShortInfo();

    HttpApiResponse<TokenResponseDto> verifyTwoFactor(@Valid VerifyTwoFactorRequest dto);
}
