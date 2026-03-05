package com.company.student.app.service.mapper;

import com.company.student.app.dto.univerAdmin.UniversityCreateRequestDto;
import com.company.student.app.dto.university.UniversityProfileResponse;
import com.company.student.app.dto.university.UniversityResponse;
import com.company.student.app.dto.university.UniversityShortResponse;
import com.company.student.app.dto.university.UniversityUpdateRequest;
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

    public abstract UniversityProfileResponse mapToUniversityProfile(University university);
}
