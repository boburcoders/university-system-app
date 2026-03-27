package com.company.student.app.repository;

import com.company.student.app.model.AuditingLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditingLogRepository extends JpaRepository<AuditingLog, Long> {

    @Query("select a from AuditingLog  a where a.deletedAt is null and a.organizationId=:orgId")
    Page<AuditingLog> findAllByDeletedAtIsNull(Pageable pageable,Long orgId);
}
