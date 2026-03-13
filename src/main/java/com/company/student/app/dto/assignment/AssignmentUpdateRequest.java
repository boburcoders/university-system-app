package com.company.student.app.dto.assignment;

import com.company.student.app.model.enums.AssignmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentUpdateRequest {
    private String title;
    private String description;
    private Double maxScore;
    private LocalDateTime deadline;
    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;
    private AssignmentStatus status;
    private String fileName;
}
