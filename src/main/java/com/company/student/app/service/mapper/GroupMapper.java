package com.company.student.app.service.mapper;

import com.company.student.app.dto.group.GroupRequestDto;
import com.company.student.app.dto.group.GroupResponse;
import com.company.student.app.dto.group.GroupShortResponse;
import com.company.student.app.dto.group.GroupUpdateRequest;
import com.company.student.app.model.Group;
import org.mapstruct.*;

@Mapper(componentModel = "spring")

public abstract class GroupMapper {

    @Mapping(target = "facultyName", source = "group.faculty.name")
    @Mapping(target = "id", source = "group.id")
    public abstract GroupResponse mapToResponse(Group group);

    public abstract GroupShortResponse mapToShortResponse(Group group);

    public abstract Group mapToEntity(GroupRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntity(@MappingTarget Group group, GroupUpdateRequest request);
}
