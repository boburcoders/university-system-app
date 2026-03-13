package com.company.student.app.repository;

import com.company.student.app.model.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("select s from Submission s where s.assignment.id=:assignmentId and s.studentProfile.id=:studentId and s.organizationId=:orgId and s.deletedAt is null")
    Optional<Submission> findByAssignmentIdAndStudent(Long assignmentId, Long studentId, Long orgId);

    Optional<Submission> findTopByAssignmentIdAndStudentProfileIdAndOrganizationIdOrderByAttemptNumberDesc(Long assignmentId, Long id, Long universityId);

    @Query("select s from Submission s where s.id=:id and s.organizationId=:universityId and s.deletedAt is null")
    Optional<Submission> findByIdAndAndOrganizationId(Long id, Long universityId);

    @Query("select s from Submission s where s.assignment.id=:id and s.organizationId=:orgId and s.deletedAt is null")
    List<Submission> findAllByAssignmentId(Long id, Long orgId);

    @Query("select s from Submission s where s.studentProfile.id=:studentId and s.organizationId=:universityId and s.deletedAt is null")
    Page<Submission> findAllStudentSubmission(Long studentId, Long universityId, Pageable pageable);

    @Query("select count(s)>0 from Submission s where s.id=:submissionId and s.organizationId=:universityId and s.deletedAt is null")
    boolean existsByIdAndDeletedAtIsNull(Long submissionId, Long universityId);

    @Modifying
    @Query("update Submission s set s.deletedAt=:now where s.assignment.id=:assignmentId and s.organizationId=:orgId and s.deletedAt is null")
    void softDeleteSubmissions(Long assignmentId, Long orgId, LocalDateTime now);

    @Query("select s from Submission s where s.assignment.id=:assignmentId and s.organizationId=:universityId and s.deletedAt is null")
    Page<Submission> findAllPagesByAssignmentId(Long assignmentId, Long universityId, Pageable pageable);

    @Query("select s from Submission s where s.assignment.course.id=:courseId and s.studentProfile.id=:studentId and s.organizationId=:universityId and s.deletedAt is null")
    Page<Submission> findAllByCourseId(Long courseId, Long studentId, Long universityId, Pageable pageable);
}
