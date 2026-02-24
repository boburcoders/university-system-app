package com.company.student.app.dto;

import com.company.student.app.model.Lesson;
import com.company.student.app.model.enums.AttendanceStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceResponse {
    private String courseName;
    private AttendanceStatus status;
    private String note;
    private LocalDateTime takenDateTime;

}
