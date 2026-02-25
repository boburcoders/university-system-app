package com.company.student.app.repository;

import com.company.student.app.model.StudentProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    boolean existsStudentProfileByEmail(String email);

    boolean existsStudentProfileByStudentNumberAndOrganizationId(String studentNumber, Long organizationId);

    @Query("select sp from StudentProfile sp where sp.organizationId=:universityId and sp.deletedAt is null ")
    Page<StudentProfile> findAllByUniversityId(Long universityId, Pageable pageable);

    @Query("select sp from StudentProfile sp where sp.group.id=:groupId and sp.organizationId=:universityId and sp.deletedAt is null")
    Page<StudentProfile> findAllByGroupId(Long groupId, Long universityId, Pageable pageable);

    @Query("select sp from StudentProfile sp where sp.user.id=:userId and sp.organizationId=:universityId and sp.deletedAt is null ")
    Optional<StudentProfile> findByUserIdAndOrganizationId(Long userId, Long universityId);

    Integer countByOrganizationIdAndDeletedAtIsNull(Long universityId);

    @Query("select sp from StudentProfile sp where sp.id=:studentId and sp.organizationId=:universityId and sp.deletedAt is null")
    Optional<StudentProfile> findByIdAndOrganizationId(Long studentId, Long universityId);
}
