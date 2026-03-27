package com.company.student.app.repository;

import com.company.student.app.model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

    List<TimeTable> findAllByOrganizationIdAndTeacherIdAndDeletedAtIsNull(Long universityId, Long teacherId);

    List<TimeTable> findAllByGroupIdAndOrganizationIdAndDeletedAtIsNull(Long groupId, Long universityId);

    @Modifying
    @Query("""
             update TimeTable t
             set t.deletedAt=:now
             where t.organizationId=:universityId and t.deletedAt is null
            """)
    void softDeleteByUniversity(Long universityId, LocalDateTime now);

    List<TimeTable> findAllByOrganizationIdAndGroupIdAndDeletedAtIsNull(Long universityId, Long groupId);

    List<TimeTable> findAllByOrganizationIdAndRoomIdAndDeletedAtIsNull(Long universityId, Long roomId);

    @Query("select t from TimeTable t where t.id=:id and t.organizationId=:universityId and t.deletedAt is null")
    Optional<TimeTable> findByIdAndOrganizationId(Long id, Long universityId);
}
