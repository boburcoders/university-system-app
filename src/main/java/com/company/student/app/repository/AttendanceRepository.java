package com.company.student.app.repository;

import com.company.student.app.model.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findAllByLessonIdAndOrganizationIdAndStudentIdIn(Long lessonId, Long organizationId, Collection<Long> studentIds);

    @Query("select a from Attendance a where a.lesson.id=:lessonId and a.student.id=:studentId and a.organizationId=:universityId and a.deletedAt is null ")
    Optional<Attendance> findByLessonIdAndStudentIdAndOrganizationId(Long lessonId, Long studentId, Long universityId);

    @Query("select a from Attendance a where a.lesson.course.id=:courseId and a.student.id=:studentId and a.organizationId=:organizationId and a.deletedAt is null ")
    Page<Attendance> findAllByCourseIdAndStudentIdAndOrganizationId(Long courseId, Long studentId, Long organizationId, Pageable pageable);
}