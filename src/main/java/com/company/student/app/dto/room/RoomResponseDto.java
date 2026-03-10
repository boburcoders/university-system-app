package com.company.student.app.dto.room;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomResponseDto {
    private Long id;
    private String name;
    private Integer number;
}
