package com.company.student.app.service.mapper;

import com.company.student.app.dto.LessonCreateRequest;
import com.company.student.app.dto.LessonResponse;
import com.company.student.app.model.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class LessonMapper {
    public Lesson mapToLesson(LessonCreateRequest request) {
        return Lesson.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();
    }

    @Mapping(target = "courseCode",source = "lesson.course.code")
    public abstract LessonResponse mapToLessonResponse(Lesson lesson);
}
