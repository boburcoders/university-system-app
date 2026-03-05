package com.company.student.app.dto.university;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniversityUpdateRequest {
    private String name;
    private String code;
    private String description;
    private String logoName;
    private String address;
}

