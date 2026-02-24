package com.company.student.app.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMeResponse {
    private Long id;
    private Long universityId;
    private String username;
    private String role;
}
