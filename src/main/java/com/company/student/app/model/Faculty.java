package com.company.student.app.model;

import com.company.student.app.model.enums.DegreeType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Faculty extends MultiTenantEntity {
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer durationYear;

    private String logoName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DegreeType degreeType;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
}
