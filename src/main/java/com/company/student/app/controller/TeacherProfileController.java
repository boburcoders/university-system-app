package com.company.student.app.controller;

import com.company.student.app.dto.*;
import com.company.student.app.service.TeacherProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teacher-profile")
@PreAuthorize("hasRole('TEACHER')")
public class TeacherProfileController {
    private final TeacherProfileService teacherProfileService;


    @GetMapping("/me")
    public ResponseEntity<HttpApiResponse<UserMeResponse>> getCurrentUser(Authentication authentication) {
        HttpApiResponse<UserMeResponse> response = teacherProfileService.getMe(authentication);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-profile")
    public ResponseEntity<HttpApiResponse<TeacherProfileResponse>> getTeacherProfile() {
        HttpApiResponse<TeacherProfileResponse> response = teacherProfileService.getTeacherProfile();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-time-table")
    public ResponseEntity<HttpApiResponse<List<TimeTableResponse>>> getTimeTable(
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Long groupId
    ) {
        HttpApiResponse<List<TimeTableResponse>> response = teacherProfileService.getTimeTable(teacherId, groupId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/create-lesson")
    public ResponseEntity<HttpApiResponse<Boolean>> createLesson(@RequestBody LessonCreateRequest request) {
        HttpApiResponse<Boolean> response = teacherProfileService.createLesson(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping(value = "/create-lesson-materails/{lesson_id}")
    public ResponseEntity<HttpApiResponse<Boolean>> createLessonMaterials(
            @PathVariable Long lesson_id,
            @RequestBody LessonMaterialRequest request,
            @RequestParam List<String> fileNames) {
        HttpApiResponse<Boolean> response = teacherProfileService.createLessonMaterials(lesson_id, request, fileNames);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-teacher-courses")
    public ResponseEntity<HttpApiResponse<Page<CourseResponseDto>>> getTeacherCourses(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer sizeNumber
    ) {
        HttpApiResponse<Page<CourseResponseDto>> response
                = teacherProfileService.getTeacherCourses(PageRequest.of(pageNumber, sizeNumber));
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-course-lessons/{course_id}")
    public ResponseEntity<HttpApiResponse<Page<LessonResponse>>> getCourseLessons(
            @PathVariable Long course_id,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer sizeNumber
    ) {
        HttpApiResponse<Page<LessonResponse>> response
                = teacherProfileService.getCourseLessons(PageRequest.of(pageNumber, sizeNumber), course_id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/lessons-materials/{lesson_id}")
    public ResponseEntity<HttpApiResponse<List<LessonMaterialResponse>>> getLessonMaterials(
            @PathVariable Long lesson_id
    ) {
        HttpApiResponse<List<LessonMaterialResponse>> response
                = teacherProfileService.getLessonMaterials(lesson_id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-teacher-groups")
    public ResponseEntity<HttpApiResponse<Page<GroupResponse>>> getTeacherGroups(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer sizeNumber
    ) {
        HttpApiResponse<Page<GroupResponse>> response
                = teacherProfileService.getTeacherGroups(PageRequest.of(pageNumber, sizeNumber));
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-students-by-lesson/{group_id}")
    public ResponseEntity<HttpApiResponse<Page<StudentAttendanceResponse>>> getStudentsByGroupId(
            @PathVariable Long group_id,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer sizeNumber
    ) {
        HttpApiResponse<Page<StudentAttendanceResponse>> response
                = teacherProfileService.getStudentsByGroupId(group_id, PageRequest.of(pageNumber, sizeNumber));
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/create-attendence/{lesson_id}")
    public ResponseEntity<HttpApiResponse<Boolean>> createAttendance(
            @PathVariable Long lesson_id,
            @RequestBody List<AttendanceRequestDto> dto) {
        HttpApiResponse<Boolean> response = teacherProfileService.createAttendance(lesson_id, dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @PutMapping("/update-profile")
    public ResponseEntity<HttpApiResponse<Boolean>> updateProfile(@RequestBody TeacherUpdateRequest request) {
        HttpApiResponse<Boolean> response = teacherProfileService.updateProfile(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update-password")
    public ResponseEntity<HttpApiResponse<Boolean>> updatePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) {
        HttpApiResponse<Boolean> response = teacherProfileService.updatePassword(oldPassword, newPassword);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
