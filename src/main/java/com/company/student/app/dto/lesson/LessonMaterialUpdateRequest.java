package com.company.student.app.dto.lesson;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonMaterialUpdateRequest {
    private String title;
    private String description;


}
