package com.company.student.app.repository;

import com.company.student.app.model.LessonMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonMaterialRepository extends JpaRepository<LessonMaterial, Long> {
    @Query("select lm from LessonMaterial lm where lm.lesson.id=:lessonId and lm.organizationId=:universityId and lm.deletedAt is null ")
    List<LessonMaterial> findAllByLessonIdAndOrganisationId(Long lessonId, Long universityId);

    @Modifying
    @Query("""
            update LessonMaterial lm
            set lm.deletedAt = CURRENT_TIMESTAMP
            where lm.lesson.course.id = :courseId
            and lm.deletedAt is null
            """)
    void softDeleteMaterialsByCourseId(Long courseId);

    @Modifying
    @Query("""
            update LessonMaterial lm
            set lm.deletedAt = CURRENT_TIMESTAMP
            where lm.lesson.course.faculty.department.id = :departmentId
            """)
    void softDeleteMaterialsByDepartment(Long departmentId);

    @Modifying
    @Query("""
            update LessonMaterial lm
            set lm.deletedAt = CURRENT_TIMESTAMP
            where lm.lesson.course.faculty.id = :facultyId
            and lm.deletedAt is null
            """)
    void softDeleteMaterialsByFaculty(Long facultyId);
}
