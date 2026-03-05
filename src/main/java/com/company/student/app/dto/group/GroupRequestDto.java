package com.company.student.app.dto.group;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String code;
}
