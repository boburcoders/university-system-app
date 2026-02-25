package com.company.student.app.repository;

import com.company.student.app.model.UniversityUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UniversityUserRoleRepository extends JpaRepository<UniversityUserRole, Long> {

    @Query("""
                SELECT uur
                FROM UniversityUserRole uur
                JOIN FETCH uur.user u
                JOIN FETCH uur.university uni
                WHERE u.username = :username
                AND uni.id = :universityId
                AND uni.deletedAt IS NULL
            """)
    Optional<UniversityUserRole> findUserWithRole(
            String username,
            Long universityId
    );

    @Query("select ur from UniversityUserRole ur where ur.user.id=:id and ur.organizationId=:universityId and ur.deletedAt is null ")
    Optional<UniversityUserRole> findByUserIdAndOrganizationId(Long id, Long universityId);

    @Modifying
    @Query("""
             update UniversityUserRole ur
             set ur.deletedAt=:now
             where ur.organizationId=:universityId and ur.deletedAt is null
            """)
    void softDeleteByUniversity(Long universityId, LocalDateTime now);
}
