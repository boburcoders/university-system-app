package com.company.student.app.repository;

import com.company.student.app.model.SystemAdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuperAdminRepository extends JpaRepository<SystemAdminProfile, Long> {

    boolean existsSystemAdminProfileByEmailAndOrganizationId(String email, Long organizationId);

    boolean existsSystemAdminProfileByPhoneNumber(String phoneNumber);

    @Query("select sa from SystemAdminProfile sa where sa.user.id=:userId and sa.deletedAt is null and sa.organizationId=:organisationId")
    Optional<SystemAdminProfile> findByUserIdAndDeletedIsNull(Long userId, Long organisationId);

}
