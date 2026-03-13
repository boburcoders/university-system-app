package com.company.student.app.repository;

import com.company.student.app.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    @Query("select g from Grade g where g.submission.id=:submissionId and g.deletedAt is null and g.organizationId=:orgId")
    Optional<Grade> findBySubmissionId(Long submissionId,Long orgId);

    @Modifying
    @Query("update Grade g set g.deletedAt=:now where g.submission.assignment.id=:assignmentId and g.organizationId=:orgId and g.deletedAt is null")
    void softDeleAllGrade(Long assignmentId, Long orgId, LocalDateTime now);

    @Query("select g from Grade g where g.id=:id and g.organizationId=:universityId and g.deletedAt is null")
    Optional<Grade> findByIdAndDeletedAtIsNullAndOrganizationId(Long id, Long universityId);
}
