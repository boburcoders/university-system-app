package com.company.student.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonMaterialRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
}
