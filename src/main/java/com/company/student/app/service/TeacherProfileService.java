package com.company.student.app.service;

import com.company.student.app.dto.attedance.AttendanceRequestDto;
import com.company.student.app.dto.course.CourseResponseDto;
import com.company.student.app.dto.group.GroupResponse;
import com.company.student.app.dto.lesson.*;
import com.company.student.app.dto.response.HttpApiResponse;
import com.company.student.app.dto.response.UserMeResponse;
import com.company.student.app.dto.room.RoomResponseDto;
import com.company.student.app.dto.student.StudentAttendanceResponse;
import com.company.student.app.dto.teacher.TeacherProfileResponse;
import com.company.student.app.dto.teacher.TeacherUpdateRequest;
import com.company.student.app.dto.timetable.TimeTableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TeacherProfileService {
    HttpApiResponse<TeacherProfileResponse> getTeacherProfile();

    HttpApiResponse<List<TimeTableResponse>> getTimeTable(Long teacherId, Long groupId,Long roomId);

    HttpApiResponse<Boolean> createLesson(LessonCreateRequest request);

    HttpApiResponse<Page<CourseResponseDto>> getTeacherCourses(Pageable pageable);

    HttpApiResponse<Page<LessonResponse>> getCourseLessons(Pageable pageable, Long courseId);

    HttpApiResponse<Page<GroupResponse>> getTeacherGroups(Pageable pageable);

    HttpApiResponse<Page<StudentAttendanceResponse>> getStudentsByGroupId(Long groupId, Pageable pageable);

    HttpApiResponse<Boolean> createAttendance(Long lessonId, List<AttendanceRequestDto> dto);

    HttpApiResponse<Boolean> updateProfile(TeacherUpdateRequest request);

    HttpApiResponse<Boolean> updatePassword(String oldPassword, String newPassword);

    HttpApiResponse<Boolean> createLessonMaterials(Long lessonId, LessonMaterialRequest request, List<String> fileNames);

    HttpApiResponse<List<LessonMaterialResponse>> getLessonMaterials(Long lessonId);


    HttpApiResponse<UserMeResponse> getMe(Authentication authentication);

    HttpApiResponse<Page<RoomResponseDto>> getAllRoomInUniversity(Pageable pageable);

    HttpApiResponse<Page<LessonResponse>> getTeacherLessons(Pageable pageable);

    HttpApiResponse<Boolean> updateLessonMaterials(Long materialId, LessonMaterialUpdateRequest request,List<String> fileNames);

    HttpApiResponse<Boolean> updateLesson(Long lessonId, LessonUpdateRequest request);
}
