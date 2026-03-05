package com.company.student.app.service.mapper;

import com.company.student.app.dto.lesson.LessonMaterialResponse;
import com.company.student.app.model.LessonMaterial;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class LessonMaterialMapper {

    public abstract LessonMaterialResponse mapToLessonMaterialResponse(LessonMaterial material);
}
