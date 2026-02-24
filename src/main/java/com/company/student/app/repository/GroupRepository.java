package com.company.student.app.repository;

import com.company.student.app.model.Group;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("select g from Group g where g.organizationId=:universityId and g.deletedAt is null ")
    Page<Group> findAllByOrganisationId(Long universityId, Pageable pageable);

    @Query("select g from Group g where g.id=:groupId and g.organizationId=:universityId and g.deletedAt is null ")
    Optional<Group> findByIdAndOrganizationId(@NotBlank Long groupId, Long universityId);
}
