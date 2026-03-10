package com.company.student.app.repository;

import com.company.student.app.dto.lesson.LessonResponse;
import com.company.student.app.model.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("select l from Lesson l where l.organizationId=:universityId and l.deletedAt is null and l.course.id=:courseId")
    Page<Lesson> findAllByCourseId(Long universityId, Long courseId, Pageable pageable);


    @Query("select l from Lesson l where l.id=:lessonId and l.organizationId=:universityId and l.deletedAt is null ")
    Optional<Lesson> findByIdAndOrganizationIdAndDeletedAtIsNull(Long lessonId, Long universityId);


    @Modifying
    @Query("""
            update Lesson l
            set l.deletedAt = CURRENT_TIMESTAMP
            where l.course.id = :courseId
            and l.deletedAt is null
            """)
    void softDeleteLessonsByCourseId(Long courseId);

    @Modifying
    @Query("""
            update Lesson l
            set l.deletedAt = CURRENT_TIMESTAMP
            where l.course.faculty.department.id = :departmentId
            """)
    void softDeleteLessonsByDepartment(Long departmentId);

    @Modifying
    @Query("""
            update Lesson l
            set l.deletedAt = CURRENT_TIMESTAMP
            where l.course.faculty.id = :facultyId
            and l.deletedAt is null
            """)
    void softDeleteLessonsByFaculty(Long facultyId);

    @Modifying
    @Query("""
             update Lesson l
             set l.deletedAt=:now
             where l.organizationId=:universityId and l.deletedAt is null
            """)
    void softDeleteByUniversity(Long universityId, LocalDateTime now);

    @Query("select distinct new com.company.student.app.dto.lesson.LessonResponse(l.id,l.title,l.description,c.code) from CourseAssignment ca join ca.course c inner join Lesson l on l.course.id=c.id where ca.teacher.id=:teacherId and ca.organizationId=:organizationId")
    Page<LessonResponse> findAllByTeacherId(Long teacherId, Long organizationId, Pageable pageable);

    @Query("""
                select count(distinct l.id)
                from StudentProfile s
                join s.group g
                join g.faculty f
                join Course c on c.faculty = f
                join Lesson l on l.course = c
                where s.id = :studentId
                  and s.organizationId = :universityId
                  and s.deletedAt is null
                  and g.deletedAt is null
                  and f.deletedAt is null
                  and c.deletedAt is null
                  and l.deletedAt is null
            """)
    Long findAllStudentLesson(@Param("studentId") Long studentId,
                              @Param("universityId") Long universityId);
}
