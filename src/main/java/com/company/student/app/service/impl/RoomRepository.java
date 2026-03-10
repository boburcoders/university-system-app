package com.company.student.app.service.impl;

import com.company.student.app.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findAllByOrganizationIdAndDeletedAtIsNull(Long organizationId, Pageable pageable);

    @Query("select r from Room r where r.id=:roomId and r.organizationId=:organizationId and r.deletedAt is null ")
    Optional<Room> findByIdAndOrgId(Long roomId, Long organizationId);
}