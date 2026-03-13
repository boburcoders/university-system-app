package com.company.student.app.service.mapper;

import com.company.student.app.dto.grade.GradeRequest;
import com.company.student.app.dto.grade.GradeResponse;
import com.company.student.app.dto.grade.GradeUpdateRequest;
import com.company.student.app.model.Grade;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public abstract class GradeMapper {

    public abstract GradeResponse mapToResponse(Grade grade);

    public abstract Grade mapToEntity(GradeRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntity(@MappingTarget Grade grade, GradeUpdateRequest request);
}
