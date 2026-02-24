package com.company.student.app.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentProfileResponse {
    private String firstName;
    private String lastName;
    private String middleName;
    private String studentNumber;
    private String phoneNumber;
    private String email;
    private String avatarUrl;
    private String username;
    private AddressResponseDto address;
    private GroupResponse group;
}
