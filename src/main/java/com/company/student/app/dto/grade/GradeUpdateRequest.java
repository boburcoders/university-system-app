package com.company.student.app.dto.grade;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeUpdateRequest {
    private BigDecimal score;
    private String feedback;
}
