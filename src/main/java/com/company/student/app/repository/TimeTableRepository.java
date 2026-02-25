package com.company.student.app.repository;

import com.company.student.app.model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

    List<TimeTable> findAllByOrganizationIdAndTeacherIdAndDeletedAtIsNull(Long universityId, Long teacherId);

    List<TimeTable> findAllByOrganizationIdAndTeacherIdAndDayOfWeekAndDeletedAtIsNull(Long universityId, Long teacherId, DayOfWeek dayOfWeek);


    List<TimeTable> findAllByOrganizationIdAndTeacherIdAndDayOfWeekInAndDeletedAtIsNull(Long universityId, Long teacherId, List<DayOfWeek> days);

    List<TimeTable> findAllByGroupIdAndOrganizationIdAndDeletedAtIsNull(Long groupId, Long universityId);

    @Modifying
    @Query("""
             update TimeTable t
             set t.deletedAt=:now
             where t.organizationId=:universityId and t.deletedAt is null
            """)
    void softDeleteByUniversity(Long universityId, LocalDateTime now);
}
