package com.company.student.app.controller;

import com.company.student.app.dto.*;
import com.company.student.app.service.SuperAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/super-admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SystemAdminController {
    private final SuperAdminService superAdminService;

    @PostMapping("/create-university")
    public ResponseEntity<HttpApiResponse<Long>> createUniversity(
            @Valid @RequestBody UniversityCreateRequestDto dto) {

        HttpApiResponse<Long> response = superAdminService.createUniversity(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/university/{universityId}/create-admin")
    public ResponseEntity<HttpApiResponse<Long>> createUniversityAdmin(
            @Valid @RequestBody UniversityAdminRequest request,
            @PathVariable Long universityId) {

        HttpApiResponse<Long> response =
                superAdminService.createUniversityAdmin(request, universityId);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<HttpApiResponse<SuperAdminResponse>> getProfile() {
        HttpApiResponse<SuperAdminResponse> response = superAdminService.getProfile();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<HttpApiResponse<UserMeResponse>> getCurrentUser(Authentication authentication) {
        HttpApiResponse<UserMeResponse> response = superAdminService.getMe(authentication);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/getAll-university")
    public ResponseEntity<HttpApiResponse<Page<UniversityResponse>>> getAllUniversity(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        HttpApiResponse<Page<UniversityResponse>> response =
                superAdminService.getAllUniversity(PageRequest.of(page, size));

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-all-user-count")
    public ResponseEntity<HttpApiResponse<Integer>> getAllUserCountInSystem() {
        HttpApiResponse<Integer> response = superAdminService.getAllUserCountInSystem();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/university/{universityId}")
    public ResponseEntity<HttpApiResponse<Boolean>> deleteUniversity(
            @PathVariable Long universityId) {

        HttpApiResponse<Boolean> response = superAdminService.deleteUniversity(universityId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<HttpApiResponse<Boolean>> updateProfile(@RequestBody SystemAdminUpdateRequest request) {
        HttpApiResponse<Boolean> response = superAdminService.updateProfile(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @PutMapping("/update-password")
    public ResponseEntity<HttpApiResponse<Boolean>> updatePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) {
        HttpApiResponse<Boolean> response = superAdminService.updatePassword(oldPassword, newPassword);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


}
