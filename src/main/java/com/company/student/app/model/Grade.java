package com.company.student.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Grade extends MultiTenantEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", unique = true)
    private Submission submission;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private TeacherProfile teacher;

    private BigDecimal score;

    @Column(columnDefinition = "TEXT")
    private String feedback;


}
