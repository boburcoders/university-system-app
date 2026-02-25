package com.company.student.app.repository;

import com.company.student.app.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("select d from Department d where d.organizationId=:universityId and d.id=:departmentId and d.deletedAt is null ")
    Optional<Department> findByIdAndOrganisationId(Long departmentId, Long universityId);

    @Query("select d from Department d where d.organizationId=:universityId and d.deletedAt is null")
    Page<Department> findAllByOrganizationId(Long universityId, Pageable pageable);

    Integer countByOrOrganizationIdAndDeletedAtIsNull(Long universityId);

    boolean existsByNameAndOrganizationIdAndDeletedAtIsNull(String name, Long universityId);
}