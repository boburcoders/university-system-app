package com.company.student.app.repository;

import com.company.student.app.model.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {

    @Query("select u from University u where u.id=:id and u.deletedAt is null ")
    Optional<University> findByIdAndDeletedAtIsNull(Long id);

    Page<University> findAllByDeletedAtIsNull(Pageable pageable);

    @Query("select count(u.id)>0 from University u where u.code=:code and u.deletedAt is null")
    boolean existsUniversitiesByCodeAndDeletedAtIsNull(String code);

    @Query("select count(u.id)>0 from University u where u.name=:name and u.deletedAt is null")
    boolean existsUniversitiesByNameAndDeletedAtIsNull(String name);

    boolean existsByNameAndIdNotAndDeletedAtIsNull(String name, Long id);

    boolean existsByCodeAndIdNotAndDeletedAtIsNull(String code, Long id);

    @Query("select u from University  u where u.deletedAt is null ")
    List<University> findAllUniversity();
}
