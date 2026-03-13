package com.company.student.app.dto.grade;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeRequest {
    private BigDecimal score;
    private String feedback;
}
