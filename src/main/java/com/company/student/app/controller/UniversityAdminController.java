package com.company.student.app.controller;

import com.company.student.app.dto.*;
import com.company.student.app.service.UniversityAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/univer-admin")
@PreAuthorize("hasRole('UNIVERSITY_ADMIN')")
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

    @PostMapping(value = "/create-faculty/{department_id}")
    public ResponseEntity<HttpApiResponse<Boolean>> createFaculty(@PathVariable Long department_id, @RequestBody FacultyRequestDto requestDto) {
        HttpApiResponse<Boolean> response = universityAdminService.createFaculty(department_id, requestDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping(value = "/create-department")
    public ResponseEntity<HttpApiResponse<Boolean>> createDepartment(@RequestBody @Valid DepartmentRequestDto requestDto) {
        HttpApiResponse<Boolean> response = universityAdminService.createDepartment(requestDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @PostMapping(value = "/create-course/{faculty_id}")
    public ResponseEntity<HttpApiResponse<Boolean>> createCourse(@PathVariable Long faculty_id, @RequestBody CourseRequestDto requestDto) {
        HttpApiResponse<Boolean> response = universityAdminService.createCourse(faculty_id, requestDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping(value = "/create-group/{faculty_id}")
    public ResponseEntity<HttpApiResponse<Boolean>> createGroup(@PathVariable Long faculty_id, @RequestBody GroupRequestDto requestDto) {
        HttpApiResponse<Boolean> response = universityAdminService.createGroup(faculty_id, requestDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<HttpApiResponse<UserMeResponse>> getCurrentUser(Authentication authentication) {
        HttpApiResponse<UserMeResponse> response = universityAdminService.getMe(authentication);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<HttpApiResponse<UniversityAdminProfileResponse>> getUniversityAdminProfile() {
        HttpApiResponse<UniversityAdminProfileResponse> response
                = universityAdminService.getUniversityAdminProfile();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/statistics")
    public ResponseEntity<HttpApiResponse<StatisticResponse>> getStatisticsDashboard() {
        HttpApiResponse<StatisticResponse> response = universityAdminService.getStatisticsDashboard();
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

    @GetMapping("/get-all-department")
    public ResponseEntity<HttpApiResponse<Page<DepartmentResponse>>> getAllDepartmentInUniversity(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer sizeNumber
    ) {
        HttpApiResponse<Page<DepartmentResponse>> response
                = universityAdminService.getAllDepartmentInUniversity(PageRequest.of(pageNumber, sizeNumber));
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update-university")
    public ResponseEntity<HttpApiResponse<Boolean>> updateUniversity(@RequestBody UniversityUpdateRequest request) {
        HttpApiResponse<Boolean> response = universityAdminService.updateUniversity(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update-department/{departmentId}")
    public ResponseEntity<HttpApiResponse<Boolean>> updateDepartment(
            @RequestBody DepartmentUpdateRequest request,
            @PathVariable Long departmentId) {
        HttpApiResponse<Boolean> response = universityAdminService.updateDepartment(request, departmentId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update-faculty/{facultyId}")
    public ResponseEntity<HttpApiResponse<Boolean>> updateFaculty(@RequestBody FacultyUpdateRequest request, @PathVariable Long facultyId) {
        HttpApiResponse<Boolean> response = universityAdminService.updateFaculty(request, facultyId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @PutMapping("/update-profile")
    public ResponseEntity<HttpApiResponse<Boolean>> updateProfile(@RequestBody UniversityAdminUpdateRequest request) {
        HttpApiResponse<Boolean> response = universityAdminService.updateProfile(request);
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

    @DeleteMapping("/teacher/{teacherId}")
    public ResponseEntity<HttpApiResponse<Boolean>> removeTeacher(@PathVariable Long teacherId) {
        HttpApiResponse<Boolean> response = universityAdminService.removeTeacher(teacherId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<HttpApiResponse<Boolean>> removeStudent(@PathVariable Long studentId) {
        HttpApiResponse<Boolean> response = universityAdminService.removeStudent(studentId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/course/{courseId}")
    public ResponseEntity<HttpApiResponse<Boolean>> removeCourse(@PathVariable Long courseId) {
        HttpApiResponse<Boolean> response = universityAdminService.removeCourse(courseId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/group/{groupId}")
    public ResponseEntity<HttpApiResponse<Boolean>> removeGroup(@PathVariable Long groupId) {
        HttpApiResponse<Boolean> response = universityAdminService.removeGroup(groupId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/department/{departmentId}")
    public ResponseEntity<HttpApiResponse<Boolean>> removeDepartment(@PathVariable Long departmentId) {
        HttpApiResponse<Boolean> response = universityAdminService.removeDepartment(departmentId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/faculty/{facultyId}")
    public ResponseEntity<HttpApiResponse<Boolean>> removeFaculty(@PathVariable Long facultyId) {
        HttpApiResponse<Boolean> response = universityAdminService.removeFaculty(facultyId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
