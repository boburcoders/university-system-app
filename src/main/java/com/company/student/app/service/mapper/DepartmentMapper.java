package com.company.student.app.service.mapper;

import com.company.student.app.dto.DepartmentRequestDto;
import com.company.student.app.dto.DepartmentResponse;
import com.company.student.app.dto.DepartmentUpdateRequest;
import com.company.student.app.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class DepartmentMapper {

    public abstract DepartmentResponse mapToResponse(Department department);

    public abstract Department mapToEntity(DepartmentRequestDto requestDto);

    public abstract Department updateEntity(@MappingTarget Department department, DepartmentUpdateRequest request);
}
