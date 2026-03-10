package com.company.student.app.dto.course;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseUpdateRequest {
    private String code;
    private String title;
    private String logoName;
    private String description;
}
