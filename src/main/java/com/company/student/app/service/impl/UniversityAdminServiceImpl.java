package com.company.student.app.service.impl;

import com.company.student.app.config.security.UserSession;
import com.company.student.app.config.storage.MinioService;
import com.company.student.app.dto.*;
import com.company.student.app.model.*;
import com.company.student.app.model.enums.UniversityRole;
import com.company.student.app.repository.*;
import com.company.student.app.service.UniversityAdminService;
import com.company.student.app.service.mapper.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UniversityAdminServiceImpl implements UniversityAdminService {
    private final UniversityAdminProfileRepository universityAdminProfileRepository;
    private final AuthUserRepository authUserRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final UniversityAdminMapper universityAdminMapper;
    private final TeacherProfileRepository teacherProfileRepository;
    private final TeacherProfileMapper teacherProfileMapper;
    private final UserSession userSession;
    private final UniversityRepository universityRepository;
    private final UniversityUserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentProfileMapper studentProfileMapper;
    private final UniversityMapper universityMapper;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;
    private final MinioService minioService;

    @Override
    @Transactional
    public HttpApiResponse<Long> createTeacher(TeacherCreateRequest request) {
        Long organizationId = userSession.universityId();

        if (authUserRepository.existsByUsernameAndOrganizationIdAndDeletedAtIsNull(request.getEmail(), organizationId)) {
            throw new IllegalArgumentException("email.already.exsist");
        }
        if (teacherProfileRepository.existsTeacherProfileByEmail(request.getEmail())) {
            throw new IllegalArgumentException("email.already.exsist");
        }


        University university = universityRepository.findByIdAndDeletedAtIsNull(organizationId)
                .orElseThrow(() -> new EntityNotFoundException("university.not.found"));

        AuthUser user = AuthUser.builder()
                .username(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .organizationId(organizationId)
                .build();
        authUserRepository.saveAndFlush(user);

        TeacherProfile profile = TeacherProfile.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .user(user)
                .organizationId(organizationId)
                .build();
        teacherProfileRepository.save(profile);


        UniversityUserRole userRole = UniversityUserRole.builder()
                .user(user)
                .university(university)
                .role(UniversityRole.TEACHER)
                .organizationId(organizationId)
                .build();

        userRoleRepository.save(userRole);

        return HttpApiResponse.<Long>builder()
                .success(true)
                .message("ok")
                .status(201)
                .data(profile.getId())
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Long> createStudent(StudentCreateRequest request) {
        Long universityId = userSession.universityId();

        String email = request.getEmail();
        String studentNumber = request.getUsername();
        String password = request.getPassword();
        String phoneNumber = request.getPhoneNumber();

        if (studentProfileRepository.existsStudentProfileByEmail(email)) {
            throw new IllegalArgumentException("email.already.exist");
        }
        if (studentProfileRepository.existsStudentProfileByStudentNumberAndOrganizationId(studentNumber, universityId)) {
            throw new IllegalArgumentException("studentNumber.already.exist");
        }

        University university = universityRepository.findByIdAndDeletedAtIsNull(universityId)
                .orElseThrow(() -> new EntityNotFoundException("university.not.found"));

        AuthUser authUser = AuthUser.builder()
                .username(studentNumber)
                .password(passwordEncoder.encode(password))
                .organizationId(universityId)
                .build();
        authUserRepository.saveAndFlush(authUser);

        StudentProfile profile = StudentProfile.builder()
                .email(email)
                .user(authUser)
                .studentNumber(studentNumber)
                .organizationId(universityId)
                .phoneNumber(phoneNumber)
                .build();
        studentProfileRepository.save(profile);

        UniversityUserRole userRole = UniversityUserRole.builder()
                .organizationId(universityId)
                .role(UniversityRole.STUDENT)
                .user(authUser)
                .university(university)
                .build();
        userRoleRepository.save(userRole);

        return HttpApiResponse.<Long>builder()
                .success(true)
                .message("ok")
                .status(201)
                .data(profile.getId())
                .build();
    }

    @Override
    public HttpApiResponse<Boolean> createStudentByExcelFile(MultipartFile file) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public HttpApiResponse<Page<TeacherShortResponseDto>> getAllTeacherInUniversity(Pageable pageable) {
        Long universityId = userSession.universityId();
        Page<TeacherProfile> teacherPageList = teacherProfileRepository.findAllByUniversityId(universityId, pageable);

        return HttpApiResponse.<Page<TeacherShortResponseDto>>builder()
                .success(true)
                .message("ok")
                .status(200)
                .data(teacherPageList.map(teacherProfileMapper::mapToShortResponse))
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<Page<StudentShortResponse>> getAllStudentInUniversity(Pageable pageable) {
        Long universityId = userSession.universityId();

        Page<StudentProfile> studentProfilePage =
                studentProfileRepository.findAllByUniversityId(universityId, pageable);

        Page<StudentShortResponse> responseDto =
                studentProfilePage.map(studentProfileMapper::mapToStudentShortResponse);

        return HttpApiResponse.<Page<StudentShortResponse>>builder()
                .success(true)
                .message("ok")
                .status(200)
                .data(responseDto)
                .build();
    }

    @Transactional
    @Override
    public HttpApiResponse<Boolean> updateUniversity(UniversityUpdateRequest request) {
        Long universityId = userSession.universityId();
        University university = universityRepository.findByIdAndDeletedAtIsNull(universityId)
                .orElseThrow(() -> new EntityNotFoundException("university.not.found"));

        universityMapper.updateUniversity(university, request);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .message("university.updated")
                .status(200)
                .data(true)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> removeTeacher(Long teacherId) {

        Long universityId = userSession.universityId();

        TeacherProfile profile =
                teacherProfileRepository
                        .findByIdAndOrganizationIdAndDeletedAtIsNull(teacherId, universityId)
                        .orElseThrow(() -> new EntityNotFoundException("teacher.not.found"));

        AuthUser user = profile.getUser();

        UniversityUserRole userRole =
                userRoleRepository
                        .findByUserIdAndOrganizationId(user.getId(), universityId)
                        .orElseThrow(() -> new EntityNotFoundException("user.role.not.found"));

        LocalDateTime now = LocalDateTime.now();

        userRole.setDeletedAt(now);
        user.setDeletedAt(now);
        profile.setDeletedAt(now);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("teacher.removed")
                .data(true)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<Page<CourseResponseDto>> getAllCourseInUniversity(Pageable pageable) {
        Long universityId = userSession.universityId();

        Page<Course> coursePage =
                courseRepository.findAllByOrganizationId(universityId, pageable);

        Page<CourseResponseDto> responseDtoPage =
                coursePage.map(courseMapper::mapToCourseResponse);

        return HttpApiResponse.<Page<CourseResponseDto>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(responseDtoPage)
                .build();
    }

    @Override
    public HttpApiResponse<Page<GroupResponse>> getAllGroupsInUniversity(Pageable pageable) {
        Long universityId = userSession.universityId();

        Page<Group> groupPage = groupRepository.findAllByOrganisationId(universityId, pageable);

        Page<GroupResponse> groupResponses = groupPage.map(groupMapper::mapToResponse);

        return HttpApiResponse.<Page<GroupResponse>>builder()
                .success(true)
                .message("ok")
                .status(200)
                .data(groupResponses)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public HttpApiResponse<Page<FacultyResponse>> getAllFacultyInUniversity(Pageable pageable) {
        Long universityId = userSession.universityId();
        Page<Faculty> facultyPage = facultyRepository.findAllByOrganisationIdAndDeletedAtIsNull(universityId, pageable);

        Page<FacultyResponse> responsePage = facultyPage.map(facultyMapper::mapToResponse);

        return HttpApiResponse.<Page<FacultyResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(responsePage)
                .build();
    }

    @Transactional
    @Override
    public HttpApiResponse<Boolean> updateProfile(UniversityAdminUpdateRequest request) {
        Long universityId = userSession.universityId();
        UniversityAdminProfile adminProfile = getCurrentUniversityAdmin();
        if (request.getEmail() != null) {
            if (universityAdminProfileRepository.existsUniversityAdminProfileByEmail(request.getEmail())) {
                throw new IllegalArgumentException("email.already.exist");
            }

            if (authUserRepository.existsByUsernameAndOrganizationIdAndDeletedAtIsNull(request.getEmail(), universityId)) {
                throw new IllegalArgumentException("email.already.exist");

            }
        }
        adminProfile.getUser().setUsername(request.getEmail());
        universityAdminMapper.updateEntity(adminProfile, request);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("profile.updated")
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
            com.company.student.app.model.UniversityAdminProfile profile = getCurrentUniversityAdmin();
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

    @Transactional
    @Override
    public HttpApiResponse<Boolean> updatePassword(String oldPassword, String newPassword) {

        AuthUser authUser = authUserRepository.findByIdAndDeletedAtIsNull(userSession.userId())
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));

        if (!passwordEncoder.matches(oldPassword, authUser.getPassword())) {
            throw new IllegalArgumentException("password.incorrect");
        }
        authUser.setPassword(passwordEncoder.encode(newPassword));

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("password.updated")
                .data(true)
                .build();
    }

    private UniversityAdminProfile getCurrentUniversityAdmin() {
        return universityAdminProfileRepository.findByUserIdAndDeletedIsNull(userSession.userId(), userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));
    }
}
