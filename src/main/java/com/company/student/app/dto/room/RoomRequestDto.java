package com.company.student.app.dto.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomRequestDto {
    @NotBlank
    private String name;
    @NotNull
    private Integer number;
}
