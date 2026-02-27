package com.company.student.app.service.impl;

import com.company.student.app.config.security.TenantContext;
import com.company.student.app.config.security.UserSession;
import com.company.student.app.dto.*;
import com.company.student.app.model.*;
import com.company.student.app.repository.*;
import com.company.student.app.service.StudentProfileService;
import com.company.student.app.service.mapper.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Primary
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final StudentProfileMapper studentProfileMapper;
    private final UserSession userSession;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final TimeTableRepository timeTableRepository;
    private final TimeTableMapper timeTableMapper;
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final UniversityUserRoleRepository userRoleRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final TeacherProfileMapper teacherProfileMapper;
    private final LessonMaterialRepository lessonMaterialRepository;
    private final LessonMaterialMapper lessonMaterialMapper;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;

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

    private StudentProfile getCurrentStudent() {
        return studentProfileRepository.findByUserIdAndOrganizationId(userSession.userId(), userSession.universityId()).
                orElseThrow(() -> new EntityNotFoundException("user.not.found"));
    }

    @Override
    public HttpApiResponse<StudentProfileResponse> getProfile() {
        StudentProfile profile = getCurrentStudent();

        return HttpApiResponse.<StudentProfileResponse>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(studentProfileMapper.toProfileResponse(profile))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public HttpApiResponse<Page<CourseResponseDto>> getStudentCourses(Pageable pageable) {
        Long id = getCurrentStudent().getId();
        Page<Course> coursePage =
                courseRepository.findAllCoursesByStudentIdAndOrganizationId(id, userSession.universityId(), pageable);

        return HttpApiResponse.<Page<CourseResponseDto>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(coursePage.map(courseMapper::mapToCourseResponse))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public HttpApiResponse<Page<LessonResponse>> getCourseLessons(Pageable pageable, Long courseId) {
        Page<Lesson> lessonPage = lessonRepository.findAllByCourseId(userSession.universityId(), courseId, pageable);

        return HttpApiResponse.<Page<LessonResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(lessonPage.map(lessonMapper::mapToLessonResponse))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public HttpApiResponse<List<LessonMaterialResponse>> getLessonMaterials(Long lessonId) {
        List<LessonMaterial> lessonMaterials = lessonMaterialRepository.findAllByLessonIdAndOrganisationId(lessonId, userSession.universityId());
        if (lessonMaterials.isEmpty())
            return HttpApiResponse.<List<LessonMaterialResponse>>builder()
                    .success(false)
                    .status(404)
                    .message("materail.not.found")
                    .build();
        return HttpApiResponse.<List<LessonMaterialResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(lessonMaterials.stream().map(lessonMaterialMapper::mapToLessonMaterialResponse).toList())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<Page<AttendanceResponse>> getStudentAttendances(
            Pageable pageable, Long lessonId, Long courseId) {

        if (lessonId == null && courseId == null) {
            throw new IllegalArgumentException("courseId.or.lessonId.must.be.given");
        }

        if (lessonId != null) {
            Attendance attendance = attendanceRepository
                    .findByLessonIdAndStudentIdAndOrganizationId(
                            lessonId,
                            getCurrentStudent().getId(),
                            userSession.universityId())
                    .orElseThrow(() -> new EntityNotFoundException("attendance.not.found"));

            AttendanceResponse response = attendanceMapper.mapToAttendanceResponse(attendance);

            Page<AttendanceResponse> page =
                    new PageImpl<>(List.of(response), pageable, 1);

            return HttpApiResponse.<Page<AttendanceResponse>>builder()
                    .success(true)
                    .status(200)
                    .message("ok")
                    .data(page)
                    .build();
        }

        // courseId bo‘yicha list qaytarish
        Page<Attendance> attendances =
                attendanceRepository.findAllByCourseIdAndStudentIdAndOrganizationId(
                        courseId,
                        getCurrentStudent().getId(),
                        userSession.universityId(),
                        pageable);

        Page<AttendanceResponse> responsePage =
                attendances.map(attendanceMapper::mapToAttendanceResponse);

        return HttpApiResponse.<Page<AttendanceResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(responsePage)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<List<GroupShortResponse>> getAllGroupShortResponse() {
        List<Group> groupList = groupRepository.getAllByOrganizationId(userSession.universityId());

        return HttpApiResponse.<List<GroupShortResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(groupList.stream().map(groupMapper::mapToShortResponse).toList())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public HttpApiResponse<List<TeacherResponse>> getAllTeacher() {
        Long universityId = userSession.universityId();
        List<TeacherProfile> teacherProfileList = teacherProfileRepository.findAllByOrganizationIdAndDeletedAtIsNull(universityId);

        return HttpApiResponse.<List<TeacherResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(teacherProfileList.stream().map(teacherProfileMapper::mapToTeacherResponse).toList())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public HttpApiResponse<List<TimeTableResponse>> getTimeTable(Long teacherId, Long groupId) {
        Long universityId = userSession.universityId();

        if (teacherId == null && groupId == null) {
            throw new IllegalArgumentException("teacherId yoki groupId berilishi kerak");
        }

        if (teacherId != null && groupId != null) {
            throw new IllegalArgumentException("Faqat bittasi berilishi mumkin: teacherId yoki groupId");
        }

        List<TimeTable> timeTables;

        if (teacherId != null) {
            timeTables = timeTableRepository
                    .findAllByOrganizationIdAndTeacherIdAndDeletedAtIsNull(
                            universityId, teacherId);
        } else {
            timeTables = timeTableRepository
                    .findAllByOrganizationIdAndGroupIdAndDeletedAtIsNull(
                            universityId, groupId);
        }

        List<TimeTableResponse> responseList =
                timeTableMapper.mapToResponseList(timeTables);

        return HttpApiResponse.<List<TimeTableResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(responseList)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> updateProfile(StudentProfileUpdateRequest request) {
        StudentProfile studentProfile = getCurrentStudent();

        studentProfileMapper.updateProfile(studentProfile, request);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(true)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> updatePassword(String oldPassword, String newPassword) {
        AuthUser authUser = authUserRepository.findByIdAndDeletedAtIsNull(userSession.userId())
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));
        if (!passwordEncoder.matches(oldPassword, authUser.getPassword()))
            throw new IllegalArgumentException("password.incorrect");

        authUser.setPassword(passwordEncoder.encode(newPassword));

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(true)
                .build();
    }
}
