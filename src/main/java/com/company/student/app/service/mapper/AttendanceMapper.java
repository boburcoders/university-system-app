package com.company.student.app.service.mapper;

import com.company.student.app.dto.AttendanceResponse;
import com.company.student.app.model.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class AttendanceMapper {
    @Mapping(target = "takenDateTime", source = "attendance.startTime")
    @Mapping(target = "courseName", source = "attendance.lesson.course.code")
    public abstract AttendanceResponse mapToAttendanceResponse(Attendance attendance);
}
