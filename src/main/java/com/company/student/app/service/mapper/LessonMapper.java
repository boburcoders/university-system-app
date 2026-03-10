package com.company.student.app.service.mapper;

import com.company.student.app.dto.lesson.LessonCreateRequest;
import com.company.student.app.dto.lesson.LessonMaterialUpdateRequest;
import com.company.student.app.dto.lesson.LessonResponse;
import com.company.student.app.dto.lesson.LessonUpdateRequest;
import com.company.student.app.model.Lesson;
import com.company.student.app.model.LessonMaterial;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class LessonMapper {
    public Lesson mapToLesson(LessonCreateRequest request) {
        return Lesson.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();
    }

    @Mapping(target = "courseCode", source = "lesson.course.code")
    public abstract LessonResponse mapToLessonResponse(Lesson lesson);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntity(@MappingTarget Lesson lesson, LessonUpdateRequest request);
}
