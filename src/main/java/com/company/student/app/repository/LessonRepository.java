package com.company.student.app.repository;

import com.company.student.app.model.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("select l from Lesson l where l.organizationId=:universityId and l.deletedAt is null and l.course.id=:courseId")
    Page<Lesson> findAllByCourseId(Long universityId, Long courseId, Pageable pageable);


    @Query("select l from Lesson l where l.id=:lessonId and l.organizationId=:universityId and l.deletedAt is null ")
    Optional<Lesson> findByIdAndOrganizationIdAndDeletedAtIsNull(Long lessonId, Long universityId);
}
