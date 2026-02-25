package com.company.student.app.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticResponse {
    private Integer studentCount;
    private Integer courseCount;
    private Integer teacherCount;
    private Integer facultyCount;
    private Integer groupCount;
    private Integer departmentCount;
}
