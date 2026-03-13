package com.company.student.app.service;

import com.company.student.app.dto.assignment.AssignmentResponse;
import com.company.student.app.dto.attedance.AttendanceResponse;
import com.company.student.app.dto.course.CourseResponseDto;
import com.company.student.app.dto.grade.GradeResponse;
import com.company.student.app.dto.group.GroupShortResponse;
import com.company.student.app.dto.lesson.LessonMaterialResponse;
import com.company.student.app.dto.lesson.LessonResponse;
import com.company.student.app.dto.response.HttpApiResponse;
import com.company.student.app.dto.response.UserMeResponse;
import com.company.student.app.dto.student.StudentProfileResponse;
import com.company.student.app.dto.student.StudentProfileUpdateRequest;
import com.company.student.app.dto.submission.SubmissionRequest;
import com.company.student.app.dto.submission.SubmissionResponse;
import com.company.student.app.dto.teacher.TeacherResponse;
import com.company.student.app.dto.timetable.TimeTableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface StudentProfileService {
    HttpApiResponse<StudentProfileResponse> getProfile();

    HttpApiResponse<Page<CourseResponseDto>> getStudentCourses(Pageable pageable);

    HttpApiResponse<Page<AttendanceResponse>> getStudentAttendances(Pageable pageable, Long courseId);

    HttpApiResponse<Boolean> updateProfile(StudentProfileUpdateRequest request);

    HttpApiResponse<Boolean> updatePassword(String oldPassword, String newPassword);

    HttpApiResponse<Page<LessonResponse>> getCourseLessons(Pageable of, Long courseId);

    HttpApiResponse<List<TimeTableResponse>> getTimeTable(Long teacherId, Long groupId);

    HttpApiResponse<List<GroupShortResponse>> getAllGroupShortResponse();

    HttpApiResponse<UserMeResponse> getMe(Authentication authentication);

    HttpApiResponse<List<TeacherResponse>> getAllTeacher();

    HttpApiResponse<List<LessonMaterialResponse>> getLessonMaterials(Long lessonId);

    HttpApiResponse<Map<String, Long>> getStudentStatistics();

    HttpApiResponse<AssignmentResponse> getAssignmentById(Long id);

    HttpApiResponse<List<AssignmentResponse>> getAllAssignmentByCourse(Long courseId);

    HttpApiResponse<List<AssignmentResponse>> getAllAssignmentByStudent();

    HttpApiResponse<Boolean> createSubmission(SubmissionRequest request, Long assignmentId);

    HttpApiResponse<SubmissionResponse> getSubmissionById(Long id);

    HttpApiResponse<List<SubmissionResponse>> getSubmissionByAssignment(Long id);

    HttpApiResponse<Page<SubmissionResponse>> getSubmissionByStudent(Pageable pageable, Long courseId);

    HttpApiResponse<GradeResponse> getGradeBySubmission(Long submissionId);
}
