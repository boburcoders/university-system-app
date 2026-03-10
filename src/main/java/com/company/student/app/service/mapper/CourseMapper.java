package com.company.student.app.service.mapper;

import com.company.student.app.dto.course.CourseRequestDto;
import com.company.student.app.dto.course.CourseResponseDto;
import com.company.student.app.dto.course.CourseUpdateRequest;
import com.company.student.app.model.Course;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class CourseMapper {

    @Mapping(target = "facultyName", source = "course.faculty.name")
    public abstract CourseResponseDto mapToCourseResponse(Course course);

    public abstract Course mapToEntity(CourseRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateCourse(@MappingTarget Course course, CourseUpdateRequest request);
}
