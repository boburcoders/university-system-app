package com.company.student.app.service.mapper;

import com.company.student.app.dto.FacultyRequestDto;
import com.company.student.app.dto.FacultyResponse;
import com.company.student.app.dto.FacultyUpdateRequest;
import com.company.student.app.model.Faculty;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class FacultyMapper {

    @Mapping(target = "departmentName", source = "faculty.department.name")
    public abstract FacultyResponse mapToResponse(Faculty faculty);

    public abstract Faculty mapToEntity(FacultyRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Faculty updateEntity(@MappingTarget Faculty faculty, FacultyUpdateRequest request);
}
