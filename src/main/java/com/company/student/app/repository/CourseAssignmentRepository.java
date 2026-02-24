package com.company.student.app.repository;

import com.company.student.app.model.Course;
import com.company.student.app.model.CourseAssignment;
import com.company.student.app.model.Group;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseAssignmentRepository extends JpaRepository<CourseAssignment, Long> {

    @Query("""
            select ca.course
            from CourseAssignment ca
            where ca.teacher.id = :teacherId
              and ca.organizationId = :universityId
              and ca.deletedAt is null
            """)
    Page<Course> findTeacherCourses(Long teacherId, Long universityId, Pageable pageable);

    @Query("select ca.group from CourseAssignment ca where ca.teacher.id=:profileId and ca.organizationId=:universityId and ca.deletedAt is null ")
    Page<Group> findTeacherGroups(Long profileId, Long universityId, Pageable pageable);

    boolean existsByTeacherIdAndCourseIdAndGroupIdAndOrganizationId(Long id, @NotBlank Long courseId, @NotBlank Long groupId, Long universityId);

    boolean existsByTeacherIdAndGroupIdAndOrganizationIdAndDeletedAtIsNull(Long teacherId, Long groupId, Long universityId);
}
