package com.company.student.app.service;

import com.company.student.app.dto.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthService {
    HttpApiResponse<TokenResponseDto> login(@Valid TokeRequestDto dto);

    HttpApiResponse<UserMeResponse> getCurrentUser(Authentication authentication);

    HttpApiResponse<List<UniversityShortResponse>> getUniversitiesShortInfo();
}
