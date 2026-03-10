package com.company.student.app.service.mapper;

import com.company.student.app.dto.teacher.TeacherProfileResponse;
import com.company.student.app.dto.teacher.TeacherResponse;
import com.company.student.app.dto.teacher.TeacherShortResponseDto;
import com.company.student.app.dto.teacher.TeacherUpdateRequest;
import com.company.student.app.model.TeacherProfile;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class TeacherProfileMapper {

    public abstract TeacherShortResponseDto mapToShortResponse(TeacherProfile profile);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "username", source = "profile.user.username")
    public abstract TeacherProfileResponse mapToProfileResponse(TeacherProfile profile);

    @Mapping(target = "email", source = "profile.email")
    public abstract TeacherResponse mapToTeacherResponse(TeacherProfile profile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntity(@MappingTarget TeacherProfile profile, TeacherUpdateRequest request);
}
