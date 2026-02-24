package com.company.student.app.service.mapper;

import com.company.student.app.dto.GroupResponse;
import com.company.student.app.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public abstract class GroupMapper {

    @Mapping(target = "facultyName", source = "group.faculty.name")
    public abstract GroupResponse mapToResponse(Group group);

}
