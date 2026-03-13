package com.company.student.app.dto.submission;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubmissionRequest {
    private String answerText;
    private List<String> fileNames = new ArrayList<>();
}
