package com.company.student.app.controller;

import com.company.student.app.dto.assignment.AssignmentRequest;
import com.company.student.app.dto.assignment.AssignmentResponse;
import com.company.student.app.dto.assignment.AssignmentUpdateRequest;
import com.company.student.app.dto.attedance.AttendanceRequestDto;
import com.company.student.app.dto.course.CourseResponseDto;
import com.company.student.app.dto.grade.GradeRequest;
import com.company.student.app.dto.grade.GradeResponse;
import com.company.student.app.dto.grade.GradeUpdateRequest;
import com.company.student.app.dto.group.GroupResponse;
import com.company.student.app.dto.lesson.*;
import com.company.student.app.dto.response.HttpApiResponse;
import com.company.student.app.dto.response.UserMeResponse;
import com.company.student.app.dto.room.RoomResponseDto;
import com.company.student.app.dto.student.StudentAttendanceResponse;
import com.company.student.app.dto.submission.SubmissionResponse;
import com.company.student.app.dto.teacher.TeacherProfileResponse;
import com.company.student.app.dto.teacher.TeacherUpdateRequest;
import com.company.student.app.dto.timetable.TimeTableResponse;
import com.company.student.app.service.TeacherProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(required = false) Long groupId,
            @RequestParam(required = false) Long roomId
    ) {
        HttpApiResponse<List<TimeTableResponse>> response = teacherProfileService.getTimeTable(teacherId, groupId, roomId);
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

    @GetMapping("/get-teacher-lessons")
    public ResponseEntity<HttpApiResponse<Page<LessonResponse>>> getTeacherLessons(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer sizeNumber
    ) {
        HttpApiResponse<Page<LessonResponse>> response
                = teacherProfileService.getTeacherLessons(PageRequest.of(pageNumber, sizeNumber));
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

    @GetMapping("/all-rooms")
    public ResponseEntity<HttpApiResponse<Page<RoomResponseDto>>> getAllRoomInUniversity(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer sizeNumber
    ) {
        HttpApiResponse<Page<RoomResponseDto>> response
                = teacherProfileService.getAllRoomInUniversity(PageRequest.of(pageNumber, sizeNumber));
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

    @PutMapping("/update-lesson/{lessonId}")
    public ResponseEntity<HttpApiResponse<Boolean>> updateLesson(
            @PathVariable Long lessonId,
            @RequestBody LessonUpdateRequest request
    ) {
        HttpApiResponse<Boolean> response = teacherProfileService.updateLesson(lessonId, request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update-lesson-materials/{materialId}")
    public ResponseEntity<HttpApiResponse<Boolean>> updateLessonMaterials(
            @PathVariable Long materialId,
            @RequestBody LessonMaterialUpdateRequest request,
            @RequestParam List<String> fileNames
    ) {
        HttpApiResponse<Boolean> response = teacherProfileService.updateLessonMaterials(materialId, request, fileNames);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    /*Assignment Section*/

    @PostMapping("/assignment/{courseId}")
    public ResponseEntity<HttpApiResponse<Boolean>> createAssignment(@RequestBody @Valid AssignmentRequest request, @PathVariable Long courseId) {
        HttpApiResponse<Boolean> response = teacherProfileService.createAssignment(request, courseId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<HttpApiResponse<AssignmentResponse>> getAssignmentById(@PathVariable Long assignmentId) {
        HttpApiResponse<AssignmentResponse> response = teacherProfileService.getAssignmentById(assignmentId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/assignment/getByCourse/{courseId}")
    public ResponseEntity<HttpApiResponse<Page<AssignmentResponse>>> getAllAssignmentByCourse(
            @PathVariable Long courseId,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int sizeNumber) {
        HttpApiResponse<Page<AssignmentResponse>> response =
                teacherProfileService.getAllAssignmentByCourse(courseId, PageRequest.of(pageNumber, sizeNumber));
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/assignment/getall-by-teacher")
    public ResponseEntity<HttpApiResponse<Page<AssignmentResponse>>> getAllAssignmentByTeacherProfile(
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int sizeNumber
    ) {
        HttpApiResponse<Page<AssignmentResponse>> response =
                teacherProfileService.getAllAssignmentByTeacherProfile(PageRequest.of(pageNumber, sizeNumber));
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/assignment/{id}")
    public ResponseEntity<HttpApiResponse<Long>> updateAssignment(@RequestBody AssignmentUpdateRequest request, @PathVariable Long id) {
        HttpApiResponse<Long> response = teacherProfileService.updateAssignment(request, id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/assignment/{id}")
    public ResponseEntity<HttpApiResponse<Boolean>> deleteAssignment(@PathVariable Long id) {
        HttpApiResponse<Boolean> response = teacherProfileService.deleteAssignment(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /*Grade Section*/

    @PostMapping("/grade/{submissionId}")
    public ResponseEntity<HttpApiResponse<Long>> createGrade(@RequestBody @Valid GradeRequest request, @PathVariable Long submissionId) {
        HttpApiResponse<Long> response = teacherProfileService.createGrade(request, submissionId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/grade/{id}")
    public ResponseEntity<HttpApiResponse<GradeResponse>> getGradeById(@PathVariable Long id) {
        HttpApiResponse<GradeResponse> response = teacherProfileService.getGradeById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/grade-by-submission/{submissionId}")
    public ResponseEntity<HttpApiResponse<GradeResponse>> getGradeBySubmissionId(@PathVariable Long submissionId) {
        HttpApiResponse<GradeResponse> response = teacherProfileService.getGradeBySubmissionId(submissionId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @PutMapping("/grade/{id}")
    public ResponseEntity<HttpApiResponse<Boolean>> updateGrade(@RequestBody GradeUpdateRequest request, @PathVariable Long id) {
        HttpApiResponse<Boolean> response = teacherProfileService.updateGrade(request, id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/grade/{id}")
    public ResponseEntity<HttpApiResponse<Boolean>> deleteGrade(@PathVariable Long id) {
        HttpApiResponse<Boolean> response = teacherProfileService.deleteGrade(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /*Submission Section*/

    @GetMapping("/submission/{assignmentId}")
    public ResponseEntity<HttpApiResponse<Page<SubmissionResponse>>> getAllSubmissionByAssignment(
            @PathVariable Long assignmentId,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int sizeNumber) {
        HttpApiResponse<Page<SubmissionResponse>> response =
                teacherProfileService.getAllSubmissionByAssignment(assignmentId, PageRequest.of(pageNumber, sizeNumber));
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
