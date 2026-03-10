package com.company.student.app.dto.lesson;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonUpdateRequest {
    private String title;
    private String description;
}
