package com.company.student.app.repository;

import com.company.student.app.model.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    @Query("select a from Assignment a where a.id=:id and a.organizationId=:universityId and a.deletedAt is null")
    Optional<Assignment> getByIdAndOrganizationId(Long id, Long universityId);

    @Query("select a from Assignment a where a.course.id=:courseId and a.organizationId=:universityId and a.deletedAt is null")
    List<Assignment> getAllByCourseId(Long courseId, Long universityId);

    @Query("""
                select a
                from Assignment a
                join CourseAssignment ca on a.course = ca.course
                join ca.group g
                join StudentProfile s on s.group = g
                where s.id = :studentId
                  and s.organizationId = :universityId
                  and a.deletedAt is null
                  and s.deletedAt is null
                  and g.deletedAt is null
                  and ca.deletedAt is null
            """)
    List<Assignment> findAssignmentsForStudent(Long studentId, Long universityId);

    @Query("""
            select count(a) > 0
            from Assignment a
            where a.id = :id
            and a.organizationId = :orgId
            """)
    boolean existsAssignment(Long id, Long orgId);

    @Query("select a from Assignment a where a.id=:assignmentId and a.deletedAt is null and a.organizationId=:orgId")
    Optional<Assignment> findByIdAndDeletedAtIsNull(Long assignmentId, Long orgId);

    @Query("select a from Assignment a where a.course.id=:courseId and a.organizationId=:orgId and a.deletedAt is null")
    Page<Assignment> findAllByCourseId(Long courseId, Long orgId,Pageable pageable);

    @Query("select a from Assignment a where a.teacher.id=:teacherId and a.organizationId=:universityId and a.deletedAt is null")
    Page<Assignment> findAllByTeacherId(Long teacherId, Long universityId, Pageable pageable);
}
