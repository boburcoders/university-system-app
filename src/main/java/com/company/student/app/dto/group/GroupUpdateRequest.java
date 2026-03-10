package com.company.student.app.dto.group;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupUpdateRequest {
    private String name;
    private String code;
}
