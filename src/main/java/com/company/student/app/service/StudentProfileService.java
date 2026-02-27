package com.company.student.app.service;

import com.company.student.app.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public interface StudentProfileService {
    HttpApiResponse<StudentProfileResponse> getProfile();

    HttpApiResponse<Page<CourseResponseDto>> getStudentCourses(Pageable pageable);

    HttpApiResponse<Page<AttendanceResponse>> getStudentAttendances(Pageable pageable, Long lessonId, Long courseId);

    HttpApiResponse<Boolean> updateProfile(StudentProfileUpdateRequest request);

    HttpApiResponse<Boolean> updatePassword(String oldPassword, String newPassword);

    HttpApiResponse<Page<LessonResponse>> getCourseLessons(Pageable of, Long courseId);

    HttpApiResponse<List<TimeTableResponse>> getTimeTable(Long teacherId, Long groupId);

    HttpApiResponse<List<GroupShortResponse>> getAllGroupShortResponse();

    HttpApiResponse<UserMeResponse> getMe(Authentication authentication);

    HttpApiResponse<List<TeacherResponse>> getAllTeacher();

    HttpApiResponse<List<LessonMaterialResponse>> getLessonMaterials(Long lessonId);
}
