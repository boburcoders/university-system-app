package com.company.student.app.service.mapper;

import com.company.student.app.dto.UniversityCreateRequestDto;
import com.company.student.app.dto.UniversityResponse;
import com.company.student.app.dto.UniversityShortResponse;
import com.company.student.app.dto.UniversityUpdateRequest;
import com.company.student.app.model.University;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class UniversityMapper {

    public abstract UniversityResponse mapToUniversityResponse(University university);

    @Mapping(target = "address", ignore = true)
    public abstract University mapToEntity(UniversityCreateRequestDto dto);

    public abstract UniversityShortResponse mapToUniversityShortResponse(University university);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateUniversity(@MappingTarget University university, UniversityUpdateRequest request);
}
