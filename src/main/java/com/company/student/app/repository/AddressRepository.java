package com.company.student.app.repository;

import com.company.student.app.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("select a from Address a where a.id=:id and a.organizationId=:organizationId and a.deletedAt is null ")
    Optional<Address> findByIdAndOrgzanizationId(Long id, Long organizationId);
}
