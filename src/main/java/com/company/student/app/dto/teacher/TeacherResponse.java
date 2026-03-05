package com.company.student.app.dto.teacher;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherResponse {
    private Long id;
    private String firstName;
    private String lastName;
}
