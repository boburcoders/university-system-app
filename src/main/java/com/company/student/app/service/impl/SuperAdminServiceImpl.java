package com.company.student.app.service.impl;

import com.company.student.app.config.security.UserSession;
import com.company.student.app.config.storage.MinioService;
import com.company.student.app.dto.*;
import com.company.student.app.model.*;
import com.company.student.app.model.enums.UniversityRole;
import com.company.student.app.repository.*;
import com.company.student.app.service.SuperAdminService;
import com.company.student.app.service.mapper.AuthUserMapper;
import com.company.student.app.service.mapper.SuperAdminMapper;
import com.company.student.app.service.mapper.UniversityAdminMapper;
import com.company.student.app.service.mapper.UniversityMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {
    private final SuperAdminRepository superAdminRepository;
    private final UniversityRepository universityRepository;
    private final AuthUserRepository authUserRepository;
    private final UniversityAdminProfileRepository universityAdminProfileRepository;
    private final UniversityUserRoleRepository universityUserRoleRepository;
    private final SuperAdminMapper superAdminMapper;
    private final UniversityAdminMapper universityAdminMapper;
    private final AuthUserMapper authUserMapper;
    private final UniversityMapper universityMapper;
    private final UserSession userSession;
    private final PasswordEncoder passwordEncoder;
    private final MinioService minioService;

    @Override
    @Transactional
    public HttpApiResponse<Long> createUniversityAdmin(UniversityAdminRequest request, Long universityId) {

        University university = universityRepository.findByIdAndDeletedAtIsNull(universityId)
                .orElseThrow(() -> new EntityNotFoundException("entity.not.found"));

        if (authUserRepository.existsByUsernameAndOrganizationIdAndDeletedAtIsNull(request.getEmail(), universityId)) {
            throw new IllegalArgumentException("email.already.exists");
        }
        if (universityAdminProfileRepository.existsUniversityAdminProfileByEmail(request.getEmail())) {
            throw new IllegalArgumentException("email.already.exists");
        }

        AuthUser authUser = AuthUser.builder()
                .username(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .organizationId(universityId)
                .build();
        authUserRepository.save(authUser);

        UniversityAdminProfile profile = universityAdminMapper.mapToEntity(request);
        profile.setOrganizationId(universityId);
        profile.setUser(authUser);
        universityAdminProfileRepository.save(profile);

        UniversityUserRole role = UniversityUserRole.builder()
                .user(authUser)
                .university(university)
                .organizationId(universityId)
                .role(UniversityRole.UNIVERSITY_ADMIN)
                .build();
        universityUserRoleRepository.save(role);

        return HttpApiResponse.<Long>builder()
                .success(true)
                .status(201)
                .message("univer.admin.created")
                .data(profile.getId())
                .build();
    }


    @Override
    public HttpApiResponse<SuperAdminResponse> getProfile() {

        SystemAdminProfile systemAdminProfile = getCurrentSystemAdmin();

        SuperAdminResponse response =
                superAdminMapper.mapToSuperAdminResponse(systemAdminProfile);
        response.setRole(UniversityRole.SUPER_ADMIN.name());

        return HttpApiResponse.<SuperAdminResponse>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("OK")
                .data(response)
                .build();
    }

    @Override
    public HttpApiResponse<Page<UniversityResponse>> getAllUniversity(Pageable pageable) {
        Page<University> universityPage = universityRepository.findAllByDeletedAtIsNull(pageable);

        Page<UniversityResponse> responsePage =
                universityPage.map(universityMapper::mapToUniversityResponse);

        return HttpApiResponse.<Page<UniversityResponse>>builder()
                .success(true)
                .status(200)
                .message("OK")
                .data(responsePage)
                .build();
    }

    @Override
    public HttpApiResponse<Boolean> deleteUniversity(Long universityId) {
        University university = universityRepository.findByIdAndDeletedAtIsNull(universityId)
                .orElseThrow(() -> new EntityNotFoundException("entity.not.found"));
        university.setDeletedAt(LocalDateTime.now());

        universityRepository.save(university);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("university.deleted.successfully")
                .data(true)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Long> createUniversity(UniversityCreateRequestDto dto) {
        if (universityRepository.existsUniversitiesByCodeAndDeletedAtIsNull(dto.getCode())) {
            throw new IllegalArgumentException("university.code.already.exists");
        }
        if (universityRepository.existsUniversitiesByNameAndDeletedAtIsNull((dto.getCode()))) {
            throw new IllegalArgumentException("university.code.already.exists");
        }
        University university = universityMapper.mapToEntity(dto);
        universityRepository.save(university);

        return HttpApiResponse.<Long>builder()
                .success(true)
                .status(201)
                .message("university.created.successfully")
                .data(university.getId())
                .build();
    }

    @Override
    public HttpApiResponse<Integer> getAllUserCountInSystem() {
        Integer count = Math.toIntExact(authUserRepository.count());

        return HttpApiResponse.<Integer>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(count)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> updatePassword(String oldPassword, String newPassword) {

        SystemAdminProfile systemAdminProfile = getCurrentSystemAdmin();

        String currentPassword = systemAdminProfile.getUser().getPassword();

        if (!passwordEncoder.matches(oldPassword, currentPassword)) {
            return HttpApiResponse.<Boolean>builder()
                    .success(false)
                    .status(400)
                    .message("password.incorrect")
                    .data(false)
                    .build();
        }

        systemAdminProfile.getUser().setPassword(passwordEncoder.encode(newPassword));
        superAdminRepository.save(systemAdminProfile);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("password.updated.successfully")
                .data(true)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> updateProfile(SystemAdminUpdateRequest request) {
        Long organizationId = userSession.universityId();

        SystemAdminProfile profile = getCurrentSystemAdmin();

        if (request.getEmail() != null) {
            if (superAdminRepository.existsSystemAdminProfileByEmailAndOrganizationId(request.getEmail(), organizationId)) {
                throw new IllegalArgumentException("email.already.exists");
            }
        }

        if (request.getPhoneNumber() != null) {
            if (superAdminRepository.existsSystemAdminProfileByPhoneNumber((request.getEmail()))) {
                throw new IllegalArgumentException("phoneNumber.already.exists");
            }
        }

        superAdminMapper.updateEntity(profile, request);

        if (request.getUsername() != null) {
            AuthUser user = profile.getUser();
            if (authUserRepository.existsByUsernameAndOrganizationIdAndDeletedAtIsNull(request.getUsername(), organizationId)) {
                throw new IllegalArgumentException("username.already.exist");
            }
            user.setUsername(request.getUsername());
        }
        superAdminRepository.save(profile);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("profile.update.successfully")
                .data(true)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> uploadProfileImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("file.is.empty");
        }
        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("it should be image");
        }
        try {
            String fileName = minioService.uploadFile(file);
            String fileUrl = minioService.getFileUrl(fileName);
            com.company.student.app.model.SystemAdminProfile profile = getCurrentSystemAdmin();
            profile.setAvatarUrl(fileUrl);

            return HttpApiResponse.<Boolean>builder()
                    .success(true)
                    .status(200)
                    .message("ok")
                    .data(true)
                    .build();
        } catch (Exception e) {
            return HttpApiResponse.<Boolean>builder()
                    .success(false)
                    .status(400)
                    .message("unable to upload image")
                    .build();
        }
    }

    private SystemAdminProfile getCurrentSystemAdmin() {
        Long userId = userSession.userId();
        Long universityId = userSession.universityId();
        return superAdminRepository.findByUserIdAndDeletedIsNull(userId, universityId)
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));
    }
}
