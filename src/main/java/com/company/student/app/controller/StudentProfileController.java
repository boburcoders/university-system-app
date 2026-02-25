package com.company.student.app.controller;

import com.company.student.app.dto.*;
import com.company.student.app.service.StudentProfileService;
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
@RequestMapping("/api/student-profile")
@PreAuthorize("hasRole('STUDENT')")
public class StudentProfileController {
    private final StudentProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<HttpApiResponse<UserMeResponse>> getCurrentUser(Authentication authentication) {
        HttpApiResponse<UserMeResponse> response = profileService.getMe(authentication);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<HttpApiResponse<StudentProfileResponse>> getProfile() {
        HttpApiResponse<StudentProfileResponse> response = profileService.getProfile();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-course-lessons/{course_id}")
    public ResponseEntity<HttpApiResponse<Page<LessonResponse>>> getCourseLessons(
            @PathVariable Long course_id,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer sizeNumber
    ) {
        HttpApiResponse<Page<LessonResponse>> response
                = profileService.getCourseLessons(PageRequest.of(pageNumber, sizeNumber), course_id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-course")
    public ResponseEntity<HttpApiResponse<Page<CourseResponseDto>>> getStudentCourses(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        HttpApiResponse<Page<CourseResponseDto>> response
                = profileService.getStudentCourses(PageRequest.of(page, size));
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-attendances")
    public ResponseEntity<HttpApiResponse<Page<AttendanceResponse>>> getStudentAttendances(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        HttpApiResponse<Page<AttendanceResponse>> response
                = profileService.getStudentAttendances(PageRequest.of(page, size), startDate, endDate);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-all-group")
    public ResponseEntity<HttpApiResponse<List<GroupShortResponse>>> getAllGroup() {
        HttpApiResponse<List<GroupShortResponse>> response = profileService.getAllGroupShortResponse();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-all-teacher")
    public ResponseEntity<HttpApiResponse<List<TeacherResponse>>> getAllTeacher() {
        HttpApiResponse<List<TeacherResponse>> response = profileService.getAllTeacher();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-time-table")
    public ResponseEntity<HttpApiResponse<List<TimeTableResponse>>> getTimeTable(
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Long groupId
    ) {
        HttpApiResponse<List<TimeTableResponse>> response = profileService.getTimeTable(teacherId, groupId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @PutMapping("/update-profile")
    public ResponseEntity<HttpApiResponse<Boolean>> updateProfile(@RequestBody StudentProfileUpdateRequest request) {
        HttpApiResponse<Boolean> response = profileService.updateProfile(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update-password")
    public ResponseEntity<HttpApiResponse<Boolean>> updatePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) {
        HttpApiResponse<Boolean> response = profileService.updatePassword(oldPassword, newPassword);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
