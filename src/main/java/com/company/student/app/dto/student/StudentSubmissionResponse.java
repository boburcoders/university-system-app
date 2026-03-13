package com.company.student.app.dto.student;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentSubmissionResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String studentNumber;
}
