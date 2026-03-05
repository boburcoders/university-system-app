package com.company.student.app.service;

import com.company.student.app.dto.auth.TokeRequestDto;
import com.company.student.app.dto.auth.TokenResponseDto;
import com.company.student.app.dto.response.HttpApiResponse;
import com.company.student.app.dto.university.UniversityShortResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthService {
    HttpApiResponse<TokenResponseDto> login(@Valid TokeRequestDto dto);

    HttpApiResponse<List<UniversityShortResponse>> getUniversitiesShortInfo();
}
