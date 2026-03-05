package com.company.student.app.dto.univerAdmin;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UniversityCreateRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String code;
    @NotBlank
    private String description;
    @NotBlank
    private String logoName;

}
