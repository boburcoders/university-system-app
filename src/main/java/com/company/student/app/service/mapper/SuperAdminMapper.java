package com.company.student.app.service.mapper;

import com.company.student.app.dto.SuperAdminResponse;
import com.company.student.app.dto.SystemAdminUpdateRequest;
import com.company.student.app.model.SystemAdminProfile;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class SuperAdminMapper {
    @Mapping(target = "username", source = "profile.user.username")
    public abstract SuperAdminResponse mapToSuperAdminResponse(SystemAdminProfile profile);
//1219289e-947a-4325-894a-877fa8e82499-photo_2025-11-13_15-18-07.jpg
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    public abstract SystemAdminProfile updateEntity(@MappingTarget SystemAdminProfile systemAdminProfile, SystemAdminUpdateRequest request);
}
