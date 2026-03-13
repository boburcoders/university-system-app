package com.company.student.app.dto.grade;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeResponse {
    private Long id;
    private BigDecimal score;
    private String feedback;
}
