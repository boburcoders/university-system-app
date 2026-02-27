package com.company.student.app.controller;

import com.company.student.app.dto.*;
import com.company.student.app.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<HttpApiResponse<TokenResponseDto>> login(@RequestBody @Valid TokeRequestDto dto) {
        HttpApiResponse<TokenResponseDto> response = authService.login(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @GetMapping("/get-universities")
    public ResponseEntity<HttpApiResponse<List<UniversityShortResponse>>> getUniversitiesShortInfo() {
        HttpApiResponse<List<UniversityShortResponse>> response = authService.getUniversitiesShortInfo();
        return ResponseEntity.status(response.getStatus()).body(response);
    }


}
