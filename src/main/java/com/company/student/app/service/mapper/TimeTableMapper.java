package com.company.student.app.service.mapper;

import com.company.student.app.dto.TimeTableResponse;
import com.company.student.app.model.TimeTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TimeTableMapper {

    @Mapping(target = "courseCode", source = "table.course.code")
    @Mapping(target = "day", source = "table.dayOfWeek")
    @Mapping(target = "groupName", source = "table.group.name")
    @Mapping(target = "roomNumber", source = "table.room.number")
    @Mapping(target = "teacherFullName",
            expression = "java(table.getTeacher().getFirstName() + \" \" + table.getTeacher().getLastName())")
    public abstract TimeTableResponse mapToResponseDto(TimeTable table);


    public abstract List<TimeTableResponse> mapToResponseList(List<TimeTable> timeTables);
}
