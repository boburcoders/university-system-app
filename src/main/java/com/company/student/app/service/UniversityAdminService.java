package com.company.student.app.service;

import com.company.student.app.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    HttpApiResponse<Boolean> uploadProfileImage(MultipartFile file);
}
