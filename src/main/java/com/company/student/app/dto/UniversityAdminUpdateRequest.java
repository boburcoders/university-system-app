package com.company.student.app.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniversityAdminUpdateRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private String logoName;

}
