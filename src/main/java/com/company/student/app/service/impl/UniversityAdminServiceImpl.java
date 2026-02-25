package com.company.student.app.service.impl;

import com.company.student.app.config.security.TenantContext;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final LessonRepository lessonRepository;
    private final LessonMaterialRepository lessonMaterialRepository;
    private final TimeTableRepository timeTableRepository;


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

    @Override
    @Transactional(readOnly = true)
    public HttpApiResponse<UniversityAdminProfileResponse> getUniversityAdminProfile() {
        UniversityAdminProfile adminProfile = universityAdminProfileRepository.findByUserIdAndDeletedIsNull(userSession.userId(), userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));

        return HttpApiResponse.<UniversityAdminProfileResponse>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(universityAdminMapper.mapToProfileResponse(adminProfile))
                .build();
    }

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

    @Override
    @Transactional
    public HttpApiResponse<Boolean> createCourse(Long facultyId, CourseRequestDto requestDto) {
        Faculty faculty = facultyRepository.findByIdAndOrganizationId(facultyId, userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));
        if (courseRepository.existsByCodeAndOrganizationIdAndDeletedAtIsNull(requestDto.getCode(), userSession.universityId())) {
            throw new IllegalArgumentException("code.already.exsists");
        }
        Course course = courseMapper.mapToEntity(requestDto);
        course.setOrganizationId(userSession.universityId());
        course.setFaculty(faculty);

        courseRepository.save(course);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(201)
                .message("ok")
                .data(true)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> createDepartment(DepartmentRequestDto requestDto) {
        University university = universityRepository.findByIdAndDeletedAtIsNull(userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("university.not.found"));

        if (departmentRepository.existsByNameAndOrganizationIdAndDeletedAtIsNull(requestDto.getName(), userSession.universityId())) {
            throw new IllegalArgumentException("name.already.exists");
        }

        Department department = departmentMapper.mapToEntity(requestDto);
        department.setUniversity(university);
        department.setOrganizationId(university.getId());

        departmentRepository.save(department);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(201)
                .message("ok")
                .data(true)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> createGroup(Long facultyId, GroupRequestDto requestDto) {
        Faculty faculty = facultyRepository.findByIdAndOrganizationId(facultyId, userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));
        Group group = groupMapper.mapToEntity(requestDto);
        group.setOrganizationId(userSession.universityId());
        group.setFaculty(faculty);

        groupRepository.save(group);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(201)
                .message("ok")
                .data(true)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> createFaculty(Long departmentId, FacultyRequestDto requestDto) {
        Department department = departmentRepository.findByIdAndOrganisationId(departmentId, userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("department.not.found"));

        if (facultyRepository.existsByNameAndOrganizationIdAndDeletedAtIsNull(requestDto.getName(), userSession.universityId()))
            throw new IllegalArgumentException("name.already.exists");

        Faculty faculty = facultyMapper.mapToEntity(requestDto);
        faculty.setDepartment(department);
        faculty.setOrganizationId(userSession.universityId());

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(201)
                .message("ok")
                .data(true)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public HttpApiResponse<Page<DepartmentResponse>> getAllDepartmentInUniversity(Pageable pageable) {
        Long universityId = userSession.universityId();
        Page<Department> departmentPage = departmentRepository.findAllByOrganizationId(universityId, pageable);

        return HttpApiResponse.<Page<DepartmentResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(departmentPage.map(departmentMapper::mapToResponse))
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
        if (universityRepository.existsUniversitiesByCodeAndDeletedAtIsNull(request.getCode())) {
            throw new IllegalArgumentException("code.already.exists");
        }
        if (universityRepository.existsUniversitiesByNameAndDeletedAtIsNull(request.getName())) {
            throw new IllegalArgumentException("name.already.exists");
        }
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

    @Override
    @Transactional(readOnly = true)
    public HttpApiResponse<StatisticResponse> getStatisticsDashboard() {
        Long universityId = userSession.universityId();
        Integer studentCount = studentProfileRepository.countByOrganizationIdAndDeletedAtIsNull(universityId);
        Integer courseCount = courseRepository.countByOrganizationIdAndDeletedAtIsNull(universityId);
        Integer teacherCount = teacherProfileRepository.countByOrganizationIdAndDeletedAtIsNull(universityId);
        Integer facultyCount = facultyRepository.countByOrganizationIdAndDeletedAtIsNull(universityId);
        Integer groupCount = groupRepository.countByOrganizationIdAndDeletedAtIsNull(universityId);
        Integer departmentCount = departmentRepository.countByOrOrganizationIdAndDeletedAtIsNull(universityId);

        StatisticResponse response = StatisticResponse.builder()
                .studentCount(studentCount)
                .courseCount(courseCount)
                .teacherCount(teacherCount)
                .facultyCount(facultyCount)
                .groupCount(groupCount)
                .departmentCount(departmentCount)
                .build();

        return HttpApiResponse.<StatisticResponse>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(response)
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
    @Transactional(readOnly = true)
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

    @Override
    @Transactional
    public HttpApiResponse<Boolean> updateDepartment(DepartmentUpdateRequest request, Long departmentId) {
        Department department = departmentRepository.findByIdAndOrganisationId(departmentId, userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("department.not.found"));
        if (request.getName() != null) {
            if (departmentRepository.existsByNameAndOrganizationIdAndDeletedAtIsNull(request.getName(), userSession.universityId())) {
                throw new IllegalArgumentException("name.already.exists");
            }
        }
        departmentMapper.updateEntity(department, request);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(true)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> updateFaculty(FacultyUpdateRequest request, Long facultyId) {
        Faculty faculty = facultyRepository.findByIdAndOrganizationId(facultyId, userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("faculty.not.found"));
        if (request.getName() != null) {
            if (facultyRepository.existsByNameAndOrganizationIdAndDeletedAtIsNull(request.getName(), userSession.universityId())) {
                throw new IllegalArgumentException("name.already.exists");
            }
        }
        facultyMapper.updateEntity(faculty, request);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(true)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> removeStudent(Long studentId) {
        StudentProfile profile = studentProfileRepository.findByIdAndOrganizationId(studentId, userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));

        UniversityUserRole userRole = userRoleRepository.findByUserIdAndOrganizationId(profile.getUser().getId(), userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("role.not.found"));

        profile.setDeletedAt(LocalDateTime.now());
        profile.getUser().setDeletedAt(LocalDateTime.now());
        userRole.setDeletedAt(LocalDateTime.now());

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(true)
                .build();
    }

    @Transactional
    @Override
    public HttpApiResponse<Boolean> removeCourse(Long courseId) {

        Course course = courseRepository
                .findByIdAndOrganisationId(courseId, userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("course.not.found"));

        // 🔥 bulk delete (FAST)
        lessonMaterialRepository.softDeleteMaterialsByCourseId(courseId);
        lessonRepository.softDeleteLessonsByCourseId(courseId);

        course.setDeletedAt(LocalDateTime.now());

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("course.deleted")
                .data(true)
                .build();
    }

    @Transactional
    @Override
    public HttpApiResponse<Boolean> removeDepartment(Long departmentId) {

        Department department = departmentRepository
                .findByIdAndOrganisationId(departmentId, userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("department.not.found"));

        lessonMaterialRepository.softDeleteMaterialsByDepartment(departmentId);
        lessonRepository.softDeleteLessonsByDepartment(departmentId);
        courseRepository.softDeleteCoursesByDepartment(departmentId);
        facultyRepository.softDeleteFacultyByDepartment(departmentId);

        department.setDeletedAt(LocalDateTime.now());

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("department.deleted")
                .data(true)
                .build();
    }

    @Transactional
    @Override
    public HttpApiResponse<Boolean> removeFaculty(Long facultyId) {

        Long orgId = userSession.universityId();

        Faculty faculty = facultyRepository
                .findByIdAndOrganizationId(facultyId, orgId)
                .orElseThrow(() -> new EntityNotFoundException("faculty.not.found"));

        // 🔥 BULK SOFT DELETE (ultra fast)
        lessonMaterialRepository.softDeleteMaterialsByFaculty(facultyId);
        lessonRepository.softDeleteLessonsByFaculty(facultyId);
        courseRepository.softDeleteCoursesByFaculty(facultyId);
        groupRepository.softDeleteGroupsByFaculty(facultyId);

        faculty.setDeletedAt(LocalDateTime.now());

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("faculty.deleted")
                .data(true)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> removeGroup(Long groupId) {
        Group group = groupRepository.findByIdAndOrganizationId(groupId, userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("group.not.found"));
        List<TimeTable> timeTableList =
                timeTableRepository.findAllByGroupIdAndOrganizationIdAndDeletedAtIsNull(groupId, userSession.universityId());
        if (!timeTableList.isEmpty()) {
            for (TimeTable timeTable : timeTableList) {
                timeTable.setDeletedAt(LocalDateTime.now());
            }
        }
        group.setDeletedAt(LocalDateTime.now());

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("group.deleted")
                .data(true)
                .build();
    }
}
