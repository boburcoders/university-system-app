package com.company.student.app.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentShortResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String studentNumber;
    private String phoneNumber;
    private String email;
    private String logoName;
    private String groupName;
}
