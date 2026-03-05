package com.company.student.app.dto.department;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentUpdateRequest {
    private String name;
    private String logoName;

}
