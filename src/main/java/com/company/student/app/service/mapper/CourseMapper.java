package com.company.student.app.service.mapper;

import com.company.student.app.dto.CourseRequestDto;
import com.company.student.app.dto.CourseResponseDto;
import com.company.student.app.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class CourseMapper {

    @Mapping(target = "facultyName", source = "course.faculty.name")
    public abstract CourseResponseDto mapToCourseResponse(Course course);

    public abstract Course mapToEntity(CourseRequestDto requestDto);
}
