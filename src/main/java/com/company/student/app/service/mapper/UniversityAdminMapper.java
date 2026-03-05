package com.company.student.app.service.mapper;

import com.company.student.app.dto.univerAdmin.UniversityAdminProfileResponse;
import com.company.student.app.dto.univerAdmin.UniversityAdminRequest;
import com.company.student.app.dto.univerAdmin.UniversityAdminUpdateRequest;
import com.company.student.app.model.UniversityAdminProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class UniversityAdminMapper {
    public abstract UniversityAdminProfile mapToEntity(UniversityAdminRequest request);


    public abstract void updateEntity(@MappingTarget UniversityAdminProfile adminProfile, UniversityAdminUpdateRequest request);

    @Mapping(target = "username", source = "profile.user.username")
    public abstract UniversityAdminProfileResponse mapToProfileResponse(UniversityAdminProfile profile);
}
