package com.company.student.app.repository;

import com.company.student.app.model.Department;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("select d from Department d where d.organizationId=:universityId and d.id=:departmentId and d.deletedAt is null ")
    Optional<Department> findByIdAndOrganisationId(Long departmentId, Long universityId);

    @Query("select d from Department d where d.organizationId=:universityId and d.deletedAt is null")
    Page<Department> findAllByOrganizationId(Long universityId, Pageable pageable);

    Integer countByOrOrganizationIdAndDeletedAtIsNull(Long universityId);

    boolean existsByNameAndOrganizationIdAndDeletedAtIsNullAndIdNot(String name, Long universityId,Long departmentId);

    @Modifying
    @Query("""
             update Department d
             set d.deletedAt=:now
             where d.organizationId=:universityId and d.deletedAt is null
            """)
    void softDeleteByUniversity(Long universityId, LocalDateTime now);

    boolean existsByNameAndOrganizationIdAndDeletedAtIsNull(@NotBlank String name, Long aLong);
}