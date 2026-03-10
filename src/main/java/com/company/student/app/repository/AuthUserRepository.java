package com.company.student.app.repository;

import com.company.student.app.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    @Query("select u from AuthUser u where u.username=:username and u.organizationId=:universityId and u.deletedAt is null ")
    Optional<AuthUser> findByAuthUserNameAndOrganisationId(String username, Long universityId);

    @Query("select u from AuthUser u where u.username=:username and u.deletedAt is null and u.organizationId=:organisationId")
    Optional<AuthUser> findByUserName(String username, Long organisationId);

    @Query("select u from AuthUser u where u.id=:userId and u.deletedAt is null and u.organizationId=:organizationId")
    Optional<AuthUser> findByIdAndDeletedAtIsNull(Long userId,Long organizationId);

    @Modifying
    @Query("""
             update AuthUser au
             set au.deletedAt=:now
             where au.organizationId=:universityId and au.deletedAt is null
            """)
    void softDeleteByUniversity(Long universityId, LocalDateTime now);

    @Query("select a from AuthUser a where a.id=:userId and a.organizationId=:universityId and a.deletedAt is null ")
    AuthUser findByIdAndOrganizationIdAndDeletedAtIsNull(Long userId, Long universityId);

    boolean existsByUsernameAndOrganizationIdAndDeletedAtIsNull(String email, Long universityId);

    boolean existsByUsernameIgnoreCaseAndOrganizationIdAndDeletedAtIsNullAndIdNot(String newUsername, Long universityId, Long id);
}
