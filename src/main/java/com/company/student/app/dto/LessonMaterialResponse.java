package com.company.student.app.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonMaterialResponse {
    private Long id;
    private String title;
    private String description;
    private String fileName;


}
