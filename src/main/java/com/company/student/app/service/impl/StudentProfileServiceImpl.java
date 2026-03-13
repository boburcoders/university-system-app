package com.company.student.app.service.impl;

import com.company.student.app.config.security.TenantContext;
import com.company.student.app.config.security.UserSession;
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
import com.company.student.app.model.*;
import com.company.student.app.model.enums.SubmissionStatus;
import com.company.student.app.repository.*;
import com.company.student.app.service.StudentProfileService;
import com.company.student.app.service.mapper.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Primary
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final StudentProfileMapper studentProfileMapper;
    private final UserSession userSession;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final TimeTableRepository timeTableRepository;
    private final TimeTableMapper timeTableMapper;
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final UniversityUserRoleRepository userRoleRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final TeacherProfileMapper teacherProfileMapper;
    private final LessonMaterialRepository lessonMaterialRepository;
    private final LessonMaterialMapper lessonMaterialMapper;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;
    private final AddressMapper addressMapper;
    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final GradeRepository gradeRepository;
    private final AssignmentMapper assignmentMapper;
    private final SubmissionMapper submissionMapper;
    private final GradeMapper gradeMapper;

    @Override
    public HttpApiResponse<UserMeResponse> getMe(Authentication authentication) {
        Long universityId = TenantContext.getTenantId();

        AuthUser authUser = authUserRepository.findByUserName(authentication.getName(), universityId)
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));

        UniversityUserRole userRole = userRoleRepository.findUserWithRole(authentication.getName(), universityId)
                .orElseThrow(() -> new EntityNotFoundException("user.role.not.found"));

        UserMeResponse response = UserMeResponse.builder()
                .id(authUser.getId())
                .universityId(universityId)
                .username(authUser.getUsername())
                .role(userRole.getRole().name())
                .build();

        return HttpApiResponse.<UserMeResponse>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(response)
                .build();
    }

    private StudentProfile getCurrentStudent() {
        return studentProfileRepository.findByUserIdAndOrganizationId(userSession.userId(), userSession.universityId()).
                orElseThrow(() -> new EntityNotFoundException("user.not.found"));
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<StudentProfileResponse> getProfile() {
        StudentProfile profile = getCurrentStudent();

        StudentProfileResponse response = studentProfileMapper.toProfileResponse(profile);
        Address address = profile.getUser().getAddress();
        Group group = profile.getGroup();

        if (address != null)
            response.setAddress(addressMapper.mapToResponse(address));
        if (group != null)
            response.setGroup(groupMapper.mapToResponse(group));
        return HttpApiResponse.<StudentProfileResponse>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(response)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public HttpApiResponse<Page<CourseResponseDto>> getStudentCourses(Pageable pageable) {
        Long id = getCurrentStudent().getId();
        Page<Course> coursePage =
                courseRepository.findAllCoursesByStudentIdAndOrganizationId(id, userSession.universityId(), pageable);

        return HttpApiResponse.<Page<CourseResponseDto>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(coursePage.map(courseMapper::mapToCourseResponse))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public HttpApiResponse<Page<LessonResponse>> getCourseLessons(Pageable pageable, Long courseId) {
        Page<Lesson> lessonPage = lessonRepository.findAllByCourseId(userSession.universityId(), courseId, pageable);

        return HttpApiResponse.<Page<LessonResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(lessonPage.map(lessonMapper::mapToLessonResponse))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public HttpApiResponse<List<LessonMaterialResponse>> getLessonMaterials(Long lessonId) {
        List<LessonMaterial> lessonMaterials = lessonMaterialRepository.findAllByLessonIdAndOrganisationId(lessonId, userSession.universityId());
        if (lessonMaterials.isEmpty())
            return HttpApiResponse.<List<LessonMaterialResponse>>builder()
                    .success(false)
                    .status(404)
                    .message("materail.not.found")
                    .build();
        return HttpApiResponse.<List<LessonMaterialResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(lessonMaterials.stream().map(lessonMaterialMapper::mapToLessonMaterialResponse).toList())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<Page<AttendanceResponse>> getStudentAttendances(
            Pageable pageable, Long courseId) {
        Page<Attendance> attendances =
                attendanceRepository.findAllByCourseIdAndStudentIdAndOrganizationId(
                        courseId,
                        getCurrentStudent().getId(),
                        userSession.universityId(),
                        pageable);

        Page<AttendanceResponse> responsePage =
                attendances.map(attendanceMapper::mapToAttendanceResponse);

        return HttpApiResponse.<Page<AttendanceResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(responsePage)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<Map<String, Long>> getStudentStatistics() {
        Long studentId = getCurrentStudent().getId();
        Long universityId = userSession.universityId();
        Long attendanceCount = attendanceRepository.findAllByStudentId(studentId, universityId);
        Long lessonCount = lessonRepository.findAllStudentLesson(studentId, universityId);
        Long courseCount = courseRepository.findAllStudentCourseCount(studentId, universityId);
        Map<String, Long> map = Map.of(
                "attendanceCount", attendanceCount,
                "lessonCount", lessonCount,
                "courseCount", courseCount);

        return HttpApiResponse.<Map<String, Long>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(map)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<List<GroupShortResponse>> getAllGroupShortResponse() {
        List<Group> groupList = groupRepository.getAllByOrganizationId(userSession.universityId());

        return HttpApiResponse.<List<GroupShortResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(groupList.stream().map(groupMapper::mapToShortResponse).toList())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public HttpApiResponse<List<TeacherResponse>> getAllTeacher() {
        Long universityId = userSession.universityId();
        List<TeacherProfile> teacherProfileList = teacherProfileRepository.findAllByOrganizationIdAndDeletedAtIsNull(universityId);

        return HttpApiResponse.<List<TeacherResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(teacherProfileList.stream().map(teacherProfileMapper::mapToTeacherResponse).toList())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public HttpApiResponse<List<TimeTableResponse>> getTimeTable(Long teacherId, Long groupId) {
        Long universityId = userSession.universityId();

        if (teacherId == null && groupId == null) {
            throw new IllegalArgumentException("teacherId yoki groupId berilishi kerak");
        }

        if (teacherId != null && groupId != null) {
            throw new IllegalArgumentException("Faqat bittasi berilishi mumkin: teacherId yoki groupId");
        }

        List<TimeTable> timeTables;

        if (teacherId != null) {
            timeTables = timeTableRepository
                    .findAllByOrganizationIdAndTeacherIdAndDeletedAtIsNull(
                            universityId, teacherId);
        } else {
            timeTables = timeTableRepository
                    .findAllByOrganizationIdAndGroupIdAndDeletedAtIsNull(
                            universityId, groupId);
        }

        List<TimeTableResponse> responseList =
                timeTableMapper.mapToResponseList(timeTables);

        return HttpApiResponse.<List<TimeTableResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(responseList)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> updateProfile(StudentProfileUpdateRequest request) {
        StudentProfile studentProfile = getCurrentStudent();

        studentProfileMapper.updateProfile(studentProfile, request);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(true)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> updatePassword(String oldPassword, String newPassword) {
        AuthUser authUser = authUserRepository.findByIdAndDeletedAtIsNull(userSession.userId(), userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));
        if (!passwordEncoder.matches(oldPassword, authUser.getPassword()))
            throw new IllegalArgumentException("password.incorrect");

        authUser.setPassword(passwordEncoder.encode(newPassword));

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(true)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<AssignmentResponse> getAssignmentById(Long id) {
        Long universityId = userSession.universityId();
        Assignment assignment = assignmentRepository.getByIdAndOrganizationId(id, universityId)
                .orElseThrow(() -> new EntityNotFoundException("assignment.not.found"));

        AssignmentResponse response = assignmentMapper.mapToAssignmentResponse(assignment);

        return HttpApiResponse.<AssignmentResponse>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(response)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<List<AssignmentResponse>> getAllAssignmentByCourse(Long courseId) {
        boolean exists = courseRepository.existsById(courseId);
        if (!exists) {
            throw new IllegalArgumentException("course.not.found");
        }
        List<Assignment> assignmentList = assignmentRepository.getAllByCourseId(courseId, userSession.universityId());

        return HttpApiResponse.<List<AssignmentResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(assignmentList.stream().map(assignmentMapper::mapToAssignmentResponse).toList())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<List<AssignmentResponse>> getAllAssignmentByStudent() {
        Long studentId = getCurrentStudent().getId();
        List<Assignment> assignmentList = assignmentRepository.findAssignmentsForStudent(studentId, userSession.universityId());

        return HttpApiResponse.<List<AssignmentResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(assignmentList.stream().map(assignmentMapper::mapToAssignmentResponse).toList())
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> createSubmission(SubmissionRequest request, Long assignmentId) {
        Long universityId = userSession.universityId();
        StudentProfile currentStudent = getCurrentStudent();

        Assignment assignment = assignmentRepository
                .getByIdAndOrganizationId(assignmentId, universityId)
                .orElseThrow(() -> new EntityNotFoundException("assignment.not.found"));
        if (!LocalDateTime.now().isAfter(assignment.getAvailableFrom())) {
            throw new IllegalArgumentException("you.can.not.submit.now");
        }

        Submission lastSubmission = submissionRepository
                .findTopByAssignmentIdAndStudentProfileIdAndOrganizationIdOrderByAttemptNumberDesc(
                        assignmentId,
                        currentStudent.getId(),
                        universityId
                )
                .orElse(null);

        int nextAttemptNumber = (lastSubmission == null || lastSubmission.getAttemptNumber() == null)
                ? 1
                : lastSubmission.getAttemptNumber() + 1;

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudentProfile(currentStudent);
        submission.setAttemptNumber(nextAttemptNumber);
        submission.setOrganizationId(userSession.universityId());

        if (request.getAnswerText() != null && !request.getAnswerText().isBlank()) {
            submission.setAnswerText(request.getAnswerText());
        }

        if (request.getFileNames() != null && !request.getFileNames().isEmpty()) {
            submission.setFileNames(request.getFileNames());
        }

        boolean isLate = LocalDateTime.now().isAfter(assignment.getDeadline());

        if (nextAttemptNumber > 1) {
            submission.setStatus(isLate ? SubmissionStatus.LATE : SubmissionStatus.RESUBMITTED);
        } else {
            submission.setStatus(isLate ? SubmissionStatus.LATE : SubmissionStatus.SUBMITTED);
        }

        submissionRepository.save(submission);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(201)
                .message("ok")
                .data(true)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<SubmissionResponse> getSubmissionById(Long id) {
        Submission submission = submissionRepository.findByIdAndAndOrganizationId(id, userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("submission.not.found"));

        SubmissionResponse submissionResponse = submissionMapper.mapToResponse(submission);

        return HttpApiResponse.<SubmissionResponse>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(submissionResponse)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<List<SubmissionResponse>> getSubmissionByAssignment(Long id) {
        boolean exist = assignmentRepository.existsAssignment(id, userSession.universityId());
        if (!exist) {
            throw new EntityNotFoundException("assignment.not.found");
        }
        List<Submission> submissionList = submissionRepository.findAllByAssignmentId(id, userSession.universityId());

        return HttpApiResponse.<List<SubmissionResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(submissionList.stream().map(submissionMapper::mapToResponse).toList())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<Page<SubmissionResponse>> getSubmissionByStudent(Pageable pageable, Long courseId) {
        Long studentId = getCurrentStudent().getId();
        Long universityId = userSession.universityId();
        Page<Submission> submissionPage;
        if (courseId != null) {
            submissionPage = submissionRepository.findAllByCourseId(courseId, studentId, userSession.universityId(), pageable);
        } else
            submissionPage = submissionRepository.findAllStudentSubmission(studentId, universityId, pageable);

        return HttpApiResponse.<Page<SubmissionResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(submissionPage.map(submissionMapper::mapToResponse))
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<GradeResponse> getGradeBySubmission(Long submissionId) {
        boolean exists = submissionRepository.existsByIdAndDeletedAtIsNull(submissionId, userSession.universityId());
        if (!exists) {
            throw new EntityNotFoundException("submission.not.found");
        }
        Grade grade = gradeRepository.findBySubmissionId(submissionId,userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("grade.not.found"));
        GradeResponse gradeResponse = gradeMapper.mapToResponse(grade);

        return HttpApiResponse.<GradeResponse>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(gradeResponse)
                .build();
    }
}
