package com.company.student.app.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseAssignmentRequest {
    @NotNull
    @Positive
    private Long courseId;
    @NotNull
    @Positive
    private Long groupId;
    @NotNull
    @Positive
    private Long teacherId;
    @NotNull
    @Positive
    private Integer semester;
    @NotBlank
    private String academicYear;
}
