package com.company.student.app.dto.teacher;

import com.company.student.app.dto.address.AddressResponseDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherProfileResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String description;
    private String email;
    private String phoneNumber;
    private String logoName;
    private String username;
    private String password;
    private AddressResponseDto address;
}
