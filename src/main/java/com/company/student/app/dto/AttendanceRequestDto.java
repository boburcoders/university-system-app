package com.company.student.app.dto;

import com.company.student.app.model.enums.AttendanceStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceRequestDto {
    private Long studentId;
    private AttendanceStatus status;
    private String note;
}

