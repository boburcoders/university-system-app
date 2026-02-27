package com.company.student.app.dto;

import com.company.student.app.model.enums.AttendanceStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceRequestDto {
    @JsonFormat(pattern = "dd:MM:yyyy HH:mm")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "dd:MM:yyyy HH:mm")
    private LocalDateTime endTime;

    private Long studentId;
    private AttendanceStatus status;
    private String note;
}

