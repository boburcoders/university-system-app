package com.company.student.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonCreateRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private Long courseId;
}
