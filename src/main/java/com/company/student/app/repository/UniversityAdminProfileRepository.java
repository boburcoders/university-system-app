package com.company.student.app.repository;

import com.company.student.app.model.UniversityAdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniversityAdminProfileRepository extends JpaRepository<UniversityAdminProfile, Long> {
    boolean existsUniversityAdminProfileByEmail(String email);

    @Query("select ua from UniversityUserRole ua where ua.user.id=:userId and ua.deletedAt is null and ua.organizationId=:universityId")
    Optional<UniversityAdminProfile> findByUserIdAndDeletedIsNull(Long userId, Long universityId);

}
