package com.company.student.app.dto;

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
    private String logoUrl;
}

