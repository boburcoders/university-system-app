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
    private LocalDateTime startDateTime;

    @NotBlank
    private LocalDateTime endDateTime;

    @NotBlank
    private Long groupId;

    @NotBlank
    private Long courseId;
}
