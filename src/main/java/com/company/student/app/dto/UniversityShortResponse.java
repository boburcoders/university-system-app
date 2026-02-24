package com.company.student.app.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniversityShortResponse {
    private Long id;
    private String name;
    private String code;
    private String logoUrl;

}
