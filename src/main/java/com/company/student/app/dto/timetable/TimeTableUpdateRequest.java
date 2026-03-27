package com.company.student.app.dto.timetable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeTableUpdateRequest {
    private Long groupId;
    private Long roomId;
    private Long courseId;
    private Long teacherId;
    private DayOfWeek dayOfWeek;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
}
