package com.company.student.app.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherUpdateRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private String description;
    private String phoneNumber;
    private String username;
    private String logoName;

}
