package com.company.student.app.model;

import com.company.student.app.model.enums.SubmissionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"assignment_id", "student_id", "attempt_number"})
})
public class Submission extends MultiTenantEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfile studentProfile;

    @Column(columnDefinition = "TEXT")
    private String answerText;

    @ElementCollection
    private List<String> fileNames = new ArrayList<>();

    private SubmissionStatus status;
    private Integer attemptNumber;
}
