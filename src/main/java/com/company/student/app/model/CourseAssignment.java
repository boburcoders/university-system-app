package com.company.student.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class CourseAssignment extends MultiTenantEntity {
    @ManyToOne(optional = false)
    private Course course;

    @ManyToOne(optional = false)
    private Group group;

    @ManyToOne(optional = false)
    private TeacherProfile teacher;

    private Integer semester;
    private String academicYear;
}
