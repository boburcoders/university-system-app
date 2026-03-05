package com.company.student.app.dto.course;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseRequestDto {
    @NotBlank
    private String code;
    @NotBlank
    private String title;
    @NotBlank
    private String logoName;
    @NotBlank
    private String description;
}
