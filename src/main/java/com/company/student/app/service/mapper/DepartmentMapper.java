package com.company.student.app.service.mapper;

import com.company.student.app.dto.department.DepartmentRequestDto;
import com.company.student.app.dto.department.DepartmentResponse;
import com.company.student.app.dto.department.DepartmentUpdateRequest;
import com.company.student.app.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class DepartmentMapper {

    public abstract DepartmentResponse mapToResponse(Department department);

    public abstract Department mapToEntity(DepartmentRequestDto requestDto);

    public abstract Department updateEntity(@MappingTarget Department department, DepartmentUpdateRequest request);
}
