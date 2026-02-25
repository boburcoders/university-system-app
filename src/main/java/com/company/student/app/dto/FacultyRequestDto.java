package com.company.student.app.dto;

import com.company.student.app.model.enums.DegreeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacultyRequestDto {
    private String name;
    private Integer durationYear;
    private String logoName;
    private DegreeType degreeType;
}
