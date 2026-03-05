package com.company.student.app.dto.group;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupShortResponse {
    private Long id;
    private String name;
    private String code;
}
