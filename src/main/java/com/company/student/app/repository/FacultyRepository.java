package com.company.student.app.repository;

import com.company.student.app.model.Faculty;
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
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    @Query("select f from Faculty f where f.organizationId=:universityId and f.deletedAt is null ")
    Page<Faculty> findAllByOrganisationIdAndDeletedAtIsNull(Long universityId, Pageable pageable);

    @Query("select f from Faculty f where f.id=:facultyId and f.organizationId=:universityId and f.deletedAt is null ")
    Optional<Faculty> findByIdAndOrganizationId(Long facultyId, Long universityId);

    Integer countByOrganizationIdAndDeletedAtIsNull(Long universityId);

    boolean existsByNameAndOrganizationIdAndDeletedAtIsNull(String name, Long universityId);


    @Modifying
    @Query("""
            update Faculty f
            set f.deletedAt = CURRENT_TIMESTAMP
            where f.department.id = :departmentId
            """)
    void softDeleteFacultyByDepartment(Long departmentId);

    @Modifying
    @Query("""
             update Faculty f
             set f.deletedAt=:now
             where f.organizationId=:universityId and f.deletedAt is null
            """)
    void softDeleteByUniversity(Long universityId, LocalDateTime now);
}
