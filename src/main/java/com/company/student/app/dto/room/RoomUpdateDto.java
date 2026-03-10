package com.company.student.app.dto.room;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomUpdateDto {
    private String name;
    private Integer number;
}

