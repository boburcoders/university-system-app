package com.company.student.app.controller;

import com.company.student.app.dto.*;
import com.company.student.app.service.TeacherProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teacher-profile")
public class TeacherProfileController {
    private final TeacherProfileService teacherProfileService;


    @GetMapping("/get-profile")
    public ResponseEntity<HttpApiResponse<TeacherProfileResponse>> getTeacherProfile() {
        HttpApiResponse<TeacherProfileResponse> response = teacherProfileService.getTeacherProfile();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-time-table")
    public ResponseEntity<HttpApiResponse<List<TimeTableResponse>>> getTimeTable(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) DayOfWeek day
    ) {
        HttpApiResponse<List<TimeTableResponse>> response = teacherProfileService.getTimeTable(startDate, endDate, day);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/create-lesson")
    public ResponseEntity<HttpApiResponse<Boolean>> createLesson(@RequestBody LessonCreateRequest request) {
        HttpApiResponse<Boolean> response = teacherProfileService.createLesson(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/create-lesson-materails/{lesson_id}")
    public ResponseEntity<HttpApiResponse<Boolean>> createLessonMaterials(
            @PathVariable Long lesson_id,
            @RequestBody LessonMaterialRequest request,
            @RequestParam List<MultipartFile> files) throws Exception {
        HttpApiResponse<Boolean> response = teacherProfileService.createLessonMaterials(lesson_id, request, files);
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

    @GetMapping("/get-lessons-materials/{lesson_id}")
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

    @PostMapping(value = "/upload-profile-image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpApiResponse<Boolean>> uploadProfileImage(@RequestParam MultipartFile file) {
        HttpApiResponse<Boolean> response = teacherProfileService.uploadProfileImage(file);
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
