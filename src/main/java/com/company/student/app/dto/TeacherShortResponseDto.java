package com.company.student.app.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherShortResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String description;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
}
