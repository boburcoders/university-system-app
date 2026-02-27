package com.company.student.app.service.impl;

import com.company.student.app.config.security.TenantContext;
import com.company.student.app.config.security.UserSession;
import com.company.student.app.dto.*;
import com.company.student.app.model.*;
import com.company.student.app.model.enums.UniversityRole;
import com.company.student.app.repository.*;
import com.company.student.app.service.SuperAdminService;
import com.company.student.app.service.mapper.AddressMapper;
import com.company.student.app.service.mapper.SuperAdminMapper;
import com.company.student.app.service.mapper.UniversityAdminMapper;
import com.company.student.app.service.mapper.UniversityMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private final UniversityMapper universityMapper;
    private final UserSession userSession;
    private final PasswordEncoder passwordEncoder;
    private final UniversityUserRoleRepository userRoleRepository;
    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final LessonMaterialRepository lessonMaterialRepository;
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final GroupRepository groupRepository;
    private final CourseAssignmentRepository courseAssignmentRepository;
    private final TimeTableRepository timeTableRepository;
    private final AddressMapper addressMapper;

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
    public HttpApiResponse<UserMeResponse> getMe(Authentication authentication) {
        Long universityId = TenantContext.getTenantId();

        AuthUser authUser = authUserRepository.findByUserName(authentication.getName(), universityId)
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));

        UniversityUserRole userRole = userRoleRepository.findUserWithRole(authentication.getName(), universityId)
                .orElseThrow(() -> new EntityNotFoundException("user.role.not.found"));

        UserMeResponse response = UserMeResponse.builder()
                .id(authUser.getId())
                .universityId(universityId)
                .username(authUser.getUsername())
                .role(userRole.getRole().name())
                .build();

        return HttpApiResponse.<UserMeResponse>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(response)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<SuperAdminResponse> getProfile() {

        SystemAdminProfile systemAdminProfile = getCurrentSystemAdmin();

        SuperAdminResponse response =
                superAdminMapper.mapToSuperAdminResponse(systemAdminProfile);

        Address address = systemAdminProfile.getUser().getAddress();
        if (address != null)
            response.setAddressResponseDto(addressMapper.mapToResponse(address));

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

    @Transactional
    @Override
    public HttpApiResponse<Boolean> deleteUniversity(Long universityId) {
        University university = universityRepository.findByIdAndDeletedAtIsNull(universityId)
                .orElseThrow(() -> new EntityNotFoundException("entity.not.found"));
        LocalDateTime now = LocalDateTime.now();

        lessonMaterialRepository.softDeleteByUniversity(universityId, now);
        lessonRepository.softDeleteByUniversity(universityId, now);
        courseRepository.softDeleteByUniversity(universityId, now);
        courseAssignmentRepository.softDeleteByUniversity(universityId, now);
        groupRepository.softDeleteByUniversity(universityId, now);
        timeTableRepository.softDeleteByUniversity(universityId, now);
        facultyRepository.softDeleteByUniversity(universityId, now);
        departmentRepository.softDeleteByUniversity(universityId, now);

        studentProfileRepository.softDeleteByUniversity(universityId, now);
        teacherProfileRepository.softDeleteByUniversity(universityId, now);
        universityAdminProfileRepository.softDeleteByUniversity(universityId, now);

        userRoleRepository.softDeleteByUniversity(universityId, now);
        authUserRepository.softDeleteByUniversity(universityId, now);

        university.setDeletedAt(now);

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

        // EMAIL
        if (request.getEmail() != null && !request.getEmail().equals(profile.getEmail())) {
            if (superAdminRepository.existsSystemAdminProfileByEmailAndOrganizationId(
                    request.getEmail(), organizationId)) {
                throw new IllegalArgumentException("email.already.exists");
            }
        }

        // PHONE
        if (request.getPhoneNumber() != null &&
                !request.getPhoneNumber().equals(profile.getPhoneNumber())) {
            if (superAdminRepository.existsSystemAdminProfileByPhoneNumber(
                    request.getPhoneNumber())) {
                throw new IllegalArgumentException("phoneNumber.already.exists");
            }
        }

        superAdminMapper.updateEntity(profile, request);

        // USERNAME
        if (request.getUsername() != null) {
            AuthUser user = profile.getUser();

            if (!request.getUsername().equals(user.getUsername())) {
                if (authUserRepository.existsByUsernameAndOrganizationIdAndDeletedAtIsNull(
                        request.getUsername(), organizationId)) {
                    throw new IllegalArgumentException("username.already.exist");
                }
                user.setUsername(request.getUsername());
            }
        }

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("profile.update.successfully")
                .data(true)
                .build();
    }


    private SystemAdminProfile getCurrentSystemAdmin() {
        Long userId = userSession.userId();
        Long universityId = userSession.universityId();
        return superAdminRepository.findByUserIdAndDeletedIsNull(userId, universityId)
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));
    }
}
