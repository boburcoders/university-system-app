package com.company.student.app.service.impl;

import com.company.student.app.config.security.UserSession;
import com.company.student.app.config.storage.MinioService;
import com.company.student.app.dto.*;
import com.company.student.app.model.*;
import com.company.student.app.repository.*;
import com.company.student.app.service.StudentProfileService;
import com.company.student.app.service.mapper.CourseMapper;
import com.company.student.app.service.mapper.LessonMapper;
import com.company.student.app.service.mapper.StudentProfileMapper;
import com.company.student.app.service.mapper.TimeTableMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
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
    private final MinioService minioService;

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
    public HttpApiResponse<Page<AttendanceResponse>> getStudentAttendances(Pageable pageable, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public HttpApiResponse<List<TimeTableResponse>> getTimeTableByGroupId(Long groupId) {
        List<TimeTable> timeTables =
                timeTableRepository.findAllByGroupIdAndOrganizationIdAndDeletedAtIsNull(groupId, userSession.universityId());

        List<TimeTableResponse> responseList = timeTableMapper.mapToResponseList(timeTables);

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

            StudentProfile profile = getCurrentStudent();
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
