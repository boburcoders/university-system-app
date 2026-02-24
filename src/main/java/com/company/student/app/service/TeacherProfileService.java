package com.company.student.app.service;

import com.company.student.app.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public interface TeacherProfileService {
    HttpApiResponse<TeacherProfileResponse> getTeacherProfile();

    HttpApiResponse<List<TimeTableResponse>> getTimeTable(LocalDate startDate, LocalDate endDate, DayOfWeek day);

    HttpApiResponse<Boolean> createLesson(LessonCreateRequest request);

    HttpApiResponse<Page<CourseResponseDto>> getTeacherCourses(Pageable pageable);

    HttpApiResponse<Page<LessonResponse>> getCourseLessons(Pageable pageable, Long courseId);

    HttpApiResponse<Page<GroupResponse>> getTeacherGroups(Pageable pageable);

    HttpApiResponse<Page<StudentAttendanceResponse>> getStudentsByGroupId(Long groupId, Pageable pageable);

    HttpApiResponse<Boolean> createAttendance(Long lessonId, List<AttendanceRequestDto> dto);

    HttpApiResponse<Boolean> updateProfile(TeacherUpdateRequest request);

    HttpApiResponse<Boolean> updatePassword(String oldPassword, String newPassword);

    HttpApiResponse<Boolean> createLessonMaterials(Long lessonId, LessonMaterialRequest request, List<MultipartFile> files) throws Exception;

    HttpApiResponse<List<LessonMaterialResponse>> getLessonMaterials(Long lessonId);

    HttpApiResponse<Boolean> uploadProfileImage(MultipartFile file);
}
