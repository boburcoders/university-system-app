package com.company.student.app.repository;

import com.company.student.app.model.LessonMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonMaterialRepository extends JpaRepository<LessonMaterial, Long> {
    @Query("select lm from LessonMaterial lm where lm.lesson.id=:lessonId and lm.organizationId=:universityId and lm.deletedAt is null ")
    List<LessonMaterial> findAllByLessonIdAndOrganisationId(Long lessonId, Long universityId);
}
