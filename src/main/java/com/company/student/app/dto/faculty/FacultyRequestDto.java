package com.company.student.app.dto.faculty;

import com.company.student.app.model.enums.DegreeType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacultyRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private Integer durationYear;
    @NotBlank
    private String logoName;
    @NotBlank
    private DegreeType degreeType;
}
