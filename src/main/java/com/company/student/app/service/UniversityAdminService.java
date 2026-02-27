package com.company.student.app.service;

import com.company.student.app.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UniversityAdminService {
    HttpApiResponse<Long> createTeacher(@Valid TeacherCreateRequest request);

    HttpApiResponse<Long> createStudent(@Valid StudentCreateRequest request);

    HttpApiResponse<Boolean> createStudentByExcelFile(MultipartFile file);

    HttpApiResponse<Page<TeacherShortResponseDto>> getAllTeacherInUniversity(Pageable pageable);

    HttpApiResponse<Page<StudentShortResponse>> getAllStudentInUniversity(Pageable pageable);

    HttpApiResponse<Boolean> updateUniversity(UniversityUpdateRequest request);

    HttpApiResponse<Boolean> removeTeacher(Long teacherId);

    HttpApiResponse<Page<CourseResponseDto>> getAllCourseInUniversity(Pageable pageable);

    HttpApiResponse<Page<GroupResponse>> getAllGroupsInUniversity(Pageable pageable);

    HttpApiResponse<Page<FacultyResponse>> getAllFacultyInUniversity(Pageable pageable);

    HttpApiResponse<Boolean> updateProfile(UniversityAdminUpdateRequest request);

    HttpApiResponse<Boolean> updatePassword(String oldPassword, String newPassword);

    HttpApiResponse<UniversityAdminProfileResponse> getUniversityAdminProfile();

    HttpApiResponse<UserMeResponse> getMe(Authentication authentication);

    HttpApiResponse<Boolean> createCourse(Long facultyId, CourseRequestDto requestDto);

    HttpApiResponse<Boolean> createGroup(Long facultyId, GroupRequestDto requestDto);

    HttpApiResponse<Boolean> createFaculty(Long departmentId, FacultyRequestDto requestDto);

    HttpApiResponse<Page<DepartmentResponse>> getAllDepartmentInUniversity(Pageable pageable);

    HttpApiResponse<Boolean> createDepartment(DepartmentRequestDto requestDto);

    HttpApiResponse<Boolean> updateDepartment(DepartmentUpdateRequest request, Long departmentId);

    HttpApiResponse<Boolean> updateFaculty(FacultyUpdateRequest request,Long facultyId);

    HttpApiResponse<Boolean> removeStudent(Long studentId);

    HttpApiResponse<Boolean> removeCourse(Long courseId);

    HttpApiResponse<Boolean> removeDepartment(Long departmentId);

    HttpApiResponse<Boolean> removeFaculty(Long facultyId);

    HttpApiResponse<StatisticResponse> getStatisticsDashboard();

    HttpApiResponse<Boolean> removeGroup(Long groupId);

    HttpApiResponse<Boolean> createTimeTable(@Valid TimeTableRequest request);

    HttpApiResponse<Boolean> createCourseAssigment(@Valid CourseAssignmentRequest request);

    HttpApiResponse<Boolean> assignStudentToGroup(Long studentId, Long groupId);
}
