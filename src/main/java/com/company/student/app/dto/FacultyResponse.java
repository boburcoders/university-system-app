package com.company.student.app.dto;

import com.company.student.app.model.Department;
import com.company.student.app.model.enums.DegreeType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacultyResponse {
    private String name;
    private Integer durationYear;
    private String logoUrl;
    private DegreeType degreeType;
    private String departmentName;
}
