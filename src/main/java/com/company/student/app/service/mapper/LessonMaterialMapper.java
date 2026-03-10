package com.company.student.app.service.mapper;

import com.company.student.app.dto.lesson.LessonMaterialResponse;
import com.company.student.app.dto.lesson.LessonMaterialUpdateRequest;
import com.company.student.app.model.LessonMaterial;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class LessonMaterialMapper {

    public abstract LessonMaterialResponse mapToLessonMaterialResponse(LessonMaterial material);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "fileName", ignore = true)
    public abstract void updateMaterial(@MappingTarget LessonMaterial lessonMaterial, LessonMaterialUpdateRequest request);
}
