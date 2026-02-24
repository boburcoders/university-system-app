package com.company.student.app.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentAttendanceResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String studentNumber;

}
