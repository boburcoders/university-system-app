package com.company.student.app.dto.student;

import com.company.student.app.dto.address.AddressResponseDto;
import com.company.student.app.dto.group.GroupResponse;
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
    private String logoName;
    private String username;
    private AddressResponseDto address;
    private GroupResponse group;
}
