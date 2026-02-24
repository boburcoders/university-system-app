package com.company.student.app.controller;

import com.company.student.app.dto.*;
import com.company.student.app.service.UniversityAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/univer-admin")
public class UniversityAdminController {
    private final UniversityAdminService universityAdminService;

    @PostMapping("/add-teacher")
    public ResponseEntity<HttpApiResponse<Long>> createTeacher(@RequestBody @Valid TeacherCreateRequest request) {
        HttpApiResponse<Long> response = universityAdminService.createTeacher(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/add-student")
    public ResponseEntity<HttpApiResponse<Long>> createStudent(@RequestBody @Valid StudentCreateRequest request) {
        HttpApiResponse<Long> response = universityAdminService.createStudent(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping(value = "/add-student-by-excel")
    public ResponseEntity<HttpApiResponse<Boolean>> createStudentByExcelFile(@RequestParam MultipartFile file) {
        HttpApiResponse<Boolean> response = universityAdminService.createStudentByExcelFile(file);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-all-teacher")
    public ResponseEntity<HttpApiResponse<Page<TeacherShortResponseDto>>> getAllTeacherInUniversity(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer sizeNumber
    ) {
        HttpApiResponse<Page<TeacherShortResponseDto>> response
                = universityAdminService.getAllTeacherInUniversity(PageRequest.of(pageNumber, sizeNumber));
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-all-student")
    public ResponseEntity<HttpApiResponse<Page<StudentShortResponse>>> getAllStudentInUniversity(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer sizeNumber
    ) {
        HttpApiResponse<Page<StudentShortResponse>> response
                = universityAdminService.getAllStudentInUniversity(PageRequest.of(pageNumber, sizeNumber));
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-all-course")
    public ResponseEntity<HttpApiResponse<Page<CourseResponseDto>>> getAllCourseInUniversity(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer sizeNumber
    ) {
        HttpApiResponse<Page<CourseResponseDto>> response
                = universityAdminService.getAllCourseInUniversity(PageRequest.of(pageNumber, sizeNumber));
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-all-groups")
    public ResponseEntity<HttpApiResponse<Page<GroupResponse>>> getAllGroupsInUniversity(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer sizeNumber
    ) {
        HttpApiResponse<Page<GroupResponse>> response
                = universityAdminService.getAllGroupsInUniversity(PageRequest.of(pageNumber, sizeNumber));
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-all-faculty")
    public ResponseEntity<HttpApiResponse<Page<FacultyResponse>>> getAllFacultyInUniversity(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer sizeNumber
    ) {
        HttpApiResponse<Page<FacultyResponse>> response
                = universityAdminService.getAllFacultyInUniversity(PageRequest.of(pageNumber, sizeNumber));
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update-university")
    public ResponseEntity<HttpApiResponse<Boolean>> updateUniversity(@RequestBody UniversityUpdateRequest request) {
        HttpApiResponse<Boolean> response = universityAdminService.updateUniversity(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/remove-teacher")
    public ResponseEntity<HttpApiResponse<Boolean>> removeTeacher(@RequestParam Long teacherId) {
        HttpApiResponse<Boolean> response = universityAdminService.removeTeacher(teacherId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<HttpApiResponse<Boolean>> updateProfile(@RequestBody UniversityAdminUpdateRequest request) {
        HttpApiResponse<Boolean> response = universityAdminService.updateProfile(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/upload-profile-image")
    public ResponseEntity<HttpApiResponse<Boolean>> uploadProfileImage(@RequestParam MultipartFile file) {
        HttpApiResponse<Boolean> response = universityAdminService.uploadProfileImage(file);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update-password")
    public ResponseEntity<HttpApiResponse<Boolean>> updatePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) {
        HttpApiResponse<Boolean> response = universityAdminService.updatePassword(oldPassword, newPassword);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
