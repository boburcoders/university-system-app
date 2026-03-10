package com.company.student.app.dto.attedance;

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
    private String lessonTitle;
    private AttendanceStatus status;
    private String note;
    private LocalDateTime takenDateTime;

}
