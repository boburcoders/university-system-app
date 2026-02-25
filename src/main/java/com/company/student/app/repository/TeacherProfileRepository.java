package com.company.student.app.repository;

import com.company.student.app.model.TeacherProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {
    boolean existsTeacherProfileByEmail(String email);

    @Query("""
            select t from TeacherProfile t
            where t.organizationId = :universityId
            and t.deletedAt is null
            """)
    Page<TeacherProfile> findAllByUniversityId(
            @Param("universityId") Long universityId,
            Pageable pageable
    );


    @Query("select tp from TeacherProfile tp where tp.organizationId=:universityId and tp.id=:teacherId and tp.deletedAt is null ")
    Optional<TeacherProfile> findByIdAndOrganizationIdAndDeletedAtIsNull(Long teacherId, Long universityId);

    @Query("select tp from TeacherProfile tp where tp.user.id=:userId and tp.organizationId=:orgId and tp.deletedAt is null ")
    Optional<TeacherProfile> findByUserIdAndOrganisationId(Long userId, Long orgId);

    Integer countByOrganizationIdAndDeletedAtIsNull(Long universityId);

    @Modifying
    @Query("""
             update TeacherProfile tp
             set tp.deletedAt=:now
             where tp.organizationId=:universityId and tp.deletedAt is null
            """)
    void softDeleteByUniversity(Long universityId, LocalDateTime now);
}
