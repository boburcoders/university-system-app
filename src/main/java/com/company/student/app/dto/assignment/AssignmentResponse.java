package com.company.student.app.dto.assignment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentResponse {
    private Long id;
    private String title;
    private String description;
    private Double maxScore;
    private LocalDateTime deadline;
    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;
    private String status;
    private String fileName;
}
