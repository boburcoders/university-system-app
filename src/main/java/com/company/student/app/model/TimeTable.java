package com.company.student.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class TimeTable extends MultiTenantEntity {
    @ManyToOne(optional = false)
    private Group group;

    @ManyToOne(optional = false)
    private Course course;

    @ManyToOne(optional = false)
    private TeacherProfile teacher;

    @ManyToOne(optional = false)
    private Room room;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private LocalTime startTime;
    private LocalTime endTime;
}
