package com.company.student.app.dto.course;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseResponseDto {
    private Long id;
    private String code;
    private String title;
    private String logoName;
    private String description;
    private String facultyName;
}
