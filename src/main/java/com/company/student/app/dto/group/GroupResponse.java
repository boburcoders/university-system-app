package com.company.student.app.dto.group;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupResponse {
    private String name;
    private String code;
    private String facultyName;
}
