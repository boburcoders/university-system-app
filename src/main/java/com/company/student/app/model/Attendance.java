package com.company.student.app.model;

import com.company.student.app.model.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"lesson_id", "student_id"})
        }
)
public class Attendance extends MultiTenantEntity {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne(optional = false)
    private Lesson lesson;

    @ManyToOne(optional = false)
    private StudentProfile student;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    private String note;
}

