package com.company.student.app.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeTableResponse {
    private String groupName;
    private String courseCode;
    private String day;
    private Integer roomNumber;
    private String teacherFullName;
    private LocalTime startTime;
    private LocalTime endTime;
}
