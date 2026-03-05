package com.company.student.app.service;

import com.company.student.app.dto.response.HttpApiResponse;
import com.company.student.app.dto.response.UserMeResponse;
import com.company.student.app.dto.systemAdmin.SuperAdminResponse;
import com.company.student.app.dto.systemAdmin.SystemAdminUpdateRequest;
import com.company.student.app.dto.univerAdmin.UniversityAdminRequest;
import com.company.student.app.dto.univerAdmin.UniversityCreateRequestDto;
import com.company.student.app.dto.university.UniversityResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface SuperAdminService {
    HttpApiResponse<Long> createUniversityAdmin(UniversityAdminRequest request, Long universityId);

    HttpApiResponse<SuperAdminResponse> getProfile();

    HttpApiResponse<Page<UniversityResponse>> getAllUniversity(Pageable pageable);

    HttpApiResponse<Boolean> deleteUniversity(Long universityId);

    HttpApiResponse<Long> createUniversity(@Valid UniversityCreateRequestDto dto);

    HttpApiResponse<Integer> getAllUserCountInSystem();

    HttpApiResponse<Boolean> updatePassword(String oldPassword, String newPassword);

    HttpApiResponse<Boolean> updateProfile(SystemAdminUpdateRequest request);

    HttpApiResponse<UserMeResponse> getMe(Authentication authentication);
}
