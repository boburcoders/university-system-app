package com.company.student.app.service.mapper;

import com.company.student.app.dto.UniversityAdminProfileResponse;
import com.company.student.app.dto.UniversityAdminRequest;
import com.company.student.app.dto.UniversityAdminUpdateRequest;
import com.company.student.app.model.AuthUser;
import com.company.student.app.model.UniversityAdminProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class UniversityAdminMapper {
    public abstract UniversityAdminProfile mapToEntity(UniversityAdminRequest request);


    public abstract void updateEntity(@MappingTarget UniversityAdminProfile adminProfile, UniversityAdminUpdateRequest request);

    public abstract UniversityAdminProfileResponse mapToProfileResponse(UniversityAdminProfile adminProfile);
}
