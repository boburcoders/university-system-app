package com.company.student.app.dto.student;

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
