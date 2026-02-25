package com.company.student.app.repository;

import com.company.student.app.model.Faculty;
import com.company.student.app.model.Group;
import jakarta.validation.constraints.NotBlank;
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
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("select g from Group g where g.organizationId=:universityId and g.deletedAt is null ")
    Page<Group> findAllByOrganisationId(Long universityId, Pageable pageable);

    @Query("select g from Group g where g.id=:groupId and g.organizationId=:universityId and g.deletedAt is null ")
    Optional<Group> findByIdAndOrganizationId(@NotBlank Long groupId, Long universityId);

    @Query("select g from Group g where g.organizationId=:organizationId and g.deletedAt is null")
    List<Group> getAllByOrganizationId(Long organizationId);

    Integer countByOrganizationIdAndDeletedAtIsNull(Long universityId);

    @Modifying
    @Query("""
            update Group g
            set g.deletedAt = CURRENT_TIMESTAMP
            where g.faculty.id = :facultyId
            and g.deletedAt is null
            """)
    void softDeleteGroupsByFaculty(Long facultyId);

    @Modifying
    @Query("""
             update Group g
             set g.deletedAt=:now
             where g.organizationId=:universityId and g.deletedAt is null
            """)
    void softDeleteByUniversity(Long universityId, LocalDateTime now);
}
