package com.company.student.app.dto;

import com.company.student.app.model.enums.DegreeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
