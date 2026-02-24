package com.company.student.app.service.mapper;

import com.company.student.app.dto.StudentAttendanceResponse;
import com.company.student.app.dto.StudentProfileResponse;
import com.company.student.app.dto.StudentProfileUpdateRequest;
import com.company.student.app.dto.StudentShortResponse;
import com.company.student.app.model.StudentProfile;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class StudentProfileMapper {

    @Mapping(target = "groupName", source = "profile.group.name")
    public abstract StudentShortResponse mapToStudentShortResponse(StudentProfile profile);

    public abstract StudentAttendanceResponse mapToStudentAttendanceResponse(StudentProfile profile);

    @Mapping(target = "username", source = "profile.user.username")
    public abstract StudentProfileResponse toProfileResponse(StudentProfile profile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateProfile(@MappingTarget StudentProfile studentProfile, StudentProfileUpdateRequest request);
}
