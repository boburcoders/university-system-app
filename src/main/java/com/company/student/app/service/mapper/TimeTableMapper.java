package com.company.student.app.service.mapper;

import com.company.student.app.dto.timetable.TimeTableResponse;
import com.company.student.app.dto.timetable.TimeTableUpdateRequest;
import com.company.student.app.model.TimeTable;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TimeTableMapper {

    @Mapping(target = "courseCode", source = "table.course.code")
    @Mapping(target = "courseTitle", source = "table.course.title")
    @Mapping(target = "day", source = "table.dayOfWeek")
    @Mapping(target = "groupName", source = "table.group.name")
    @Mapping(target = "roomNumber", source = "table.room.number")
    @Mapping(target = "teacherFullName",
            expression = "java(table.getTeacher().getFirstName() + \" \" + table.getTeacher().getLastName())")
    public abstract TimeTableResponse mapToResponseDto(TimeTable table);


    public abstract List<TimeTableResponse> mapToResponseList(List<TimeTable> timeTables);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "group", ignore = true)
    public abstract void updateEntity(@MappingTarget TimeTable timeTable, TimeTableUpdateRequest request);
}
