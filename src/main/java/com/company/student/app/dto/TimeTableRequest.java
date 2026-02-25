package com.company.student.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeTableRequest {
    @NotBlank
    private Long groupId;
    @NotBlank
    private Long courseId;
    @NotBlank
    private Long teacherId;
    @NotBlank
    private DayOfWeek dayOfWeek;
    @NotBlank
    private LocalTime startTime;
    @NotBlank
    private LocalTime endTime;
}
