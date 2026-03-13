package com.company.student.app.dto.submission;

import com.company.student.app.dto.student.StudentSubmissionResponse;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubmissionResponse {
    private Long id;
    private String assignmentTitle;
    private StudentSubmissionResponse studentProfile;
    private String answerText;
    private List<String> fileNames = new ArrayList<>();
    private String status;
    private Integer attemptNumber;
}
