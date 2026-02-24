package com.company.student.app.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniversityResponse {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String logoUrl;
}
