package com.company.student.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String logoName;
}
