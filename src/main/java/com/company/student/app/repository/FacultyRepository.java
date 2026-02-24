package com.company.student.app.repository;

import com.company.student.app.model.Faculty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    @Query("select f from Faculty f where f.organizationId=:universityId and f.deletedAt is null ")
    Page<Faculty> findAllByOrganisationIdAndDeletedAtIsNull(Long universityId, Pageable pageable);
}
