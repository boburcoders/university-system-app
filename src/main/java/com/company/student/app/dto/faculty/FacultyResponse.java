package com.company.student.app.dto.faculty;

import com.company.student.app.model.enums.DegreeType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacultyResponse {
    private Long id;
    private String name;
    private Integer durationYear;
    private String logoName;
    private DegreeType degreeType;
    private String departmentName;
}
