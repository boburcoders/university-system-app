package com.company.student.app.dto;

import com.company.student.app.model.Address;
import com.company.student.app.model.AuthUser;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
    private String avatarUrl;
    private String username;
    private String password;
    private AddressResponseDto address;
}
