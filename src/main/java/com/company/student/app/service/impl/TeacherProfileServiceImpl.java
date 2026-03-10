package com.company.student.app.service.impl;

import com.company.student.app.config.security.TenantContext;
import com.company.student.app.config.security.UserSession;
import com.company.student.app.dto.attedance.AttendanceRequestDto;
import com.company.student.app.dto.course.CourseResponseDto;
import com.company.student.app.dto.group.GroupResponse;
import com.company.student.app.dto.lesson.*;
import com.company.student.app.dto.response.HttpApiResponse;
import com.company.student.app.dto.response.UserMeResponse;
import com.company.student.app.dto.room.RoomResponseDto;
import com.company.student.app.dto.student.StudentAttendanceResponse;
import com.company.student.app.dto.teacher.TeacherProfileResponse;
import com.company.student.app.dto.teacher.TeacherUpdateRequest;
import com.company.student.app.dto.timetable.TimeTableResponse;
import com.company.student.app.model.*;
import com.company.student.app.repository.*;
import com.company.student.app.service.TeacherProfileService;
import com.company.student.app.service.mapper.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TeacherProfileServiceImpl implements TeacherProfileService {
    private final UserSession userSession;
    private final AuthUserRepository authUserRepository;
    private final TimeTableRepository timeTableRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final LessonRepository lessonRepository;
    private final TeacherProfileMapper teacherProfileMapper;
    private final TimeTableMapper timeTableMapper;
    private final CourseRepository courseRepository;
    private final CourseAssignmentRepository courseAssignmentRepository;
    private final CourseMapper courseMapper;
    private final LessonMapper lessonMapper;
    private final GroupMapper groupMapper;
    private final StudentProfileRepository studentProfileRepository;
    private final StudentProfileMapper studentProfileMapper;
    private final PasswordEncoder passwordEncoder;
    private final LessonMaterialRepository lessonMaterialRepository;
    private final LessonMaterialMapper lessonMaterialMapper;
    private final UniversityUserRoleRepository userRoleRepository;
    private final AttendanceRepository attendanceRepository;
    private final AddressMapper addressMapper;
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;


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

    @Override
    public HttpApiResponse<TeacherProfileResponse> getTeacherProfile() {
        TeacherProfile currentTeacher = getCurrentTeacher();
        TeacherProfileResponse response = teacherProfileMapper.mapToProfileResponse(currentTeacher);

        Address address = currentTeacher.getUser().getAddress();
        if (address != null)
            response.setAddress(addressMapper.mapToResponse(address));

        return HttpApiResponse.<TeacherProfileResponse>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(response)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<List<TimeTableResponse>> getTimeTable(
            Long teacherId,
            Long groupId,
            Long roomId
    ) {
        Long universityId = userSession.universityId();

        validateOnlyOneFilter(teacherId, groupId, roomId);

        List<TimeTable> timeTables =
                teacherId != null
                        ? timeTableRepository.findAllByOrganizationIdAndTeacherIdAndDeletedAtIsNull(universityId, teacherId)
                        : groupId != null
                        ? timeTableRepository.findAllByOrganizationIdAndGroupIdAndDeletedAtIsNull(universityId, groupId)
                        : timeTableRepository.findAllByOrganizationIdAndRoomIdAndDeletedAtIsNull(universityId, roomId);

        return HttpApiResponse.<List<TimeTableResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(timeTableMapper.mapToResponseList(timeTables))
                .build();
    }

    private void validateOnlyOneFilter(Long teacherId, Long groupId, Long roomId) {
        int count = 0;
        if (teacherId != null) count++;
        if (groupId != null) count++;
        if (roomId != null) count++;

        if (count == 0) {
            throw new IllegalArgumentException("teacherId, groupId yoki roomId dan bittasi berilishi kerak");
        }

        if (count > 1) {
            throw new IllegalArgumentException("Faqat bittasi berilishi mumkin: teacherId, groupId yoki roomId");
        }
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> createLesson(LessonCreateRequest request) {
        Long universityId = userSession.universityId();
        TeacherProfile profile = getCurrentTeacher();

        boolean exists = courseAssignmentRepository.existsByTeacherIdAndCourseIdAndOrganizationIdAndDeletedAtIsNull(
                profile.getId(),
                request.getCourseId(),
                universityId
        );
        if (!exists)
            throw new IllegalArgumentException("not.allowed");

        Course course = courseRepository.findByIdAndOrganisationId(request.getCourseId(), universityId)
                .orElseThrow(() -> new EntityNotFoundException("course.not.found"));


        Lesson lesson = lessonMapper.mapToLesson(request);

        lesson.setCourse(course);
        lesson.setOrganizationId(universityId);

        lessonRepository.save(lesson);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(201)
                .message("ok")
                .data(true)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> createLessonMaterials(Long lessonId, LessonMaterialRequest request, List<String> fileNames) {
        Lesson lesson = lessonRepository.findByIdAndOrganizationIdAndDeletedAtIsNull(lessonId, userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("lesson.not.found"));
        LessonMaterial lessonMaterial = LessonMaterial.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .fileName(fileNames)
                .lesson(lesson)
                .organizationId(userSession.universityId())
                .build();

        lessonMaterialRepository.save(lessonMaterial);
        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(201)
                .message("ok")
                .data(true)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<Page<LessonResponse>> getTeacherLessons(Pageable pageable) {
        TeacherProfile currentTeacher = getCurrentTeacher();
        Page<LessonResponse> allByTeacherId = lessonRepository.findAllByTeacherId(currentTeacher.getId(), userSession.universityId(), pageable);

        return HttpApiResponse.<Page<LessonResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(allByTeacherId)
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

    @Override
    public HttpApiResponse<Page<CourseResponseDto>> getTeacherCourses(Pageable pageable) {
        Long universityId = userSession.universityId();

        TeacherProfile profile = getCurrentTeacher();

        Page<Course> coursePage = courseAssignmentRepository.findTeacherCourses(profile.getId(), universityId, pageable);

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
        Long universityId = userSession.universityId();

        Page<Lesson> lessonPage = lessonRepository.findAllByCourseId(universityId, courseId, pageable);

        return HttpApiResponse.<Page<LessonResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(lessonPage.map(lessonMapper::mapToLessonResponse))
                .build();
    }

    @Override
    public HttpApiResponse<Page<GroupResponse>> getTeacherGroups(Pageable pageable) {
        Long universityId = userSession.universityId();

        TeacherProfile profile = getCurrentTeacher();

        Page<Group> teacherGroups = courseAssignmentRepository.findTeacherGroups(profile.getId(), universityId, pageable);

        return HttpApiResponse.<Page<GroupResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(teacherGroups.map(groupMapper::mapToResponse))
                .build();

    }

    @Transactional(readOnly = true)
    @Override
    public HttpApiResponse<Page<RoomResponseDto>> getAllRoomInUniversity(Pageable pageable) {
        Page<Room> allByOrganizationId = roomRepository.findAllByOrganizationIdAndDeletedAtIsNull(userSession.universityId(), pageable);
        Page<RoomResponseDto> map = allByOrganizationId.map(roomMapper::mapToRoomResponse);

        return HttpApiResponse.<Page<RoomResponseDto>>builder()
                .status(200)
                .success(true)
                .message("ok")
                .data(map)
                .build();
    }

    @Override
    public HttpApiResponse<Page<StudentAttendanceResponse>> getStudentsByGroupId(Long groupId, Pageable pageable) {
        Long universityId = userSession.universityId();

        boolean allowed = courseAssignmentRepository
                .existsByTeacherIdAndGroupIdAndOrganizationIdAndDeletedAtIsNull(getCurrentTeacher().getId(), groupId, universityId);

        if (!allowed)
            throw new IllegalArgumentException("not.allowed");

        Page<StudentProfile> profilePage = studentProfileRepository.findAllByGroupId(groupId, universityId, pageable);

        return HttpApiResponse.<Page<StudentAttendanceResponse>>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(profilePage.map(studentProfileMapper::mapToStudentAttendanceResponse))
                .build();
    }

    @Transactional
    @Override
    public HttpApiResponse<Boolean> createAttendance(Long lessonId, List<AttendanceRequestDto> dtoList) {
        Long teacherId = getCurrentTeacher().getId();

        List<Long> teacherGroupIds =
                courseAssignmentRepository.findTeacherGroupIds(
                        teacherId,
                        userSession.universityId());

        if (teacherGroupIds.isEmpty()) {
            throw new AccessDeniedException("teacher.has.no.groups");
        }

        Lesson lesson = lessonRepository.findByIdAndOrganizationIdAndDeletedAtIsNull(lessonId, userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("lesson.not.found"));
        List<Long> studentIds =
                dtoList.stream().map(AttendanceRequestDto::getStudentId).toList();

        List<StudentProfile> students =
                studentProfileRepository.findAllByIdInAndOrganizationId(
                        studentIds,
                        userSession.universityId());

        for (StudentProfile student : students) {
            if (!teacherGroupIds.contains(student.getGroup().getId())) {
                throw new AccessDeniedException(
                        "teacher.cannot.mark.attendance.for.student: " + student.getId());
            }
        }

        List<Attendance> existingList = attendanceRepository.
                findAllByLessonIdAndOrganizationIdAndStudentIdIn(lessonId, userSession.universityId(), studentIds);
        Map<Long, Attendance> existingMap = existingList.stream()
                .collect(Collectors.toMap(a -> a.getStudent().getId(), a -> a));

        List<Attendance> toSave = new ArrayList<>();

        for (AttendanceRequestDto dto : dtoList) {
            Attendance attendance = existingMap.get(dto.getStudentId());

            if (attendance == null) {
                // create
                StudentProfile student = studentProfileRepository.findByIdAndOrganizationId(dto.getStudentId(), userSession.universityId())
                        .orElseThrow(() -> new RuntimeException("Student not found: " + dto.getStudentId()));

                attendance = Attendance.builder()
                        .lesson(lesson)
                        .student(student)
                        .status(dto.getStatus())
                        .startTime(dto.getStartTime())
                        .endTime(dto.getEndTime())
                        .note(dto.getNote())
                        .organizationId(userSession.universityId())
                        .build();
            } else {
                // update
                attendance.setStatus(dto.getStatus());
                attendance.setNote(dto.getNote());
            }

            toSave.add(attendance);
        }

        attendanceRepository.saveAll(toSave);
        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(201)
                .message("ok")
                .data(true)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> updateProfile(TeacherUpdateRequest request) {

        Long universityId = userSession.universityId();
        TeacherProfile profile = getCurrentTeacher();

        if (request.getUsername() != null) {

            String newUsername = request.getUsername().trim().toLowerCase();
            String currentUsername = profile.getUser().getUsername();

            if (!newUsername.equalsIgnoreCase(currentUsername)) {

                boolean exists = authUserRepository
                        .existsByUsernameIgnoreCaseAndOrganizationIdAndDeletedAtIsNullAndIdNot(
                                newUsername,
                                universityId,
                                profile.getUser().getId()
                        );

                if (exists) {
                    throw new IllegalArgumentException("username.already.exist");
                }

                profile.getUser().setUsername(newUsername);
            }
        }

        teacherProfileMapper.updateEntity(profile, request);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(true)
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> updateLessonMaterials(
            Long materialId,
            LessonMaterialUpdateRequest request,
            List<String> fileNames
    ) {
        Long universityId = userSession.universityId();

        LessonMaterial lessonMaterial = lessonMaterialRepository
                .findByIdAndOrganizationId(materialId, universityId)
                .orElseThrow(() -> new EntityNotFoundException("material.not.found"));

        lessonMaterialMapper.updateMaterial(lessonMaterial, request);

        if (fileNames != null) {
            List<String> updatedFileNames = fileNames.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(name -> !name.isBlank())
                    .distinct()
                    .collect(Collectors.toCollection(ArrayList::new)); // mutable

            lessonMaterial.setFileName(updatedFileNames);
        }

        // no need to call save() here if entity is managed in @Transactional
        // lessonMaterialRepository.save(lessonMaterial);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("material.updated")
                .data(true)
                .build();
    }


    @Transactional
    @Override
    public HttpApiResponse<Boolean> updateLesson(Long lessonId, LessonUpdateRequest request) {
        Lesson lesson = lessonRepository.findByIdAndOrganizationIdAndDeletedAtIsNull(lessonId, userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("lesson.not.found"));

        lessonMapper.updateEntity(lesson, request);

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
        Long userId = userSession.userId();
        Long universityId = userSession.universityId();
        AuthUser authUser = authUserRepository.findByIdAndDeletedAtIsNull(userId,universityId)
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


    private TeacherProfile getCurrentTeacher() {
        Long userId = userSession.userId();
        Long universityId = userSession.universityId();
        return teacherProfileRepository.findByUserIdAndOrganisationId(userId, universityId)
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));
    }


    /*@Transactional
    public void assignGroup(Long studentId, Long groupId) {

        StudentProfile student = studentRepository
                .findByIdAndOrganizationId(...)
            .orElseThrow(...);

        Group group = groupRepository
                .findByIdAndOrganizationId(...)
            .orElseThrow(...);

        student.setGroup(group);
    }*/
}
