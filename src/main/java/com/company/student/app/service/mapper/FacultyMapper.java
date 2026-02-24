package com.company.student.app.service.mapper;

import com.company.student.app.dto.FacultyResponse;
import com.company.student.app.model.Faculty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class FacultyMapper {

    @Mapping(target = "departmentName", source = "faculty.department.name")
    public abstract FacultyResponse mapToResponse(Faculty faculty);
}
