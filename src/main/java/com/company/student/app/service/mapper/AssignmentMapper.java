package com.company.student.app.service.mapper;

import com.company.student.app.dto.assignment.AssignmentRequest;
import com.company.student.app.dto.assignment.AssignmentResponse;
import com.company.student.app.dto.assignment.AssignmentUpdateRequest;
import com.company.student.app.model.Assignment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public abstract class AssignmentMapper {

    public abstract AssignmentResponse mapToAssignmentResponse(Assignment assignment);

    public abstract Assignment mapToEntity(AssignmentRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntity(@MappingTarget Assignment assignment, AssignmentUpdateRequest request);
}
