package com.company.student.app.repository;

import com.company.student.app.model.Course;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select c from Course c where c.organizationId=:universityId and c.deletedAt is null ")
    Page<Course> findAllByOrganizationId(Long universityId, Pageable pageable);

    @Query("select c from Course c where c.id=:courseId and c.organizationId=:universityId and c.deletedAt is null ")
    Optional<Course> findByIdAndOrganisationId(@NotBlank Long courseId, Long universityId);


    @Query("""
            select c
            from StudentProfile s
            join s.group g
            join g.faculty f
            join Course c on c.faculty = f
            where s.id = :studentId
            and s.organizationId = :universityId
            """)
    Page<Course> findAllCoursesByStudentIdAndOrganizationId(
            @Param("studentId") Long studentId,
            @Param("universityId") Long universityId,
            Pageable pageable);


}
