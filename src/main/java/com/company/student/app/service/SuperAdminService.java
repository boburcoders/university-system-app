package com.company.student.app.service;

import com.company.student.app.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    HttpApiResponse<Boolean> uploadProfileImage(MultipartFile file);
}
