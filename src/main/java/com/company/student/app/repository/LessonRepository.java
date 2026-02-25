package com.company.student.app.repository;

import com.company.student.app.model.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
