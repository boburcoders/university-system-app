package com.company.student.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
